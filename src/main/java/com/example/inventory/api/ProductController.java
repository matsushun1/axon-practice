package com.example.inventory.api;

import com.example.inventory.command.AddInventoryCommand;
import com.example.inventory.command.CreateProductCommand;
import com.example.inventory.command.RemoveInventoryCommand;
import com.example.inventory.query.FindAllProductsQuery;
import com.example.inventory.query.FindProductByIdQuery;
import com.example.inventory.query.ProductQueryEntity;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public ProductController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    // 商品作成
    @PostMapping
    public CompletableFuture<String> createProduct(@RequestBody CreateProductRequest request) {
        String productId = UUID.randomUUID().toString();
        return commandGateway.send(new CreateProductCommand(
                productId,
                request.getName(),
                request.getInitialQuantity()
        ));
    }

    // 在庫追加
    @PostMapping("/{productId}/add-inventory")
    public CompletableFuture<Void> addInventory(
            @PathVariable String productId,
            @RequestBody UpdateInventoryRequest request) {
        return commandGateway.send(new AddInventoryCommand(productId, request.getQuantity()));
    }

    // 在庫減少
    @PostMapping("/{productId}/remove-inventory")
    public CompletableFuture<Void> removeInventory(
            @PathVariable String productId,
            @RequestBody UpdateInventoryRequest request) {
        return commandGateway.send(new RemoveInventoryCommand(productId, request.getQuantity()));
    }

    // すべての商品を取得
    @GetMapping
    public CompletableFuture<List<ProductQueryEntity>> getAllProducts() {
        return queryGateway.query(
                new FindAllProductsQuery(),
                org.axonframework.messaging.responsetypes.ResponseTypes.multipleInstancesOf(ProductQueryEntity.class)
        );
    }

    // 特定の商品を取得
    @GetMapping("/{productId}")
    public CompletableFuture<ResponseEntity<ProductQueryEntity>> getProduct(@PathVariable String productId) {
        return queryGateway.query(
                new FindProductByIdQuery(productId),
                org.axonframework.messaging.responsetypes.ResponseTypes.optionalInstanceOf(ProductQueryEntity.class)
        ).thenApply(optionalProduct ->
                optionalProduct.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build())
        );
    }
}
