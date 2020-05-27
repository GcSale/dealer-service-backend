package com.gcsale.dealerbackend.infrastructure.web.dtos

import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


data class SaveProductIncomeDto(@field:NotNull @field:Size(min = 3, max = 500) val name: String)

data class ProductInfoDto(val name: String, val uuid: UUID)

data class ProductListItemDto(val name: String, val uuid: UUID)