# 🐾 BuddyFindr

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14-blue.svg)](https://www.postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis-7-red.svg)](https://redis.io/)
[![Docker](https://img.shields.io/badge/Docker-20+-blue.svg)](https://www.docker.com/)

一个功能完整的宠物社交应用后端系统，支持用户管理、宠物管理、走失宠物发布、社交功能等。

## 🚀 快速开始

### 前置要求

- **Java 17+**
- **Maven 3.6+**
- **Docker & Docker Compose**
- **Git**

### 本地开发

```bash
# 1. 克隆项目
git clone <your-repo-url>
cd bk

# 2. 启动本地开发环境
./start-local.sh

# 3. 等待启动完成，然后测试
./quick-test.sh
```

### 访问地址

- **API服务**: http://localhost:8080
- **数据库管理**: http://localhost:8082 (Adminer)
- **Redis管理**: http://localhost:8081 (Redis Commander)

## 📋 功能特性

### 🔐 用户管理系统
- ✅ 邮箱注册登录
- ✅ 短信验证码登录
- ✅ 第三方登录（Facebook、Google）
- ✅ JWT双令牌认证
- ✅ 用户信息管理
- ✅ 密码重置

### 🐕 宠物管理
- ✅ 宠物档案管理
- ✅ 多宠物支持
- ✅ 宠物照片上传
- ✅ 宠物品种分类
- ✅ 宠物状态管理

### 🚨 走失宠物系统
- ✅ 走失宠物发布
- ✅ GPS定位支持
- ✅ 附近走失宠物查询
- ✅ 收藏走失宠物
- ✅ 图片上传

### 📱 设备管理
- ✅ 设备注册
- ✅ FCM推送通知
- ✅ 设备状态管理

### 🔧 系统配置
- ✅ 用户配置管理
- ✅ 系统参数配置
- ✅ 多语言支持

### 🔒 安全功能
- ✅ 请求签名验证
- ✅ JWT令牌认证
- ✅ 防重放攻击
- ✅ 数据加密传输

## 🏠 本地开发环境

### 🔧 本地环境特性

- **模拟模式开启**: 邮箱和短信验证码固定为 `123456`
- **签名验证禁用**: 便于开发测试
- **自动数据库迁移**: 首次启动自动创建表结构
- **热重载支持**: 代码修改后自动重启
- **完整的管理界面**: 数据库和Redis管理

### 📝 环境配置

本地开发使用 `application-local.yml` 配置文件：

```yaml
spring:
  profiles:
    active: local
    
# 所有云服务都有模拟替代
verification:
  code:
    mock-enabled: true  # 启用模拟验证码
    
signature:
  enabled: false  # 禁用签名验证

# ⚠️ 敏感信息（如数据库、邮箱、密钥等）请勿提交到公开仓库，已移除，需自行填写！
```

### 🧪 API测试

使用提供的测试工具：

```bash
# 快速测试主要功能
./quick-test.sh

# 或使用HTTP文件测试
# 在VS Code中打开 BuddyFindr-V1-API.http
```

## ☁️ 云服务集成

### 📧 邮件服务

**Gmail配置**:
1. 开启两步验证
2. 生成应用密码
3. 更新 `application-prod.yml`:

```yaml
spring:
  mail:
    username: your-email@gmail.com
    password: your-16-digit-app-password
```

### 📱 短信服务（待定）

### 🔑 第三方登录

**Facebook登录**:
- 创建Facebook应用
- 获取App ID和App Secret

**Google登录**:
- 创建Google Cloud项目
- 配置OAuth 2.0

## 🏭 生产环境部署

### 🔧 生产环境配置

创建 `.env` 文件：

```bash
# 数据库配置
DB_PASSWORD=【请填写数据库密码】

# Redis配置
REDIS_PASSWORD=【Redis密码】

# JWT配置
JWT_SECRET=【JWT密钥】

# 云服务配置
ALIYUN_SMS_ACCESS_KEY_ID=【AccessKeyId】
ALIYUN_SMS_ACCESS_KEY_SECRET=【AccessKeySecret】
MAIL_USERNAME=【邮箱账号】
MAIL_PASSWORD=【邮箱密码】
```

### 🐳 Docker部署

```bash
# 构建镜像
cd buddyfindr-backend-java
docker build -t buddyfindr-api:latest .

# 运行容器
docker run -d --name buddyfindr-api \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  buddyfindr-api:latest
```

## 📊 API文档

### 🔗 主要端点

**用户管理**:
- `POST /v1/register` - 用户注册
- `POST /v1/login` - 用户登录
- `POST /v1/login/vcode` - 验证码登录
- `GET /v1/user/{id}` - 获取用户信息

**宠物管理**:
- `POST /v1/pet` - 添加宠物
- `GET /v1/pets/{userid}` - 获取用户宠物
- `PUT /v1/pet/{id}` - 更新宠物信息

**走失宠物**:
- `POST /v1/lost` - 发布走失宠物
- `GET /v1/neighbours/lost_pets` - 获取附近走失宠物

## 🔍 监控与维护

### 📊 健康检查

```bash
# 检查应用状态
curl http://localhost:8080/actuator/health

# 检查指标
curl http://localhost:8080/actuator/metrics
```

### 📝 日志管理

```bash
# 查看应用日志
tail -f logs/buddyfindr.log

# 查看Docker日志
docker logs buddyfindr-api
```

### 🔄 备份策略

**数据库备份**:

```bash
pg_dump -h localhost -U buddyfindr_user buddyfindr > backup.sql
```

**文件备份**:
```bash
tar -czf uploads_backup.tar.gz uploads/
```

## 🛠️ 开发说明

### 🏗️ 架构设计

- **分层架构**: Controller → Service → Repository
- **依赖注入**: Spring IoC容器管理
- **事务管理**: Spring @Transactional
- **缓存策略**: Redis缓存热点数据
- **安全设计**: JWT + 签名验证

### 📁 目录结构

```
buddyfindr-backend-java/
├── src/main/java/com/buddyfindr/
│   ├── controller/     # 控制器层
│   ├── service/        # 服务层
│   ├── repository/     # 数据访问层
│   ├── entity/         # 实体类
│   ├── dto/            # 数据传输对象
│   ├── config/         # 配置类
│   ├── security/       # 安全配置
│   └── util/           # 工具类
└── src/main/resources/
    ├── application.yml           # 默认配置
    ├── application-local.yml     # 本地开发配置
    └── application-prod.yml      # 生产环境配置
```

## 🆘 故障排除

### 常见问题

**1. 数据库连接失败**
```bash
# 检查数据库状态
docker logs buddyfindr-postgres
```

**2. Redis连接失败**
```bash
# 测试Redis连接
redis-cli ping
```

**3. 应用启动失败**
```bash
# 查看详细日志
mvn spring-boot:run -Dspring-boot.run.profiles=local -X
```

**4. 验证码收不到**

- 检查邮件配置
- 确认应用密码正确
- 查看应用日志

### 🆘 获取帮助

## 📜 许可证

该项目使用 GNU 3.0 许可证。详情请参阅 `LICENSE` 文件。

## 🤝 贡献

欢迎提交Issue和Pull Request！如果觉得该项目有帮助，欢迎 star 支持。 
