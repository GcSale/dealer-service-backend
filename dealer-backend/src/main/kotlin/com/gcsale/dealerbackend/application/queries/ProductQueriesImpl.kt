package com.gcsale.dealerbackend.application.queries

import com.gcsale.dealerbackend.application.converters.ProductQueriesConverter
import com.gcsale.dealerbackend.application.helpers.ValidationHelper
import com.gcsale.dealerbackend.application.repository.ProductRepository
import com.gcsale.dealerbackend.application.validators.ProductsListRequestValidator
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProductQueriesImpl(private val productRepository: ProductRepository,
                         private val productQueriesConverter: ProductQueriesConverter,
                         private val validator: ProductsListRequestValidator) : ProductQueries {

    override fun getProductsList(request: ProductsListRequest): ProductsListResponse {
        ValidationHelper.performValidation(request, validator)

        val page = if (request.name != null) {
            productRepository.findAllByNameContainsIgnoreCase(request.name, request.pageable)
        } else {
            productRepository.findAll(request.pageable)
        }
        return ProductsListResponse(page.map { productQueriesConverter.convertProductToListItem(it) })
    }

    override fun getProductInfo(uuid: UUID): ProductInfoResponse? {
        return productRepository.findByExternalUUID(uuid)
                ?.let { productQueriesConverter.convertProductToProductInfo(it) }
    }
}