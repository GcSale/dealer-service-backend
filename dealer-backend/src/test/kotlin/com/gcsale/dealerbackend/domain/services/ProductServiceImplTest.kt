package com.gcsale.dealerbackend.domain.services

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

internal class ProductServiceImplTest {

    private val productService = ProductServiceImpl()

    @Test
    fun createProduct() {
        val externalUUID = UUID.randomUUID()
        val name = "new brand product"

        val actual = productService.createProduct(ProductCreationDto(externalUUID, name))

        assertEquals(name, actual.name)
        assertEquals(externalUUID, actual.externalUUID)
    }
}