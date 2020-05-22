package com.gcsale.dealerbackend.web.controllers

import com.gcsale.dealerbackend.services.ProductsService
import com.gcsale.dealerbackend.services.converters.ProductInfoConverter
import com.gcsale.dealerbackend.web.controllers.dtos.ProductInfoDto
import com.gcsale.dealerbackend.web.controllers.dtos.SaveProductIncomeDto
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/products")
class ProductsController(private val serviceProductsService: ProductsService,
                         private val productInfoConverter: ProductInfoConverter) {

    @PutMapping("/{uuid}")
    fun saveCar(@PathVariable uuid: UUID, @RequestBody dto: SaveProductIncomeDto): ProductInfoDto {
        val product = serviceProductsService.storeProduct(externalUUID = uuid, name = dto.name)
        return productInfoConverter.convertToTransport(product)
    }
}