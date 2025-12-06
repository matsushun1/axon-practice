package com.example.inventory.api;

public class UpdateInventoryRequest {

    private int quantity;

    public UpdateInventoryRequest() {
    }

    public UpdateInventoryRequest(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
