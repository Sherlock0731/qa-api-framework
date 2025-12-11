# Архитектура API Test Framework

## Обзор

Фреймворк построен на принципах модульности, расширяемости и поддержки многопоточности. Архитектура следует best practices тестирования REST API и использует проверенные паттерны проектирования.

## Слои архитектуры

```
┌─────────────────────────────────────────────────┐
│           Test Layer (JUnit 5)                  │
│  - Test Classes (examples/users, products)      │
│  - Annotations (@Tag, @Epic, @Feature)          │
└─────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────┐
│         Client Layer (REST Clients)             │
│  - ReqResClient                                 │
│  - BeeceptorClient                              │
│  - BaseRestClient (abstract)                    │
└─────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────┐
│    Communication Layer (REST Assured)           │
│  - RequestSpecification (thread-local)          │
│  - HTTP Methods (GET, POST, PUT, DELETE)        │
│  - Allure Reporting Integration                 │
└─────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────┐
│      Data Layer (DTOs + Jackson)                │
│  - UserDto, ProductDto, etc.                    │
│  - Serialization/Deserialization                │
└─────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────┐
│   Infrastructure Layer (Config, Utils, Logs)    │
│  - Configuration Management (Owner)             │
│  - Utilities (DateUtils, UuidUtils)             │
│  - Logging (Logback)                            │
└─────────────────────────────────────────────────┘
```

## Основные компоненты

### 1. Test Layer

**Цель**: Организация и выполнение тестов

**Компоненты**:
- `BaseTest` - базовый класс для всех тестов
- Test Classes - конкретные тестовые классы
- JUnit 5 Annotations - метаданные тестов

**Особенности**:
```java
@Execution(ExecutionMode.CONCURRENT)  // Параллельное выполнение
public abstract class BaseTest {
    protected ReqResClient reqResClient;
    protected BeeceptorClient beeceptorClient;
}
```

### 2. Client Layer

**Цель**: Инкапсуляция взаимодействия с API

**Паттерн**: REST Client Pattern (адаптация Page Object для API)

**Компоненты**:
- `BaseRestClient` - абстрактный базовый клиент
- `ReqResClient` - клиент для ReqRes API
- `BeeceptorClient` - клиент для Beeceptor API

**Преимущества**:
- Переиспользование кода
- Легкость поддержки
- Абстракция HTTP деталей
- Интеграция с Allure через @Step

**Пример**:
```java
public class ReqResClient extends BaseRestClient {
    @Step("Get users list")
    public Response getUsersList() {
        return get(API_USERS);
    }
}
```

### 3. Communication Layer

**Цель**: HTTP коммуникация с API

**Библиотека**: REST Assured

**Thread-Safety**:
```java
private static final ThreadLocal<RequestSpecification> requestSpec = new ThreadLocal<>();

protected RequestSpecification getRequestSpec() {
    if (requestSpec.get() == null) {
        requestSpec.set(createRequestSpec());
    }
    return requestSpec.get();
}
```

**Особенности**:
- Thread-local RequestSpecification
- Автоматическое логирование запросов/ответов
- Интеграция с Allure
- Конфигурируемые таймауты и retry

### 4. Data Layer

**Цель**: Представление данных API

**Паттерн**: Data Transfer Object (DTO)

**Библиотеки**:
- Jackson - JSON сериализация/десериализация
- Lombok - генерация boilerplate кода

**Пример DTO**:
```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    @JsonProperty("id")
    private Integer id;
    
    @JsonProperty("email")
    private String email;
    // ...
}
```

**Преимущества**:
- Типобезопасность
- Автоматическая валидация
- Удобство работы с данными
- Immutability через Builder

### 5. Infrastructure Layer

**Цель**: Поддержка инфраструктуры фреймворка

**Компоненты**:

#### Configuration Management
```java
@Config.Sources({
    "system:properties",
    "system:env",
    "classpath:config/${env}.properties"
})
public interface TestConfig extends Config {
    String reqresBaseUrl();
    String reqresApiKey();
    // ...
}
```

**Особенности**:
- Многоуровневая конфигурация
- Environment-specific настройки
- Override через system properties/env vars

#### Utilities
- `DateUtils` - работа с датами (ISO 8601)
- `UuidUtils` - валидация и работа с UUID

#### Logging
```xml
<appender name="SIFT" class="ch.qos.logback.classic.sift.SiftingAppender">
    <!-- Отдельный лог файл для каждого потока -->
</appender>
```

**Особенности**:
- Асинхронная запись (AsyncAppender)
- Thread-safe (prudent mode)
- Логи по потокам отдельно
- Thread ID в каждой записи

## Многопоточность

### Стратегия

Фреймворк использует **параллелизм на уровне методов**:

```xml
<parallel>methods</parallel>
<threadCount>${thread.count}</threadCount>
```

### Thread Safety гарантии

1. **RequestSpecification** - ThreadLocal
2. **Configuration** - Immutable + Cached
3. **Logging** - Asynchronous + Prudent mode
4. **DTOs** - Immutable через Builder
5. **Clients** - создаются в @BeforeEach для каждого теста

### Настройка параллелизма

**Maven**:
```bash
mvn test -Dthread.count=4
```

**JUnit Platform**:
```properties
junit.jupiter.execution.parallel.enabled = true
junit.jupiter.execution.parallel.config.strategy = fixed
junit.jupiter.execution.parallel.config.fixed.parallelism = 4
```

## Управление зависимостями

### Версионирование

Все версии зависимостей определены в properties:
```xml
<properties>
    <junit.version>5.10.1</junit.version>
    <rest-assured.version>5.4.0</rest-assured.version>
    <!-- ... -->
</properties>
```

### Scope управление

- `compile` - основные зависимости (REST Assured, Jackson)
- `provided` - Lombok
- `test` - JUnit, AssertJ (не используется, т.к. тесты в src/test)

## Отчетность

### Allure Integration

**Уровни интеграции**:

1. **Test Level**:
```java
@Epic("Users API")
@Feature("User Login")
@Story("Authentication")
void testLogin() { }
```

2. **Step Level**:
```java
@Step("Login user with email: {userDto.email}")
public Response login(UserDto userDto) { }
```

3. **REST Assured Level**:
```java
.addFilter(new AllureRestAssured())
```

### Artifacts

- Allure Results - `target/allure-results`
- Allure Report - `target/site/allure-maven-plugin`
- Logs - `target/logs/`
- JUnit XML - `target/surefire-reports`

## CI/CD Integration

### GitHub Actions Architecture

```
┌──────────────┐
│  Trigger     │ (push, PR, schedule, manual)
└──────┬───────┘
       ↓
┌──────────────┐
│  Checkout    │
└──────┬───────┘
       ↓
┌──────────────┐
│  Setup Java  │
└──────┬───────┘
       ↓
┌──────────────┐
│  Run Tests   │ (with env vars from secrets)
└──────┬───────┘
       ↓
┌──────────────┐
│Generate Report│
└──────┬───────┘
       ↓
┌──────────────┐
│Upload Artifacts│ (always)
└──────────────┘
```

### Environment Configuration

**Local**:
```properties
env=local
reqres.base.url=https://reqres.in
```

**CI/CD**:
```properties
env=ci
reqres.base.url=${REQRES_BASE_URL}
```

## Расширяемость

### Добавление нового API

1. Создать DTOs в `qa.autotest.app.dto`
2. Создать Client в `qa.autotest.framework.client`
3. Расширить `BaseRestClient`
4. Добавить конфигурацию в `TestConfig`
5. Создать тесты в `examples/`

### Добавление нового окружения

1. Создать `{env}.properties` в `src/main/resources/config/`
2. Определить специфичные настройки
3. Использовать: `mvn test -Denv={env}`

### Добавление новых утилит

Создать класс в `qa.autotest.framework.utils`:
```java
public final class MyUtils {
    private MyUtils() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    public static ReturnType utilityMethod(params) {
        // implementation
    }
}
```

## Best Practices

### 1. Immutability
- DTOs через Builder pattern
- Final для utility классов
- Неизменяемая конфигурация

### 2. Thread Safety
- ThreadLocal для изменяемого состояния
- Stateless где возможно
- Prudent logging

### 3. Separation of Concerns
- Слоистая архитектура
- Каждый компонент - одна ответственность
- Минимальные зависимости между слоями

### 4. Test Organization
- Группировка по функциональности
- Понятные названия (@DisplayName)
- Метаданные через аннотации

### 5. Error Handling
- Explicit assertions
- Meaningful error messages
- Always() blocks для cleanup

## Performance Considerations

### Оптимизации

1. **Параллельное выполнение** - до 8 потоков
2. **Connection pooling** - REST Assured по умолчанию
3. **Async logging** - не блокирует тесты
4. **Maven caching** - в CI/CD
5. **Dependency caching** - Docker layers

### Метрики

Типичное время выполнения:
- 1 тест: ~2-5 секунд
- 30 тестов (sequential): ~90-150 секунд
- 30 тестов (parallel, 4 threads): ~25-40 секунд

## Troubleshooting

### Частые проблемы

1. **ConcurrentModificationException**
   - Решение: проверить thread-safety
   - Использовать ThreadLocal

2. **Connection timeout**
   - Решение: увеличить `request.timeout`
   - Проверить network connectivity

3. **Flaky tests**
   - Решение: добавить retry logic
   - Проверить race conditions

## Заключение

Архитектура фреймворка обеспечивает:
- ✅ Масштабируемость
- ✅ Поддерживаемость
- ✅ Расширяемость
- ✅ Thread-safety
- ✅ CI/CD готовность
- ✅ Полную отчетность

Фреймворк готов к промышленному использованию и может быть легко адаптирован под специфические требования проекта.
