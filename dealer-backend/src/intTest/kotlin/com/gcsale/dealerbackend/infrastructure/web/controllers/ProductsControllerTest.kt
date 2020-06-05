package com.gcsale.dealerbackend.infrastructure.web.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gcsale.dealerbackend.application.repository.ProductRepository
import com.gcsale.dealerbackend.domain.models.Product
import com.gcsale.dealerbackend.infrastructure.web.config.ValidationErrorDto
import com.gcsale.dealerbackend.infrastructure.web.dtos.PageDto
import com.gcsale.dealerbackend.infrastructure.web.dtos.ProductInfoDto
import com.gcsale.dealerbackend.infrastructure.web.dtos.ProductListItemDto
import com.gcsale.dealerbackend.infrastructure.web.dtos.SaveProductIncomeDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.options
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*
import javax.transaction.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
internal class ProductsControllerTest {

    @Autowired
    lateinit var productRepository: ProductRepository

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var mapper: ObjectMapper

    @Test
    fun `create new product`() {
        val uuid = UUID.randomUUID()
        val dto = SaveProductIncomeDto("super car")
        mockMvc.perform(put("/v1/products/${uuid}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk)

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
        mockMvc.perform(put("/v1/products/${existedProduct.externalUUID}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk)

        val product = productRepository.findByExternalUUID(existedProduct.externalUUID)
        assertNotNull(product)
        assertNotNull(product!!.id)
        assertEquals(existedProduct.externalUUID, product.externalUUID)
        assertEquals(dto.name, product.name)
    }

    @Test
    fun `create new product validation exception`() {
        val uuid = UUID.randomUUID()
        val dto = SaveProductIncomeDto("c")
        val response = mockMvc.perform(put("/v1/products/${uuid}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andReturn()
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.response.status)
        val data: List<ValidationErrorDto> = mapper.readValue(response.response.contentAsString)
        assertEquals(1, data.size)
        assertEquals("name", data[0].field)
        assertEquals("value.minlen", data[0].errorCode)
    }

    @Test
    fun `find second page of products filtered by name with default sorting id desc`() {
        val products = (0..10).map {
            Product("p${it}", UUID.randomUUID()).also { p -> productRepository.saveAndFlush(p) }
        }
        val expectedProducts = products.reversed().subList(3, 6)

        val response = mockMvc.get("/v1/products/?name=p&pageSize=3&page=1").andReturn()
        assertEquals(HttpStatus.OK.value(), response.response.status)

        val data: PageDto<ProductListItemDto> = mapper.readValue(response.response.contentAsString)
        (0..2).forEach { i ->
            assertEquals(expectedProducts[i].name, data.items[i].name)
            assertEquals(expectedProducts[i].externalUUID, data.items[i].uuid)
        }
    }

    @Test
    fun `find first page of products sorted by name desc`() {
        val products = (0..9).map {
            Product("p${9 - it}", UUID.randomUUID()).also { p -> productRepository.saveAndFlush(p) }
        }
        val expectedProducts = products.subList(0, 4)

        val response = mockMvc.get("/v1/products/?pageSize=4&page=0&sort=-name").andReturn()
        assertEquals(HttpStatus.OK.value(), response.response.status)

        val data: PageDto<ProductListItemDto> = mapper.readValue(response.response.contentAsString)
        (0..3).forEach { i ->
            assertEquals(expectedProducts[i].name, data.items[i].name)
            assertEquals(expectedProducts[i].externalUUID, data.items[i].uuid)
        }
    }

    @Test
    fun `find by name substring case insensitive`() {
        Product("MYSUPERBOAT", UUID.randomUUID()).also { p -> productRepository.saveAndFlush(p) }

        val response = mockMvc.get("/v1/products/?name=super").andReturn()
        assertEquals(HttpStatus.OK.value(), response.response.status)

        val data: PageDto<ProductListItemDto> = mapper.readValue(response.response.contentAsString)
        assertEquals(1, data.items.size)
        assertEquals("MYSUPERBOAT", data.items[0].name)
    }

    @Test
    fun `find find products by non existing property`() {
        val response = mockMvc.get("/v1/products/?pageSize=4&page=0&sort=-unknown").andReturn()
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.response.status)
    }

    @Test
    fun `get info about existed product`() {
        val product = Product("super boat", UUID.randomUUID()).also { productRepository.saveAndFlush(it) }

        val response = mockMvc.get("/v1/products/${product.externalUUID}").andReturn()
        assertEquals(HttpStatus.OK.value(), response.response.status)

        val data: ProductInfoDto = mapper.readValue(response.response.contentAsString)

        assertEquals(product.name, data.name)
        assertEquals(product.externalUUID, data.uuid)
    }

    @Test
    fun `get info about not existed product`() {
        val response = mockMvc.get("/v1/products/${UUID.randomUUID()}").andReturn()
        assertEquals(HttpStatus.NOT_FOUND.value(), response.response.status)
    }

    @Test
    fun `delete existed product`() {
        val product = Product("super boat", UUID.randomUUID()).also { productRepository.saveAndFlush(it) }
        val response = mockMvc.delete("/v1/products/${product.externalUUID}").andReturn()
        assertEquals(HttpStatus.NO_CONTENT.value(), response.response.status)
    }

    @Test
    fun `delete not existed product`() {
        val response = mockMvc.delete("/v1/products/${UUID.randomUUID()}").andReturn()
        assertEquals(HttpStatus.NO_CONTENT.value(), response.response.status)
    }

    @Test
    fun `test accessible by CORS policy`() {
        val response = mockMvc.options("/v1/products", dsl = {
            header(HttpHeaders.ORIGIN, "http://acme.com:3000")
            header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
        }).andReturn()
        assertEquals(HttpStatus.OK.value(), response.response.status)
        assertEquals("http://acme.com:3000", response.response.getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN))
    }
}