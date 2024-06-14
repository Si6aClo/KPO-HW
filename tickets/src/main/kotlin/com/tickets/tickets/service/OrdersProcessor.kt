package com.tickets.tickets.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class OrdersProcessor @Autowired constructor(val ordersService: OrdersService) {
    // function that every 10 seconds process tickets with status 1
    @Scheduled(fixedRate = 10000)
    fun processOrders() {
        val ticketsToProcess = ordersService.getOrdersByStatus(1)
        for (ticket in ticketsToProcess) {
            ticket.status = if (Random.nextBoolean()) 2 else 3
            ordersService.updateOrder(ticket)
        }
    }
}