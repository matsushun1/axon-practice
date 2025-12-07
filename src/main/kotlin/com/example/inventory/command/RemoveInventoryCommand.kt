package com.example.inventory.command

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class RemoveInventoryCommand(
    @TargetAggregateIdentifier
    val productId: String,
    val quantity: Int
)
