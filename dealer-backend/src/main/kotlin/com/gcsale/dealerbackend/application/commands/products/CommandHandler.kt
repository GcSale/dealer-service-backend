package com.gcsale.dealerbackend.application.commands.products

interface CommandHandler<T> {
    fun execute(command: T)
}