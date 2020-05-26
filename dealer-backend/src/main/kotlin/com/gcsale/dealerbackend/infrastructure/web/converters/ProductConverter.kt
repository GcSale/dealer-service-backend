package com.gcsale.dealerbackend.infrastructure.web.converters

import com.gcsale.dealerbackend.application.queries.ProductsListItemResponse
import com.gcsale.dealerbackend.infrastructure.web.dtos.ProductListItemDto
import org.springframework.stereotype.Service

@Service
class ProductConverter {
    fun convertProductListItem(item: ProductsListItemResponse): ProductListItemDto {
        return ProductListItemDto(item.name, item.uuid)
    }
}