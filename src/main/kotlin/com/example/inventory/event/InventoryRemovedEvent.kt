package com.example.inventory.event

data class InventoryRemovedEvent(
    val productId: String,
    val quantity: Int
)
