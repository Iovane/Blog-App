# Blog Application
This is a Spring Boot application that serves as a backend for a blogging platform. The application provides RESTful APIs for managing blog posts, comments, and users.

## Features
- User authentication using JWT.
- CRUD operations for blog posts and comments.
- Role-based access control.
- Integration with Swagger for API documentation.

## Prerequisites
Before you begin, ensure you have met the following requirements:

- **Java 17** or higher
- **IntelliJ IDEA**
- **PostgreSQL**
- **Postman** (for testing authentication and JWT retrieval)
- **Swagger** (for easier testing after authentication)

## Installation
1. Clone the repository:

   ```bash
   git clone <repository-url>
   cd blog_app
   ```

2. Build the project using Maven:

   ```bash
   mvn clean install
   ```

3. Run the application:

   ```bash
   mvn spring-boot:run
   ```

## Running the application

After the initial run, you will have to provide database information inside **application.properties**, by default application uses PostgreSQL:
- spring.datasource.url=
- spring.datasource.username=
- spring.datasource.password=

schema.sql will create all the necessary tables and data.sql will insert 2 default users:
1. Admin, with **username** 'admin' with **password** 'admin123' and authorities **ADMIN** and **USER**
2. User, with **username** 'user' with **password** 'user123' and authorities **USER**

It's recommended to delete both files after first run, or edit **application.properties** file so that files don't get executed each consequtive time.
Just delete the followinf fields:
1. spring.jpa.defer-datasource-initialization=true
2. spring.sql.init.mode=always



## Authentication and Testing

### Step 1: Obtain JWT Token using Postman
1. Open Postman and create a GET request to the `/authenticate` endpoint.
2. Choose Basic Auth as Authorization type and provide credentials.
3. If successful, you will receive a JWT token in the response headers.

### Step 2: Access Swagger for API Testing
1. Navigate to `http://localhost:8080/swagger-ui.html` in your web browser.
2. Click on the "Authorize" button and enter the JWT token obtained from Postman.
3. Once authenticated, you can test the various endpoints directly through the Swagger UI.

## API Documentation
The API is documented using Swagger. After running the application, you can access the documentation at `http://localhost:8080/swagger-ui.html`.

## Contributing
If you want to contribute to this project, feel free to fork the repository and submit a pull request.
