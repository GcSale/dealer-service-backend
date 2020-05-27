package com.gcsale.dealerbackend.infrastructure.web.config

import com.gcsale.dealerbackend.infrastructure.web.converters.ProductSortFieldsToEnumConverter
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig : WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(ProductSortFieldsToEnumConverter())
    }
}