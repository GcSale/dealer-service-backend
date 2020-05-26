package com.gcsale.dealerbackend.application.queries

import com.gcsale.dealerbackend.application.converters.ProductQueriesConverter
import com.gcsale.dealerbackend.application.repository.ProductRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

data class ProductsListRequest(val name: String?, val pageable: Pageable)
data class ProductsListItemResponse(val uuid: UUID, val name: String)
data class ProductsListResponse(val page: Page<ProductsListItemResponse>)

@Service
class ProductQueriesImpl(private val productRepository: ProductRepository,
                         private val productQueriesConverter: ProductQueriesConverter) : ProductQueries {

    override fun getProductsList(request: ProductsListRequest): ProductsListResponse {
        val page = if (request.name != null) {
            productRepository.findAllByNameContains(request.name, request.pageable)
        } else {
            productRepository.findAll(request.pageable)
        }
        return ProductsListResponse(page.map { productQueriesConverter.convertProductToListItem(it) })
    }
}