# Матрица тест-кейсов

## Общая информация

- **Всего тест-кейсов**: 28
- **Критичных (P0)**: 12
- **Высокий приоритет (P1)**: 10
- **Средний приоритет (P2)**: 8

## Users API (17 тест-кейсов)

### GET /api/users (5 тестов)

| ID | Название | Приоритет | Класс | Статус |
|----|----------|-----------|-------|--------|
| TC-001 | Successfully get users list with valid API key | P0 | GetUsersListTests | ✅ Implemented |
| TC-002 | Verify user object structure | P0 | GetUsersListTests | ✅ Implemented |
| TC-003 | Get second page of users | P1 | GetUsersListTests | ✅ Implemented |

### POST /api/login (6 тестов)

| ID | Название | Приоритет | Класс | Статус |
|----|----------|-----------|-------|--------|
| TC-006 | Successfully login with valid credentials | P0 | LoginTests | ✅ Implemented |
| TC-007 | Login with incorrect password | P0 | LoginTests | ✅ Implemented |
| TC-008 | Login with non-existent email | P0 | LoginTests | ✅ Implemented |
| TC-009 | Login without password field | P1 | LoginTests | ✅ Implemented |
| TC-010 | Login without email field | P1 | LoginTests | ✅ Implemented |
| TC-011 | Login with invalid email format | P1 | LoginTests | ✅ Implemented |

### POST /api/users (6 тестов)

| ID | Название | Приоритет | Класс | Статус |
|----|----------|-----------|-------|--------|
| TC-012 | Successfully create user with valid data | P0 | CreateUserTests | ✅ Implemented |
| TC-013 | Create user without name field | P1 | CreateUserTests | ✅ Implemented |
| TC-014 | Create user without job field | P1 | CreateUserTests | ✅ Implemented |
| TC-015 | Create user with empty body | P1 | CreateUserTests | ✅ Implemented |
| TC-016 | Verify createdAt field format | P2 | CreateUserTests | ✅ Implemented |
| TC-017 | Verify ID uniqueness | P2 | CreateUserTests | ✅ Implemented |

## Products API (13 тест-кейсов)

### GET /products (4 теста)

| ID | Название | Приоритет | Класс | Статус |
|----|----------|-----------|-------|--------|
| TC-018 | Successfully get products list | P0 | GetProductsListTests | ✅ Implemented |
| TC-019 | Verify product object data types | P0 | GetProductsListTests | ✅ Implemented |
| TC-020 | Verify UUID format in product ID | P2 | GetProductsListTests | ✅ Implemented |
| TC-021 | Verify price validation | P2 | GetProductsListTests | ✅ Implemented |

### GET /product/{id} (4 теста)

| ID | Название | Приоритет | Класс | Статус |
|----|----------|-----------|-------|--------|
| TC-022 | Successfully get product by existing ID | P0 | GetProductByIdTests | ✅ Implemented |
| TC-023 | Get product with non-existent ID | P0 | GetProductByIdTests | ✅ Implemented |
| TC-024 | Get product with invalid UUID format | P2 | GetProductByIdTests | ✅ Implemented |
| TC-025 | Verify data consistency | P2 | GetProductByIdTests | ✅ Implemented |

### POST /products (5 тестов)

| ID | Название | Приоритет | Класс | Статус |
|----|----------|-----------|-------|--------|
| TC-026 | Successfully create product with empty body | P0 | CreateProductTests | ✅ Implemented |
| TC-027 | Verify default values generation | P2 | CreateProductTests | ✅ Implemented |
| TC-028 | Create product with custom data | P0 | CreateProductTests | ✅ Implemented |
| TC-029 | Verify createdAt format with timezone | P2 | CreateProductTests | ✅ Implemented |
| TC-030 | Create product with negative price | P2 | CreateProductTests | ✅ Implemented |

## Статистика покрытия

### По тегам

| Тег | Количество тестов |
|-----|-------------------|
| users | 15 |
| products | 13 |
| smoke | 6 |

## Smoke тесты

Smoke suite включает следующие критичные тесты:

| ID | Название | API |
|----|----------|-----|
| TC-001 | Get users list | Users |
| TC-002 | Verify user structure | Users |
| TC-006 | Login success | Users |
| TC-018 | Get products list | Products |
| TC-019 | Verify product data types | Products |

## Метрики качества

### Целевые метрики

| Метрика | Цель | Текущее значение |
|---------|------|------------------|
| Pass Rate | > 95% | Зависит от запуска |
| Execution Time (all) | < 3 min | ~2-3 min (4 threads) |
| Execution Time (smoke) | < 1 min | ~30-45 sec |
| Flakiness | < 5% | Требует мониторинга |
| Code Coverage | > 80% | N/A (API tests) |

### Требования к стабильности

- Smoke тесты должны проходить на 100%
- Critical (P0) тесты должны проходить на > 98%
- Общий Pass Rate > 95%
- Время выполнения не увеличивается более чем на 10% между релизами

## Интеграция с CI/CD

### Стратегия запуска

| Event | Tests | Threads | Max Time |
|-------|-------|---------|----------|
| Push to develop | Smoke | 4 | 1 min |
| Pull Request | All | 4 | 3 min |
| Nightly | All | 8 | 2 min |
| Release | All | 4 | 5 min |
| Manual (Users) | Users | 2 | 2 min |
| Manual (Products) | Products | 2 | 1.5 min |

## Отчетность

### Allure Features

Отчет содержит:
- ✅ Suites по API (Users, Products)
- ✅ Graphs (Overview, Trends)
- ✅ Behaviors (Epics, Features, Stories)
- ✅ Packages
- ✅ Timeline
- ✅ Categories
- ✅ HTTP запросы/ответы
- ✅ Attachments (logs)

### Метрики в отчете

- Total tests
- Passed/Failed/Broken/Skipped
- Duration
- Retries
- Flaky tests
- Categories of defects

## Поддержка и обновление

### Регулярный review

- Еженедельно: анализ flaky tests
- Ежемесячно: обновление dependencies
- Quarterly: пересмотр test coverage
- Yearly: major refactoring

### Контакты

- Framework Owner: Vitaliy Popravka
- Repository: https://github.com/Sherlock0731/qa-api-framework
- Documentation: docs/
- Issues: https://github.com/Sherlock0731/qa-api-framework/issues
