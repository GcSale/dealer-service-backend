package com.gcsale.dealerbackend.application.queries

import com.gcsale.dealerbackend.application.converters.ProductQueriesConverter
import com.gcsale.dealerbackend.application.repository.ProductRepository
import com.gcsale.dealerbackend.domain.models.Product
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.util.*

internal class ProductQueriesImplTest {

    private val productRepository = mockk<ProductRepository>()
    private val productConverter = mockk<ProductQueriesConverter>()
    private val productQueries = ProductQueriesImpl(productRepository, productConverter)

    @Test
    fun `get products without filtering`() {
        val pageable = PageRequest.of(1, 2)

        val findAllSlot = slot<Pageable>()
        val convertProduct = slot<Product>()

        val products = (0..2).map { Product("p${it}", UUID.randomUUID()) }
        every { productRepository.findAll(capture(findAllSlot)) } returns PageImpl(products, pageable, 100)
        every {
            productConverter.convertProductToListItem(capture(convertProduct))
        } answers { ProductsListItemResponse(convertProduct.captured.externalUUID, convertProduct.captured.name) }

        val actual = productQueries.getProductsList(ProductsListRequest(null, pageable))

        assertEquals(100, actual.page.totalElements)
        assertEquals(1, actual.page.number)
        assertEquals(2, actual.page.size)
        assertEquals(50, actual.page.totalPages)

        for (i in 0..2) {
            assertEquals(products[i].name, actual.page.content[i].name)
            assertEquals(products[i].externalUUID, actual.page.content[i].uuid)
        }

        verify {
            productRepository.findAll(pageable)
        }

        verifySequence {
            productConverter.convertProductToListItem(products[0])
            productConverter.convertProductToListItem(products[1])
            productConverter.convertProductToListItem(products[2])
        }
    }

    @Test
    fun `get products with filtering by name`() {
        val pageable = PageRequest.of(1, 2)

        val findAllSlotName = slot<String>()
        val findAllSlotPageable = slot<Pageable>()
        val convertProduct = slot<Product>()
        val products = (0..2).map { Product("p${it}", UUID.randomUUID()) }

        every {
            productRepository.findAllByNameContains(capture(findAllSlotName), capture(findAllSlotPageable))
        } returns PageImpl(products, pageable, 100)
        every {
            productConverter.convertProductToListItem(capture(convertProduct))
        } answers { ProductsListItemResponse(convertProduct.captured.externalUUID, convertProduct.captured.name) }

        val actual = productQueries.getProductsList(ProductsListRequest("p", pageable))

        assertEquals(100, actual.page.totalElements)
        assertEquals(1, actual.page.number)
        assertEquals(2, actual.page.size)
        assertEquals(50, actual.page.totalPages)

        for (i in 0..2) {
            assertEquals(products[i].name, actual.page.content[i].name)
            assertEquals(products[i].externalUUID, actual.page.content[i].uuid)
        }

        verify {
            productRepository.findAllByNameContains("p", pageable)
        }

        verifySequence {
            productConverter.convertProductToListItem(products[0])
            productConverter.convertProductToListItem(products[1])
            productConverter.convertProductToListItem(products[2])
        }
    }
}