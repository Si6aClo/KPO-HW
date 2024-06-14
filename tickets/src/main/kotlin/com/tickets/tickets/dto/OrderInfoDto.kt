package com.tickets.tickets.dto

import java.util.Date

data class OrderInfoDto (
    val tokenId: String? = null,
    val stationFromId: Long,
    val stationToId: Long,
)

data class OrderFullInfoDto (
    val userId: Long,
    val stationFromId: Long,
    val stationToId: Long,
    val date: Date? = null,
    val status: Int,
)