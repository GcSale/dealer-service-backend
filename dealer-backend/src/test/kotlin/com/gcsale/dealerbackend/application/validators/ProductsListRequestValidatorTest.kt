package com.gcsale.dealerbackend.application.validators

import com.gcsale.dealerbackend.application.queries.ProductsListRequest
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.validation.Errors

internal class ProductsListRequestValidatorTest {

    private val errors = mockk<Errors>()
    private val validator = ProductsListRequestValidator()

    @Test
    fun `validate success`() {
        every { errors.rejectValue(any(), any()) } answers { }
        validator.validate(ProductsListRequest(null, PageRequest.of(1, 1, Sort.by("name"))), errors)

        verify {
            errors wasNot Called
        }
    }

    @Test
    fun `validate no sorts forbidden`() {
        every { errors.rejectValue(any(), any()) } answers { }
        validator.validate(ProductsListRequest(null, PageRequest.of(1, 1)), errors)

        verify {
            errors.rejectValue("pageable.sort", "value.unsorted")
        }
    }

    @Test
    fun `validate several sorts forbidden`() {
        every { errors.rejectValue(any(), any()) } answers { }
        validator.validate(ProductsListRequest(null, PageRequest.of(1, 1, Sort.by("id", "name"))), errors)

        verify {
            errors.rejectValue("pageable.sort", "value.maxlen")
        }
    }

    @Test
    fun `validate sort by forbidden property`() {
        every { errors.rejectValue(any(), any()) } answers { }
        validator.validate(ProductsListRequest(null, PageRequest.of(1, 1, Sort.by("unknown"))), errors)

        verify {
            errors.rejectValue("pageable.sort", "value.non_sortable_field")
        }
    }
}