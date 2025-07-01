# Hotel Booking System - RESTful API

A Spring Boot-based RESTful API for managing hotel room bookings, built with role-based access control, JWT authentication, and MySQL persistence. It supports  **Admins** ,  **Hotel Managers** , and  **Customers** , offering secure booking and hotel management functionalities.

Author: Soumya Prakash Sahoo

## Tech Stack

* **Backend** : Spring Boot
* **Database** : MySQL
* **Authentication** : JWT (Stateless)
* **Authorization Roles** : `ADMIN`, `HOTEL_MANAGER`, `CUSTOMER`
* **Build Tool** : Gradle
* **Testing** : JUnit 5, Mockito
* **API Testing Tool** : Postman

---

## How to Run

1. Ensure MySQL is running and the database `test_db` exists.
2. Set up `application.properties` accordingly. Along with JWT secret key and expiration defined in application.properties file.
3. Run the application:

```
bash
   ./gradlew bootRun
```

4. Server starts at:

`	http://localhost:8081/`

## Roles & Permissions

| Role              | Permissions                                                                      |
| ----------------- | -------------------------------------------------------------------------------- |
| `ADMIN`         | Register,login,create hotel, delete hotel,get all hotels                         |
| `HOTEL_MANAGER` | Register,login,update hotel details, cancel bookings,get all hotels              |
| `CUSTOMER`      | Register, login, book rooms (1 per request), get booking details, get all hotels |

---

## Sample Credentials

* **Admin** : Use role `"ADMIN"` during registration
* **Hotel Manager** : Use role `"HOTEL_MANAGER"`
* **Customer** : Defaults to `"CUSTOMER"` if role not specified or you can use role "`CUSTOMER`".

---

## API Endpoints

### Auth APIs (Public)

| Method | Endpoint                | Description                |
| ------ | ----------------------- | -------------------------- |
| POST   | `/api/users/register` | Register and get JWT token |
| POST   | `/api/users/login`    | Login and get JWT token    |

---

### Hotel Management

| Method | Endpoint                  | Description          | Access       |
| ------ | ------------------------- | -------------------- | ------------ |
| POST   | `/api/hotels`           | Create a hotel       | Admin only   |
| GET    | `/api/hotels`           | View all hotels      | Public       |
| PUT    | `/api/hotels/{hotelId}` | Update hotel details | Manager only |
| DELETE | `/api/hotels/{hotelId}` | Delete a hotel       | Admin only   |

---

### Booking Management

| Method | Endpoint                      | Description         | Access        |
| ------ | ----------------------------- | ------------------- | ------------- |
| POST   | `/api/bookings/{hotelId}`   | Book a hotel room   | Customer only |
| GET    | `/api/bookings/{bookingId}` | Get booking details | Customer only |
| DELETE | `/api/bookings/{bookingId}` | Cancel booking      | Manager only  |

## Validation & Business Rules

* Only **one** room can be booked per request.
* Booking only allowed if `availableRooms > 0`.
* **Customers** cannot cancel bookings.
* **Check-in date** must be in the future.
* **Check-out date** must be after check-in.
* Dates must follow `YYYY-MM-DD` format.
* Password must be:
  * At least 8 characters

## Error Handling

| HTTP Code | Meaning                              |
| --------- | ------------------------------------ |
| 400       | Bad Request (Validation)             |
| 401       | Unauthorized (No token)              |
| 403       | Forbidden (Insufficient role)        |
| 404       | Not Found (User, Hotel, Booking etc) |

## Testing

Unit testing are included using JUnit 5, Mockito

./gradlew test

### Included Test Classes:

* `AuthServiceImplTest`
* `HotelServiceImplTest`
* `BookServiceImplTest`

## Project Structure

com.takehome.stayease
│
├── Config/
│   └── Security/
│       ├── AuthenticationFilter.java
│       └── SecurityConfig.java
│
├── Controller/
│   ├── AuthController.java
│   ├── BookingController.java
│   └── HotelController.java
│
├── DTO/
│   ├── RequestDto/
│   └── ResponseDto/
│
├── Entity/
│   ├── Booking.java
│   ├── Hotel.java
│   ├── User.java
│   └── Enum/
│
├── Exception/
│
├── Mapper/
│
├── Repository/
│
├── Service/
│   ├── Interface/
│   │   ├── AuthService.java
│   │   ├── BookingService.java
│   │   ├── HotelService.java
│   │   └── UserService.java
│   └── Implementation/
│       ├── AuthServiceImpl.java
│       ├── BookingServiceImpl.java
│       ├── HotelServiceImpl.java
│       ├── JwtService.java
│       └── UserServiceImpl.java
│
├── StayeaseApplication.java
│
└── test/
    └── java/com/takehome/stayease/service/
        ├── AuthServiceImplTest.java
        ├── BookingServiceImplTest.java
        └── HotelServiceImplTest.java
