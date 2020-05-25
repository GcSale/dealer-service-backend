package com.gcsale.dealerbackend.infrastructure.web.controllers

import com.gcsale.dealerbackend.application.repository.ProductRepository
import com.gcsale.dealerbackend.controllers.dtos.SaveProductIncomeDto
import com.gcsale.dealerbackend.domain.models.Product
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ProductsControllerTest {

    @Autowired
    lateinit var productRepository: ProductRepository

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun `create new product`() {
        val uuid = UUID.randomUUID()
        val dto = SaveProductIncomeDto("super car")
        testRestTemplate.put("/products/${uuid}", dto, Void::class.java)

        val product = productRepository.findByExternalUUID(uuid)
        assertNotNull(product)
        assertNotNull(product!!.id)
        assertEquals(uuid, product.externalUUID)
        assertEquals(dto.name, product.name)
    }

    @Test
    fun `update product`() {
        val existedProduct = Product("old name", UUID.randomUUID())
        productRepository.save(existedProduct)

        val dto = SaveProductIncomeDto("new name")
        testRestTemplate.put("/products/${existedProduct.externalUUID}", dto, Void::class.java)

        val product = productRepository.findByExternalUUID(existedProduct.externalUUID)
        assertNotNull(product)
        assertNotNull(product!!.id)
        assertEquals(existedProduct.externalUUID, product.externalUUID)
        assertEquals(dto.name, product.name)
    }
}