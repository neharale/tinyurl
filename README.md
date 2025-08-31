# TinyURL - URL Shortener Service

A URL shortener service built with Java Spring Boot. This service allows users to create short URLs from long URLs and provides redirect functionality.

## Features

- URL Shortening: Convert long URLs to short, manageable codes
- Custom Aliases: Optional custom short codes for URLs
- Persistence: H2 database with automatic schema updates
- RESTful API: Clean HTTP endpoints for URL operations
- Docker Support: Containerized deployment ready
- H2 Console: Built-in database management interface

## Technology Stack

- Java 17 - Modern Java runtime
- Spring Boot 3.3.2 - Application framework
- Spring Data JPA - Data persistence
- H2 Database - In-memory/file-based database
- Maven - Build and dependency management
- Docker - Containerization

## Prerequisites

- JDK 17 or higher
- Maven 3.6+
- Docker (optional, for containerized deployment)

## Security Notice

⚠️ Important: This application is configured for development use by default. Before deploying to production:
- Change all default passwords and credentials
- Disable development-only features (H2 console, debug endpoints)
- Use HTTPS and secure database connections
- Review and implement proper security measures

## Quick Start

### Local Development

1. **Clone and navigate to the project:**
   ```bash
   cd tinyurl
   ```

2. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

3. **Test the service:**
   ```bash
   # Shorten a URL
   curl -X POST http://localhost:8080/shorten \
     -H "Content-Type: application/json" \
     -d '{"url":"https://example.com/some/very/long/path"}'
   
   # Response: {"shortUrl":"http://localhost:8080/1"}
   
   # Visit the short URL in your browser:
   # http://localhost:8080/1
   ```

### Docker Deployment

1. **Build and run with Docker:**
   ```bash
   docker build -t tinyurl .
   docker run -p 8080:8080 tinyurl
   ```

2. **Or use Docker Compose (if available):**
   ```bash
   docker-compose up --build
   ```

## API Endpoints

### Shorten URL
- **POST** `/shorten`
- **Request Body:**
  ```json
  {
    "url": "https://example.com/very/long/url/path",
    "alias": "optional-custom-code"
  }
  ```
- **Response:**
  ```json
  {
    "shortUrl": "http://localhost:8080/abc123"
  }
  ```

### Redirect to Original URL
- **GET** `/{code}`
- **Response:** 302 redirect to the original URL
- **Error:** 404 if short code doesn't exist

## Database

The application uses H2 database with the following configuration:
- File Location: `./data/tinyurl.mv.db`
- Console Access: http://localhost:8080/h2-console (development only)
- Schema: Auto-updated on startup

> ⚠️ Security Note: The H2 console and default credentials are for development only. Never expose H2 console in production environments.

## Configuration

Key configuration options in `application.properties`:
- Server Port: 8080 (configurable via `server.port`)
- Base Domain: http://localhost:8080 (update for production via `app.base-domain`)
- Database: H2 with file persistence (development only)
- JPA: Auto-schema updates enabled

> ⚠️ Security Note: Never commit sensitive configuration values to version control. Use environment variables or external configuration files for production deployments.

## Project Structure

```
src/main/java/com/tinyurl/
├── TinyUrlApplication.java    # Main application class
├── UrlController.java         # REST API endpoints
├── model/                     # Data models
│   ├── ShortenRequest.java
│   ├── ShortenResponse.java
│   └── UrlMapping.java
├── repository/                # Data access layer
│   └── UrlRepository.java
└── service/                   # Business logic
    ├── Base62.java           # Base62 encoding utility
    └── UrlService.java       # URL shortening service
```

## Development

### Building
```bash
mvn clean package
```

### Running Tests
```bash
mvn test
```

### Running with Profile
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Production Deployment

### Security Considerations

Before deploying to production, ensure you:

1. Disable H2 Console - Set `spring.h2.console.enabled=false` in production
2. Use Strong Database Credentials - Never use default/empty passwords
3. Configure HTTPS - Use SSL/TLS for all production endpoints
4. Set Secure Headers - Implement security headers and CORS policies
5. Use Production Database - Consider PostgreSQL, MySQL, or cloud databases
6. Environment Isolation - Use separate databases for dev/staging/prod

### Environment Variables
Set these for production deployment:
- `SPRING_PROFILES_ACTIVE=prod`
- `APP_BASE_DOMAIN=https://yourdomain.com`
- `SPRING_DATASOURCE_URL` (for production database)
- `SPRING_DATASOURCE_USERNAME` (strong username)
- `SPRING_DATASOURCE_PASSWORD` (strong password)
- `SPRING_H2_CONSOLE_ENABLED=false`

### Cloud Deployment
The application is ready for deployment on:
- Google Cloud Run
- AWS Lambda (with modifications)
- Azure App Service
- Heroku
- Render

## Monitoring and Health

- Health Check: Available at `/actuator/health` (if Spring Boot Actuator is added)
- Logs: Spring Boot default logging

> ⚠️ Security Note: In production, ensure monitoring endpoints are properly secured and don't expose sensitive system information.

## Future Enhancements

- [ ] Click analytics and tracking
- [ ] URL expiration dates

## License

This project is open source and available under the [MIT License](LICENSE).

## Support

For issues and questions:
- Create an issue in the repository
- Review application logs for debugging information
- Check the application status endpoint for health information
