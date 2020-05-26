package com.gcsale.dealerbackend.application.commands.products

import java.util.*

data class DeleteProductCommand(val externalUUID: UUID)

interface DeleteProductCommandHandler : CommandHandler<DeleteProductCommand> {
    override fun execute(command: DeleteProductCommand)
}