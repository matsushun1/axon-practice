# Axon Framework 在庫管理デモ

Axon FrameworkとSpring Bootを使った商品在庫管理アプリケーションです。Event SourcingとCQRSパターンの基本を学ぶための最小限の実装です。

## プロジェクト構成

```
src/main/java/com/example/inventory/
├── aggregate/          # Aggregate (ビジネスロジックとイベント発行)
│   └── ProductAggregate.java
├── command/            # Commands (書き込みリクエスト)
│   ├── CreateProductCommand.java
│   ├── AddInventoryCommand.java
│   └── RemoveInventoryCommand.java
├── event/              # Events (状態変更を表すイベント)
│   ├── ProductCreatedEvent.java
│   ├── InventoryAddedEvent.java
│   └── InventoryRemovedEvent.java
├── query/              # Query側 (読み取り専用モデル)
│   ├── ProductQueryEntity.java
│   ├── ProductRepository.java
│   ├── ProductProjection.java
│   ├── FindAllProductsQuery.java
│   └── FindProductByIdQuery.java
├── api/                # REST API
│   ├── ProductController.java
│   ├── CreateProductRequest.java
│   └── UpdateInventoryRequest.java
└── InventoryApplication.java
```

## Axon Frameworkの主要コンセプト

### 1. Command（コマンド）
- システムへの書き込みリクエスト
- 商品作成、在庫追加、在庫減少など
- `@TargetAggregateIdentifier`でAggregateを特定

### 2. Event（イベント）
- システム内で発生した事実を表す
- 過去形で命名（ProductCreated、InventoryAdded）
- Event Sourcingの基盤

### 3. Aggregate（アグリゲート）
- ビジネスロジックの中心
- Commandを受け取り、検証してEventを発行
- `@CommandHandler`でCommandを処理
- `@EventSourcingHandler`で自身の状態を更新

### 4. Projection（プロジェクション）
- Eventを受け取りQueryモデルを更新
- `@EventHandler`でEventを処理
- `@QueryHandler`でQueryに応答

### 5. CQRS（Command Query Responsibility Segregation）
- 書き込み（Command）と読み取り（Query）を分離
- 書き込み側: Aggregate
- 読み取り側: Projection + Query Model

## 実行方法

### 1. アプリケーションの起動

```bash
# Gradleを使用
./gradlew bootRun

# またはIDEから InventoryApplication.java を実行
```

### 2. API使用例

#### 商品を作成
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name": "ノートPC", "initialQuantity": 10}'
```

レスポンス: `"3fa85f64-5717-4562-b3fc-2c963f66afa6"` (商品ID)

#### すべての商品を取得
```bash
curl http://localhost:8080/api/products
```

#### 特定の商品を取得
```bash
curl http://localhost:8080/api/products/3fa85f64-5717-4562-b3fc-2c963f66afa6
```

#### 在庫を追加
```bash
curl -X POST http://localhost:8080/api/products/3fa85f64-5717-4562-b3fc-2c963f66afa6/add-inventory \
  -H "Content-Type: application/json" \
  -d '{"quantity": 5}'
```

#### 在庫を減少
```bash
curl -X POST http://localhost:8080/api/products/3fa85f64-5717-4562-b3fc-2c963f66afa6/remove-inventory \
  -H "Content-Type: application/json" \
  -d '{"quantity": 3}'
```

### 3. H2 Database Console

開発用のH2コンソールにアクセスできます:
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:inventory`
- Username: `sa`
- Password: (空白)

## 学習ポイント

### Event Sourcingの流れ

1. **Commandの送信**: REST API → CommandGateway
2. **Aggregateで処理**:
   - `@CommandHandler`がCommandを受け取る
   - ビジネスルールを検証
   - `AggregateLifecycle.apply()`でEventを発行
3. **Event Sourcing**:
   - `@EventSourcingHandler`でAggregateの状態を更新
   - Eventは永続化される（Event Store）
4. **Projectionで更新**:
   - `@EventHandler`がEventを受け取る
   - QueryモデルをUpdate（データベース）
5. **Queryの実行**:
   - REST API → QueryGateway
   - `@QueryHandler`がQueryを処理
   - Queryモデルから結果を返す

### 重要な設定

`application.properties`の重要な設定:

```properties
# Axon Serverを無効化（単体動作モード）
axon.axonserver.enabled=false

# Event StoreとしてJPAを使用（H2 Database）
```

## 次のステップ

このプロジェクトを拡張するアイデア:

1. **Saga実装**: 複雑なビジネスプロセスの実装
2. **イベントリプレイ**: 過去のイベントから状態を再構築
3. **複数のProjection**: 異なる用途の読み取りモデル
4. **Deadlineハンドリング**: 時間ベースのイベント
5. **Axon Serverの導入**: 分散システムとしての運用

## 依存関係

- Spring Boot 3.2.0
- Axon Framework 4.9.1
- H2 Database（開発用インメモリDB）
- Spring Data JPA

## 参考資料

- [Axon Framework公式ドキュメント](https://docs.axoniq.io/)
- [CQRSパターン](https://martinfowler.com/bliki/CQRS.html)
- [Event Sourcing](https://martinfowler.com/eaaDev/EventSourcing.html)
