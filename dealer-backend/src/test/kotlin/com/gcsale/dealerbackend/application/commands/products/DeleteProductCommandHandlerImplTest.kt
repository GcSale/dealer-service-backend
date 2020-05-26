package com.gcsale.dealerbackend.application.commands.products

import com.gcsale.dealerbackend.application.repository.ProductRepository
import com.gcsale.dealerbackend.application.validators.DeleteProductCommandValidator
import com.gcsale.dealerbackend.domain.models.Product
import com.gcsale.dealerbackend.helpers.Factories
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.util.*

internal class DeleteProductCommandHandlerImplTest {

    private val productRepository = mockk<ProductRepository>()
    private val validator = mockk<DeleteProductCommandValidator>()
    private val deleteProductCommandHandler = DeleteProductCommandHandlerImpl(productRepository, validator)

    @Test
    fun `delete existed product`() {
        val product = Factories.createProduct("super boat")

        every { productRepository.findByExternalUUID(any()) } returns product
        every { productRepository.delete(any()) } answers { }

        deleteProductCommandHandler.execute(DeleteProductCommand(product.externalUUID))

        verifyAll {
            productRepository.findByExternalUUID(product.externalUUID)
            productRepository.delete(product)
        }
    }

    @Test
    fun `delete not existed product`() {
        val deleteSlot = slot<Product>()
        every { productRepository.findByExternalUUID(any()) } returns null
        every { productRepository.delete(capture(deleteSlot)) } answers { }

        val externalUUID = UUID.randomUUID()
        deleteProductCommandHandler.execute(DeleteProductCommand(externalUUID))

        verify {
            productRepository.findByExternalUUID(externalUUID)
        }
        assertFalse(deleteSlot.isCaptured)
    }
}