# ShopTillYouDrop : A full-stack web application for a Marketplace 
## Technologies:
- Spring Boot (with Spring Security, Spring Web, Spring Data JPA)
- MySQL
- Thymeleaf
- Bootstrap
- AWS (Amazon RDS for dataabse, Amazon S3 for file storage)
- Heroku for app hosting
## Functionalities:
There are three types of users: Admin, Seller, and Buyer. 
All users can register, login, search and view the products. Although unauthenticated visitors can view products, they will be prompted to sign in or log in to begin adding things to a cart.  
Admins manage the lists of users, categories, and orders throughout the site. They can also add, edit, and in some cases delete.  
Sellers can view, create, update and delete listings of products they own. They also can view their sales.    
Buyers can add and remove products to/from their cart and proceed to checkout. They also can view their orders.  

## How to run the app:
- Clone the repository to your local machine
- In the project's root directory, create a .env file to define your environment variables.
- Open the .env file and add the required environment variables:
  * MYSQL_HOST: The hostname or URL of the MySQL database.
  * MYSQL_PORT: The port MySQL is running on (default is 3306).
  * MYSQL_DB: The name of the MySQL database.
  * DB_USER: The username for the MySQL database.
  * DB_PASSWORD: The password for the MySQL database.
  * PORT: The port on which the application will run (default is 8080).
  * accessKeyId: The AWS access key for programmatic access.
  * secretKey: The AWS secret access key.
  * bucketName: The name of the S3 bucket used by the application.
  * region: The AWS region where the S3 bucket is located.
- In IntelliJ, go to "Run", then "Edit Configurations", then add "Environment Variables"; and add the .env file's path as a line under "Modify Options".
