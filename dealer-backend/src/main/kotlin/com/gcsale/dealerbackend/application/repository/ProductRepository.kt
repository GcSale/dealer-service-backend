package com.gcsale.dealerbackend.application.repository

import com.gcsale.dealerbackend.domain.models.Product
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProductRepository : CrudRepository<Product, Long> {
    fun findByExternalUUID(externalUUID: UUID): Product?
}