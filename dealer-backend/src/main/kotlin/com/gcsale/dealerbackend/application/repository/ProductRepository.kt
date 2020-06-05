package com.gcsale.dealerbackend.application.repository

import com.gcsale.dealerbackend.domain.models.Product
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    fun findByExternalUUID(externalUUID: UUID): Product?
    override fun findAll(pageable: Pageable): Page<Product>
    fun findAllByNameContainsIgnoreCase(name: String, pageable: Pageable): Page<Product>
}