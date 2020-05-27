package com.gcsale.dealerbackend.application.commands.products

import com.gcsale.dealerbackend.application.helpers.ValidationHelper
import org.springframework.validation.BindingResult
import org.springframework.validation.DataBinder
import org.springframework.validation.Validator

abstract class BaseCommandHandler<T : Any>(private val validator: Validator) : CommandHandler<T> {

    override fun execute(command: T) {
        val results = validate(command)
        ValidationHelper.checkValidation(results)
        handle(command)
    }

    override fun validate(command: T): BindingResult {
        return ValidationHelper.validate(command, validator)
    }

    abstract fun handle(command: T)
}