package com.gcsale.dealerbackend.application.queries

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

data class ProductsListRequest(val name: String?, val pageable: Pageable)
data class ProductsListItemResponse(val uuid: UUID, val name: String)
data class ProductsListResponse(val page: Page<ProductsListItemResponse>)
data class ProductInfoResponse(val name: String, val uuid: UUID)

interface ProductQueries {
    fun getProductsList(request: ProductsListRequest): ProductsListResponse

    fun getProductInfo(uuid: UUID): ProductInfoResponse?
}
