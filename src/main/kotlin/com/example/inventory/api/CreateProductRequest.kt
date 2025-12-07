package com.example.inventory.api

data class CreateProductRequest(
    var name: String = "",
    var initialQuantity: Int = 0
)
