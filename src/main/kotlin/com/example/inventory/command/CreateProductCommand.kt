package com.example.inventory.command

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class CreateProductCommand(
    @TargetAggregateIdentifier
    val productId: String,
    val name: String,
    val initialQuantity: Int
)
