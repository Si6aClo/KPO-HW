package com.tickets.tickets.service

import com.tickets.tickets.dto.OrderFullInfoDto
import com.tickets.tickets.dto.OrderInfoDto
import com.tickets.tickets.exception.OrderException
import com.tickets.tickets.model.Order
import com.tickets.tickets.repository.OrderRepository
import com.tickets.tickets.repository.StationRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrdersService(
    private val orderRepository: OrderRepository,
    private val stationRepository: StationRepository
) {
    fun createOrder(orderInfoDto: OrderInfoDto, userId: Long): ResponseEntity<OrderFullInfoDto> {
        val fromStation = stationRepository.findById(orderInfoDto.stationFromId)
            .orElseThrow { OrderException("Станция отправления не найдена") }

        val toStation = stationRepository.findById(orderInfoDto.stationToId)
            .orElseThrow { OrderException("Станция прибытия не найдена") }

        if (fromStation.id == toStation.id) {
            throw OrderException("Станция отправления и прибытия не могут совпадать")
        }

        val newOrder = Order(
            userId = userId,
            fromStation = fromStation,
            toStation = toStation,
            created = Date(),
            status = 1,
        )

        val insertedOrder = orderRepository.save(newOrder)

        return ResponseEntity.ok(
            OrderFullInfoDto(
                userId = insertedOrder.userId,
                stationFromId = insertedOrder.fromStation.id,
                stationToId = insertedOrder.toStation.id,
                date = insertedOrder.created,
                status = insertedOrder.status,
            )
        )
    }

    fun getOrder(orderId: Long): ResponseEntity<OrderFullInfoDto> {
        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderException("Билет не найден") }

        return ResponseEntity.ok(
            OrderFullInfoDto(
                userId = order.userId,
                stationFromId = order.fromStation.id,
                stationToId = order.toStation.id,
                date = order.created,
                status = order.status,
            )
        )
    }

    fun getOrdersByStatus(status: Int): List<Order> {
        return orderRepository.findByStatus(status)
    }

    fun updateOrder(order: Order): Order {
        return orderRepository.save(order)
    }
}