package com.gcsale.dealerbackend.services

import com.gcsale.dealerbackend.models.Car
import com.gcsale.dealerbackend.repository.CarsRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class CarsService(private val repository: CarsRepository) {
    fun storeCar(externalUUID: UUID, name: String): Car {
        val car = repository.findByExternalUUID(externalUUID) ?: Car(
                externalUUID = externalUUID,
                tempCarName = name
        )
        car.tempCarName = name
        return repository.save(car)
    }
}