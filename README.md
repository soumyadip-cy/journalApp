# **journalApp | Backend project with Spring Boot and Mongo DB**

Journal App is a **backend project** made using **Spring Boot** and **Mongo DB**.
> GitHub link for this app is [journalApp](https://github.com/soumyadip-cy/journalApp)

---

### Features

1. The project follows a Model-View-Controller (MVC) architecture, with components organized into distinct layers:
   - Controller: Exposes the REST APIs (the presentation layer).
   - Service: Encapsulates the business logic (the application/business logic layer).
   - Repository: Facilitates and extends database querying (the data access/persistence layer).
   - Entity: Holds the Plain Old Java Objects (POJOs) that map to database documents (part of the domain layer).
   - Security: Stores security configurations (often a cross-cutting concern or part of an infrastructure layer). 

2. The project utilizes MongoDB as its database and Spring Security for authentication. JUnit is used for automated unit and integration testing.