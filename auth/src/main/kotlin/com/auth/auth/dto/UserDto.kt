package com.auth.auth.dto

data class UserInfoDto (
    val nickname: String,
    val email: String,
    val password: String
)

data class UserLoginDto (
    val email: String,
    val password: String
)

data class UserAuthDto (
    val token: String,
    val detail: String
)

data class UserPublicInfoDto (
    val nickname: String,
    val email: String,
    val id: Long,
    val detail: String
)