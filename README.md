# Mobile Provider API - SE 4458

A backend API designed to manage mobile service providers and their offerings. Built with Spring Boot, this project provides RESTful endpoints for operations on providers, plans, and users.

---

## 🔗 Source Code

> **GitHub Repository**: [https://github.com/BerkantGC/mobile-provider](https://github.com/BerkantGC/mobile-provider)

---
## 🌐 API Access

- **Base URL**: `https://mobile-provider-1.onrender.com`
- **Swagger UI**: [https://mobile-provider-1.onrender.com/swagger-ui.html](https://mobile-provider-1.onrender.com/swagger-ui.html)
- **OpenAPI Docs (JSON)**: [https://mobile-provider-1.onrender.com/v1/api-docs](https://mobile-provider-1.onrender.com/v1/api-docs)

---

## 🧠 Design, Assumptions & Issues

### 🧩 Design

- **Framework**: Spring Boot for building RESTful services.
- **Database**: PostgreSQL used for persistent storage.
- **Architecture**: Follows a clean layered architecture: Controller → Service → Repository.
- **API Documentation**: Auto-generated using Springdoc OpenAPI & Swagger.

## 🗂️ Data Model (ER Diagram)
- ER Diagram is in root folder.
  
## 🚀 Getting Started

### 📦 Requirements
- Internet access (the API is hosted online)
- /swagger-ui.html is not show api docs by default. Don't forget to change url to `/v1/api-docs` 

### 🛠 Requests
- Public endpoints can be tested directly.
- Authentication-required endpoints can be tested with token.
