package com.example.inventory.query

import com.example.inventory.event.InventoryAddedEvent
import com.example.inventory.event.InventoryRemovedEvent
import com.example.inventory.event.ProductCreatedEvent
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component
import java.util.*

@Component
class ProductProjection(
    private val productRepository: ProductRepository
) {

    // Eventハンドラー: 商品作成時にQueryモデルに追加
    @EventHandler
    fun on(event: ProductCreatedEvent) {
        val product = ProductQueryEntity(
            event.productId,
            event.name,
            event.initialQuantity
        )
        productRepository.save(product)
    }

    // Eventハンドラー: 在庫追加時にQueryモデルを更新
    @EventHandler
    fun on(event: InventoryAddedEvent) {
        productRepository.findById(event.productId).ifPresent { product ->
            product.quantity = product.quantity + event.quantity
            productRepository.save(product)
        }
    }

    // Eventハンドラー: 在庫減少時にQueryモデルを更新
    @EventHandler
    fun on(event: InventoryRemovedEvent) {
        productRepository.findById(event.productId).ifPresent { product ->
            product.quantity = product.quantity - event.quantity
            productRepository.save(product)
        }
    }

    // Queryハンドラー: すべての商品を取得
    @QueryHandler
    fun handle(query: FindAllProductsQuery): List<ProductQueryEntity> {
        return productRepository.findAll()
    }

    // Queryハンドラー: 特定の商品を取得
    @QueryHandler
    fun handle(query: FindProductByIdQuery): Optional<ProductQueryEntity> {
        return productRepository.findById(query.productId)
    }
}
