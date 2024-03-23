# Call-Of-Project Backend
![cop_logo](https://github.com/CallOfProject/CallOfProject-Backend/assets/147416047/651d115b-fdbb-41c2-84b5-a6ad113d1088)

# Contents

- [Javadoc Documentation](#documentation)
- [Overview](#overview)
- [Architecture](#architecture)
- [Used Technologies](#used-technologies-frameworks-environments)
- [Services](#services)
  - [Detailed Descriptions](#detailed-descriptions)
- [Admin Panel](#admin-panel)
- [Frontend](#frontend)
- [Interview Part](#interview-part)
- [Mobile Part (Android)](#mobile-part-android)
- [Architecture Diagram](#architecture-diagram)
- [ER Diagrams](#er-diagrams)
- [Test Results](#test-results)
- [NOTES](#notes)

## Javadoc Documentation
- [Javadoc for some services](https://callofproject.github.io/Call-of-Project-API-Documentation/)

## Overview

Call-Of-Project is an application where people can find project partners, collaborate on projects together, and manage project management to some extent. With this application, individuals can find friends to progress together when they have a project in mind, and people who want to join a project can also become part of a project. The project owner can create a project, approve joining requests for the project, and assign tasks to team members within the same project. Additionally, the project owner can prepare coding or testing interviews to select the right people to join the project. This way, they can work on a project together.

## Architecture

Call-Of-Project follows a **microservices-based architecture**. Our application has adopted microservices architecture and uses **Apache Kafka** to ensure data integrity, collect and process data, and ensure the smooth operation of various processes. In this context, communication is established between microservices and data transfer is carried out using **Kafka's message queue** feature. Furthermore, with the features provided by Kafka such as clustering and high availability, the system's durability and performance are enhanced. Thus, while data flow is provided throughout the application, data integrity and proper processing of transactions are ensured. Users send all requests through a single endpoint via **API Gateway**. Each microservice has its own database. User authentication and authorization operations are performed using **JWT**. Sensitive information such as passwords is stored in hashed form. Actions in our application are sent to the necessary users via notification and/or email. We used **Apache Kafka** and **Websocket** for notifications to be delivered in real-time. Additionally, there are **scheduler services** in our application that run at specific times every day. These services are responsible for sending reminder emails to users, handling applications with past feedback time, and closing projects with expired deadlines, among other purposes.

### Used Technologies
- Java 17
- Spring Boot 3.1.5
- Apache Kafka
- Websocket
- JWT
- MySQL
- MongoDB
- H2 for tests
- Eureka Server
- API Gateway
- Our Team Libraries
- JUnit
- Mock
- Mapstruct
- Amazon S3 (for file and photos)
- ...

## Services

Our application consists of 11 services:

1. **Authentication Service**
2. **Project Service**
3. **Interview Service**
4. **Community Service**
5. **Task Service**
6. **Ticket Service**
7. **Notification Service**
8. **Scheduler Service**
9. **Email Service**
10. **Environment Service**
11. **Filter and Search Service**

These services are independent and can operate as standalone applications, adhering to the principles of microservices architecture. Additionally, we used **EurekaServer** to facilitate the discovery of services. We aimed to make our application scalable and adaptable to growth as much as possible. We also adhered to **SOLID principles** as much as possible while developing services.

### Detailed Descriptions

- **Authentication Service**: Responsible for user information, authentication, and user profile-related operations. Communicates with the **Environment service** to obtain necessary information. Also, it publishes user information that is saved, deleted, or updated to the required topic.
- **Project Service**: Manages operations related to projects, such as creating and updating projects. It also contains special methods for the user, admin, and root roles.
- **Interview Service**: Manages the preparation of interviews by project owners and allows participant candidates to attend interviews. With this service, you can prepare Coding and Test interviews. You can participate in the interview via the link sent to you by email. In the Coding interview, you will be greeted with an IDE where you can run and test your code.
    - **Frontend**: [CallOfProject/Call-Of-Project-Interview](https://github.com/CallOfProject/Call-Of-Project-Interview)
- **Community Service**: A service where users can connect with each other. Perhaps a chat system could be added later.
- **Task Service**: Allows project owners to assign tasks to participants and enables participants to track their tasks. The **Scheduler service** comes into play for actions such as upcoming tasks and canceled tasks.
- **Ticket Service**: A service where users can report inappropriate incidents on the platform or report errors in the application.
- **Notification Service**: Notification messages sent to users are received by the Notification Service via **Apache Kafka**, which then sends notifications to users via **Websocket**.
- **Scheduler Service**: Triggers scheduled events. It updates the status of overdue interviews, projects, or projects whose start time has arrived.
- **Email Service**: Responsible for sending emails asynchronously.
- **Environment Service**: A service where information such as school, course, and experience of users is processed. It is dependent on a NoSQL database.
- **Filter and Search Service**: A service that performs search and filtering operations for users.

## Admin Panel
Our application also features an Admin Panel where events can be managed. You can explore it through the following link:
- **Admin Panel:** [Call-of-Project Admin Panel](https://github.com/CallOfProject/CallOfProject-AdminPanel)

## Frontend
The main frontend part of our application is not finished yet. Once completed, we will provide the link below.
- **Frontend Link:** 

## Interview Part
You can look at the interview service frontend part
- **Interview Part:** [Call-of-Project Interview](https://github.com/CallOfProject/Call-Of-Project-Interview)

## Mobile Part (Android)
- **Android:** [Call-of-Project Android](https://github.com/CallOfProject/Call-Of-Project-Android)

### Architecture Diagram
- ![architecture_diagram](https://github.com/CallOfProject/CallOfProject-Backend/assets/62218588/5caaa177-1e5a-4a0a-bdd6-40ffba8404b7)

  
- ![app](https://github.com/CallOfProject/CallOfProject-Backend/assets/147416047/83e7a49b-6fe2-4c3b-89cc-0cd3e807f180)

### ER Diagrams
- ![community_db](https://github.com/CallOfProject/CallOfProject-Backend/assets/147416047/1465e756-7be7-411a-9cb2-84e42e6d3045)
- ![cop_authentication_db](https://github.com/CallOfProject/CallOfProject-Backend/assets/147416047/a39ace64-2fdd-466b-8c8d-0624395ca85e)
- ![cop_interview_db](https://github.com/CallOfProject/CallOfProject-Backend/assets/147416047/3c7015ae-017b-4ada-831d-3edcaae493b9)
- ![cop_project_db](https://github.com/CallOfProject/CallOfProject-Backend/assets/147416047/e6bf09af-00d4-4aef-b05a-3d8384d909e3)
- ![cop_task_db](https://github.com/CallOfProject/CallOfProject-Backend/assets/147416047/e36f16e3-541d-4cb1-882d-51c949b8efef)

### Test Results
https://github.com/CallOfProject/CallOfProject-Backend/assets/147416047/135e0303-538a-4fe2-b190-3a8956683cbc 
- **Test Result:** Passed all tests

# NOTES
- Testing and bug fixing processes are ongoing...

- **Author:** Nuri Can ÖZTÜRK
