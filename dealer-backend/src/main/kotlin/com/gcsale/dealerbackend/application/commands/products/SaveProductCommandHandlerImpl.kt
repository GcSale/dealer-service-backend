package com.gcsale.dealerbackend.application.commands.products

import com.gcsale.dealerbackend.application.repository.ProductRepository
import com.gcsale.dealerbackend.domain.services.ProductCreationDto
import com.gcsale.dealerbackend.domain.services.ProductService
import com.gcsale.dealerbackend.domain.services.ProductUpdateDto
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class SaveProductCommandHandlerImpl(private val productService: ProductService,
                                    private val productRepository: ProductRepository) : SaveProductCommandHandler {
    @Transactional
    override fun execute(command: SaveProductCommand) {
        val existedProduct = productRepository.findByExternalUUID(command.externalUUID)
        val product = if (existedProduct != null) {
            val dto = ProductUpdateDto(existedProduct, command.name)
            productService.updateProduct(dto)
        } else {
            val dto = ProductCreationDto(command.externalUUID, command.name)
            productService.createProduct(dto)
        }
        productRepository.save(product)
    }
}