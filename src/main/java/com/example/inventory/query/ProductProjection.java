package com.example.inventory.query;

import com.example.inventory.event.InventoryAddedEvent;
import com.example.inventory.event.InventoryRemovedEvent;
import com.example.inventory.event.ProductCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductProjection {

    private final ProductRepository productRepository;

    public ProductProjection(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Eventハンドラー: 商品作成時にQueryモデルに追加
    @EventHandler
    public void on(ProductCreatedEvent event) {
        ProductQueryEntity product = new ProductQueryEntity(
                event.getProductId(),
                event.getName(),
                event.getInitialQuantity()
        );
        productRepository.save(product);
    }

    // Eventハンドラー: 在庫追加時にQueryモデルを更新
    @EventHandler
    public void on(InventoryAddedEvent event) {
        productRepository.findById(event.getProductId()).ifPresent(product -> {
            product.setQuantity(product.getQuantity() + event.getQuantity());
            productRepository.save(product);
        });
    }

    // Eventハンドラー: 在庫減少時にQueryモデルを更新
    @EventHandler
    public void on(InventoryRemovedEvent event) {
        productRepository.findById(event.getProductId()).ifPresent(product -> {
            product.setQuantity(product.getQuantity() - event.getQuantity());
            productRepository.save(product);
        });
    }

    // Queryハンドラー: すべての商品を取得
    @QueryHandler
    public List<ProductQueryEntity> handle(FindAllProductsQuery query) {
        return productRepository.findAll();
    }

    // Queryハンドラー: 特定の商品を取得
    @QueryHandler
    public Optional<ProductQueryEntity> handle(FindProductByIdQuery query) {
        return productRepository.findById(query.getProductId());
    }
}
