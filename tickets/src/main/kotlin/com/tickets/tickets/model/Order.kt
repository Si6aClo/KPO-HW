package com.tickets.tickets.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "orders")
data class Order(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @JoinColumn(name = "user_id")
    val userId: Long,
    @ManyToOne
    @JoinColumn(name = "from_station_id")
    val fromStation: Station,
    @ManyToOne
    @JoinColumn(name = "to_station_id")
    val toStation: Station,
    var status: Int = 1, // 1: check, 2: success, 3: rejection
    // auto now date
    val created: Date = Date(),
)

