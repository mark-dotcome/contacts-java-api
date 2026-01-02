# Contacts Java API

A RESTful API for managing contacts built with Spring Boot and MongoDB. This is a Java port of the original Python FastAPI contacts application.

## Technology Stack

- Java 17
- Spring Boot 3.2.0
- Spring Data MongoDB
- MongoDB Atlas
- Maven
- Lombok

## Project Structure

```
src/main/java/com/contacts/api/
├── ContactsApiApplication.java    # Main application entry point
├── controller/
│   └── ContactController.java     # REST endpoints
├── dto/
│   └── ContactSearchResponse.java # Search response DTO
├── exception/
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── model/
│   ├── Address.java               # Embedded address model
│   └── Contact.java               # Contact entity
├── repository/
│   ├── ContactRepository.java     # Spring Data repository
│   ├── CustomContactRepository.java
│   └── CustomContactRepositoryImpl.java  # Custom search implementation
└── service/
    └── ContactService.java        # Business logic layer
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/contacts/` | Get all contacts (max 100, sorted by lastName descending) |
| GET | `/contacts/search` | Search contacts with pagination and filtering |
| GET | `/contacts/{contactId}` | Get a contact by ID |
| POST | `/contacts/` | Create a new contact |
| PUT | `/contacts/{contactId}` | Update an existing contact |
| DELETE | `/contacts/{contactId}` | Delete a contact |

### Search Parameters

The `/contacts/search` endpoint supports the following query parameters:

- `q` - Search query (searches across firstName, lastName, email, phone, and address fields)
- `page` - Page number (default: 1)
- `sort_by` - Field to sort by (default: lastName)
- `order` - Sort order: "asc" or "desc" (default: asc)
- `limit` - Results per page (default: 10, max: 100)

### Contact Model

```json
{
  "id": "string",
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "phone": "string",
  "address": {
    "street": "string",
    "city": "string",
    "state": "string",
    "zip": "string"
  },
  "app": "string",
  "createdBy": "string",
  "createdDt": "datetime",
  "modifiedBy": "string",
  "modifiedDt": "datetime"
}
```

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MongoDB Atlas account (or local MongoDB instance)

## Configuration

The application requires the following environment variables:

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `MONGODB_URI` | Yes | - | MongoDB connection string |
| `MONGODB_DATABASE` | No | ContactDb | Database name |
| `PORT` | No | 8080 | Server port |

Example:
```bash
export MONGODB_URI="mongodb+srv://user:password@cluster.mongodb.net/?retryWrites=true&w=majority"
```

## Running the Application

### Using Maven

```bash
MONGODB_URI="your-mongodb-uri" ./mvnw spring-boot:run
```

### Building and Running JAR

```bash
./mvnw clean package
java -jar target/contacts-api-1.0.0.jar
```

The API will be available at `http://localhost:8080`.

## Example Requests

### Create a Contact

```bash
curl -X POST http://localhost:8080/contacts/ \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phone": "555-1234",
    "address": {
      "street": "123 Main St",
      "city": "Springfield",
      "state": "IL",
      "zip": "62701"
    },
    "app": "contacts-app"
  }'
```

### Search Contacts

```bash
curl "http://localhost:8080/contacts/search?q=john&page=1&limit=10&sort_by=lastName&order=asc"
```

### Get All Contacts

```bash
curl http://localhost:8080/contacts/
```

### Get Contact by ID

```bash
curl http://localhost:8080/contacts/{contactId}
```

### Update a Contact

```bash
curl -X PUT http://localhost:8080/contacts/{contactId} \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Smith",
    "email": "john.smith@example.com",
    "phone": "555-5678",
    "address": {
      "street": "456 Oak Ave",
      "city": "Springfield",
      "state": "IL",
      "zip": "62702"
    },
    "app": "contacts-app"
  }'
```

### Delete a Contact

```bash
curl -X DELETE http://localhost:8080/contacts/{contactId}
```

## License

This project is open source.
