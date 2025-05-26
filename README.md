
# Overview

A full-stack web application for a marketplace platform that connects sellers with customers.

## Technologies

- **Backend**: Spring Boot (with Spring Security, Spring Web, Spring Data JPA)
- **Database**: MySQL
- **Frontend**: Thymeleaf, Bootstrap 5
- **Cloud Services**: 
  - AWS RDS for database
  - AWS S3 for image storage
- **Payment Processing**: Stripe API

## Features

- User authentication and role-based authorization
- Product catalog with search and filtering
- Shopping cart functionality
- Secure checkout process
- Order management system
- User profile management
- Product listing management for sellers
- Admin dashboard with comprehensive controls

## Installation & Setup

1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/shoptillyoudrop.git
   cd shoptillyoudrop
   ```

2. **Configure Environment Variables**
   Create a `dev.env` file in the project's root directory with the following variables:

   ```properties
   # Database Configuration
   MYSQL_HOST=your-database-host
   MYSQL_PORT=3306
   MYSQL_DB=marketplace
   DB_USER=your-username
   DB_PASSWORD=your-password
   
   # Server Configuration  
   PORT=8080
   
   # AWS Configuration
   accessKeyId=your-aws-access-key
   secretKey=your-aws-secret-key
   bucketName=your-s3-bucket-name
   region=your-aws-region
   
   # Payment Processing
   stripeSecretKey=your-stripe-secret-key
   ```

3. **Set Up Development Environment**
    In IntelliJ IDEA:
    - Go to Run → Edit Configurations
    - Add Environment Variables under Modify Options
    - Add the path to your dev.env file

4. **Build and Run the Application**
   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

## Database Structure

The application uses MySQL database with the following schema:
- Users: Stores user information and credentials
- Products: Manages product listings and inventory
- Orders: Tracks customer orders
- Order_Items: Stores line items for each order
- Cart_Items: Manages shopping cart contents

The application uses Hibernate with `spring.jpa.hibernate.ddl-auto=update` to automatically manage schema changes.
