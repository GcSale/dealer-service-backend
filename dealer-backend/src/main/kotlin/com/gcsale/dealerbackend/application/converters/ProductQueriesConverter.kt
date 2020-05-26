package com.gcsale.dealerbackend.application.converters

import com.gcsale.dealerbackend.application.queries.ProductInfoResponse
import com.gcsale.dealerbackend.application.queries.ProductsListItemResponse
import com.gcsale.dealerbackend.domain.models.Product
import org.springframework.stereotype.Service

@Service
class ProductQueriesConverter {

    fun convertProductToListItem(product: Product): ProductsListItemResponse {
        return ProductsListItemResponse(name = product.name, uuid = product.externalUUID)
    }

    fun convertProductToProductInfo(product: Product): ProductInfoResponse {
        return ProductInfoResponse(name = product.name, uuid = product.externalUUID)
    }
}