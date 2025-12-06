package com.example.inventory.query;

public class FindProductByIdQuery {

    private final String productId;

    public FindProductByIdQuery(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }
}
