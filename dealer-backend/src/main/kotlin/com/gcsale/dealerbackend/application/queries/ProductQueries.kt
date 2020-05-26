package com.gcsale.dealerbackend.application.queries

interface ProductQueries {
    fun getProductsList(request: ProductsListRequest): ProductsListResponse
}