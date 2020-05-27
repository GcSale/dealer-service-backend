package com.gcsale.dealerbackend.application.commands.products

import com.gcsale.dealerbackend.application.repository.ProductRepository
import com.gcsale.dealerbackend.application.validators.DeleteProductCommandValidator
import com.gcsale.dealerbackend.domain.models.Product
import com.gcsale.dealerbackend.helpers.Factories
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.springframework.validation.Errors
import java.util.*

internal class DeleteProductCommandHandlerImplTest {

    private val productRepository = mockk<ProductRepository>()
    private val validator = mockk<DeleteProductCommandValidator>()
    private val deleteProductCommandHandler = DeleteProductCommandHandlerImpl(productRepository, validator)

    @Test
    fun `delete existed product`() {
        val product = Factories.createProduct("super boat")
        val validateErrors = slot<Errors>()

        every { productRepository.findByExternalUUID(any()) } returns product
        every { productRepository.delete(any()) } answers { }
        every { validator.supports(any()) } returns true
        every { validator.validate(any(), capture(validateErrors)) } answers {}

        val command = DeleteProductCommand(product.externalUUID)
        deleteProductCommandHandler.execute(command)

        verifyAll {
            productRepository.findByExternalUUID(product.externalUUID)
            productRepository.delete(product)
            validator.supports(DeleteProductCommand::class.java)
            validator.validate(command, validateErrors.captured)
        }
    }

    @Test
    fun `delete not existed product`() {
        val deleteSlot = slot<Product>()
        val validateErrors = slot<Errors>()

        every { productRepository.findByExternalUUID(any()) } returns null
        every { productRepository.delete(capture(deleteSlot)) } answers { }
        every { validator.supports(any()) } returns true
        every { validator.validate(any(), capture(validateErrors)) } answers {}

        val externalUUID = UUID.randomUUID()
        val command = DeleteProductCommand(externalUUID)
        deleteProductCommandHandler.execute(command)

        verify {
            productRepository.findByExternalUUID(externalUUID)
            validator.supports(DeleteProductCommand::class.java)
            validator.validate(command, validateErrors.captured)
        }
        assertFalse(deleteSlot.isCaptured)
    }
}