# Инструкция по запуску тестов

## Локальный запуск

### Предварительные требования

1. Установить Java 17 или выше:
```bash
java -version
```

2. Установить Maven 3.8+:
```bash
mvn -version
```

### Способы запуска

#### 1. Через Maven напрямую

**Все тесты:**
```bash
mvn clean test
```

**С указанием количества потоков:**
```bash
mvn clean test -Dthread.count=4
```

**Только Users тесты:**
```bash
mvn clean test -Pusers
```

**Только Products тесты:**
```bash
mvn clean test -Pproducts
```

**Только Smoke тесты:**
```bash
mvn clean test -Psmoke
```

**Комбинированный запуск:**
```bash
mvn clean test -Pusers -Dthread.count=2
```

#### 2. Через bash скрипт (Linux/Mac)

**Все тесты:**
```bash
./run-tests.sh
```

**С 4 потоками:**
```bash
./run-tests.sh -t 4
```

**Users тесты с 2 потоками:**
```bash
./run-tests.sh -g users -t 2
```

**С генерацией отчета:**
```bash
./run-tests.sh -t 4 -r
```

**Помощь:**
```bash
./run-tests.sh -h
```

#### 3. Через Docker

**Запуск с дефолтными настройками:**
```bash
docker-compose -f docker/docker-compose.yml up
```

**С параметрами:**
```bash
THREAD_COUNT=4 TEST_GROUPS=users docker-compose -f docker/docker-compose.yml up
```

**В фоновом режиме:**
```bash
docker-compose -f docker/docker-compose.yml up -d
```

**Просмотр логов:**
```bash
docker-compose -f docker/docker-compose.yml logs -f
```

**Остановка:**
```bash
docker-compose -f docker/docker-compose.yml down
```

## Генерация отчетов

### Allure Report

**Сгенерировать и открыть в браузере:**
```bash
mvn allure:serve
```

**Только сгенерировать (без открытия):**
```bash
mvn allure:report
```

Отчет будет доступен в: `target/site/allure-maven-plugin/index.html`

### Просмотр логов

**Общий лог:**
```bash
tail -f target/logs/test-execution.log
```

## Запуск в CI/CD

### GitHub Actions

Тесты автоматически запускаются при:
- Push в main/develop
- Pull Request в main
- По расписанию (daily)
- Вручную через GitHub UI

**Ручной запуск:**
1. Перейти в Actions
2. Выбрать workflow (All, Users, Products, Smoke)
3. Нажать "Run workflow"
4. Выбрать количество потоков
5. Запустить

### Настройка переменных окружения

В GitHub Settings → Secrets and variables → Actions добавить:

```
REQRES_BASE_URL=[нужное значение]
REQRES_API_KEY=[нужное значение]
BEECEPTOR_BASE_URL=[нужное значение]
TEST_USER_EMAIL=george.bluth@reqres.in[нужное значение]
TEST_USER_PASSWORD=[нужное значение]
TEST_USER_FIRSTNAME=[нужное значение]
TEST_USER_LASTNAME=[нужное значение]
TEST_USER_ID=[нужное значение]
```

## Troubleshooting

### Проблема: "Tests are not running"

**Решение:**
1. Проверить Java версию: `java -version`
2. Проверить Maven: `mvn -version`
3. Очистить кеш: `mvn clean`
4. Пересобрать: `mvn clean compile`

### Проблема: "Connection timeout"

**Решение:**
1. Проверить интернет соединение
2. Проверить доступность API:
   ```bash
   curl https://reqres.in/api/users
   curl https://mpff4edd8d35adf16840.free.beeceptor.com/products
   ```
3. Увеличить timeout в `config/default.properties`:
   ```properties
   request.timeout=60000
   ```

### Проблема: "Port already in use" (Allure)

**Решение:**
```bash
# Убить процесс на порту
lsof -ti:${PORT} | xargs kill -9

# Или указать другой порт
mvn allure:serve -Dallure.serve.port=8081
```

### Проблема: "Out of memory"

**Решение:**
Увеличить heap size:
```bash
export MAVEN_OPTS="-Xmx2048m"
mvn clean test
```

### Проблема: "Tests are flaky"

**Решение:**
1. Запустить с 1 потоком: `-Dthread.count=1`
2. Проверить thread-safety кода
3. Добавить retry логику
4. Увеличить timeout

## Полезные команды

**Компиляция без тестов:**
```bash
mvn clean compile -DskipTests
```

**Запуск конкретного теста:**
```bash
mvn test -Dtest=GetUsersListTests#testGetUsersListSuccess
```

**Запуск конкретного класса:**
```bash
mvn test -Dtest=GetUsersListTests
```

**С дебаг выводом:**
```bash
mvn test -X
```

**Offline режим:**
```bash
mvn test -o
```

**Пропустить compilation:**
```bash
mvn surefire:test
```

## Примеры типичных сценариев

**Быстрая проверка (smoke):**
```bash
./run-tests.sh -g smoke -t 4
```

**Полный прогон:**
```bash
./run-tests.sh -t 8 -r
```

**Тестирование Users API:**
```bash
./run-tests.sh -g users -t 2 -r
```

**Тестирование Products API:**
```bash
./run-tests.sh -g products -t 2 -r
```

**Debug режим (1 поток):**
```bash
./run-tests.sh -t 1
```
