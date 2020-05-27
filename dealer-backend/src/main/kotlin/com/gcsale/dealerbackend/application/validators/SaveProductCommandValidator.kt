package com.gcsale.dealerbackend.application.validators

import com.gcsale.dealerbackend.application.commands.products.SaveProductCommand
import org.springframework.stereotype.Service
import org.springframework.validation.Errors
import org.springframework.validation.Validator

@Service
class SaveProductCommandValidator : Validator {
    override fun validate(target: Any, errors: Errors) {
        val command = target as SaveProductCommand
        if (command.name.length < 3) {
            errors.rejectValue("name", "value.minlen")
        }
        if (command.name.length > 50) {
            errors.rejectValue("name", "value.maxlen")
        }
    }

    override fun supports(clazz: Class<*>): Boolean {
        return SaveProductCommand::class.java == clazz
    }
}