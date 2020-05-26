package com.gcsale.dealerbackend.infrastructure.web.controllers

import com.gcsale.dealerbackend.application.commands.products.SaveProductCommand
import com.gcsale.dealerbackend.application.commands.products.SaveProductCommandHandler
import com.gcsale.dealerbackend.application.queries.ProductQueries
import com.gcsale.dealerbackend.application.queries.ProductsListRequest
import com.gcsale.dealerbackend.infrastructure.web.converters.ProductConverter
import com.gcsale.dealerbackend.infrastructure.web.dtos.PageDto
import com.gcsale.dealerbackend.infrastructure.web.dtos.PageInfo
import com.gcsale.dealerbackend.infrastructure.web.dtos.ProductListItemDto
import com.gcsale.dealerbackend.infrastructure.web.dtos.SaveProductIncomeDto
import com.gcsale.dealerbackend.utils.SortUtils
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/products")
class ProductsController(private val productQueries: ProductQueries,
                         private val saveProductCommandHandler: SaveProductCommandHandler,
                         private val productConverter: ProductConverter) {

    @GetMapping("/")
    fun getProducts(
            @RequestParam name: String?,
            @RequestParam(defaultValue = "20") pageSize: Int,
            @RequestParam(defaultValue = "0") page: Int,
            @RequestParam sort: String?): PageDto<ProductListItemDto> {
        val pageable = PageRequest.of(page, pageSize, SortUtils.getSortFromString(sort))
        val data = productQueries.getProductsList(ProductsListRequest(name, pageable))
        val items = data.page.map { productConverter.convertProductListItem(it) }.content
        val pageInfo = PageInfo(data.page.size, data.page.number, data.page.totalPages)
        return PageDto(items, pageInfo)
    }

    @PutMapping("/{uuid}")
    fun saveProduct(@PathVariable uuid: UUID, @RequestBody dto: SaveProductIncomeDto) {
        val command = SaveProductCommand(uuid, dto.name)
        saveProductCommandHandler.execute(command)
    }
}