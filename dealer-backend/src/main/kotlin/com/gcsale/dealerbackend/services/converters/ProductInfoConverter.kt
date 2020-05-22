package com.gcsale.dealerbackend.services.converters

import com.gcsale.dealerbackend.models.Product
import com.gcsale.dealerbackend.web.controllers.dtos.ProductInfoDto
import org.springframework.stereotype.Service

@Service
class ProductInfoConverter {

    fun convertToTransport(product: Product): ProductInfoDto {
        return ProductInfoDto(name = product.name, uuid = product.externalUUID)
    }
}