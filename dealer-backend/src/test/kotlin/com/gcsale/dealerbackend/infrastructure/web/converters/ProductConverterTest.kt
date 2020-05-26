package com.gcsale.dealerbackend.infrastructure.web.converters

import com.gcsale.dealerbackend.application.queries.ProductsListItemResponse
import com.gcsale.dealerbackend.infrastructure.web.dtos.ProductListItemDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

internal class ProductConverterTest {

    private val converter = ProductConverter()

    @Test
    fun convertProductListItem() {
        val name = "a"
        val uuid = UUID.randomUUID()
        val expected = ProductListItemDto(name, uuid)
        val actual = converter.convertProductListItem(ProductsListItemResponse(uuid, name))
        assertEquals(expected, actual)
    }
}