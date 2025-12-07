package com.example.inventory.event

data class ProductCreatedEvent(
    val productId: String,
    val name: String,
    val initialQuantity: Int
)
