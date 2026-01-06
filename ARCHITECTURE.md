# Banking Application - Architecture Documentation

## Overview

This banking application follows a microservices architecture pattern with Spring Boot and Spring Cloud components. The system is designed to be scalable, resilient, and maintainable.

## Architecture Diagram

```
┌──────────────────────────────────────────────────────────────────────────┐
│                              External Clients                             │
│                          (Web, Mobile, Desktop)                           │
└────────────────────────────────┬─────────────────────────────────────────┘
                                 │
                                 │ HTTP/HTTPS
                                 ▼
                    ┌────────────────────────┐
                    │                        │
                    │    API Gateway         │
                    │    (Port 8080)         │
                    │                        │
                    │  - Route Management    │
                    │  - Load Balancing      │
                    │  - Request Filtering   │
                    └────────┬───────────────┘
                             │
                             │ Service Discovery
                             │
           ┌─────────────────┼─────────────────┐
           │                 │                 │
           ▼                 ▼                 ▼
┌──────────────────┐  ┌──────────────┐  ┌──────────────────┐
│  Eureka Server   │  │   Config     │  │  Infrastructure  │
│  (Port 8761)     │  │   Server     │  │    Services      │
│                  │  │  (Port 8888) │  │                  │
│ - Service        │  │              │  │                  │
│   Registry       │  │ - Central    │  │                  │
│ - Health Checks  │  │   Config     │  │                  │
└──────────────────┘  └──────────────┘  └──────────────────┘
                                                │
                   ┌────────────────────────────┼────────────────────────────┐
                   │                            │                            │
                   │                            │                            │
                   ▼                            ▼                            ▼
         ┌─────────────────┐         ┌──────────────────┐        ┌──────────────────┐
         │  Account         │         │  Transaction     │        │  Customer        │
         │  Service         │         │  Service         │        │  Service         │
         │  (Port 8081)     │         │  (Port 8082)     │        │  (Port 8083)     │
         │                  │         │                  │        │                  │
         │ - Account CRUD   │         │ - Deposits       │        │ - Customer CRUD  │
         │ - Balance Mgmt   │◄────────┤ - Withdrawals    │────────┤ - Profile Mgmt   │
         │ - Account Types  │         │ - Transfers      │        │ - Status Mgmt    │
         └────────┬─────────┘         └────────┬─────────┘        └────────┬─────────┘
                  │                            │                            │
                  │                            │                            │
                  └────────────────────────────┼────────────────────────────┘
                                               │
                                               │ Event Notification
                                               │
                                               ▼
                                    ┌─────────────────────┐
                                    │  Notification       │
                                    │  Service            │
                                    │  (Port 8084)        │
                                    │                     │
                                    │ - Transaction Alert │
                                    │ - Balance Alert     │
                                    │ - Login Alert       │
                                    └─────────────────────┘
                                               │
                                               ▼
                                    ┌─────────────────────┐
                                    │  Message Queue      │
                                    │  (Future: Kafka)    │
                                    └─────────────────────┘
```

## Component Architecture

### 1. Infrastructure Layer

#### Config Server (Port 8888)
- **Purpose**: Centralized configuration management
- **Technology**: Spring Cloud Config
- **Features**:
  - Git-based configuration storage
  - Profile-based configuration (dev, prod, test)
  - Dynamic configuration refresh
  - Encrypted properties support

#### Eureka Server (Port 8761)
- **Purpose**: Service discovery and registration
- **Technology**: Spring Cloud Netflix Eureka
- **Features**:
  - Service registration
  - Service health checks
  - Load balancing support
  - Failover support

#### API Gateway (Port 8080)
- **Purpose**: Single entry point for all microservices
- **Technology**: Spring Cloud Gateway
- **Features**:
  - Intelligent routing
  - Load balancing
  - Request/Response filtering
  - Circuit breaker integration (future)
  - Rate limiting (future)
  - Authentication/Authorization (future)

### 2. Business Services Layer

#### Account Service (Port 8081)
- **Purpose**: Manage banking accounts
- **Database**: H2 (In-memory)
- **Entity**: Account
- **Features**:
  - Create accounts (Checking, Savings, Business)
  - Read account details
  - Update account information
  - Close accounts (soft delete)
  - Check account balance
  - List accounts by customer

**Account Entity Schema:**
```
Account
├── id (Long)
├── accountNumber (String) - Auto-generated
├── customerId (Long)
├── accountType (CHECKING, SAVINGS, BUSINESS)
├── balance (Double)
├── status (ACTIVE, INACTIVE, CLOSED)
├── createdAt (DateTime)
└── updatedAt (DateTime)
```

#### Transaction Service (Port 8082)
- **Purpose**: Process and track banking transactions
- **Database**: H2 (In-memory)
- **Entity**: Transaction
- **Features**:
  - Process deposits
  - Process withdrawals
  - Process transfers
  - Transaction history
  - Transaction status tracking
  - Date range queries

**Transaction Entity Schema:**
```
Transaction
├── id (Long)
├── transactionRef (String) - Auto-generated
├── fromAccount (String)
├── toAccount (String) - Optional
├── amount (Double)
├── transactionType (DEPOSIT, WITHDRAWAL, TRANSFER)
├── status (PENDING, COMPLETED, FAILED, CANCELLED)
├── description (String)
└── transactionDate (DateTime)
```

#### Customer Service (Port 8083)
- **Purpose**: Manage customer information
- **Database**: H2 (In-memory)
- **Entity**: Customer (Patient)
- **Features**:
  - Customer registration
  - Customer profile management
  - Customer status management
  - Customer information updates

**Customer Entity Schema:**
```
Customer
├── id (Long)
├── nom (String) - Last name
├── prenom (String) - First name
├── age (Integer)
├── tel (Integer) - Phone
├── email (String)
├── address (String)
├── status (ACTIVE, INACTIVE, BLOCKED)
├── createdAt (DateTime)
└── updatedAt (DateTime)
```

#### Notification Service (Port 8084)
- **Purpose**: Send and track notifications
- **Database**: H2 (In-memory)
- **Entity**: Notification
- **Features**:
  - Transaction success notifications
  - Insufficient balance alerts
  - Login alerts
  - Account update notifications
  - Notification history

**Notification Entity Schema:**
```
Notification
├── id (Long)
├── customerId (Long)
├── message (String)
├── type (TRANSACTION_SUCCESS, INSUFFICIENT_BALANCE, 
│         LOGIN_ALERT, ACCOUNT_UPDATE)
├── status (PENDING, SENT, FAILED)
├── transactionRef (String) - Optional
├── sentAt (DateTime)
└── createdAt (DateTime)
```

## Communication Patterns

### 1. Synchronous Communication
- **Protocol**: HTTP/REST
- **Use Cases**:
  - Client → API Gateway
  - API Gateway → Business Services
  - Service → Service (via OpenFeign)
  
### 2. Service Discovery
- All services register with Eureka Server on startup
- API Gateway discovers services dynamically
- Load balancing across service instances

### 3. Configuration Management
- Services can fetch configuration from Config Server
- Support for multiple profiles
- Dynamic configuration refresh (future)

## Design Patterns

### 1. API Gateway Pattern
- Single entry point for all client requests
- Routes requests to appropriate microservices
- Handles cross-cutting concerns

### 2. Service Registry Pattern
- Services register themselves with Eureka
- Dynamic service discovery
- Health monitoring

### 3. Database per Service
- Each microservice has its own database
- Data independence
- Schema evolution independence

### 4. Centralized Configuration
- Configuration stored in central location
- Environment-specific configurations
- Version-controlled configurations

## Data Flow Examples

### Example 1: Create Account Flow
```
1. Client → API Gateway: POST /api/accounts
2. API Gateway → Eureka: Discover account-service
3. API Gateway → Account Service: Forward request
4. Account Service → Database: Save account
5. Account Service → API Gateway: Return account
6. API Gateway → Client: Return account
```

### Example 2: Process Transfer Flow
```
1. Client → API Gateway: POST /api/transactions/transfer
2. API Gateway → Eureka: Discover transaction-service
3. API Gateway → Transaction Service: Forward request
4. Transaction Service → Database: Save transaction
5. Transaction Service → Notification Service: Trigger notification (future)
6. Transaction Service → API Gateway: Return transaction
7. API Gateway → Client: Return transaction
```

## Technology Stack

### Core Framework
- **Spring Boot 3.5.8**: Application framework
- **Spring Cloud 2025.0.0**: Cloud-native toolkit

### Service Discovery
- **Spring Cloud Netflix Eureka**: Service registry

### API Gateway
- **Spring Cloud Gateway**: Reactive API gateway

### Configuration
- **Spring Cloud Config**: Centralized configuration

### Data Access
- **Spring Data JPA**: Data access layer
- **H2 Database**: In-memory database
- **Hibernate**: ORM framework

### Utilities
- **Lombok**: Reduce boilerplate code
- **Jakarta Validation**: Input validation
- **Spring Boot Actuator**: Monitoring and health checks

### Build Tool
- **Maven**: Dependency management and build

### Containerization
- **Docker**: Containerization
- **Docker Compose**: Multi-container orchestration

## Scalability Considerations

### Horizontal Scaling
- Each service can be scaled independently
- Load balancing through Eureka and Gateway
- Stateless service design

### Performance
- In-memory database for fast operations
- Asynchronous processing capability (future)
- Caching support (future)

### Resilience
- Service health checks via Actuator
- Circuit breaker pattern (future)
- Retry mechanisms (future)

## Security Architecture (Future)

### Planned Security Features
1. **JWT Authentication**
   - Token-based authentication
   - Stateless security

2. **OAuth2 Authorization**
   - Role-based access control
   - Resource server configuration

3. **HTTPS/TLS**
   - Encrypted communication
   - Certificate management

4. **Input Validation**
   - Bean validation
   - SQL injection prevention
   - XSS protection

## Monitoring and Observability

### Current Implementation
- **Spring Boot Actuator**
  - Health endpoints
  - Metrics endpoints
  - Info endpoints

### Future Enhancements
- **Distributed Tracing** (Spring Cloud Sleuth)
- **Centralized Logging** (ELK Stack)
- **Metrics Dashboard** (Prometheus + Grafana)
- **APM** (Application Performance Monitoring)

## Deployment Architecture

### Local Development
```
├── Run services individually with Maven
├── H2 in-memory databases
└── Service discovery on localhost
```

### Docker Deployment
```
├── Each service in separate container
├── Docker Compose orchestration
├── Network isolation
└── Volume mounting for configurations
```

### Cloud Deployment (Future)
```
├── Kubernetes clusters
├── Cloud-native databases
├── Load balancers
└── Auto-scaling groups
```

## Future Enhancements

### Short Term
1. Add Spring Security with JWT
2. Implement circuit breaker pattern
3. Add API documentation (Swagger/OpenAPI)
4. Implement caching (Redis)

### Medium Term
1. Add message queue (RabbitMQ/Kafka)
2. Implement event-driven architecture
3. Add distributed tracing
4. Implement CQRS pattern

### Long Term
1. Migrate to cloud-native databases
2. Kubernetes deployment
3. Service mesh (Istio)
4. Advanced monitoring and alerting

## Best Practices Implemented

1. ✅ Separate database per service
2. ✅ Service discovery and registration
3. ✅ API Gateway pattern
4. ✅ Centralized configuration
5. ✅ Health checks and monitoring
6. ✅ Container-ready (Docker)
7. ✅ RESTful API design
8. ✅ Input validation
9. ✅ Proper HTTP status codes
10. ✅ Logging and error handling

## Conclusion

This architecture provides a solid foundation for a banking application using microservices. It's designed to be:
- **Scalable**: Each service can scale independently
- **Resilient**: Service discovery and health monitoring
- **Maintainable**: Clear separation of concerns
- **Flexible**: Easy to add new services
- **Cloud-ready**: Docker support for containerization

The architecture follows industry best practices and can be extended with additional features as needed.
