package com.example.inventory.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class RemoveInventoryCommand {

    @TargetAggregateIdentifier
    private final String productId;
    private final int quantity;

    public RemoveInventoryCommand(String productId, int quantity) {
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
