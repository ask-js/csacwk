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

# REPORT

## PART 1:

### Q: 
In your report, explain the default lifecycle of a JAX-RS Resource class. Is a
new instance instantiated for every incoming request, or does the runtime treat it as a
singleton? Elaborate on how this architectural decision impacts the way you manage and
synchronize your in-memory data structures (maps/lists) to prevent data loss or race con-
Ditions.

### A:
In a default JAX-RS lifecycle, a new instance of a resource class is created for each incoming request, rather than using a single shared instance. For example, every time a request is made to RoomResource, a new object is created to handle it, and then discarded after the response is sent. This makes the resource classes request-scoped as opposed to being singletons. 

Because of this, any data stored inside the resource class itself would be lost between requests, as each request gets a fresh instance. To solve this, I used a separate DataStorage class with static HashMap and ArrayList structures so that the data is shared across all requests. This allows the API to maintain state without using a database like SQL, which follows the coursework requirements.

However, this approach does introduce potential concurrency issues, since multiple requests could access and modify the same data at the same time. In this implementation, this was an acceptable risk due to the small scale of the application, but in a real-world system, thread-safe structures such as ConcurrentHashMap or synchronisation techniques would have been used instead to prevent race conditions.

### Q)
Why is the provision of ”Hypermedia” (links and navigation within responses)
considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach
benefit client developers compared to static documentation?

### A:
Hypermedia is considered a hallmark of advanced RESTful design because it allows the API to return not just data, but also links that guide the client on what actions can be performed next. Instead of simply receiving raw data, the client can navigate the API dynamically using the links provided in the response, which is the core idea behind HATEOAS.

This approach benefits client developers because, without hypermedia, they would need to rely on static documentation to understand and hardcode all possible endpoints. In contrast, with HATEOAS, the API becomes self-describing, allowing clients to discover available resources and actions at runtime by following links, similar to navigating a website.

## Part 2

## Part 3
### Q)
You implemented this filtering using @QueryParam. Contrast this with an alterna-
tive design where the type is part of the URL path (e.g., /api/vl/sensors/type/CO2). Why
is the query parameter approach generally considered superior for filtering and searching
Collections

### A:
The alternative to query parameters is using path parameters. Path parameters are mainly used to identify specific resources. In my API, this is used when specifying a particular sensor, for example /sensors/{sensorId}. Using this same approach for filtering would not be ideal, as it would make the URL structure more complex and harder to manage.

Query parameters, on the other hand, are more suitable for filtering because they represent search conditions rather than resource identities. For example, /sensors?type=CO2 or /sensors?type=Temperature filters the sensor collection based on the type. This keeps the base resource (/sensors) consistent while allowing flexible filtering.

Another advantage of query parameters is that they are optional and scalable. The API can return all sensors when no filter is provided, and easily support multiple filters in the future, such as /sensors?type=CO2&status=ACTIVE, without making the URL difficult to read.

