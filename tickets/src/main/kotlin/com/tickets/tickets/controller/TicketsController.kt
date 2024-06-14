package com.tickets.tickets.controller

import com.tickets.tickets.dto.OrderFullInfoDto
import com.tickets.tickets.dto.OrderInfoDto
import com.tickets.tickets.exception.AuthException
import com.tickets.tickets.service.AuthService
import com.tickets.tickets.service.OrdersService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tickets")
class TicketsController(
    private val ordersService: OrdersService,
    private val authService: AuthService
) {
    @PostMapping("/create")
    fun createOrder(
        @CookieValue("token", required = false) tokenCookie: String?,
        @RequestBody orderInfoDto: OrderInfoDto
    ): ResponseEntity<OrderFullInfoDto> {
        val token = tokenCookie ?: orderInfoDto.tokenId ?: throw AuthException("Token not found")
        if (!authService.checkAuth(token)) {
            throw AuthException("Unauthorized")
        }
        val userId = authService.getUserId(token) ?: throw AuthException("User not found")
        return ordersService.createOrder(orderInfoDto, userId)
    }

    @GetMapping("/get/{orderId}")
    fun getOrder(
        @CookieValue("token", required = false) tokenCookie: String?,
        @RequestBody(required = false) body: Map<String, String>?,
        @PathVariable orderId: Long
    ): ResponseEntity<OrderFullInfoDto> {
        val token = tokenCookie ?: body?.get("token") ?: throw AuthException("Token not found")
        if (!authService.checkAuth(token)) {
            throw AuthException("Unauthorized")
        }
        authService.getUserId(token) ?: throw AuthException("Unauthorized")
        return ordersService.getOrder(orderId)
    }
}