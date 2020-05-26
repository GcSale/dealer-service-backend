package com.gcsale.dealerbackend.application.validators

import com.gcsale.dealerbackend.application.commands.products.SaveProductCommand
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.validation.Errors
import java.util.*

internal class SaveProductCommandValidatorTest {

    private val errors = mockk<Errors>()
    private val validator = SaveProductCommandValidator()

    @Test
    fun `validate success`() {
        every { errors.rejectValue(any(), any()) } answers { }
        validator.validate(SaveProductCommand(UUID.randomUUID(), "super"), errors)

        verify {
            errors wasNot Called
        }
    }

    @Test
    fun `validate name minlen`() {
        every { errors.rejectValue(any(), any()) } answers { }
        validator.validate(SaveProductCommand(UUID.randomUUID(), "s"), errors)

        verify {
            errors.rejectValue("name", "value.minlen")
        }
    }

    @Test
    fun `validate name maxlen`() {
        every { errors.rejectValue(any(), any()) } answers { }
        validator.validate(SaveProductCommand(UUID.randomUUID(), "x".repeat(501)), errors)

        verify {
            errors.rejectValue("name", "value.maxlen")
        }
    }
}