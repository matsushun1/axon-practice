package com.example.inventory.query

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "products")
class ProductQueryEntity() {

    @Id
    var productId: String = ""
    var name: String = ""
    var quantity: Int = 0

    constructor(productId: String, name: String, quantity: Int) : this() {
        this.productId = productId
        this.name = name
        this.quantity = quantity
    }
}
