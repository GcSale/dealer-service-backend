package com.gcsale.dealerbackend.services

import com.gcsale.dealerbackend.models.Product
import com.gcsale.dealerbackend.repository.ProductRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProductsService(private val repository: ProductRepository) {
    fun storeProduct(externalUUID: UUID, name: String): Product {
        val car = repository.findByExternalUUID(externalUUID) ?: Product(
                externalUUID = externalUUID,
                name = name
        )
        car.name = name
        return repository.save(car)
    }
}