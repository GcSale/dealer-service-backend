package com.gcsale.dealerbackend.services

import com.gcsale.dealerbackend.models.Car
import com.gcsale.dealerbackend.repository.CarsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*

internal class CarsServiceTest {

    private val carsRepository = mockk<CarsRepository>()

    @Test
    fun `Store new car`() {
        val newCarUUID = UUID.randomUUID()
        val name = "mega car"
        val id: Long = 100500

        val slot = slot<Car>()

        every { carsRepository.findByExternalUUID(newCarUUID) } returns null
        every {
            carsRepository.save(capture(slot))
        } returns Car(name, newCarUUID, id)

        val car = CarsService(carsRepository).storeCar(newCarUUID, name)

        assertEquals(car.tempCarName, name)
        assertEquals(car.externalUUID, newCarUUID)
        assertEquals(car.id, id)

        assertTrue(slot.isCaptured)
        assertEquals(slot.captured.tempCarName, name)
        assertEquals(slot.captured.externalUUID, newCarUUID)
    }
}