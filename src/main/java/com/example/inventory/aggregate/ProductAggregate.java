package com.example.inventory.aggregate;

import com.example.inventory.command.AddInventoryCommand;
import com.example.inventory.command.CreateProductCommand;
import com.example.inventory.command.RemoveInventoryCommand;
import com.example.inventory.event.InventoryAddedEvent;
import com.example.inventory.event.InventoryRemovedEvent;
import com.example.inventory.event.ProductCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class ProductAggregate {

    @AggregateIdentifier
    private String productId;
    private String name;
    private int quantity;

    // Axonが要求するデフォルトコンストラクタ
    protected ProductAggregate() {
    }

    // Commandハンドラー: 商品作成
    @CommandHandler
    public ProductAggregate(CreateProductCommand command) {
        // ビジネスルール検証
        if (command.getInitialQuantity() < 0) {
            throw new IllegalArgumentException("初期在庫数は0以上である必要があります");
        }

        // Eventを発行
        AggregateLifecycle.apply(new ProductCreatedEvent(
                command.getProductId(),
                command.getName(),
                command.getInitialQuantity()
        ));
    }

    // Commandハンドラー: 在庫追加
    @CommandHandler
    public void handle(AddInventoryCommand command) {
        if (command.getQuantity() <= 0) {
            throw new IllegalArgumentException("追加する在庫数は正の数である必要があります");
        }

        AggregateLifecycle.apply(new InventoryAddedEvent(
                command.getProductId(),
                command.getQuantity()
        ));
    }

    // Commandハンドラー: 在庫減少
    @CommandHandler
    public void handle(RemoveInventoryCommand command) {
        if (command.getQuantity() <= 0) {
            throw new IllegalArgumentException("減少させる在庫数は正の数である必要があります");
        }

        if (this.quantity < command.getQuantity()) {
            throw new IllegalArgumentException("在庫が不足しています");
        }

        AggregateLifecycle.apply(new InventoryRemovedEvent(
                command.getProductId(),
                command.getQuantity()
        ));
    }

    // Eventハンドラー: 商品作成時の状態更新
    @EventSourcingHandler
    public void on(ProductCreatedEvent event) {
        this.productId = event.getProductId();
        this.name = event.getName();
        this.quantity = event.getInitialQuantity();
    }

    // Eventハンドラー: 在庫追加時の状態更新
    @EventSourcingHandler
    public void on(InventoryAddedEvent event) {
        this.quantity += event.getQuantity();
    }

    // Eventハンドラー: 在庫減少時の状態更新
    @EventSourcingHandler
    public void on(InventoryRemovedEvent event) {
        this.quantity -= event.getQuantity();
    }
}
