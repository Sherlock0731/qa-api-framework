# Примеры команд запуска

## Maven команды

### Базовые команды

```bash
# Компиляция проекта
mvn clean compile

# Запуск всех тестов
mvn clean test

# Пропустить тесты при сборке
mvn clean install -DskipTests

# Запуск с выводом stacktrace
mvn clean test -e

# Запуск с debug информацией
mvn clean test -X
```

### Запуск по профилям

```bash
# Users тесты
mvn clean test -Pusers

# Products тесты
mvn clean test -Pproducts

# Smoke тесты
mvn clean test -Psmoke

# Все тесты (по умолчанию)
mvn clean test -Pall-tests
```

### Параллельное выполнение

```bash
# 1 поток (последовательное выполнение)
mvn clean test -Dthread.count=1

# 2 потока
mvn clean test -Dthread.count=2

# 4 потока
mvn clean test -Dthread.count=4

# 8 потоков (максимальная производительность)
mvn clean test -Dthread.count=8
```

### Комбинированные запуски

```bash
# Users тесты в 2 потока
mvn clean test -Pusers -Dthread.count=2

# Products тесты в 4 потока с отчетом
mvn clean test -Pproducts -Dthread.count=4 && mvn allure:serve

# Smoke тесты в 4 потока
mvn clean test -Psmoke -Dthread.count=4

# Все тесты в 8 потоков
mvn clean test -Dthread.count=8
```

### Запуск конкретных тестов

```bash
# Один тестовый класс
mvn test -Dtest=GetUsersListTests

# Один тестовый метод
mvn test -Dtest=GetUsersListTests#testGetUsersListSuccess

# Несколько классов
mvn test -Dtest=GetUsersListTests,LoginTests

# По паттерну
mvn test -Dtest=*LoginTests
mvn test -Dtest=Get*Tests
```

### Конфигурация окружения

```bash
# Локальное окружение (по умолчанию)
mvn clean test -Denv=local

# CI окружение
mvn clean test -Denv=ci

# С переопределением параметров
mvn clean test \
  -Denv=local \
  -Dreqres.base.url=https://custom-url.com \
  -Dreqres.api.key=custom_key
```

### Allure отчеты

```bash
# Сгенерировать и открыть отчет
mvn allure:serve

# Только сгенерировать отчет
mvn allure:report

# Очистить результаты
mvn allure:clean

# Запустить тесты и сразу открыть отчет
mvn clean test && mvn allure:serve

# Сгенерировать с историей
mvn allure:report
```

## Bash скрипт

```bash
# Базовый запуск
./run-tests.sh

# С помощью
./run-tests.sh --help

# Все тесты с 4 потоками
./run-tests.sh --threads 4

# Users тесты
./run-tests.sh --group users

# Products тесты с отчетом
./run-tests.sh --group products --report

# Smoke тесты в 8 потоков
./run-tests.sh -g smoke -t 8

# Комбинированный запуск с отчетом
./run-tests.sh -g users -t 2 -e local -r

# CI окружение
./run-tests.sh -e ci -t 4
```

## Docker команды

### Docker Compose

```bash
# Базовый запуск
docker-compose -f docker/docker-compose.yml up

# С переменными окружения
ENV=local THREAD_COUNT=4 docker-compose -f docker/docker-compose.yml up

# Users тесты
TEST_GROUPS=users THREAD_COUNT=2 docker-compose -f docker/docker-compose.yml up

# Products тесты
TEST_GROUPS=products THREAD_COUNT=2 docker-compose -f docker/docker-compose.yml up

# Smoke тесты
TEST_GROUPS=smoke THREAD_COUNT=4 docker-compose -f docker/docker-compose.yml up

# В фоновом режиме
docker-compose -f docker/docker-compose.yml up -d

# Остановка
docker-compose -f docker/docker-compose.yml down

# Пересборка образа
docker-compose -f docker/docker-compose.yml build

# Просмотр логов
docker-compose -f docker/docker-compose.yml logs -f
```

### Docker напрямую

```bash
# Сборка образа
docker build -t api-tests -f docker/Dockerfile .

# Запуск контейнера
docker run --rm api-tests

# С пробросом volume
docker run --rm \
  -v $(pwd)/target:/app/target \
  -v $(pwd)/logs:/app/logs \
  api-tests

# С переменными окружения
docker run --rm \
  -e thread.count=4 \
  -e REQRES_BASE_URL=https://reqres.in \
  api-tests mvn clean test -Dthread.count=4

# Интерактивный режим
docker run -it --rm api-tests /bin/sh
```

## GitHub Actions

### Через веб-интерфейс

1. Перейти в Actions
2. Выбрать workflow:
   - API Tests - All
   - API Tests - Users
   - API Tests - Products
   - API Tests - Smoke
3. Нажать "Run workflow"
4. Выбрать параметры (thread count)
5. Нажать "Run workflow"

### Через GitHub CLI

```bash
# Установка GitHub CLI
brew install gh  # Mac
# или
sudo apt install gh  # Linux

# Аутентификация
gh auth login

# Запуск workflow
gh workflow run "API Tests - All" --ref main

# С параметрами
gh workflow run "API Tests - All" \
  --ref main \
  -f thread_count=4

# Список workflows
gh workflow list

# Просмотр запусков
gh run list

# Просмотр логов последнего запуска
gh run view --log
```

## IDE Integration

### IntelliJ IDEA

```bash
# Через Run Configuration:
# 1. Edit Configurations...
# 2. Add New -> JUnit
# 3. Test kind: All in package
# 4. Package: examples
# 5. VM options: -Dthread.count=4

# Или Maven goal:
# 1. Maven Projects -> Lifecycle -> test
# 2. Right click -> Run
# 3. Modify Run Configuration
# 4. Command line: clean test -Dthread.count=4
```

### VS Code

```json
// .vscode/launch.json
{
  "type": "java",
  "name": "Run All Tests",
  "request": "launch",
  "mainClass": "",
  "projectName": "api-test-framework",
  "args": "clean test -Dthread.count=4"
}
```

## Continuous Integration примеры

### Jenkins

```groovy
pipeline {
    agent any
    
    stages {
        stage('Test') {
            steps {
                sh 'mvn clean test -Dthread.count=4'
            }
        }
        
        stage('Report') {
            steps {
                allure includeProperties: false,
                       jdk: '',
                       results: [[path: 'target/allure-results']]
            }
        }
    }
}
```

### GitLab CI

```yaml
test:
  image: maven:3.9.6-eclipse-temurin-17
  script:
    - mvn clean test -Dthread.count=4
  artifacts:
    reports:
      junit: target/surefire-reports/*.xml
    paths:
      - target/allure-results
```

### CircleCI

```yaml
version: 2.1

jobs:
  test:
    docker:
      - image: maven:3.9.6-eclipse-temurin-17
    steps:
      - checkout
      - run: mvn clean test -Dthread.count=4
      - store_artifacts:
          path: target/allure-results
```

## Debugging

```bash
# Maven с remote debugging
mvn clean test -Dmaven.surefire.debug

# На определенном порту
mvn clean test -Dmaven.surefire.debug="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005"

# IntelliJ IDEA Remote Debug:
# 1. Run -> Edit Configurations
# 2. Add New -> Remote JVM Debug
# 3. Port: 5005
# 4. Run в debug mode

# Один тест с debug
mvn test -Dtest=GetUsersListTests#testGetUsersListSuccess -Dmaven.surefire.debug
```

## Профилирование

```bash
# С профайлером
mvn clean test -Dthread.count=4 -Djava.util.logging.config.file=logging.properties

# Измерение времени выполнения
time mvn clean test -Dthread.count=4

# С подробной статистикой
mvn clean test -Dthread.count=4 --debug
```

## Утилитарные команды

```bash
# Обновить dependencies
mvn clean install -U

# Показать dependency tree
mvn dependency:tree

# Проверить обновления плагинов
mvn versions:display-plugin-updates

# Проверить обновления dependencies
mvn versions:display-dependency-updates

# Скачать dependencies офлайн
mvn dependency:go-offline

# Анализ кода
mvn spotbugs:check

# Форматирование кода
mvn fmt:format
```

## Часто используемые комбинации

```bash
# Быстрая проверка (smoke тесты)
mvn clean test -Psmoke -Dthread.count=4

# Полный прогон с отчетом
mvn clean test -Dthread.count=8 && mvn allure:serve

# Debug конкретного теста
mvn test -Dtest=LoginTests#testLoginSuccess -Dmaven.surefire.debug

# CI-подобный запуск локально
mvn clean test -Denv=ci -Dthread.count=4

# Пересборка и запуск с нуля
mvn clean install -DskipTests && mvn test -Dthread.count=4

# Запуск с retry для flaky тестов
mvn clean test -Dsurefire.rerunFailingTestsCount=2

# Production-like запуск
ENV=prod THREAD_COUNT=4 docker-compose -f docker/docker-compose.yml up
```

## Переменные окружения

```bash
# Linux/Mac
export REQRES_BASE_URL=https://reqres.in
export REQRES_API_KEY=your_api_key
export THREAD_COUNT=4
mvn clean test

# Windows
set REQRES_BASE_URL=https://reqres.in
set REQRES_API_KEY=your_api_key
set THREAD_COUNT=4
mvn clean test

# Или inline
REQRES_BASE_URL=https://reqres.in mvn clean test
```

## Полезные алиасы (добавить в .bashrc или .zshrc)

```bash
# Базовые
alias api-test='mvn clean test'
alias api-test-fast='mvn clean test -Dthread.count=8'
alias api-test-users='mvn clean test -Pusers'
alias api-test-products='mvn clean test -Pproducts'
alias api-test-smoke='mvn clean test -Psmoke'

# С отчетом
alias api-report='mvn allure:serve'
alias api-test-report='mvn clean test && mvn allure:serve'

# Docker
alias api-docker='docker-compose -f docker/docker-compose.yml up'
alias api-docker-down='docker-compose -f docker/docker-compose.yml down'

# Комбинированные
alias api-quick='mvn clean test -Psmoke -Dthread.count=4'
alias api-full='mvn clean test -Dthread.count=8 && mvn allure:serve'
```
