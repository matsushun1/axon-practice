package com.example.inventory.aggregate

import com.example.inventory.command.AddInventoryCommand
import com.example.inventory.command.CreateProductCommand
import com.example.inventory.command.RemoveInventoryCommand
import com.example.inventory.event.InventoryAddedEvent
import com.example.inventory.event.InventoryRemovedEvent
import com.example.inventory.event.ProductCreatedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class ProductAggregate {

    @AggregateIdentifier
    private lateinit var productId: String
    private lateinit var name: String
    private var quantity: Int = 0

    // Axonが要求するデフォルトコンストラクタ
    constructor()

    // Commandハンドラー: 商品作成
    @CommandHandler
    constructor(command: CreateProductCommand) {
        // ビジネスルール検証
        require(command.initialQuantity >= 0) { "初期在庫数は0以上である必要があります" }

        // Eventを発行
        AggregateLifecycle.apply(
            ProductCreatedEvent(
                command.productId,
                command.name,
                command.initialQuantity
            )
        )
    }

    // Commandハンドラー: 在庫追加
    @CommandHandler
    fun handle(command: AddInventoryCommand) {
        require(command.quantity > 0) { "追加する在庫数は正の数である必要があります" }

        AggregateLifecycle.apply(
            InventoryAddedEvent(
                command.productId,
                command.quantity
            )
        )
    }

    // Commandハンドラー: 在庫減少
    @CommandHandler
    fun handle(command: RemoveInventoryCommand) {
        require(command.quantity > 0) { "減少させる在庫数は正の数である必要があります" }
        require(quantity >= command.quantity) { "在庫が不足しています" }

        AggregateLifecycle.apply(
            InventoryRemovedEvent(
                command.productId,
                command.quantity
            )
        )
    }

    // Eventハンドラー: 商品作成時の状態更新
    @EventSourcingHandler
    fun on(event: ProductCreatedEvent) {
        this.productId = event.productId
        this.name = event.name
        this.quantity = event.initialQuantity
    }

    // Eventハンドラー: 在庫追加時の状態更新
    @EventSourcingHandler
    fun on(event: InventoryAddedEvent) {
        this.quantity += event.quantity
    }

    // Eventハンドラー: 在庫減少時の状態更新
    @EventSourcingHandler
    fun on(event: InventoryRemovedEvent) {
        this.quantity -= event.quantity
    }
}
