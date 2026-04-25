# MODULE: (2025) 5COSC022W.2 Client-Server Architectures: Smart Campus Sensor & Room Management API Coursework

## Overview

This project is a JAX-RS RESTful API for managing a Smart Campus system. The API manages rooms, sensors assigned to rooms, and historical sensor readings.

The API includes:
- Room management
- Sensor creation and filtering
- Sensor readings using sub-resources
- Custom exception handling with JSON error responses
- Request and response logging using JAX-RS filters

## Technologies Used

- Java
- Maven
- JAX-RS / Jersey
- Apache Tomcat
- Postman for testing
- In-memory storage using HashMap and ArrayList

No external database is used, as required by the coursework.

## How to Run the Project

1. Open Apache NetBeans.
2. Make sure Apache Tomcat is added as a server.
3. Open the project in NetBeans.
4. Right-click the project and select **Clean and Build**.
5. Right-click the project again and select **Run**.
6. The API should run on Tomcat at:

```text
http://localhost:8080/csacwk/api/v1
```

## Main API Endpoints

### Discovery

| Method | Endpoint | Description |
|-------|--------|-------------|
| GET | `/api/v1` | Returns API metadata and available resources |

---

### Rooms

| Method | Endpoint | Description |
|-------|--------|-------------|
| GET | `/api/v1/rooms` | Get all rooms |
| POST | `/api/v1/rooms` | Create a new room |
| GET | `/api/v1/rooms/{roomId}` | Get a specific room |
| DELETE | `/api/v1/rooms/{roomId}` | Delete a room (only if no sensors assigned) |

---

### Sensors

| Method | Endpoint | Description |
|-------|--------|-------------|
| GET | `/api/v1/sensors` | Get all sensors |
| GET | `/api/v1/sensors?type=Temperature` | Filter sensors by type |
| POST | `/api/v1/sensors` | Create a new sensor |
| GET | `/api/v1/sensors/{sensorId}` | Get a specific sensor |

---

### Sensor Readings

| Method | Endpoint | Description |
|-------|--------|-------------|
| GET | `/api/v1/sensors/{sensorId}/readings` | Get all readings for a sensor |
| POST | `/api/v1/sensors/{sensorId}/readings` | Add a new reading |

---
