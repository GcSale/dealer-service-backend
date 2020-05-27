package com.gcsale.dealerbackend.application.commands.products

import com.gcsale.dealerbackend.application.repository.ProductRepository
import com.gcsale.dealerbackend.application.validators.DeleteProductCommandValidator
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class DeleteProductCommandHandlerImpl(private val productRepository: ProductRepository,
                                      deleteProductCommandValidator: DeleteProductCommandValidator) :
        BaseCommandHandler<DeleteProductCommand>(deleteProductCommandValidator), DeleteProductCommandHandler {

    @Transactional
    override fun handle(command: DeleteProductCommand) {
        val product = productRepository.findByExternalUUID(command.externalUUID)
        product?.let { productRepository.delete(it) }
    }
}