package com.gcsale.dealerbackend.application.commands.products

import com.gcsale.dealerbackend.application.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class DeleteProductCommandHandlerImpl(private val productRepository: ProductRepository) : DeleteProductCommandHandler {

    override fun execute(command: DeleteProductCommand) {
        val product = productRepository.findByExternalUUID(command.externalUUID)
        product?.let { productRepository.delete(it) }
    }
}