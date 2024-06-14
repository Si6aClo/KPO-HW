package com.tickets.tickets.repository

import com.tickets.tickets.model.Station
import org.springframework.data.jpa.repository.JpaRepository

interface StationRepository : JpaRepository<Station, Long> {
}