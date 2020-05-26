package com.gcsale.dealerbackend.application.converters

import com.gcsale.dealerbackend.application.queries.ProductsListItemResponse
import com.gcsale.dealerbackend.domain.models.Product
import org.springframework.stereotype.Service

@Service
class ProductQueriesConverter {

    fun convertProductToListItem(product: Product): ProductsListItemResponse {
        return ProductsListItemResponse(product.externalUUID, product.name)
    }
}