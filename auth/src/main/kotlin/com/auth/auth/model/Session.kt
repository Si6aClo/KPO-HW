package com.auth.auth.model

import jakarta.persistence.*
import java.util.Date

@Entity
@Table(name = "session")
data class Session(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @OneToOne
    @JoinColumn(name = "user_id")
    val user: User,
    val token: String,
    val expires: Date
)
