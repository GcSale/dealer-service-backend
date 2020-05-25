package com.gcsale.dealerbackend.application.commands.products

import java.util.*

data class SaveProductCommand(val externalUUID: UUID, val name: String)

interface SaveProductCommandHandler : CommandHandler<SaveProductCommand> {
    override fun execute(command: SaveProductCommand)
}