package com.gcsale.dealerbackend.application.commands.products

import com.gcsale.dealerbackend.application.exceptions.ValidationException
import org.springframework.validation.BindingResult
import org.springframework.validation.DataBinder
import org.springframework.validation.Validator

abstract class BaseCommandHandler<T>(val validator: Validator) : CommandHandler<T> {

    override fun execute(command: T) {
        val errors = validate(command)
        if (errors.hasErrors()) throw ValidationException(errors.allErrors)
        handle(command)
    }

    override fun validate(command: T): BindingResult {
        val binder = DataBinder(command)
        binder.addValidators(validator)
        binder.validate()
        return binder.bindingResult
    }

    abstract fun handle(command: T)
}