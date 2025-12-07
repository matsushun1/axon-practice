package com.example.inventory.api

import com.example.inventory.command.AddInventoryCommand
import com.example.inventory.command.CreateProductCommand
import com.example.inventory.command.RemoveInventoryCommand
import com.example.inventory.query.FindAllProductsQuery
import com.example.inventory.query.FindProductByIdQuery
import com.example.inventory.query.ProductQueryEntity
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/api/products")
class ProductController(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway
) {

    // 商品作成
    @PostMapping
    fun createProduct(@RequestBody request: CreateProductRequest): CompletableFuture<String> {
        val productId = UUID.randomUUID().toString()
        return commandGateway.send(
            CreateProductCommand(
                productId,
                request.name,
                request.initialQuantity
            )
        )
    }

    // 在庫追加
    @PostMapping("/{productId}/add-inventory")
    fun addInventory(
        @PathVariable productId: String,
        @RequestBody request: UpdateInventoryRequest
    ): CompletableFuture<Void> {
        return commandGateway.send(AddInventoryCommand(productId, request.quantity))
    }

    // 在庫減少
    @PostMapping("/{productId}/remove-inventory")
    fun removeInventory(
        @PathVariable productId: String,
        @RequestBody request: UpdateInventoryRequest
    ): CompletableFuture<Void> {
        return commandGateway.send(RemoveInventoryCommand(productId, request.quantity))
    }

    // すべての商品を取得
    @GetMapping
    fun getAllProducts(): CompletableFuture<List<ProductQueryEntity>> {
        return queryGateway.query(
            FindAllProductsQuery(),
            ResponseTypes.multipleInstancesOf(ProductQueryEntity::class.java)
        )
    }

    // 特定の商品を取得
    @GetMapping("/{productId}")
    fun getProduct(@PathVariable productId: String): CompletableFuture<ResponseEntity<ProductQueryEntity>> {
        return queryGateway.query(
            FindProductByIdQuery(productId),
            ResponseTypes.optionalInstanceOf(ProductQueryEntity::class.java)
        ).thenApply { optionalProduct ->
            optionalProduct.map { ResponseEntity.ok(it) }
                .orElse(ResponseEntity.notFound().build())
        }
    }
}
