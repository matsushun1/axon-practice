package com.example.inventory.event

data class InventoryAddedEvent(
    val productId: String,
    val quantity: Int
)
