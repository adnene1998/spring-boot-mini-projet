# Banking Application - API Examples

This document provides example API calls for testing the banking microservices application.

## Base URLs

- **Direct Service Access:**
  - Account Service: http://localhost:8081
  - Transaction Service: http://localhost:8082
  - Customer Service: http://localhost:8083
  - Notification Service: http://localhost:8084

- **Through API Gateway:** http://localhost:8080 (Recommended)

## Account Service

### Create a Checking Account
```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "accountType": "CHECKING",
    "balance": 1000
  }'
```

### Create a Savings Account
```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "accountType": "SAVINGS",
    "balance": 5000
  }'
```

### Get All Accounts
```bash
curl http://localhost:8080/api/accounts
```

### Get Account by ID
```bash
curl http://localhost:8080/api/accounts/1
```

### Get Account by Account Number
```bash
curl http://localhost:8080/api/accounts/number/ACCE076100A4D
```

### Get Accounts by Customer ID
```bash
curl http://localhost:8080/api/accounts/customer/1
```

### Get Account Balance
```bash
curl http://localhost:8080/api/accounts/ACCE076100A4D/balance
```

### Update Account
```bash
curl -X PUT http://localhost:8080/api/accounts/1 \
  -H "Content-Type: application/json" \
  -d '{
    "balance": 1500,
    "status": "ACTIVE"
  }'
```

### Close Account (Soft Delete)
```bash
curl -X DELETE http://localhost:8080/api/accounts/1
```

## Transaction Service

### Process Deposit
```bash
curl -X POST http://localhost:8080/api/transactions/deposit \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "ACCE076100A4D",
    "amount": 500,
    "description": "Initial deposit"
  }'
```

### Process Withdrawal
```bash
curl -X POST http://localhost:8080/api/transactions/withdrawal \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "ACCE076100A4D",
    "amount": 200,
    "description": "ATM withdrawal"
  }'
```

### Process Transfer
```bash
curl -X POST http://localhost:8080/api/transactions/transfer \
  -H "Content-Type: application/json" \
  -d '{
    "fromAccount": "ACCE076100A4D",
    "toAccount": "ACC2290814A73",
    "amount": 250,
    "description": "Transfer between accounts"
  }'
```

### Get All Transactions
```bash
curl http://localhost:8080/api/transactions
```

### Get Transaction by ID
```bash
curl http://localhost:8080/api/transactions/1
```

### Get Transaction by Reference
```bash
curl http://localhost:8080/api/transactions/ref/TXN7A3068A7244A
```

### Get Transactions by Account
```bash
curl http://localhost:8080/api/transactions/account/ACCE076100A4D
```

### Get Transaction History by Date Range
```bash
curl "http://localhost:8080/api/transactions/history?startDate=2025-12-01T00:00:00&endDate=2025-12-31T23:59:59"
```

### Create Manual Transaction
```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "fromAccount": "ACCE076100A4D",
    "amount": 100,
    "transactionType": "WITHDRAWAL",
    "description": "Manual transaction"
  }'
```

## Customer Service

### Create Customer
```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Mhiri",
    "prenom": "Adnene",
    "age": 30,
    "tel": 12345678,
    "email": "adnene@example.com",
    "address": "123 Main St"
  }'
```

### Get All Customers
```bash
curl http://localhost:8080/api/customers
```

### Get Customer by ID
```bash
curl http://localhost:8080/api/customers/1
```

### Update Customer
```bash
curl -X PUT http://localhost:8080/api/customers/1 \
  -H "Content-Type: application/json" \
  -d '{
    "tel": 87654321,
    "email": "newemail@example.com",
    "address": "456 New Street"
  }'
```

### Delete Customer
```bash
curl -X DELETE http://localhost:8080/api/customers/1
```

## Notification Service

### Send Transaction Success Notification
```bash
curl -X POST http://localhost:8080/api/notifications/transaction \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "transactionRef": "TXN7A3068A7244A",
    "message": "Your deposit of $500 was successful"
  }'
```

### Send Insufficient Balance Alert
```bash
curl -X POST http://localhost:8080/api/notifications/insufficient-balance \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "message": "Your account balance is insufficient for this transaction"
  }'
```

### Send Login Alert
```bash
curl -X POST http://localhost:8080/api/notifications/login-alert \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "message": "New login detected from IP: 192.168.1.1"
  }'
```

### Get All Notifications
```bash
curl http://localhost:8080/api/notifications
```

### Get Notification by ID
```bash
curl http://localhost:8080/api/notifications/1
```

### Get Notifications by Customer ID
```bash
curl http://localhost:8080/api/notifications/customer/1
```

### Create Manual Notification
```bash
curl -X POST http://localhost:8080/api/notifications \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "message": "Account update notification",
    "type": "ACCOUNT_UPDATE"
  }'
```

## Complete Workflow Example

Here's a complete workflow demonstrating the entire banking system:

### 1. Create a Customer
```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Doe",
    "prenom": "John",
    "age": 35,
    "tel": 55512345,
    "email": "john.doe@example.com",
    "address": "789 Oak Avenue"
  }'
```

### 2. Create Checking Account for Customer
```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "accountType": "CHECKING",
    "balance": 0
  }'
```

### 3. Make Initial Deposit
```bash
curl -X POST http://localhost:8080/api/transactions/deposit \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "ACCE076100A4D",
    "amount": 1000,
    "description": "Initial deposit"
  }'
```

### 4. Send Deposit Success Notification
```bash
curl -X POST http://localhost:8080/api/notifications/transaction \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "transactionRef": "TXN7A3068A7244A",
    "message": "Your deposit of $1000 was successful. New balance: $1000"
  }'
```

### 5. Create Savings Account
```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "accountType": "SAVINGS",
    "balance": 0
  }'
```

### 6. Transfer from Checking to Savings
```bash
curl -X POST http://localhost:8080/api/transactions/transfer \
  -H "Content-Type: application/json" \
  -d '{
    "fromAccount": "ACCE076100A4D",
    "toAccount": "ACC2290814A73",
    "amount": 300,
    "description": "Transfer to savings account"
  }'
```

### 7. Check Transaction History
```bash
curl http://localhost:8080/api/transactions/account/ACCE076100A4D
```

### 8. Check Account Balances
```bash
curl http://localhost:8080/api/accounts/customer/1
```

## Health Checks

### Check Service Health
```bash
# Eureka Server
curl http://localhost:8761/actuator/health

# API Gateway
curl http://localhost:8080/actuator/health

# Account Service
curl http://localhost:8081/actuator/health

# Transaction Service
curl http://localhost:8082/actuator/health

# Customer Service
curl http://localhost:8083/actuator/health

# Notification Service
curl http://localhost:8084/actuator/health
```

### View Service Info
```bash
curl http://localhost:8081/actuator/info
```

### View Metrics
```bash
curl http://localhost:8081/actuator/metrics
```

## Service Discovery

### View Eureka Dashboard
```
http://localhost:8761
```

### Get Registered Services (XML)
```bash
curl http://localhost:8761/eureka/apps
```

## H2 Console Access

Access H2 database consoles for each service:

- Account Service: http://localhost:8081/h2-console
- Transaction Service: http://localhost:8082/h2-console
- Customer Service: http://localhost:8083/h2-console
- Notification Service: http://localhost:8084/h2-console

**Connection Details:**
- JDBC URL: `jdbc:h2:mem:[servicename]db` (e.g., `jdbc:h2:mem:accountdb`)
- Username: `sa`
- Password: (leave empty)

## Notes

- All timestamps are in ISO 8601 format
- Account numbers and transaction references are auto-generated
- All monetary amounts are in USD (implicit)
- Default account status is ACTIVE
- Default transaction status is PENDING (set to COMPLETED after processing)
- Default customer status is ACTIVE
- All services support pagination (though not shown in examples)

## Error Handling

The API returns standard HTTP status codes:
- 200 OK: Successful GET request
- 201 Created: Successful POST request
- 204 No Content: Successful DELETE request
- 400 Bad Request: Invalid input data
- 404 Not Found: Resource not found
- 500 Internal Server Error: Server error

Example error response:
```json
{
  "timestamp": "2025-12-28T18:00:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/accounts"
}
```
