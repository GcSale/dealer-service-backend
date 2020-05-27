package com.gcsale.dealerbackend.infrastructure.web.dtos

data class PageDto<T>(val items: List<T>, val pageInfo: PageInfo)

data class PageInfo(val pageSize: Int, val pageNumber: Int, val totalPages: Int)
