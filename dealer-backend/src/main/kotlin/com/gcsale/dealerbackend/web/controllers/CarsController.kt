package com.gcsale.dealerbackend.web.controllers

import com.gcsale.dealerbackend.services.CarsService
import com.gcsale.dealerbackend.services.converters.CarInfoConverter
import com.gcsale.dealerbackend.web.controllers.dtos.CarInfoDto
import com.gcsale.dealerbackend.web.controllers.dtos.SaveCarIncomeDto
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/cars")
class CarsController(private val serviceCarsService: CarsService,
                     private val carInfoConverter: CarInfoConverter) {

    @PutMapping("/{uuid}")
    fun saveCar(@PathVariable uuid: UUID, @RequestBody dto: SaveCarIncomeDto): CarInfoDto {
        val car = serviceCarsService.storeCar(externalUUID = uuid, name = dto.name)
        return carInfoConverter.convertToTransport(car)
    }
}