# Library Web API

This project involves the development of a CRUD Web API for simulating a library system using the Spring framework.

## Project Setup

To run this project, follow these steps:

1. Clone the repository.
2. Configure the database settings in `config-server`.
3. Build and run the project using Maven or your IDE.
4. Utilize Docker for containerization and deployment.

## Main Web API Functionality

### Book Information

1. Retrieve a list of all books.
2. Get a specific book by its ID.
3. Find a book by its ISBN.
4. Add a new book.
5. Update information about an existing book.
6. Delete a book.

## Additional Web API Functionality

1. Develop an additional service (LibraryService) for tracking available books.
2. Upon adding a new book in the primary service, send a request (synchronous or asynchronous) containing the book ID.
3. The new service stores information about:
    - The book (ID).
    - Time when the book was borrowed.
    - Time when the book is due to be returned.

## Implemented Functionality

1. Retrieve a list of available books.
2. Modify book information.

## Technologies Used

1. **Spring Boot 3**
2. **Java 17**
3. **ORM**: **Spring Data JPA**
4. **RDBMS**: **MySQL** (database per microservice)
5. **Spring Cloud Config**
6. **API Gateway**
7. **Eureka Discovery**
8. **Apache Kafka** for user notifications on order creation
9. **OpenAPI Swagger**
10. **Maven Multi-Module Project**
11. **Spring Security** for authentication
12. **MapStruct**
13. **Docker** for containerization and deployment

## Database Schema

![Database Schema](./images/db.png)

## Project Architecture

![Project Architecture](./images/drawio.png)

### How to start application?
```shell
docker-compose build
docker-compose up -d
```
