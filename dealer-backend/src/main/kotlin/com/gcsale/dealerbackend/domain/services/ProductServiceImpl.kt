package com.gcsale.dealerbackend.domain.services

import com.gcsale.dealerbackend.domain.models.Product
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl : ProductService {
    override fun createProduct(dto: ProductCreationDto): Product {
        return Product(externalUUID = dto.externalUUID, name = dto.name)
    }

    override fun updateProduct(dto: ProductUpdateDto): Product {
        dto.product.name = dto.name
        return dto.product
    }
}