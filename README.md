# 📚 Webtoon Platform

A full-stack web application built with **Java and Spring Boot** for reading, creating, and managing webtoons. The platform supports user authentication, series and episode management, bookmarks, likes, reading history, personalized recommendations, and notifications.

## ✨ Features

### 👤 User Management
- User registration and login using Spring Security
- Role-based access control for Readers and Creators
- Profile management with name, bio, and profile image upload

### 📖 Series and Episodes
- Create and manage webtoon series with cover images
- Publish and browse episodes
- Explore featured and recommended webtoons
- Filter series by genre
- Search for webtoons by title or relevant keywords

### ❤️ Reader Interactions
- Like webtoon series
- Bookmark favorite series
- Track reading progress
- Continue reading from where you left off
- View personalized recommendations based on reading history

### 🔔 Notifications
- Notify users when a new series is published
- Notify readers when creators publish new episodes
- Display personalized notifications
- Sort notifications by the latest date

### 📊 Reader Dashboard
- Featured webtoon carousel
- Continue Reading section
- Genre-based browsing and filtering
- Recommended series based on reading activity

## 🛠️ Tech Stack

| Category | Technologies |
|---|---|
| Language | Java 17+ |
| Backend | Spring Boot, Spring MVC |
| Security | Spring Security |
| ORM | Spring Data JPA, Hibernate |
| Frontend | Thymeleaf, HTML, CSS, JavaScript |
| Database | MySQL / MariaDB |
| Build Tool | Maven |
| File Handling | Image upload and storage |

## 📂 Project Structure

```text
webtoon/
├── src/
│   ├── main/
│   │   ├── java/com/example/webtoon/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── WebtoonApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   └── images/
│   │       ├── templates/
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md
```

## ⚙️ Getting Started

### Prerequisites

Ensure the following are installed:

- Java 17 or later
- Maven
- MySQL or MariaDB

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/webtoon.git
cd webtoon
```

### 2. Create the Database

Create a database in MySQL:

```sql
CREATE DATABASE webtoon_db;
```

### 3. Configure the Application

Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/webtoon_db
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8081
```

Replace the database credentials with your local MySQL credentials.

**Note:** `spring.jpa.hibernate.ddl-auto=update` is suitable for local development. For production, use a database migration tool such as Flyway or Liquibase.

### 4. Run the Application

Using Maven:

```bash
mvn spring-boot:run
```

Or build and run the JAR:

```bash
mvn clean package
java -jar target/webtoon-*.jar
```

### 5. Open the Application

Visit:

```text
http://localhost:8081
```

## 🔗 Key Endpoints

| Endpoint | Description |
|---|---|
| `/reader_home` | Reader dashboard |
| `/series/{id}` | View a webtoon and its episodes |
| `/search` | Search webtoon series |
| `/profile` | View or manage the user profile |
| `/notifications` | View personal notifications |

*Note: These endpoints describe the planned application routes and should match the actual controller mappings in the implementation.*

## 🚀 Future Improvements

- Real-time notifications using WebSockets
- Episode-level comments and discussions
- Advanced recommendation engine
- Pagination and sorting for series feeds
- REST API with a React frontend
- Cloud-based image storage
- Automated tests and CI/CD integration

## 👩‍💻 Author

**Khushnuma Parween**

---

*Built with Java and Spring Boot.*
