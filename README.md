# Bank Offers Aggregator (lite)

Минимальный pet-проект с микросервисной архитектурой, агрегирующий банковские предложения. Сервисы реализованы на Java 17 / Spring Boot 3 и упакованы для локального запуска через Docker Compose.

## Архитектура

- **gateway-service** – единая точка входа (Spring Cloud Gateway). Проксирует запросы к bank-service и product-service, а также отдаёт агрегированные оферы.
- **bank-service** – CRUD банков (PostgreSQL + Flyway).
- **product-service** – CRUD продуктов, кэш популярных запросов (Redis), публикация событий в Kafka, агрегация оферов через Feign.
- **notification-service** – Kafka consumer, логирует новые продукты и инвалидирует redis-кэш product-service.
- **infra/docker** – docker-compose окружение (Kafka, Zookeeper, Redis, два PostgreSQL, все сервисы).

## Стек

- Java 17, Gradle (Kotlin DSL)
- Spring Boot 3.3, Spring Web/WebFlux, Spring Data JPA, Spring Cache (Redis), Spring for Apache Kafka
- Spring Cloud Gateway, OpenFeign, springdoc-openapi (swagger-ui)
- PostgreSQL + Flyway, Redis, Kafka
- Micrometer + Spring Boot Actuator
- Docker/Docker Compose

## Быстрый старт

1. **Сборка артефактов**

   ```bash
   ./gradlew clean bootJar
   ```

   > Скрипт `gradlew` проксирует установленный Gradle (8.8+) – при необходимости установите Gradle локально.

2. **Запуск окружения**

   ```bash
   cd infra/docker
   docker compose up -d --build
   ```

3. **Проверка статуса**

   ```bash
   docker compose ps
   ```

4. **Остановка**

   ```bash
   docker compose down -v
   ```

## Swagger UI

После старта docker-compose:

- Gateway: http://localhost:8080/swagger-ui/index.html
- Bank-service: http://localhost:8081/swagger-ui/index.html
- Product-service: http://localhost:8082/swagger-ui/index.html
- Notification-service: http://localhost:8083/swagger-ui/index.html

## Примеры запросов (через Gateway)

```bash
# Создать банк
curl -X POST http://localhost:8080/api/v1/banks \
  -H 'Content-Type: application/json' \
  -d '{"name":"Acme Bank","rating":9,"country":"USA"}'

# Создать продукт
curl -X POST http://localhost:8080/api/v1/products \
  -H 'Content-Type: application/json' \
  -d '{"bankId":1,"type":"DEPOSIT","name":"Super Deposit","rate":3.5,"termMonths":12,"currency":"USD"}'

# Получить продукты (кэшируется на 5 минут)
curl 'http://localhost:8080/api/v1/products?page=0&size=10&type=DEPOSIT'

# Получить агрегированные оферы
curl 'http://localhost:8080/api/v1/offers?page=0&size=10'
```

## Поведение системы

- `product-service` кэширует ответы `GET /api/v1/products` в Redis на 5 минут. После первого запроса повторный ответ идёт из кэша и отрабатывает быстрее.
- При создании продукта `product-service` отправляет событие `NEW_PRODUCT_CREATED` в Kafka (`product.events`).
- `notification-service` подписан на `product.events`, пишет лог «New product created» и удаляет соответствующие ключи кэша в Redis.
- Gateway отдаёт `/api/v1/offers`, проксируя агрегированные данные из product-service (банковская информация подтягивается через Feign).

## Добавление нового типа продукта

1. Добавьте константу/значение типа в клиентские приложения.
2. При необходимости расширьте валидацию в `ProductRequest`.
3. Выполните миграцию схемы (если нужны новые поля) через Flyway (`product-service/src/main/resources/db/migration`).
4. Пересоберите `product-service` и обновите docker-compose окружение (`docker compose up -d --build product-service`).

## CI

В `.github/workflows/build.yml` настроен пайплайн GitHub Actions, который выполняет `./gradlew build` и проверяет сборку всех модулей.

## Полезные команды

```bash
# Просмотреть логи сервиса
docker compose logs -f product-service

# Создать топик вручную (опционально)
docker compose exec kafka kafka-topics.sh --bootstrap-server kafka:9092 --create --if-not-exists --topic product.events --replication-factor 1 --partitions 1
```

## TODO / Follow-up

- [ ] Добавить auth (Keycloak / OAuth2) перед gateway
- [ ] Настроить централизованный логинг (ELK / Loki)
- [ ] Добавить e2e и нагрузочные тесты
- [ ] Реализовать мониторинг через Grafana + Prometheus
