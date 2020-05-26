package com.gcsale.dealerbackend.application.commands.products

import com.gcsale.dealerbackend.application.repository.ProductRepository
import com.gcsale.dealerbackend.application.validators.SaveProductCommandValidator
import com.gcsale.dealerbackend.domain.models.Product
import com.gcsale.dealerbackend.domain.services.ProductCreationDto
import com.gcsale.dealerbackend.domain.services.ProductService
import com.gcsale.dealerbackend.domain.services.ProductUpdateDto
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.util.*

internal class SaveProductCommandHandlerImplTest {

    private val productService = mockk<ProductService>()
    private val productRepository = mockk<ProductRepository>()
    private val validator = mockk<SaveProductCommandValidator>()

    private val handlerImpl = SaveProductCommandHandlerImpl(productService, productRepository, validator)

    @Test
    fun `Create new product`() {
        val newUUID = UUID.randomUUID()
        val name = "mega car"
        val id: Long = 100500
        val productMock = Product(name, newUUID)

        val findProductSlot = slot<UUID>()
        val saveProductSlot = slot<Product>()
        val createProductSlot = slot<ProductCreationDto>()

        every { productRepository.findByExternalUUID(capture(findProductSlot)) } returns null
        every { productRepository.save(capture(saveProductSlot)) } returns Product(name, newUUID, id)
        every { productService.createProduct(capture(createProductSlot)) } returns productMock

        val command = SaveProductCommand(newUUID, name)
        handlerImpl.execute(command)

        assertTrue(findProductSlot.isCaptured)
        assertEquals(newUUID, findProductSlot.captured)

        assertTrue(saveProductSlot.isCaptured)
        assertEquals(productMock, saveProductSlot.captured)

        assertTrue(createProductSlot.isCaptured)
        assertEquals(ProductCreationDto(newUUID, name), createProductSlot.captured)
    }

    @Test
    fun `Update existed product`() {
        val productUUID = UUID.randomUUID()
        val name = "mega car"
        val id: Long = 100500
        val newName = "super helicopter"

        val existedProduct = Product(name, productUUID, id)
        val newProduct = Product(newName, productUUID, id)


        val findProductSlot = slot<UUID>()
        val saveProductSlot = slot<Product>()
        val updateProductSlot = slot<ProductUpdateDto>()

        every { productRepository.findByExternalUUID(capture(findProductSlot)) } returns existedProduct
        every { productRepository.save(capture(saveProductSlot)) } returns newProduct
        every { productService.updateProduct(capture(updateProductSlot)) } returns newProduct

        val command = SaveProductCommand(productUUID, newName)
        handlerImpl.execute(command)

        assertTrue(findProductSlot.isCaptured)
        assertEquals(productUUID, findProductSlot.captured)

        assertTrue(saveProductSlot.isCaptured)
        assertEquals(newProduct, saveProductSlot.captured)

        assertTrue(updateProductSlot.isCaptured)
        assertEquals(ProductUpdateDto(existedProduct, newName), updateProductSlot.captured)
    }
}