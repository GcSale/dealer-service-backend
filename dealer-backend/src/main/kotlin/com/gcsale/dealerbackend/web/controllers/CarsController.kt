package com.gcsale.dealerbackend.web.controllers

import com.gcsale.dealerbackend.services.CarsService
import com.gcsale.dealerbackend.web.controllers.dtos.SaveCarIncomeDto
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/cars")
class CarsController(private val serviceCarsService: CarsService) {

    @PutMapping("/{uuid}")
    fun saveCar(@PathVariable uuid: UUID, @RequestBody dto: SaveCarIncomeDto) {
        serviceCarsService.storeCar(externalUUID = uuid, name = dto.name)
    }
}