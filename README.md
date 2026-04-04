# DineFlex Backend 🍽️

DineFlex is a full-stack restaurant booking platform designed to streamline table reservations for both customers and restaurant managers. This is the **backend service**, built with **Spring Boot**, exposing RESTful APIs and enabling secure user authentication, dynamic restaurant data management, and real-time booking functionality.

## 🚀 Features

### ✅ User Authentication
- JWT-based login and registration system
- Secure token generation and validation
- Role-based access control (planned)

### ✅ Restaurant Management
- Dynamic mock data seeding with `CommandLineRunner`
- Realistic restaurant details: name, location, cuisine, hours, contact, image
- Data persisted via JPA (H2 in-memory database)

### ✅ Booking System
- 7-day rolling availability slot generation
- Only authenticated users can create bookings
- Booking validation and time conflict handling (in progress)

### ✅ Error Handling
- Centralized exception handler via `@ControllerAdvice`
- Custom exceptions: `UserNotFoundException`, `InvalidCredentialsException`, etc.

## 🧪 Tech Stack

- **Java 17 + Spring Boot**
- **Spring Security + JWT**
- **JPA (Hibernate) + H2 database**
- **Lombok**
- **Maven**

## 🔐 Security

- JWT token-based authentication
- CORS enabled, CSRF disabled (for REST APIs)
- Google Maps API key secured via `.env` and excluded from version control

## 🛠️ Setup Instructions

```bash
# Clone the repository
git clone https://github.com/Scavenger233/DineFlex.git
cd dineflex-backend

# Run the application
./mvnw spring-boot:run
````

> 📝 By default, the app runs on `http://localhost:8080`

## 🧪 API Preview

| Endpoint                | Method | Description                  |
| ----------------------- | ------ | ---------------------------- |
| `/api/auth/register`    | POST   | Register new user            |
| `/api/auth/login`       | POST   | Authenticate & get JWT token |
| `/api/restaurants`      | GET    | Fetch restaurant list        |
| `/api/restaurants/{id}` | GET    | Fetch restaurant details     |
| `/api/bookings`         | POST   | Make a booking (auth only)   |

## 📦 Mock Data Strategy

* Uses a `MockDataLoader` at startup to populate:

  * 10 restaurants with varying cuisines and cities
  * 4 daily time slots per restaurant (e.g., 17:30, 18:00, 19:30, 20:30)
  * Slots generated for the next 7 days based on system time

## 📈 Roadmap

* [ ] Role-based authorization (e.g., customer vs. restaurant admin)
* [ ] Email confirmation of bookings
* [ ] PostgreSQL support for production
* [ ] Docker & Jenkins-based CI/CD deployment

## 📄 License

This project is for academic and portfolio purposes. All contributions are welcome.

---

📍 **Frontend repo**: [DineFlex--Frontend](https://github.com/Scavenger233/DineFlex--Frontend)
