```md
# 📚 Webtoon Platform

A full-stack Spring Boot web application for reading, creating, and managing webtoons with features like user authentication, series creation, episodes, notifications, likes, bookmarks, and reading history.

---

## 🚀 Features

### 👤 User System
- User registration and login (Spring Security)
- Role-based access (Reader / Creator)
- Profile management (name, bio, image upload)

### 📖 Series & Episodes
- Create new webtoon series with cover image
- Add and view episodes
- Browse featured, recommended, and genre-based series
- Search functionality for series

### ❤️ Interaction Features
- Like series
- Bookmark (favorites system)
- Continue reading tracking

### 🔔 Notifications System
- Notify users when:
  - New series is published
  - Creator posts new content
- Personalized user notifications
- Sorted by latest date

### 📊 Reader Dashboard
- Featured webtoons carousel
- Continue reading section
- Genre-based filtering
- Recommendations based on reading history

---

## 🛠️ Tech Stack

### Backend
- Java 17+
- Spring Boot
- Spring MVC
- Spring Security
- Spring Data JPA
- Hibernate

### Frontend
- Thymeleaf
- HTML, CSS
- JavaScript (basic interactivity)

### Database
- MySQL / MariaDB

### Other Tools
- Maven
- File upload system for images

---

## 📂 Project Structure

```

com.example.Webtoon
├── controller
├── model
├── repository
├── service
├── config
├── WebtoonApplication.java

````

---

## ⚙️ Setup Instructions

### 1. Clone the repository
```bash
git clone https://github.com/your-username/webtoon.git
cd webtoon
````

### 2. Configure Database

Update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_db
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

### 3. Run the project

```bash
mvn spring-boot:run
```

---

### 4. Open in browser

```
http://localhost:8081
```

---

## 📌 Key Endpoints

* `/reader_home` → Main dashboard
* `/series/{id}` → View series & episodes
* `/search` → Search series
* `/profile` → User profile
* `/notifications` → User notifications

---

## 💡 Future Improvements

* Real-time notifications (WebSocket)
* Comment system for episodes
* Advanced recommendation engine
* Pagination for series feed
* REST API + React frontend version

---

## 👩‍💻 Author

Built by **Khushnuma Parween**

---
