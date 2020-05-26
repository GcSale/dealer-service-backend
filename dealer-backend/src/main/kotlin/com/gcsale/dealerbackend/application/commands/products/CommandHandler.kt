package com.gcsale.dealerbackend.application.commands.products

import org.springframework.validation.BindingResult

interface CommandHandler<T> {
    fun execute(command: T)

    fun validate(command: T): BindingResult
}