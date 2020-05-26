package com.gcsale.dealerbackend.helpers

import com.gcsale.dealerbackend.domain.models.Product
import java.util.*

object Factories {
    fun createProduct(name: String, uuid: UUID = UUID.randomUUID(), id: Long? = null): Product {
        return Product(name, uuid, id)
    }
}