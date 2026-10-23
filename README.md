# Order Processing Platform

REST-сервис для создания и чтения заказов. Стек: **Spring Boot 4.1**, **Java 21**, **PostgreSQL 17**, **Liquibase**, **JPA**.

## Что умеет

| Метод | Путь | Описание |
|-------|------|----------|
| `POST` | `/v1/api/orders` | Создать заказ → `201` + `UUID` |
| `GET` | `/v1/api/orders/{orderId}` | Получить заказ по id |
| `GET` | `/v1/api/orders` | Список с пагинацией (`page`, `size`, `sort`) |

Статусы заказа: `NEW`, `SEND`, `SUCCESS`.

Валидация на уровне DTO и БД: `name`/`price` обязательны, `quantity > 0`, `price > 0`.

## Требования

- JDK **21**
- Maven Wrapper (`mvnw` / `mvnw.cmd`)
- Docker (для PostgreSQL и интеграционных тестов)

## Быстрый старт

```bash
# 1. Поднять БД (порт хоста 5433 → 5432 в контейнере)
docker compose up -d db

# 2. Запустить приложение
./mvnw spring-boot:run
# Windows: mvnw.cmd spring-boot:run
```

Приложение: [http://localhost:8080](http://localhost:8080)  
БД: `jdbc:postgresql://127.0.0.1:5433/order-processing` (`postgres` / `postgres`)

Схема поднимается Liquibase при старте (`ddl-auto: none`).

## Примеры запросов

**Создать заказ**

```bash
curl -s -X POST http://localhost:8080/v1/api/orders \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Laptop\",\"quantity\":2,\"price\":999.99,\"status\":\"NEW\"}"
```

**Получить заказ**

```bash
curl -s http://localhost:8080/v1/api/orders/<orderId>
```

**Список (по 20, сортировка по `created` DESC)**

```bash
curl -s "http://localhost:8080/v1/api/orders?page=0&size=20"
```

Ответ списка — Spring Data `Page` (`content`, `totalElements`, …).

## Структура

```
src/main/java/ru/com/orderplatform/
  controller/     # REST + OpenAPI-аннотации
  service/        # бизнес-логика
  repository/     # Spring Data JPA
  mapper/         # MapStruct DTO ↔ Entity
  dto/            # запрос / ответ
  model/          # JPA-сущность
  exception/      # 404 и обработка ошибок

src/main/resources/
  application.yml
  db.changelog/   # Liquibase (YAML master + SQL changeset)
```

## Тесты

```bash
./mvnw test
```

- **Unit** — сервис, контроллер (MockMvc), mapper, exception handler (AAA)
- **Integration** — `OrdersFlowIntegrationTest`: полный контекст + **Testcontainers PostgreSQL**  
  сценарий: создать → прочитать → проверить запись в БД  
  (нужен Docker)

## Конфиг

| Параметр | Значение по умолчанию |
|----------|------------------------|
| `server.port` | `8080` |
| Datasource URL | `jdbc:postgresql://127.0.0.1:5433/order-processing` |
| Page size | `20` (max `100`) |

Локальную БД остановить: `docker compose down`  
Данные в volume `order-processing-db`.
