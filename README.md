# 🛒 Order Delivery System  

## 📌 Overview  
The **Order Delivery System** is a **Spring Boot-based microservices** application that manages shopping carts, orders, and payments. It integrates **Kafka** for event-driven communication and **state machines** for order tracking (`Placed → Shipped → Delivered`).  

---

## 📂 **Project Structure**  

```
order-delivery-system/
│-- cart-service/         # Handles shopping cart operations
│-- payment-service/      # Processes payments
│-- order-service/        # Manages orders & state transitions
│-- kafka-service/        # Kafka integration for messaging
│-- common-utils/         # Shared utilities across services
│-- docker/               # Docker configurations
│-- README.md             # Project documentation
│-- pom.xml               # Maven dependencies
└── .gitignore            # Git ignore file
```

---

## ⚙️ **Tech Stack**  
- **Java 17** (Spring Boot)  
- **Spring Data JPA** (H2 Database)  
- **Spring Kafka** (Message Queue)  
- **Spring State Machine** (Order Status Management)  
- **RestTemplate** (Microservices communication)  
- **JUnit 5** (Testing)  
- **Docker** (Containerization)  

---

## 🔄 **Order Processing Flow**  

1. ✅ **Add to Cart** → Users add products to the cart.  
2. 💳 **Process Payment** → `RestTemplate` verifies payment.  
3. 📦 **Order Confirmation** → Paid carts move to **Order Service**.  
4. 🚚 **Kafka Message** → Order status set to `Order_Placed`.  
5. 🔄 **State Machine** → Updates order status (`Placed → Shipped → Delivered`).  
6. ⏳ **Scheduler** → Every 10s, it processes Kafka events and updates status.  

---

## 🛠️ **Setup & Installation**  

### 1️⃣ **Clone the Repository**  
```sh
git clone https://github.com/RathorChanchal1/order-delivery-system.git
cd order-delivery-system
```

### 2️⃣ **Build the Project**  
```sh
mvn clean install
```

### 3️⃣ **Run the Services**  
Each microservice can be run separately:  
```sh
cd cart-service
mvn spring-boot:run
```
Similarly, start **payment-service** and **order-service**.  

### 4️⃣ **Kafka Setup**  
Start Kafka & Zookeeper (if using Docker):  
```sh
docker-compose up -d
```

### 5️⃣ **Test API Endpoints**  
Use **Postman** or `curl` to test APIs:  
```sh
# Add product to cart
POST http://localhost:8083/cart/add  
Content-Type: application/json  
{
    "productId": 123,
    "shopNotShop": true
}

# Process payment
POST http://localhost:8083/payment/process
```

---

## 🔍 **Docker Deployment**  
To run the entire system in Docker:  
```sh
docker-compose up --build
```

---

## 📌 **To-Do & Future Enhancements**  
✅ Improve order tracking with notifications  
✅ Add security with OAuth2  
✅ Implement distributed logging with ELK Stack  

---

## 🤝 **Contributing**  
Feel free to fork, raise PRs, or report issues! 🚀  

---
