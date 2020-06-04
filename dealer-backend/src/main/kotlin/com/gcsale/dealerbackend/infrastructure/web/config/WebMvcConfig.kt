package com.gcsale.dealerbackend.infrastructure.web.config

import com.gcsale.dealerbackend.infrastructure.web.converters.ProductSortFieldsToEnumConverter
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableConfigurationProperties(RestConfigProperties::class)
class WebMvcConfig(private val configProperties: RestConfigProperties) : WebMvcConfigurer {

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(ProductSortFieldsToEnumConverter())
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                .allowedOrigins(configProperties.corsOrigins)
    }
}