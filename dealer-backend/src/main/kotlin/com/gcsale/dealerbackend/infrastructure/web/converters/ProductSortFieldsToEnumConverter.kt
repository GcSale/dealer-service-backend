package com.gcsale.dealerbackend.infrastructure.web.converters

import com.gcsale.dealerbackend.infrastructure.web.controllers.ProductSortFields
import org.springframework.core.convert.converter.Converter

class ProductSortFieldsToEnumConverter : Converter<String, ProductSortFields> {
    override fun convert(source: String): ProductSortFields? {
        return ProductSortFields.values().first { it.data == source }
    }
}