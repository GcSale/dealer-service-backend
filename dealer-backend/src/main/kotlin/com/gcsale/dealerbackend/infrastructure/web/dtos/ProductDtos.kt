package com.gcsale.dealerbackend.infrastructure.web.dtos

import java.util.*


data class SaveProductIncomeDto(val name: String)

data class ProductInfoDto(val name: String, val uuid: UUID)

data class ProductListItemDto(val name: String, val uuid: UUID)