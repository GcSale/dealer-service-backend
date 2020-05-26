package com.gcsale.dealerbackend.application.validators

import com.gcsale.dealerbackend.application.commands.products.DeleteProductCommand
import org.springframework.stereotype.Service
import org.springframework.validation.Errors
import org.springframework.validation.Validator

@Service
class DeleteProductCommandValidator : Validator {
    override fun validate(target: Any, errors: Errors) {
        // Nothing to check
    }

    override fun supports(clazz: Class<*>): Boolean {
        return DeleteProductCommand::class.java == clazz
    }
}