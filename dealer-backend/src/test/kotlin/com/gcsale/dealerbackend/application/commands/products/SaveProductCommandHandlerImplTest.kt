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
import io.mockk.verifyAll
import org.junit.jupiter.api.Test
import org.springframework.validation.Errors
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

        val validateErrors = slot<Errors>()

        every { productRepository.findByExternalUUID(any()) } returns null
        every { productRepository.save<Product>(any()) } returns Product(name, newUUID, id)
        every { productService.createProduct(any()) } returns productMock
        every { validator.supports(any()) } returns true
        every { validator.validate(any(), capture(validateErrors)) } answers {}

        val command = SaveProductCommand(newUUID, name)
        handlerImpl.execute(command)

        verifyAll {
            productRepository.findByExternalUUID(newUUID)
            productRepository.save(productMock)
            productService.createProduct(ProductCreationDto(newUUID, name))
            validator.supports(SaveProductCommand::class.java)
            validator.validate(command, validateErrors.captured)
        }
    }

    @Test
    fun `Update existed product`() {
        val productUUID = UUID.randomUUID()
        val name = "mega car"
        val id: Long = 100500
        val newName = "super helicopter"

        val existedProduct = Product(name, productUUID, id)
        val newProduct = Product(newName, productUUID, id)


        val validateErrors = slot<Errors>()

        every { productRepository.findByExternalUUID(any()) } returns existedProduct
        every { productRepository.save<Product>(any()) } returns newProduct
        every { productService.updateProduct(any()) } returns newProduct
        every { validator.supports(any()) } returns true
        every { validator.validate(any(), capture(validateErrors)) } answers {}

        val command = SaveProductCommand(productUUID, newName)
        handlerImpl.execute(command)

        verifyAll {
            productRepository.findByExternalUUID(productUUID)
            productRepository.save(newProduct)
            productService.updateProduct(ProductUpdateDto(existedProduct, newName))
            validator.supports(SaveProductCommand::class.java)
            validator.validate(command, validateErrors.captured)
        }
    }
}