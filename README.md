# ETL Tool (Spring Boot + MySQL)

This project is a lightweight ETL (Extract, Transform, Load) service built with Spring Boot and MySQL, deployable as a Docker container.

## 🚀 Setup Instructions

### 📦 Prerequisites
- Java 8+
- Maven
- Docker( optional )
- MySQL database running and accessible

## 🛠 Step-by-Step Setup
## Run the Spring Boot ETL App Locally (Without Docker)
### 1. **Build the Spring Boot JAR**
```bash
mvn clean package
```
This creates the file:
```
target/etl-tool-1.0.0.jar
```
### 2.Run the Spring Boot application using the Java command:
```bash
java -jar target/etl-tool-1.0.0.jar
...

## Run the Spring Boot ETL App With Docker
### 1. **Build the Spring Boot JAR**

```bash
mvn clean package
```
This creates the file:
```
target/etl-tool-1.0.0.jar
```
### 2. **Build the Docker Image**
```bash
docker build -t etl-tool .
```

### 3. **Create a Docker Network**

```bash
docker network create etl-net
```

### 4. **Run MySQL Container**

```bash
docker run -d \
  --name mysql-db \
  --network etl-net \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=etl_db \
  -p 3306:3306 \
  mysql:8
```

### 5. **Update `application.yml`**

Ensure the following is set in `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://mysql-db:3306/etl_db
    username: ****
    password: ****
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
server:
  port: 8080
  address: 0.0.0.0
```

### 6. **Run the ETL Tool Container**

```bash
docker run -d \
  --network etl-net \
  -p 8080:8080 \
  etl-tool
```

### 7. **Test the API**

```bash
curl http://localhost:8080/etl/status
```

Expected response:
```
Completed.
```
Or in browser:
```
http://localhost:8080/etl/status
```
---

## ✅ Troubleshooting

- Make sure both containers are running:  
  ```bash
  docker ps
  ```

- Check logs if something crashes:  
  ```bash
  docker logs <container_id>

> Your app will be available at: http://localhost:8080

## 🔁 Sample API Usage

### ▶️ Run ETL Job
**POST** `http://localhost:8080/etl/run-etl-job`  
**Response:**
```json
{
    "loaded": 10,
    "transformed": 10,
    "extracted": 10
}
---

### 📊 Get ETL Status
**GET** `http://localhost:8080/etl/status`  
**Response:**
```json
{
    "status": "Completed"
}

## 🧠 Design Choices

- **Spring Boot** for REST API development.
- **Modular services** to separate extract, transform, and load logic.
- **YAML & JSON config files** to define transformation rules.
- **Dockerized** for consistent environment and easy deployment.
- **MySQL** as a robust backend store for ETL data tracking.

---
