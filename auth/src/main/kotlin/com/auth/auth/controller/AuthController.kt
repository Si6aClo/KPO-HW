package com.auth.auth.controller

import org.springframework.web.bind.annotation.*

import com.auth.auth.dto.*
import com.auth.auth.service.AuthService
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/register")
    fun registerUser(@RequestBody userInfo: UserInfoDto): ResponseEntity<UserAuthDto> {
        return authService.registerUser(userInfo)
    }

    @PostMapping("/login")
    fun loginUser(@RequestBody userLogin: UserLoginDto): ResponseEntity<UserAuthDto> {
        return authService.loginUser(userLogin)
    }

    @GetMapping("/info")
    fun userInfo(
        @CookieValue("token", required = false) tokenCookie: String?,
        @RequestBody(required = false) body: Map<String, String>?,
    ) : ResponseEntity<UserPublicInfoDto> {
        val token = tokenCookie ?: body?.get("token") ?: return ResponseEntity.status(401).body(UserPublicInfoDto("", "", -1, "Token not provided"))
        return authService.userInfo(token)
    }
}