package com.auth.auth.service

import io.jsonwebtoken.*
import com.auth.auth.dto.UserAuthDto
import com.auth.auth.dto.UserInfoDto
import com.auth.auth.dto.UserLoginDto
import com.auth.auth.dto.UserPublicInfoDto
import com.auth.auth.exception.AuthException
import com.auth.auth.model.Session
import com.auth.auth.model.User
import com.auth.auth.repository.SessionRepository
import com.auth.auth.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
) {
    @Value("\${jwt.secret}")
    private lateinit var secretKey: String

    @Value("\${jwt.expiration}")
    private var jwtExpirationInMs: Long = 0

    fun registerUser(userInfo: UserInfoDto): ResponseEntity<UserAuthDto> {
        // checking that fields are not empty
        if (userInfo.nickname.isBlank() || userInfo.password.isBlank() || userInfo.email.isBlank()) {
            return ResponseEntity.status(400).body(UserAuthDto("", "Fields cannot be empty"))
        }
        // checking that email is valid
        val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        if (!emailPattern.matches(userInfo.email)) {
            return ResponseEntity.status(400).body(UserAuthDto("", "Invalid email"))
        }
        // checking that password is valid
        val passwordPattern = ".{4,}".toRegex()
        if (!passwordPattern.matches(userInfo.password)) {
            return ResponseEntity.status(400).body(UserAuthDto("", "Password must be at least 4 characters long"))
        }
        // checking that nickname and email is unique
        if (userRepository.findByNickname(userInfo.nickname) != null) {
            return ResponseEntity.status(400).body(UserAuthDto("", "User with this nickname already exists"))
        }
        if (userRepository.findByEmail(userInfo.email) != null) {
            return ResponseEntity.status(400).body(UserAuthDto("", "User with this email already exists"))
        }

        val user = User(
            nickname = userInfo.nickname,
            password = BCrypt.hashpw(userInfo.password, BCrypt.gensalt()),
            email = userInfo.email,
        )
        userRepository.save(user)
        return authUser(user.id)
    }

    fun loginUser(userLogin: UserLoginDto): ResponseEntity<UserAuthDto> {
        val user = userRepository.findByEmail(userLogin.email)
        if (user == null || !BCrypt.checkpw(userLogin.password, user.password)) {
            return ResponseEntity.status(401).body(UserAuthDto("", "Invalid email or password"))
        }
        return authUser(user.id)
    }

    fun userInfo(token: String): ResponseEntity<UserPublicInfoDto> {
        // check if token is valid
        if (!Jwts.parser().setSigningKey(secretKey).isSigned(token)) {
            return ResponseEntity.status(401).body(UserPublicInfoDto("", "", -1, "Invalid token"))
        }
        val session = sessionRepository.findByToken(token)
            ?: return ResponseEntity.status(401).body(UserPublicInfoDto("", "", -1, "Invalid token"))
        // check if token is expired
        if (session.expires.before(Date())) {
            return ResponseEntity.status(401).body(UserPublicInfoDto("", "", -1, "Token expired"))
        }

        return ResponseEntity.status(200).body(UserPublicInfoDto(
            nickname = session.user.nickname,
            email = session.user.email,
            id = session.user.id,
            detail = "OK"
        ))
    }

    fun authUser(userId: Long): ResponseEntity<UserAuthDto> {
        val user = userRepository.findById(userId)
            .orElseThrow { AuthException("User not found") }
        // generate jwt token
        val token = Jwts.builder()
            .setSubject(user.id.toString())
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationInMs))
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact()

        val session = Session(
            user = user,
            token = token,
            expires = Date(Date().time + jwtExpirationInMs),
        )
        sessionRepository.save(session)

        // save token in cookie
        val cookie = ResponseCookie.from("token", token)
            .httpOnly(true)
            .path("/")
            .maxAge(jwtExpirationInMs)
            .build()

        return ResponseEntity.status(HttpStatus.OK)
            .header("Set-Cookie", cookie.toString())
            .body(UserAuthDto(token, "Login successful"))
    }
}