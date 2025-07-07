# 多阶段构建 - 构建阶段
FROM openjdk:17-jdk-slim as builder

# 设置工作目录
WORKDIR /app

# 复制Maven包装器和pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# 复制源代码
COPY src src

# 构建应用
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# 运行阶段
FROM openjdk:17-jdk-slim

# 创建运行用户
RUN addgroup --system buddyfindr && adduser --system --group buddyfindr

# 设置工作目录
WORKDIR /app

# 创建必要的目录
RUN mkdir -p /app/logs /app/uploads
RUN chown -R buddyfindr:buddyfindr /app

# 复制构建的jar文件
COPY --from=builder /app/target/*.jar app.jar

# 设置文件权限
RUN chown buddyfindr:buddyfindr app.jar

# 切换到运行用户
USER buddyfindr

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# JVM参数和启动命令
ENV JAVA_OPTS="-Xms512m -Xmx2g -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/app/logs"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 