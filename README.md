# Exambyte

**Exambyte** is a web-based exam platform built with **Java**, **Spring Boot**, and **Thymeleaf**. The application enables easy creation, management, and evaluation of online tests and quizzes, supporting both teachers (admins) and students. It features user authentication via GitHub, automatic grading, and a responsive, user-friendly web interface.

---

## 🚀 Features

- **User Authentication:** Login with GitHub OAuth2.
- **Role Management:** Admins can create and manage tests; students can participate in tests.
- **Test Management:**
  - Create, edit, preview, and delete tests.
  - Set start/end times and result release windows for each test.
  - Add, edit, and remove questions (with points, explanations, answers, and correct options).
- **Taking Tests:**
  - Students view available tests and can start tests within allowed time windows.
  - Answer handling for multiple-choice and free-text questions.
  - User answers are saved and can be reviewed.
- **Automatic Grading:**
  - Tests are graded automatically based on predefined solutions.
  - Instant feedback with pass/fail status and point breakdown.
- **Results & Review:**
  - View results, including which questions were answered correctly and the total score.
  - Review correct answers and explanations post-test.
- **Session Management:** Secure login/logout, protected user sessions.
- **Responsive UI:** Clean, Bootstrap-based design with custom styling.
- **Error Handling:** User-friendly error pages and validation.

---

## 🛠️ Tech Stack

- **Backend:** Java, Spring Boot (Spring MVC, Spring Security, Spring Data JDBC)
- **Frontend:** Thymeleaf, HTML, CSS, Bootstrap
- **Database:** PostgreSQL
- **Build Tool:** Gradle (Groovy)
- **Testing:** JUnit, Mockito
- **Authentication:** OAuth2 with GitHub
- **Version Control:** Git

---


## Core Concepts Demonstrated

- Spring Boot MVC, Security, and JDBC integration
- Thymeleaf templating for server-rendered HTML
- Repository pattern and separation of concerns
- DTOs and input validation (Jakarta Validation)
- OAuth2 authentication with external providers
- Automated test grading and result feedback
- Automated testing with JUnit and Mockito
- Unit and integration tests for services and controllers

---


## 🤝 Contributing

This project is intended as a portfolio and learning resource. Issues and feedback are welcome!

---


**Author:** [bekpalin95](https://github.com/bekpalin95)

> For questions or suggestions, open an issue or contact on GitHub.
