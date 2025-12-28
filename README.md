# Banking Application with Microservices Architecture

A complete banking application built using Spring Boot microservices architecture, featuring centralized configuration, service discovery, API gateway, and various banking services.

## Architecture Overview

This application follows a microservices architecture pattern with the following components:

### Infrastructure Services
- **Config Server** (Port 8888): Centralized configuration management using Spring Cloud Config
- **Eureka Server** (Port 8761): Service discovery and registration
- **API Gateway** (Port 8080): Single entry point for all microservices with intelligent routing

### Business Services
- **Account Service** (Port 8081): Banking account management (CRUD operations, balance inquiries)
- **Transaction Service** (Port 8082): Transaction processing (deposits, withdrawals, transfers)
- **Customer Service** (Port 8083): Customer management and authentication
- **Notification Service** (Port 8084): Alert and notification management

## Technology Stack

- **Spring Boot 3.5.8**
- **Spring Cloud 2025.0.0**
- **Spring Data JPA**
- **H2 Database** (In-memory database for each service)
- **Spring Cloud Config**
- **Spring Cloud Gateway**
- **Spring Cloud Netflix Eureka**
- **Spring Cloud OpenFeign**
- **Spring Boot Actuator**
- **Lombok**
- **Maven**
- **Docker & Docker Compose**

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker and Docker Compose (optional, for containerized deployment)

## Project Structure

```
spring-boot-mini-projet/
├── config-service/          # Configuration server
├── discovery-service/       # Eureka server
├── gateway-service/         # API Gateway
├── account-service/         # Account management service
├── transaction-service/     # Transaction processing service
├── client-service/          # Customer management service
├── notification-service/    # Notification service
└── docker-compose.yml       # Docker orchestration file
```

## Getting Started

### Running Locally (Without Docker)

#### Step 1: Start Infrastructure Services

1. **Start Config Server**
```bash
cd config-service
mvn spring-boot:run
```

2. **Start Eureka Server**
```bash
cd discovery-service
mvn spring-boot:run
```

3. **Start API Gateway**
```bash
cd gateway-service
mvn spring-boot:run
```

#### Step 2: Start Business Services

Run each of the following in separate terminals:

```bash
# Account Service
cd account-service
mvn spring-boot:run

# Transaction Service
cd transaction-service
mvn spring-boot:run

# Customer Service
cd client-service
mvn spring-boot:run

# Notification Service
cd notification-service
mvn spring-boot:run
```

### Running with Docker Compose

1. **Build all services**
```bash
# Build each service
cd config-service && mvn clean package -DskipTests && cd ..
cd discovery-service && mvn clean package -DskipTests && cd ..
cd gateway-service && mvn clean package -DskipTests && cd ..
cd account-service && mvn clean package -DskipTests && cd ..
cd transaction-service && mvn clean package -DskipTests && cd ..
cd client-service && mvn clean package -DskipTests && cd ..
cd notification-service && mvn clean package -DskipTests && cd ..
```

2. **Start all services with Docker Compose**
```bash
docker-compose up -d
```

3. **Check service status**
```bash
docker-compose ps
```

4. **View logs**
```bash
docker-compose logs -f [service-name]
```

5. **Stop all services**
```bash
docker-compose down
```

## Service Endpoints

### Eureka Dashboard
- URL: http://localhost:8761
- View all registered services

### API Gateway Routes
All services are accessible through the API Gateway at http://localhost:8080

#### Account Service
- `GET /api/accounts` - Get all accounts
- `GET /api/accounts/{id}` - Get account by ID
- `GET /api/accounts/number/{accountNumber}` - Get account by number
- `GET /api/accounts/customer/{customerId}` - Get accounts by customer
- `POST /api/accounts` - Create new account
- `PUT /api/accounts/{id}` - Update account
- `DELETE /api/accounts/{id}` - Close account
- `GET /api/accounts/{accountNumber}/balance` - Get account balance

#### Transaction Service
- `GET /api/transactions` - Get all transactions
- `GET /api/transactions/{id}` - Get transaction by ID
- `GET /api/transactions/account/{accountNumber}` - Get transactions by account
- `POST /api/transactions` - Create transaction
- `POST /api/transactions/deposit` - Process deposit
- `POST /api/transactions/withdrawal` - Process withdrawal
- `POST /api/transactions/transfer` - Process transfer
- `GET /api/transactions/history` - Get transaction history by date range

#### Customer Service
- `GET /api/customers` - Get all customers
- `GET /api/customers/{id}` - Get customer by ID
- `POST /api/customers` - Create new customer
- `PUT /api/customers/{id}` - Update customer
- `DELETE /api/customers/{id}` - Delete customer

#### Notification Service
- `GET /api/notifications` - Get all notifications
- `GET /api/notifications/{id}` - Get notification by ID
- `GET /api/notifications/customer/{customerId}` - Get notifications by customer
- `POST /api/notifications` - Create notification
- `POST /api/notifications/transaction` - Send transaction notification
- `POST /api/notifications/insufficient-balance` - Send insufficient balance alert
- `POST /api/notifications/login-alert` - Send login alert

### Direct Service Access (Development)
- Config Server: http://localhost:8888
- Eureka Server: http://localhost:8761
- API Gateway: http://localhost:8080
- Account Service: http://localhost:8081
- Transaction Service: http://localhost:8082
- Customer Service: http://localhost:8083
- Notification Service: http://localhost:8084

### Actuator Endpoints
Each service exposes Spring Boot Actuator endpoints at `/actuator`:
- Health: `/actuator/health`
- Info: `/actuator/info`
- Metrics: `/actuator/metrics`

## Database Access

Each service uses H2 in-memory database with console access enabled:
- Account Service H2 Console: http://localhost:8081/h2-console
- Transaction Service H2 Console: http://localhost:8082/h2-console
- Customer Service H2 Console: http://localhost:8083/h2-console
- Notification Service H2 Console: http://localhost:8084/h2-console

**Connection Settings:**
- JDBC URL: `jdbc:h2:mem:[servicename]db`
- Username: `sa`
- Password: (empty)

## Features Implemented

### Account Management
- ✅ Create banking accounts (Checking, Savings, Business)
- ✅ View account details and balance
- ✅ Update account information
- ✅ Close/deactivate accounts
- ✅ Multiple accounts per customer support

### Transaction Processing
- ✅ Deposit money to accounts
- ✅ Withdraw money from accounts
- ✅ Transfer money between accounts
- ✅ Transaction history and tracking
- ✅ Transaction status management

### Customer Management
- ✅ Customer registration
- ✅ Customer profile management
- ✅ Customer status tracking (Active, Inactive, Blocked)
- ✅ Multiple validation rules

### Notifications
- ✅ Transaction success notifications
- ✅ Insufficient balance alerts
- ✅ Login alerts
- ✅ Account update notifications
- ✅ Notification history tracking

### Infrastructure
- ✅ Centralized configuration with Config Server
- ✅ Service discovery with Eureka
- ✅ API Gateway with intelligent routing
- ✅ Load balancing
- ✅ Health checks and monitoring
- ✅ Docker support for all services

## Testing

Run tests for individual services:

```bash
cd [service-directory]
mvn test
```

## Configuration Profiles

The application supports multiple Spring profiles:
- `default`: Local development
- `docker`: Docker/containerized deployment
- `dev`: Development environment
- `prod`: Production environment
- `test`: Testing environment

## Monitoring and Observability

- Spring Boot Actuator is enabled for all services
- Health checks are configured for Docker Compose
- Eureka dashboard provides service registry visualization

## Security Considerations

⚠️ **Note**: This is a basic implementation. For production use, please implement:
- JWT-based authentication
- OAuth2 authorization
- HTTPS/TLS encryption
- Input validation and sanitization
- Rate limiting
- API security best practices

## Future Enhancements

- [ ] Spring Security with JWT authentication
- [ ] OAuth2 implementation
- [ ] Database migration to PostgreSQL
- [ ] Distributed tracing with Sleuth/Zipkin
- [ ] Centralized logging with ELK stack
- [ ] Circuit breaker pattern with Resilience4j
- [ ] API documentation with Swagger/OpenAPI
- [ ] Integration tests
- [ ] Kubernetes deployment manifests
- [ ] Message queue integration (RabbitMQ/Kafka)

## Troubleshooting

### Services not registering with Eureka
- Ensure Eureka Server is running and accessible
- Check `eureka.client.service-url.defaultZone` configuration
- Allow 30-60 seconds for initial registration

### Gateway routing issues
- Verify services are registered in Eureka Dashboard
- Check gateway route configurations in `application.yml`
- Ensure service names match in routes and Eureka

### Database connection errors
- H2 console is enabled by default for development
- Check datasource configuration in application.properties
- Ensure ports are not in use by other applications

## Contributing

This is an educational project demonstrating microservices architecture with Spring Boot. Contributions and improvements are welcome.

## License

This project is open source and available for educational purposes.

## Authors

- Adnene Mhiri

## Acknowledgments

- Spring Boot and Spring Cloud teams
- Netflix OSS for Eureka and other cloud components
- Open source community
