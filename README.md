# 🐾 FurTrax

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14-blue.svg)](https://www.postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis-7-red.svg)](https://redis.io/)
[![Docker](https://img.shields.io/badge/Docker-20+-blue.svg)](https://www.docker.com/)

A fully-featured backend system for a pet social application, supporting user management, pet management, lost pet publishing, social features, and more.

## 🚀 Quick Start

### Prerequisites

- **Java 17+**
- **Maven 3.6+**
- **Docker & Docker Compose**
- **Git**

### Local Development

```bash
# 1. Clone the project
git clone <your-repo-url>
cd bk

# 2. Start the local development environment
./start-local.sh

# 3. Wait for startup to complete, then run tests
./quick-test.sh
```

### Access URLs

- **API Service**: http://localhost:8080
- **Database Management**: http://localhost:8082 (Adminer)
- **Redis Management**: http://localhost:8081 (Redis Commander)

## 📋 Features

### 🔐 User Management System
- ✅ Email registration and login
- ✅ SMS verification code login
- ✅ Third-party login (Facebook, Google)
- ✅ JWT dual token authentication
- ✅ User information management
- ✅ Password reset

### 🐕 Pet Management
- ✅ Pet profile management
- ✅ Multi-pet support
- ✅ Pet photo upload
- ✅ Pet breed classification
- ✅ Pet status management

### 🚨 Lost Pet System
- ✅ Lost pet publishing
- ✅ GPS location support
- ✅ Nearby lost pet search
- ✅ Favorite lost pets
- ✅ Image upload

### 📱 Device Management
- ✅ Device registration
- ✅ FCM push notifications
- ✅ Device status management

### 🔧 System Configuration
- ✅ User configuration management
- ✅ System parameter configuration
- ✅ Multi-language support

### 🔒 Security Features
- ✅ Request signature verification
- ✅ JWT token authentication
- ✅ Anti-replay attack protection
- ✅ Encrypted data transmission

## 🏠 Local Development Environment

### 🔧 Local Environment Features

- **Mock Mode Enabled**: Email and SMS verification codes are fixed to `123456`
- **Signature Verification Disabled**: For development testing convenience
- **Automatic Database Migration**: Auto-create table structure on first startup
- **Hot Reload Support**: Auto-restart after code modifications
- **Complete Management Interface**: Database and Redis management

### 📝 Environment Configuration

Local development uses the `application-local.yml` configuration file:

```yaml
spring:
  profiles:
    active: local
    
# All cloud services have mock alternatives
verification:
  code:
    mock-enabled: true  # Enable mock verification codes
    
signature:
  enabled: false  # Disable signature verification

# ⚠️ Sensitive information (such as database, email, keys, etc.) should not be committed to public repositories, removed, need to be filled in manually!
```

### 🧪 API Testing

Use the provided testing tools:

```bash
# Quick test of main features
./quick-test.sh

# Or use HTTP file testing
# Open FurTrax-V1-API.http in VS Code
```

## ☁️ Cloud Service Integration

### 📧 Email Service

**Gmail Configuration**:
1. Enable two-factor authentication
2. Generate app password
3. Update `application-prod.yml`:

```yaml
spring:
  mail:
    username: your-email@gmail.com
    password: your-16-digit-app-password
```

### 📱 SMS Service (TBD)

### 🔑 Third-party Login

**Facebook Login**:
- Create Facebook application
- Get App ID and App Secret

**Google Login**:
- Create Google Cloud project
- Configure OAuth 2.0

## 🏭 Production Environment Deployment

### 🔧 Production Environment Configuration

Create `.env` file:

```bash
# Database configuration
DB_PASSWORD=【Please fill in database password】

# Redis configuration
REDIS_PASSWORD=【Redis password】

# JWT configuration
JWT_SECRET=【JWT secret】

# Cloud service configuration
ALIYUN_SMS_ACCESS_KEY_ID=【AccessKeyId】
ALIYUN_SMS_ACCESS_KEY_SECRET=【AccessKeySecret】
MAIL_USERNAME=【Email account】
MAIL_PASSWORD=【Email password】
```

### 🐳 Docker Deployment

```bash
# Build image
cd furtrax-backend-java
docker build -t furtrax-api:latest .

# Run container
docker run -d --name furtrax-api \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  furtrax-api:latest
```

## 📊 API Documentation

### 🔗 Main Endpoints

**User Management**:
- `POST /v1/register` - User registration
- `POST /v1/login` - User login
- `POST /v1/login/vcode` - Verification code login
- `GET /v1/user/{id}` - Get user information

**Pet Management**:
- `POST /v1/pet` - Add pet
- `GET /v1/pets/{userid}` - Get user pets
- `PUT /v1/pet/{id}` - Update pet information

**Lost Pets**:
- `POST /v1/lost` - Publish lost pet
- `GET /v1/neighbours/lost_pets` - Get nearby lost pets

## 🔍 Monitoring and Maintenance

### 📊 Health Check

```bash
# Check application status
curl http://localhost:8080/actuator/health

# Check metrics
curl http://localhost:8080/actuator/metrics
```

### 📝 Log Management

```bash
# View application logs
tail -f logs/furtrax.log

# View Docker logs
docker logs furtrax-api
```

### 🔄 Backup Strategy

**Database Backup**:

```bash
pg_dump -h localhost -U furtrax_user furtrax > backup.sql
```

**File Backup**:
```bash
tar -czf uploads_backup.tar.gz uploads/
```

## 🛠️ Development Guide

### 🏗️ Architecture Design

- **Layered Architecture**: Controller → Service → Repository
- **Dependency Injection**: Spring IoC container management
- **Transaction Management**: Spring @Transactional
- **Caching Strategy**: Redis cache for hot data
- **Security Design**: JWT + signature verification

### 📁 Directory Structure

```
furtrax-backend-java/
├── src/main/java/com/furtrax/
│   ├── controller/     # Controller layer
│   ├── service/        # Service layer
│   ├── repository/     # Data access layer
│   ├── entity/         # Entity classes
│   ├── dto/            # Data transfer objects
│   ├── config/         # Configuration classes
│   ├── security/       # Security configuration
│   └── util/           # Utility classes
└── src/main/resources/
    ├── application.yml           # Default configuration
    ├── application-local.yml     # Local development configuration
    └── application-prod.yml      # Production environment configuration
```

## 🆘 Troubleshooting

### Common Issues

**1. Database Connection Failed**
```bash
# Check database status
docker logs furtrax-postgres
```

**2. Redis Connection Failed**
```bash
# Test Redis connection
redis-cli ping
```

**3. Application Startup Failed**
```bash
# View detailed logs
mvn spring-boot:run -Dspring-boot.run.profiles=local -X
```

**4. Verification Code Not Received**

- Check email configuration
- Confirm app password is correct
- View application logs

### 🆘 Getting Help

## 📜 License

This project uses GNU 3.0 License. See the `LICENSE` file for details.

## 🤝 Contributing

Welcome to submit Issues and Pull Requests! If you find this project helpful, welcome to star support. 
