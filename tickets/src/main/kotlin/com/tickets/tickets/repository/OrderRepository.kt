package com.tickets.tickets.repository

import com.tickets.tickets.model.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long> {
    fun findByStatus(status: Int): List<Order>
}