package com.example.inventory.event;

public class ProductCreatedEvent {

    private final String productId;
    private final String name;
    private final int initialQuantity;

    public ProductCreatedEvent(String productId, String name, int initialQuantity) {
        this.productId = productId;
        this.name = name;
        this.initialQuantity = initialQuantity;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getInitialQuantity() {
        return initialQuantity;
    }
}
