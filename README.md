# API Test Automation Framework

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![JUnit](https://img.shields.io/badge/JUnit-5.10.1-red.svg)](https://junit.org/junit5/)
[![Rest-Assured](https://img.shields.io/badge/Rest--Assured-5.4.0-purple.svg)](https://rest-assured.io/)
[![Allure](https://img.shields.io/badge/Allure-2.25.0-yellow.svg)](http://allure.qatools.ru/)

![Tests](https://github.com/Sherlock0731/qa-api-framework/actions/workflows/test-all.yml/badge.svg)    
[![Allure Report](https://img.shields.io/badge/Allure-Report-orange)](https://sherlock0731.github.io/qa-api-framework/)

Многопоточный фреймворк для автоматизации тестирования REST API с использованием современного стека технологий.

## Технологический стек

- **Java 17** - язык программирования
- **Maven** - система сборки и управления зависимостями
- **JUnit 5** - фреймворк для тестирования
- **REST Assured** - библиотека для тестирования REST API
- **AssertJ** - fluent assertions библиотека
- **Allure Report** - система отчетности
- **Lombok** - уменьшение boilerplate кода
- **SLF4J/Logback** - логирование с поддержкой многопоточности
- **Owner** - управление конфигурациями
- **Docker** - контейнеризация
- **GitHub Actions** - CI/CD pipeline

## Структура проекта

```
qa-api-framework/
├── src/
│   ├── main/
│   │   ├── java/qa/autotest/
│   │   │   ├── app/dto/              # Data Transfer Objects
│   │   │   └── framework/            # Core framework
│   │   │       ├── client/           # REST clients
│   │   │       ├── config/           # Configuration
│   │   │       └── utils/            # Utilities
│   │   └── resources/
│   │       ├── config/               # Environment configs
│   │       └── logback.xml           # Logging config
│   └── test/
│       ├── java/examples/            # Test cases
│       │   ├── users/                # Users API tests
│       │   └── products/             # Products API tests
│       └── resources/
├── docker/                           # Docker configs
│   ├── Dockerfile
│   └── docker-compose.yml
├── .github/workflows/                # CI/CD pipelines
│   ├── test-all.yml
│   ├── test-users.yml
│   ├── test-products.yml
│   └── test-smoke.yml
├── docs/                             # Documentation
└── pom.xml                           # Maven config
```

## Быстрый старт

### Предварительные требования

- Java 17 или выше
- Maven 3.8+
- Docker (опционально)

### Локальный запуск

```bash
# Клонировать репозиторий
git clone <repository-url>
cd qa-api-framework

# Запустить все тесты
mvn clean test

# Запустить тесты с 4 потоками
mvn clean test -Dthread.count=4

# Запустить только users тесты
mvn clean test -Pusers

# Запустить только products тесты
mvn clean test -Pproducts

# Запустить smoke тесты
mvn clean test -Psmoke

# Сгенерировать Allure отчет
mvn allure:serve
```

### Запуск в Docker

```bash
# Запустить все тесты
docker-compose -f docker/docker-compose.yml up

# Запустить с параметрами
THREAD_COUNT=4 TEST_GROUPS=users docker-compose -f docker/docker-compose.yml up

# Запустить в фоне
docker-compose -f docker/docker-compose.yml up -d
```

## Тестовые сценарии

Фреймворк включает **27 критичных тест-кейсов**:

### Users API (15 тестов)
- **GET /api/users** - получение списка пользователей (TC-001 - TC-003)
- **POST /api/login** - аутентификация (TC-006 - TC-011)
- **POST /api/users** - создание пользователя (TC-012 - TC-017)

### Products API (12 тестов)
- **GET /products** - получение списка продуктов (TC-018 - TC-021)
- **GET /product/{id}** - получение продукта по ID (TC-022 - TC-024)
- **POST /products** - создание продукта (TC-026 - TC-030)

## Конфигурация

### Файлы конфигурации

Конфигурационные файлы находятся в `src/main/resources/config/`:

- `default.properties` - дефолтные настройки
- `local.properties` - настройки для локального запуска
- `ci.properties` - настройки для CI/CD

### Переменные окружения

Для CI/CD настройте следующие переменные в GitHub Secrets:

```
REQRES_BASE_URL           # Base URL для ReqRes API
REQRES_API_KEY            # API ключ для ReqRes
BEECEPTOR_BASE_URL        # Base URL для Beeceptor API
TEST_USER_EMAIL           # Email тестового пользователя
TEST_USER_PASSWORD        # Пароль тестового пользователя
TEST_USER_FIRSTNAME       # Имя тестового пользователя
TEST_USER_LASTNAME        # Фамилия тестового пользователя
TEST_USER_ID              # ID тестового пользователя
```

### Параметры запуска

```bash
# Количество потоков (default: 1)
-Dthread.count=4

# Окружение (local, ci)
-Denv=local

# Группа тестов
-Dtest.groups=users

# Или через Maven профили
-Pusers
-Pproducts
-Psmoke
```

## Отчеты

### Allure Report

```bash
# Сгенерировать и открыть отчет
mvn allure:serve

# Только сгенерировать
mvn allure:report
```

Отчет содержит:
- Детальную информацию о прохождении тестов
- HTTP запросы и ответы
- Скриншоты (если применимо)
- История выполнения
- Графики и статистику

### Логи

Логи сохраняются в директории `target/logs/`:
- `test-execution.log` - общий лог выполнения
- `thread-{name}.log` - логи по каждому потоку отдельно

## CI/CD Pipeline

### GitHub Actions Workflows

1. **test-all.yml** - запуск всех тестов
   - Триггеры: push в main/develop, pull request, manual
   - Параллельность: настраиваемая (1-8 потоков)

2. **test-users.yml** - запуск users тестов
   - Триггеры: manual, schedule (daily at 2 AM UTC)
   - Профиль: `-Pusers`

3. **test-products.yml** - запуск products тестов
   - Триггеры: manual, schedule (daily at 3 AM UTC)
   - Профиль: `-Pproducts`

4. **test-smoke.yml** - запуск smoke тестов
   - Триггеры: push, pull request
   - Профиль: `-Psmoke`

### Артефакты

После выполнения доступны артефакты:
- Allure результаты
- Allure отчет
- Логи выполнения
- JUnit XML отчеты

## Архитектура

### Паттерны и best practices

1. **Page Object Model** - для REST API адаптирован как REST Client Pattern
2. **Builder Pattern** - для создания DTO объектов
3. **Thread-local** - для изоляции данных между потоками
4. **Configuration Management** - централизованное управление настройками
5. **Dependency Injection** - через конструкторы
6. **Fluent Assertions** - читаемые проверки через AssertJ

### Многопоточность

Фреймворк полностью поддерживает параллельное выполнение:
- Thread-safe RequestSpecification
- Thread-safe логирование
- Изолированные логи по потокам
- Конфигурируемое количество потоков

```java
@Execution(ExecutionMode.CONCURRENT)
public abstract class BaseTest {
    // Thread-safe implementation
}
```

### Логирование

Logback настроен на многопоточную работу:
- Асинхронная запись
- Отдельные файлы по потокам
- Thread ID в каждой записи
- Prudent mode для безопасной записи

## Примеры использования

### Создание нового теста

```java
@Epic("API Name")
@Feature("Feature Name")
@Tag("group-name")
public class MyNewTests extends BaseTest {
    
    @Test
    @DisplayName("TC-XXX: Test description")
    @Description("Detailed description")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Story name")
    void testSomething() {
        // Arrange
        UserDto request = UserDto.builder()
                .email("test@test.com")
                .password("pass123")
                .build();
        
        // Act
        Response response = reqResClient.login(request);
        
        // Assert
        assertThat(response.getStatusCode())
                .as("Status code should be 200")
                .isEqualTo(200);
    }
}
```

### Добавление нового DTO

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyDto {
    
    @JsonProperty("field_name")
    private String fieldName;
}
```

### Добавление нового REST Client

```java
@Slf4j
public class MyApiClient extends BaseRestClient {
    
    public MyApiClient() {
        setBaseUri(CONFIG.myApiBaseUrl());
    }
    
    @Step("Get something")
    public Response getSomething() {
        return get("/endpoint");
    }
}
```

## Contribution

1. Fork репозиторий
2. Создайте feature branch (`git checkout -b feature/amazing-feature`)
3. Commit изменения (`git commit -m 'Add amazing feature'`)
4. Push в branch (`git push origin feature/amazing-feature`)
5. Создайте Pull Request

## Документация

Подробная документация доступна в папке `docs/`:

- [Архитектура](docs/ARCHITECTURE.md)
- [Примеры команд](docs/COMMANDS_EXAMPLES.md)
- [Инструкция по запуску](docs/RUN_INSTRUCTIONS.md)
- [Матрица тест-кейсов](docs/TEST_CASES_MATRIX.md)

## License

[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)

## Authors

- **Vitaliy Popravka** - QA Automation Engineer

## Контакты

Для вопросов и предложений создавайте Issue в репозитории.
