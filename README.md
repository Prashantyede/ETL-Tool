# ETL Tool (Spring Boot + Docker + MySQL)

This project is a lightweight ETL (Extract, Transform, Load) service built with Spring Boot and MySQL, deployable as a Docker container.

---

## 🚀 Setup Instructions

### 📦 Prerequisites
- Java 8+
- Maven
- Docker
- MySQL database running and accessible

### 🛠 Build the Project
```bash
mvn clean package
```

### 🐳 Build Docker Image
```bash
docker build -t etl-tool .
```

### ▶️ Run Docker Container
```bash
docker run -p 8080:8080 etl-tool
```

> Your app will be available at: http://localhost:8080

---

## 🔁 Sample API Usage

### ▶️ Run ETL Job
**POST** `http://localhost:8080/etl/run-etl-job`  
**Request Body:**
```json
{
    "loaded": 10,
    "transformed": 10,
    "extracted": 10
}
```

**Response:**
```json
{
    "message": "ETL Job executed successfully"
}
```

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
