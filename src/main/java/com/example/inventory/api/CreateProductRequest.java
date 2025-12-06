package com.example.inventory.api;

public class CreateProductRequest {

    private String name;
    private int initialQuantity;

    public CreateProductRequest() {
    }

    public CreateProductRequest(String name, int initialQuantity) {
        this.name = name;
        this.initialQuantity = initialQuantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInitialQuantity() {
        return initialQuantity;
    }

    public void setInitialQuantity(int initialQuantity) {
        this.initialQuantity = initialQuantity;
    }
}
