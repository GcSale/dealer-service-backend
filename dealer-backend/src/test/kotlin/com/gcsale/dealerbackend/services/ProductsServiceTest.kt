package com.gcsale.dealerbackend.services

import com.gcsale.dealerbackend.models.Product
import com.gcsale.dealerbackend.repository.ProductRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*

internal class ProductsServiceTest {

    private val carsRepository = mockk<ProductRepository>()

    @Test
    fun `Store new car`() {
        val newCarUUID = UUID.randomUUID()
        val name = "mega car"
        val id: Long = 100500

        val slot = slot<Product>()

        every { carsRepository.findByExternalUUID(newCarUUID) } returns null
        every {
            carsRepository.save(capture(slot))
        } returns Product(name, newCarUUID, id)

        val car = ProductsService(carsRepository).storeProduct(newCarUUID, name)

        assertEquals(car.name, name)
        assertEquals(car.externalUUID, newCarUUID)
        assertEquals(car.id, id)

        assertTrue(slot.isCaptured)
        assertEquals(slot.captured.name, name)
        assertEquals(slot.captured.externalUUID, newCarUUID)
    }
}