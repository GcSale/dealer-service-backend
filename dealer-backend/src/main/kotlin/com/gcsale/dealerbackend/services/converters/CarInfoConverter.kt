package com.gcsale.dealerbackend.services.converters

import com.gcsale.dealerbackend.models.Car
import com.gcsale.dealerbackend.web.controllers.dtos.CarInfoDto
import org.springframework.stereotype.Service

@Service
class CarInfoConverter {

    fun convertToTransport(car: Car): CarInfoDto {
        return CarInfoDto(name = car.tempCarName, uuid = car.externalUUID)
    }
}