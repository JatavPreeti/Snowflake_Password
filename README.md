#Secure Password Rotator

A Spring Boot application to automatically rotate Snowflake user passwords and notify the change via Slack. 
This improves the security and compliance posture by enforcing regular password changes and securely alerting relevant teams.

## Features

- Generates strong, random passwords.
- Rotates Snowflake user passwords securely via JDBC.
- Sends password rotation notifications to a configured Slack webhook.
- Exposes REST API for manual password rotation.
- Simple frontend using Thymeleaf for interaction (optional homepage).
- Secure configurations using `application.properties`.

## Project Structure

src
├── main
│ ├── java
│ │ └── com.example.demo
│ │ ├── controller
│ │ │ ├── PageController.java
│ │ │ └── PasswordRotationController.java
│ │ ├── model
│ │ │ └── RotationResponse.java
│ │ ├── service
│ │ │ └── PasswordRotationService.java
│ │ ├── util
│ │ │ └── PasswordGenerator.java
│ │ └── SecurePasswordRotatorApplication.java
│ └── resources
│ └── application.properties
└── pom.xml

##Configuration

Edit the `application.properties` file with the following environment-specific values:

```properties
# Snowflake Configuration
snowflake.url=jdbc:snowflake://<your_snowflake_account>
snowflake.admin.username=YOUR_ADMIN_USERNAME
snowflake.admin.password=YOUR_ADMIN_PASSWORD
snowflake.database=YOUR_DB
snowflake.schema=YOUR_SCHEMA
snowflake.target.user=TARGET_USER_TO_ROTATE

# Slack Notification
slack.webhook.url=https://hooks.slack.com/services/...

# Server
server.port=9090
🔄 API Endpoint
POST /rotate-password
Rotates the password for the configured Snowflake user and sends a Slack notification.

Sample Response
{
  "success": true,
  "message": "Password rotated successfully!"
}
🛠️ Build & Run
Clone the repository
git clone https://github.com/<your-username>/secure-password-rotator.git
cd secure-password-rotator
Build the project
mvn clean install
Run the application

mvn spring-boot:run
Trigger the rotation

Send a POST request using Postman or curl:

curl -X POST http://localhost:9090/rotate-password
🧪 Tech Stack
Java 17
Spring Boot 3.5.x
Snowflake JDBC Driver
Slack Webhook Integration
Maven
