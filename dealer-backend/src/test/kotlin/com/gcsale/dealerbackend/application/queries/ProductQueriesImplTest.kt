package com.gcsale.dealerbackend.application.queries

import com.gcsale.dealerbackend.application.converters.ProductQueriesConverter
import com.gcsale.dealerbackend.application.repository.ProductRepository
import com.gcsale.dealerbackend.application.validators.ProductsListRequestValidator
import com.gcsale.dealerbackend.domain.models.Product
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.validation.Errors
import java.util.*

internal class ProductQueriesImplTest {

    private val productRepository = mockk<ProductRepository>()
    private val productConverter = mockk<ProductQueriesConverter>()
    private val validator = mockk<ProductsListRequestValidator>()
    private val productQueries = ProductQueriesImpl(productRepository, productConverter, validator)

    @Test
    fun `get products without filtering`() {
        val pageable = PageRequest.of(1, 2)

        val findAllSlot = slot<Pageable>()
        val convertProduct = slot<Product>()
        val validateErrors = slot<Errors>()

        val products = (0..2).map { Product("p${it}", UUID.randomUUID()) }
        every { productRepository.findAll(capture(findAllSlot)) } returns PageImpl(products, pageable, 100)
        every {
            productConverter.convertProductToListItem(capture(convertProduct))
        } answers { ProductsListItemResponse(convertProduct.captured.externalUUID, convertProduct.captured.name) }
        every { validator.supports(any()) } returns true
        every { validator.validate(any(), capture(validateErrors)) } answers {}

        val request = ProductsListRequest(null, pageable)
        val actual = productQueries.getProductsList(request)

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
            validator.supports(ProductsListRequest::class.java)
            validator.validate(request, validateErrors.captured)
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
        val validateErrors = slot<Errors>()

        every {
            productRepository.findAllByNameContainsIgnoreCase(capture(findAllSlotName), capture(findAllSlotPageable))
        } returns PageImpl(products, pageable, 100)
        every {
            productConverter.convertProductToListItem(capture(convertProduct))
        } answers { ProductsListItemResponse(convertProduct.captured.externalUUID, convertProduct.captured.name) }
        every { validator.supports(any()) } returns true
        every { validator.validate(any(), capture(validateErrors)) } answers {}

        val request = ProductsListRequest("p", pageable)
        val actual = productQueries.getProductsList(request)

        assertEquals(100, actual.page.totalElements)
        assertEquals(1, actual.page.number)
        assertEquals(2, actual.page.size)
        assertEquals(50, actual.page.totalPages)

        for (i in 0..2) {
            assertEquals(products[i].name, actual.page.content[i].name)
            assertEquals(products[i].externalUUID, actual.page.content[i].uuid)
        }

        verify {
            productRepository.findAllByNameContainsIgnoreCase("p", pageable)
            validator.supports(ProductsListRequest::class.java)
            validator.validate(request, validateErrors.captured)
        }

        verifySequence {
            productConverter.convertProductToListItem(products[0])
            productConverter.convertProductToListItem(products[1])
            productConverter.convertProductToListItem(products[2])
        }
    }

    @Test
    fun `get existed product info`() {
        val product = Product("prod", UUID.randomUUID())
        val convertProduct = slot<Product>()

        every { productRepository.findByExternalUUID(any()) } returns product
        every {
            productConverter.convertProductToProductInfo(capture(convertProduct))
        } answers { ProductInfoResponse(convertProduct.captured.name, convertProduct.captured.externalUUID) }

        val actual = productQueries.getProductInfo(product.externalUUID)

        assertNotNull(actual)
        assertEquals(product.name, actual!!.name)
        assertEquals(product.externalUUID, actual.uuid)

        verifyAll {
            productRepository.findByExternalUUID(product.externalUUID)
            productConverter.convertProductToProductInfo(product)
        }
    }

    @Test
    fun `get not existed product info`() {
        every { productRepository.findByExternalUUID(any()) } returns null

        val uuid = UUID.randomUUID()
        val actual = productQueries.getProductInfo(uuid)

        assertNull(actual)
        verifyAll {
            productRepository.findByExternalUUID(uuid)
            productConverter wasNot Called
        }
    }
}