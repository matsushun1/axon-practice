package com.example.inventory.event;

public class InventoryAddedEvent {

    private final String productId;
    private final int quantity;

    public InventoryAddedEvent(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
