package com.gcsale.dealerbackend.repository

import com.gcsale.dealerbackend.models.Car
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CarsRepository : CrudRepository<Car, Long> {
    fun findByExternalUUID(externalUUID: UUID): Car?
}