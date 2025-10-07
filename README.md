# 🏦 Bank Microservices Project

## 🎯 **Что это за проект?**
Учебное **микросервисное банковское приложение** для освоения современных Java технологий и многопоточности в Spring Boot.

## 🚀 **Основные цели**
- Закрепить **многопоточное программирование** в Spring
- Освоить **микросервисную архитектуру**
- Научиться работать с **Docker** и контейнеризацией
- Попрактиковать **современные Java фичи** (Streams, Lambdas, Collections)

## 🛠 **Технологический стек**

### **Backend**
- **Java 17** + **Spring Boot 3.2**
- **Spring Cloud** - микросервисы
- **Spring Security** + **JWT** - безопасность
- **Spring Data JPA** - работа с БД

### **Базы данных & Кэширование**
- **PostgreSQL** - основная БД
- **Redis** - кэширование, сессии, rate limiting
- **Kafka** - асинхронная коммуникация

### **Инфраструктура**
- **Docker** + **Docker Compose** - контейнеризация
- **Eureka** - service discovery
- **API Gateway** - единая точка входа
- **Maven** - сборка проекта

### **Frontend**
- **React** - пользовательский интерфейс
- **WebSocket** - real-time уведомления

## 📊 **Что буду практиковать**

### **Многопоточность**
- `@Async` аннотации
- `CompletableFuture`
- `ThreadPoolTaskExecutor`
- Потокобезопасные операции

### **Java Modern Features**
- **Stream API** - обработка данных
- **Lambda выражения** - функциональный стиль
- **Collections** - эффективные структуры данных

### **Microservices Patterns**
- Service Discovery
- API Gateway
- Inter-service communication
- Distributed transactions

## 🏗 **Архитектура**
```
Frontend → API Gateway → [Auth Service | Banking Service | Notification Service]
```