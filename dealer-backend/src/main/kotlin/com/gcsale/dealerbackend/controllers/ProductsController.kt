package com.gcsale.dealerbackend.controllers

import com.gcsale.dealerbackend.application.commands.products.SaveProductCommand
import com.gcsale.dealerbackend.application.commands.products.SaveProductCommandHandler
import com.gcsale.dealerbackend.controllers.dtos.SaveProductIncomeDto
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/products")
class ProductsController(private val saveProductCommandHandler: SaveProductCommandHandler) {

    @PutMapping("/{uuid}")
    fun saveCar(@PathVariable uuid: UUID, @RequestBody dto: SaveProductIncomeDto) {
        val command = SaveProductCommand(uuid, dto.name)
        saveProductCommandHandler.execute(command)
    }
}