package com.gcsale.dealerbackend.domain.services

import com.gcsale.dealerbackend.domain.models.Product
import java.util.*

data class ProductCreationDto(val externalUUID: UUID, val name: String)
data class ProductUpdateDto(val product: Product, val name: String)

interface ProductService {
    fun createProduct(dto: ProductCreationDto): Product
    fun updateProduct(dto: ProductUpdateDto): Product
}
