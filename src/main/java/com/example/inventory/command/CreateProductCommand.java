package com.example.inventory.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class CreateProductCommand {

    @TargetAggregateIdentifier
    private final String productId;
    private final String name;
    private final int initialQuantity;

    public CreateProductCommand(String productId, String name, int initialQuantity) {
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
