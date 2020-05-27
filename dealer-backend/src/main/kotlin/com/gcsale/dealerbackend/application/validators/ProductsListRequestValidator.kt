package com.gcsale.dealerbackend.application.validators

import com.gcsale.dealerbackend.application.queries.ProductsListRequest
import org.springframework.stereotype.Service
import org.springframework.validation.Errors
import org.springframework.validation.Validator

@Service
class ProductsListRequestValidator : Validator {
    companion object {
        val AVAILABLE_SORTS = listOf("id", "name")
    }

    override fun validate(target: Any, errors: Errors) {
        val request = target as ProductsListRequest
        val sort = request.pageable.sort
        if (sort.isUnsorted) {
            errors.rejectValue("pageable.sort", "value.unsorted")
        } else {
            val orders = sort.iterator().asSequence().toList()
            when {
                orders.size > 1 -> errors.rejectValue("pageable.sort", "value.maxlen")
                orders[0].property !in AVAILABLE_SORTS -> errors.rejectValue("pageable.sort", "value.non_sortable_field")
            }
        }
    }

    override fun supports(clazz: Class<*>): Boolean {
        return ProductsListRequest::class.java == clazz
    }
}