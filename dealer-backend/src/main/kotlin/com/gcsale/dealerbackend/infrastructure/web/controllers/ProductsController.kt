package com.gcsale.dealerbackend.infrastructure.web.controllers

import com.gcsale.dealerbackend.application.commands.products.DeleteProductCommand
import com.gcsale.dealerbackend.application.commands.products.DeleteProductCommandHandler
import com.gcsale.dealerbackend.application.commands.products.SaveProductCommand
import com.gcsale.dealerbackend.application.commands.products.SaveProductCommandHandler
import com.gcsale.dealerbackend.application.queries.ProductQueries
import com.gcsale.dealerbackend.application.queries.ProductsListRequest
import com.gcsale.dealerbackend.infrastructure.web.converters.ProductConverter
import com.gcsale.dealerbackend.infrastructure.web.dtos.*
import com.gcsale.dealerbackend.utils.SortUtils
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

enum class ProductSortFields(val data: String) {
    ID_ASC("id"),
    ID_DESC("-id"),
    NAME_ASC("name"),
    NAME_DESC("-name");

    override fun toString(): String {
        return data
    }
}

@RestController
@RequestMapping("/v1/products")
class ProductsController(private val productQueries: ProductQueries,
                         private val saveProductCommandHandler: SaveProductCommandHandler,
                         private val productConverter: ProductConverter,
                         private val deleteProductCommandHandler: DeleteProductCommandHandler) {
    @GetMapping("/")
    fun getProducts(
            @RequestParam name: String?,
            @RequestParam(defaultValue = "20") pageSize: Int,
            @RequestParam(defaultValue = "0") page: Int,
            @RequestParam sort: ProductSortFields?): PageDto<ProductListItemDto> {
        val pageable = PageRequest.of(page, pageSize, SortUtils.getSortFromString(sort?.data))
        val data = productQueries.getProductsList(ProductsListRequest(name, pageable))
        val items = data.page.map { productConverter.convertProductListItem(it) }.content
        val pageInfo = PageInfo(data.page.size, data.page.number, data.page.totalPages)
        return PageDto(items, pageInfo)
    }

    @GetMapping("/{uuid}")
    fun getProductInfo(@PathVariable uuid: UUID): ResponseEntity<ProductInfoDto> {
        val data = productQueries.getProductInfo(uuid) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(productConverter.convertProductInfo(data))
    }

    @PutMapping("/{uuid}")
    fun saveProduct(@Valid @PathVariable uuid: UUID, @Valid @RequestBody dto: SaveProductIncomeDto) {
        val command = SaveProductCommand(uuid, dto.name)
        saveProductCommandHandler.execute(command)
    }

    @DeleteMapping("/{uuid}")
    fun deleteProduct(@PathVariable uuid: UUID): ResponseEntity<Void> {
        val command = DeleteProductCommand(uuid)
        deleteProductCommandHandler.execute(command)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}