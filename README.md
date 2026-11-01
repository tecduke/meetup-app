# Meetup Application

A complete meetup web application built with Java, Jakarta Servlet, JSP, JDBC, MySQL and Bootstrap 5.

## Features
- User registration and login
- Secure password storage using BCrypt
- Dashboard with Join Meetup and Create Meetup actions
- Meetup creation with image upload
- Meetup joining and leaving without duplicates
- Chat room for authorized meetup members
- Notifications for meetup join and chat activity
- MySQL relational schema with users, meetups, participants, messages and notifications
- Responsive modern UI with blue and white styling

## Prerequisites
- Java 17+
- Maven 3.9+
- MySQL 8+
- Apache Tomcat 11+

## Local setup
1. Create a MySQL database:
   ```sql
   CREATE DATABASE meetup_app;
   ```
2. Run the SQL schema script:
   ```bash
   mysql -u root -p meetup_app < database/schema.sql
   ```
3. Set environment variables:
   ```bash
   export DB_URL='jdbc:mysql://localhost:3306/meetup_app?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC'
   export DB_USER='root'
   export DB_PASSWORD='your_password'
   ```
4. Build the project:
   ```bash
   mvn clean package
   ```
5. Deploy the WAR to Tomcat 11 or run it with a servlet container.

## Deployment notes
- The app is designed for free-tier deployment.
- For local testing, use a local MySQL instance.
- For hosted deployment, free-tier options include:
  - Railway MySQL or Postgres (if available in your region)
  - Aiven free-tier MySQL (limited resources)
  - Render or Fly.io for the Java app itself
- This app does not require any paid API keys.
- Remember that free-tier databases often have sleep cycles, small storage limits, and temporary inactivity shutdowns.

## Project structure
- `database/schema.sql` – database tables
- `src/main/java` – Java servlets and DAO layer
- `src/main/webapp/WEB-INF/views` – JSP pages
- `src/main/webapp/assets/css` – styling

## Notes
- Uploaded meetup images are stored in `src/main/webapp/uploads` during local testing.
- For production, configure a persistent storage directory or a managed file service.
