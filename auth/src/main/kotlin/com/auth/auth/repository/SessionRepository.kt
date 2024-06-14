package com.auth.auth.repository

import com.auth.auth.model.Session
import org.springframework.data.jpa.repository.JpaRepository

interface SessionRepository : JpaRepository<Session, Long> {
    fun findByToken(token: String): Session?
}