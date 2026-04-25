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
http://localhost:8080/smartcampuscwk/api/v1
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

## Example API Usage (curl)

### Create a Room
```bash
curl --location "http://localhost:8080/smartcampuscwk/api/v1/rooms" \
--header 'Content-Type: application/json' \
--data '{
  "id": "LG01",
  "name": "Lower Computer Room",
  "capacity": 80,
  "sensorIds": []
}'
```
### Get All Rooms
```bash
curl --location "http://localhost:8080/smartcampuscwk/api/v1/rooms"
```
### Create a Sensor
```bash
curl --location "http://localhost:8080/smartcampuscwk/api/v1/sensors" \
--header 'Content-Type: application/json' \
--data '{
  "id": "TEMP-001",
  "type": "Temperature",
  "status": "ACTIVE",
  "currentValue": 29.5,
  "roomId": "LG01"
}'
```
### Delete a Room
```bash
curl --location --request DELETE 'http://localhost:8080/smartcampuscwk/api/v1/rooms/LG01'
```

### Add a Sensor Reading
```bash
curl --location 'http://localhost:8080/smartcampuscwk/api/v1/sensors/TEMP-001/readings' \
--header 'Content-Type: application/json' \
--data '{
  "id": "READ-001",
  "timestamp": 1713970000000,
  "value": 25.8
}'
```
--- 
## Error Handling

- 409 = RoomNotEmptyException
- 422 = LinkedResourceNotFoundException
- 403 = SensorUnavailableException
- 500 = GlobalExceptionMapper

---

## Logging

A JAX-RS filter logs:
- HTTP method
- Request URI
- Response status

---

# REPORT

## PART 1:

### Q: 
>In your report, explain the default lifecycle of a JAX-RS Resource class. Is a
new instance instantiated for every incoming request, or does the runtime treat it as a
singleton? Elaborate on how this architectural decision impacts the way you manage and
synchronize your in-memory data structures (maps/lists) to prevent data loss or race conditions.

### A:
In a default JAX-RS lifecycle, a new instance of a resource class is created for each incoming request, rather than using a single shared instance. For example, every time a request is made to RoomResource, a new object is created to handle it, and then discarded after the response is sent. This makes the resource classes request-scoped as opposed to being singletons. 

Because of this, any data stored inside the resource class itself would be lost between requests, as each request gets a fresh instance. To solve this, I used a separate DataStorage class with static HashMap and ArrayList structures so that the data is shared across all requests. This allows the API to maintain state without using a database like SQL, which follows the coursework requirements.

However, this approach does introduce potential concurrency issues, since multiple requests could access and modify the same data at the same time. In this implementation, this was an acceptable risk due to the small scale of the application but in a real-world system, thread-safe structures such as ConcurrentHashMap or synchronisation techniques would have been used instead to prevent race conditions.

### Q:
>Why is the provision of ”Hypermedia” (links and navigation within responses)
considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach
benefit client developers compared to static documentation?

### A:
Hypermedia is considered a hallmark of advanced RESTful design because it allows the API to return not just data, but also links that guide the client on what actions can be performed next. Instead of simply receiving raw data, the client can navigate the API dynamically using the links provided in the response, which is the core idea behind HATEOAS.

This approach benefits client developers because, without hypermedia, they would need to rely on static documentation to understand and hardcode all possible endpoints. In contrast, with HATEOAS, the API becomes self-describing, allowing clients to discover available resources and actions at runtime by following links, similar to navigating a website.

---
## Part 2
### Q:
>When returning a list of rooms, what are the implications of returning only
IDs versus returning the full room objects? Consider network bandwidth and client side
Processing.

### A:
Returning only IDs uses less data per request, as the response size is smaller, which reduces network bandwidth usage and makes it faster to transfer. However, because the client does not receive the full room details, it may need to make additional requests to retrieve more information and therefore increasing the total number of requests.

On the other hand, returning full room objects provides all the necessary data in a single response, making it more convenient for the client and reducing the need for multiple requests. However, this comes at the cost of increased bandwidth usage as the response size is larger which can impact performance when dealing with a large number of objects.

Overall, returning full objects is more suitable for smaller datasets where convenience is important while returning only IDs is more efficient for larger datasets where minimising network usage is a priority.

### Q:
>Is the DELETE operation idempotent in your implementation? Provide a detailed
justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times

### A:
The DELETE operation is idempotent in my implementation because performing the same request multiple times results in the same final state. Idempotent means that repeating an operation does not change the outcome beyond the initial execution.

For example, if a client sends a DELETE request for the room “LG01”, the first request will return a 200 OK response and the room will be successfully removed from the DataStorage. If the client mistakenly sends the same DELETE request again, the API will return a 404 Not Found response because the room no longer exists.

Although the response differs between the first and subsequent requests, the final state of the system remains the same, as the room has already been deleted and cannot be deleted again. This confirms that the DELETE operation is idempotent in this implementation.

---
## Part 3

### Q:
>We explicitly use the @Consumes (MediaType.APPLICATION_JSON) annotation on
the POST method. Explain the technical consequences if a client attempts to send data in
a different format, such as text/plain or application/xml. How does JAX-RS handle this
Mismatch?

### A:
The @Consumes(MediaType.APPLICATION_JSON) annotation restricts the method to only accept JSON input. If a client sends data in a different format, such as text or xml, JAX-RS will automatically reject the request with a 415 Unsupported Media Type response. This happens before the method is executed, as JAX-RS validates the Content-Type header against the @Consumes annotation.

However, in my implementation I have included a global ExceptionMapper<Throwable> as part of Part 5. This mapper catches all unhandled exceptions and returns a generic 500 Internal Server Error. As a result, instead of returning the expected 415 error, the request may return a 500 response due to the global exception handler overriding the default JAX-RS behaviour.

### Q:
>You implemented this filtering using @QueryParam. Contrast this with an alterna-
tive design where the type is part of the URL path (e.g., /api/vl/sensors/type/CO2). Why
is the query parameter approach generally considered superior for filtering and searching
Collections

### A:
The alternative to query parameters is using path parameters. Path parameters are mainly used to identify specific resources. In my API, this is used when specifying a particular sensor, for example /sensors/{sensorId}. Using this same approach for filtering would not be ideal, as it would make the URL structure more complex and harder to manage.

Query parameters, on the other hand, are more suitable for filtering because they represent search conditions rather than resource identities. For example, /sensors?type=CO2 or /sensors?type=Temperature filters the sensor collection based on the type. This keeps the base resource (/sensors) consistent while allowing flexible filtering.

Another advantage of query parameters is that they are optional and scalable. The API can return all sensors when no filter is provided, and easily support multiple filters in the future, such as /sensors?type=CO2&status=ACTIVE, without making the URL difficult to read.


---
## Part 4:

### Q:
>Discuss the architectural benefits of the Sub-Resource Locator pattern. How
does delegating logic to separate classes help manage complexity in large APIs compared
to defining every nested path (e.g., sensors/{id}/readings/{rid}) in one massive con-
troller class?

### A:
A sub-resource locator is a way of structuring an API where a main resource delegates requests to another resource class based on the path. In my implementation, the main resource (SensorResource) defines the path and returns an instance of the sub-resource (SensorReadingResource), while the actual logic for handling the request is implemented in the separate class.

This helps manage complexity by separating responsibilities, as each class focuses on a specific part of the API. For example, SensorResource handles sensors, while SensorReadingResource handles readings. This makes the code easier to understand and maintain, as changes to one part of the API do not affect the other.

It also improves scalability, as new sub-resources can be added without making the main resource class overly complex. In contrast, defining all nested paths in a single class would result in a large and difficult to maintain code with repeated logic and poor organisation.

Overall, sub-resource locators promote a more modular design, allowing components to be reused and extended independently as the API grows.

---
## Part 5:

### Q:
>Why is HTTP 422 often considered more semantically accurate than a standard
404 when the issue is a missing reference inside a valid JSON payload?

### A:
A 404 Not Found error is used when the resource specified in the URL does not exist. However, when the issue is a missing reference inside a valid JSON payload, the request itself is still valid and has reached the correct endpoint. This makes 422 Unprocessable Entity more semantically accurate.

A 422 error is used when the server understands the request format and can process it, but the data within the request is logically incorrect. In this case, the JSON structure is valid, but the reference inside it (such as a roomId that does not exist) is invalid.

Therefore, 422 is more appropriate than 404 because the problem is not that the resource in the URL is missing, but that the data within the request contains a semantic error. This provides clearer feedback to the client about what needs to be corrected.

### Q:
>From a cybersecurity standpoint, explain the risks associated with exposing
internal Java stack traces to external API consumers. What specific information could an
attacker gather from such a trace?

### A:
Exposing internal Java stack traces to external API consumers is a security risk because it reveals detailed information about the internal structure of the system. A stack trace can include class names, file paths, method calls, and the libraries or frameworks being used.

This information can be useful to an attacker, as it effectively provides a map of how the system is built. For example, attackers can identify specific classes and methods, understand how requests are processed, and determine which libraries are being used. They could then research known vulnerabilities in those libraries or attempt to exploit weak points in the application’s logic.

Additionally, file paths and package names can reveal details about the server environment and project structure, which further increases the risk of targeted attacks.

To prevent this, a global ExceptionMapper is used to hide internal error details and return a generic error response instead. This ensures that sensitive implementation details are not exposed, improving the overall security of the API.

### Q:
>Why is it advantageous to use JAX-RS filters for cross-cutting concerns like
logging, rather than manually inserting Logger.info() statements inside every single re-
source method?

### A:
If I manually inserted Logger.info() statements inside every resource method, this would not only take a long time to implement but would also quickly clutter the code, making it repetitive and harder to maintain. Any changes to logging would require modifying multiple methods, increasing the risk of inconsistencies.

Using a JAX-RS filter is more effective because logging is a cross-cutting concern that applies to all endpoints. The filter runs automatically before and after each request, keeping the logging logic in one place. This keeps resource classes clean and focused on their main responsibilities, while ensuring consistent logging across the entire API. It also makes the system easier to maintain and scale, as changes only need to be made in a single location.




