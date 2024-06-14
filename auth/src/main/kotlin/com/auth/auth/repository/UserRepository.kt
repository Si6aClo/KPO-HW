package com.auth.auth.repository

import com.auth.auth.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun findByNickname(nickname: String): User?
}