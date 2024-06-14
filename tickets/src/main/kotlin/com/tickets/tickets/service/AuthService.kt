package com.tickets.tickets.service

import io.jsonwebtoken.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AuthService {
    @Value("\${jwt.secret}")
    private lateinit var secretKey: String

    fun checkAuth(token: String): Boolean {
        return try {
            System.currentTimeMillis() < Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).body.expiration.time
        } catch (e: Exception) {
            false
        }
    }

    fun getUserId(token: String): Long? {
        return try {
            val claims: Claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body
            claims.subject.toLong()
        } catch (e: Exception) {
            null
        }
    }
}