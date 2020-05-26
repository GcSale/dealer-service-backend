package com.gcsale.dealerbackend.application.converters

import com.gcsale.dealerbackend.domain.models.Product
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.util.*

internal class ProductQueriesConverterTest {

    private val converter = ProductQueriesConverter()

    @Test
    fun convertProductToListItem() {
        val product = Product("n", UUID.randomUUID())
        val result = converter.convertProductToListItem(product)
        assertEquals(product.name, result.name)
        assertEquals(product.externalUUID, result.uuid)
    }

    @Test
    fun convertProductToProductInfo() {
        val product = Product("n", UUID.randomUUID())
        val result = converter.convertProductToProductInfo(product)
        assertEquals(product.name, result.name)
        assertEquals(product.externalUUID, result.uuid)
    }
}