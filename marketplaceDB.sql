CREATE DATABASE  IF NOT EXISTS `marketplace` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `marketplace`;
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: marketplacedb.cfg42k2s23yg.us-east-1.rds.amazonaws.com    Database: marketplace
-- ------------------------------------------------------
-- Server version	8.0.35

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '';

--
-- Table structure for table `cart_items`
--

DROP TABLE IF EXISTS `cart_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint DEFAULT NULL,
  `amount` int NOT NULL,
  `buyer_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `productId` (`product_id`),
  KEY `cart_items_ibfk_1_idx` (`buyer_id`),
  CONSTRAINT `cart_items_ibfk_1` FOREIGN KEY (`buyer_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK1re40cjegsfvw58xrkdp6bac6` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_items`
--

LOCK TABLES `cart_items` WRITE;
/*!40000 ALTER TABLE `cart_items` DISABLE KEYS */;
INSERT INTO `cart_items` VALUES (8,2,1,14),(88,13,1,21);
/*!40000 ALTER TABLE `cart_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Electronics',_binary '\0'),(2,'Books',_binary '\0'),(3,'Clothing',_binary '\0'),(4,'Pet Supplies',_binary '\0'),(5,'Furniture',_binary '\0'),(6,'Fruits, Vegetables, and Legumes',_binary '\0'),(7,'Clothes',_binary ''),(8,'Cloth',_binary ''),(9,'Art and Design',_binary '\0'),(10,'Personal care and beauty',_binary '\0');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `price` double DEFAULT NULL,
  `amount` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_order_product` (`order_id`),
  CONSTRAINT `fk_order_product` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (1,1,2,1100,3),(2,3,2,1100,3),(3,4,2,1100,1),(4,5,2,1100,1),(5,5,9,100.54,1),(6,6,2,1100,1),(7,6,9,100.54,1),(8,7,2,1100,2),(9,7,15,0.51,4),(10,7,17,77.77,1),(11,7,16,60.6,1),(12,8,15,0.51,3),(13,9,17,77.77,1),(14,10,13,50.99,1),(15,11,18,549.5,1),(16,11,15,0.51,1),(17,12,12,0.5,1),(18,13,9,100.54,1),(19,14,15,0.51,1),(20,15,12,0.5,1),(21,16,9,100.54,1),(22,18,2,1100,1),(23,18,16,60.6,2),(24,19,17,77.77,2),(25,19,14,250.95,1),(26,19,18,549.5,2),(27,21,13,50.99,2),(28,21,14,250.95,1),(29,21,18,549.5,2),(30,22,13,50.99,1),(31,23,17,77.77,1),(32,24,16,60.6,1),(33,25,17,77.77,2),(34,26,14,250.95,1),(35,27,25,15.22,1),(36,28,16,60.6,1),(37,28,19,10000.1,1);
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_date` timestamp NULL DEFAULT NULL,
  `buyer_id` bigint NOT NULL,
  `shipment_address` varchar(255) DEFAULT NULL,
  `total_price` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `orders_ibfk_1_idx` (`buyer_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`buyer_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,NULL,16,NULL,0),(2,NULL,16,NULL,0),(3,NULL,16,NULL,0),(4,NULL,16,NULL,0),(5,NULL,16,NULL,0),(6,NULL,16,NULL,0),(7,NULL,15,NULL,0),(8,NULL,15,NULL,0),(9,NULL,15,NULL,0),(10,NULL,16,NULL,50.99),(11,NULL,16,NULL,550.01),(12,NULL,16,'Montreal, QC',0.5),(13,NULL,16,'Montreal, QC',100.54),(14,NULL,16,'Montreal, QC',0.51),(15,'2024-09-22 00:00:00',16,'Montreal, QC, Canada',0.5),(16,'2024-09-22 10:14:40',16,'Montreal, QC, Canada',100.54),(18,'2024-09-22 19:58:09',15,'2024 Jim\'s place',1221.2),(19,'2024-09-22 20:46:27',15,'2024 Jim\'s place',1505.49),(20,'2024-09-22 21:38:26',11,'N.D.G.G',0),(21,'2024-09-23 17:35:47',16,'Montreal, QC, Canada',1451.93),(22,'2024-09-23 19:19:19',3,'',50.99),(23,'2024-09-23 19:24:31',3,'1234 definitely real place',77.77),(24,'2024-09-23 19:34:40',21,'400 crescent',60.6),(25,'2024-09-23 20:48:50',15,'2024 Jim\'s place',155.54),(26,'2024-09-24 12:42:48',15,'2024 Jim\'s place',250.95),(27,'2024-09-24 13:21:34',15,'2024 Jim\'s place',15.22),(28,'2024-09-24 15:15:21',22,'1234 Demo Lane',10060.7);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `price` double NOT NULL,
  `category_id` bigint NOT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  `stock_amount` int NOT NULL,
  `seller_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `products_ibfk_2_idx` (`seller_id`),
  KEY `products_ibfk_2_idx1` (`category_id`),
  CONSTRAINT `products_ibfk_1` FOREIGN KEY (`seller_id`) REFERENCES `users` (`id`),
  CONSTRAINT `products_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (2,'Tuxedo','Hugo Boos Collection',1100,3,'https://shoptillyoudrop.s3.amazonaws.com/products/2/8edc792c-8279-4531-9e42-59b32d122cb3',_binary '\0',0,4),(3,'Test Product','this is a test product for an example',20,1,'test-url',_binary '',1,6),(5,'This is now a dog toy','Revenge against the cats',0.54,4,'https://shoptillyoudrop.s3.amazonaws.com/products/5/851308c9-4509-4395-8634-f5fbc5ea0885',_binary '',3,7),(9,'Hair Dryer','Dry your hair like never before!',100.54,1,'https://shoptillyoudrop.s3.amazonaws.com/products/9/86c0a64f-978c-400c-b2fc-94d346af7f7f',_binary '\0',0,6),(10,'Upside down cat','Being upside down!',3.5,4,'https://shoptillyoudrop.s3.amazonaws.com/products/10/502cfe72-750a-4b2a-af43-36c339c3566e',_binary '',4,6),(12,'Dog Toy','Dog Bone Toy for dogs',0.5,4,'https://shoptillyoudrop.s3.amazonaws.com/products/null/a1362348-7bf0-4033-97a1-2f1ada4ac664',_binary '\0',0,7),(13,'AM/FM Radio','A tried and true radio that won\'t let you down.',50.99,1,'https://shoptillyoudrop.s3.amazonaws.com/products/null/767bba98-3f59-45a6-a191-c5e8d2f3e9e0',_binary '\0',27,7),(14,'Computer Chair','Perfect for long nights programming a marketplace application. No refunds.',250.95,5,'https://shoptillyoudrop.s3.amazonaws.com/products/null/28dbb72b-a8d1-403c-bb78-60c92b426180',_binary '\0',8,6),(15,'Potato','A delicious potato!',0.51,6,'https://shoptillyoudrop.s3.amazonaws.com/products/null/7557a144-9bc0-46ef-882b-661c762ee450',_binary '\0',0,6),(16,'Blue Jeans','Blue jeans for every occasion!',60.6,3,'https://shoptillyoudrop.s3.amazonaws.com/products/null/3955b423-c35a-4418-8368-de94e5afd9be',_binary '\0',23,6),(17,'X-Box Controller','An X-Box controller to play your favourite games with!',77.77,1,'https://shoptillyoudrop.s3.amazonaws.com/products/null/358c7c56-8eb5-4092-b318-175e62e36d3e',_binary '\0',10,6),(18,'Writing Desk','Write the next novel that will take the world by storm with this lovely desk!',549.5,5,'https://shoptillyoudrop.s3.amazonaws.com/products/null/6567cb0f-aec4-469f-880b-68d3bc8f0f25',_binary '\0',16,17),(19,'(Not actually) The Mona Lisa','This is not the Louvre, just like how this isn\'t actually the Mona Lisa.',10000.1,9,'https://shoptillyoudrop.s3.amazonaws.com/products/null/1e020fdb-864a-464f-8335-d2d6e62121af',_binary '\0',0,6),(20,'(Not really) Van Gogh','Can you see why this might be a fake? Still worth buying.',7500,9,'https://shoptillyoudrop.s3.amazonaws.com/products/null/6a002b64-924d-4a92-8622-e37e5ad6e075',_binary '\0',2,6),(21,'Tuxedo','High quality tuxedo ',1100,3,'https://shoptillyoudrop.s3.amazonaws.com/products/null/630da0f3-48c7-44aa-ada5-ed8d05fe0448',_binary '\0',20,10),(22,'T-shirt','Classy white t-shirt',35,3,'https://shoptillyoudrop.s3.amazonaws.com/products/null/104239c4-79ab-45c6-849c-0bcd47ef511e',_binary '\0',30,10),(23,'Shoes','Shiny black shoes',85,3,'https://shoptillyoudrop.s3.amazonaws.com/products/23/80962929-8c66-435a-a5e4-c2804bbbc1e5',_binary '\0',25,10),(24,'Dress','A beautiful green dress that will make stand out',80,3,'https://shoptillyoudrop.s3.amazonaws.com/products/null/599cd6e1-b1e8-44ec-8852-2af30e2f159e',_binary '\0',20,10),(25,'Dry cat food','Dry cat food with lots of nutrients',15.22,4,'https://shoptillyoudrop.s3.amazonaws.com/products/null/c16fbd1e-f409-4d21-84fe-15f8809198c3.jpg',_binary '\0',4,23),(26,'Bookcase','Bookcase for all your books',1100,5,'https://shoptillyoudrop.s3.amazonaws.com/products/null/122c74fa-45d1-40db-8ae2-21e30e50f1a0.jpg',_binary '\0',5,23);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(230) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(100) NOT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','BUYER','SELLER') NOT NULL,
  `credit_card` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Montreal','seanadmin@email.com','seanoman','$2a$10$7MzsWRFL.qhDrUcXucemEuvsuMxG3Rf8tXbNkY518xHs.G.CELPIK','https://shoptillyoudrop.s3.amazonaws.com/users/1/ace2c940-ebe3-42da-aefe-5e8f9c2025a2','ADMIN','aeb221660ff09134bb3a46b295554820f5cdcaae8971d75d9ffa62468c850dca'),(2,NULL,'amine@mail.com','amine','$2a$10$OLSWnErDDJ5o0PqOQ9xvnOqJp93JE/5dql3DOw5aBQ4t0SVXZ6.Vi',NULL,'BUYER',NULL),(3,'1234 definitely real place','seantwo@email.com','seantwo','$2a$10$GrkIo2M0QIaPKfiMM..EpOlOjDluWnh61F5G02anMKXh3ohOViqsa','https://shoptillyoudrop.s3.amazonaws.com/users/3/e212441a-2df1-46ed-81f6-309bf492e5eb','BUYER','$2a$10$BitfkxOIrqyI9RoEg8NdIO4jRn0HPiqhXx5yztpZAm8VYmrYUbdTi'),(4,NULL,'seller1@mail.com','seller1','$2a$10$NWdMfgzMuP/RryUURP6nbezyZcCZVqjg.HLL1/.XfatFkT3qdnvrm','https://shoptillyoudrop.s3.amazonaws.com/users/4/a865a77f-9b61-4f68-8967-22e1f0db72da','SELLER',NULL),(5,NULL,'amin@mail.com','amin','$2a$10$gbfGYlRgsxKUY3ZtyBbXpu8cnwI7Bx53tUVvCZTOSmN2sEigrZxFe','https://shoptillyoudrop.s3.amazonaws.com/users/5/e000dd06-9ab4-422d-9722-56bf07d8e341','SELLER',NULL),(6,NULL,'seansell@email.com','seansell','$2a$10$Qsq0/SlTgDI5qKngrJkiMucjByf6v2AkwpYhJNHVAMY6VFcUQL/ie','https://shoptillyoudrop.s3.amazonaws.com/users/6/cec17172-1ef3-44bc-abcb-3882b00b32db','SELLER',NULL),(7,NULL,'seansell2@email.com','seansell2','$2a$10$7hsL/XB439ThmUffMJF/XOUQT4wj..u5jtTsVWJRL4sVa3i.pa/Ri',NULL,'SELLER',NULL),(8,'Montreal','amdmin@mail.com','amdmin','Code11','https://shoptillyoudrop.s3.amazonaws.com/users/8/f219c641-d867-45f4-ab9d-5836a87e211c','ADMIN','79f7928f3deb7436d6e110dfae37e8f80ea9c65f55ea84eb449895b63bc5909e'),(9,'Ottawa','adminn@mail.com','adminn','$2a$10$TdyIG/bEDE06LtrmMQJvGeezvL81wv.BWGoWpzJWzYNkHyS5Xc3zK','https://shoptillyoudrop.s3.amazonaws.com/users/9/e1ddb0aa-e15e-45a4-9faa-30a8911db563','ADMIN','fef3d83e32b4d981b0c0f75206e891268c6aa8bd8db5a315db7bf24168a4be27'),(10,'Quebec','sell@mail.com','sell','$2a$10$brsPVuTL4sbjoJ3VfyHbf.xpN6TB8iGtB7gMCVvLG/MUkvg.MvYlC','https://shoptillyoudrop.s3.amazonaws.com/users/10/04bb2462-6c9c-4fc9-a34d-07a3afbf8434.jpg','SELLER','$2a$10$b//pIgicHFDnNpx/JycEbeOil0LzLiO3xxb1joQvAQP5mGMPO3AMq'),(11,'N.D.G.G','sean1buy@buystuff.com','seannewbuyer','$2a$10$Udh0ZouVHkxAZVOPGfxUUOTsUl.9fomVFOE7736fgkQvnYof9Fm3i','https://shoptillyoudrop.s3.amazonaws.com/users/11/48fe299b-be38-4c9b-98b7-8bf2d8383b81','BUYER','$2a$10$HxsfXgAKReY0SBXtHrXMleFGRRepNGCtXfJ9hCA3qKhffaFb5PyKi'),(12,'Montreal, QC','seannewuser@email.com','seannewuser','$2a$10$zPiFjJBtmhTcBsAb0/MJK.A3V7qe1uAYp4D3LrFal.J0qv5OwLP6u','https://shoptillyoudrop.s3.amazonaws.com/users/12/0cb0b427-3703-4772-9e53-4394fba73f31','SELLER','$2a$10$Ua.Q3re3Me/RroSZEsKawelAIrG5X2fxBNCjwXzixXkYfWl07MC/G'),(13,'1234 Bob\'s house','bob@email.com','bobert','$2a$10$nP0QQuVqucvIfsogdEjgOOa5LK9GZMw4j3r9FlP5iaLEAHvN6oKcK','https://shoptillyoudrop.s3.amazonaws.com/users/13/47565731-e02f-4cb3-8d98-08ccf98ece58','BUYER','cb27648fefd05e431469c35e176beb865be1e4c6104dfc6b4bd4622ae008f61c'),(14,'2121 Clearview','lynette@email.com','lynette','$2a$10$YxvnvvbRoR3LcNRxSNzinOo5E4VvXmSRcQ/FMWPiftTRWc8tUiF1O','','BUYER','87f1293272dab69591430fcc4eed9b808866587acaf17483a5f6e773e2d8c3ee'),(15,'2024 Jim\'s place','jimmy@email.com','jimmy','$2a$10$sQGZWQLUjsOuB6LxEONXW.jHUl48XHz.m3ic4gblz8sI7i5.pFl4S','https://shoptillyoudrop.s3.amazonaws.com/users/15/1cea580f-1a9e-4e35-9f93-727c790ed764.jpg','BUYER','$2a$10$pLqlcgweb7LQ77KG4F.03.vN.NYj1.GnNs8dJbzh/eqAK1e2IsHH2'),(16,'Montreal, QC, Canada','buy@mail.com','ambuyer','$2a$10$OpKIVkbJjbP4lbBGh/Br5uMukRksDwOdPRcZqPEt9SfggtVa6Vs3S','','BUYER',NULL),(17,'9001 Avenue!','moneybags@email.com','moneybags','$2a$10$GGudRHMlp2DWYIh0KRv7ZOgKJ/fpYdyVlaigdaDEhdrQGSEFWV4he','https://shoptillyoudrop.s3.amazonaws.com/users/17/8d5263c7-8e6b-4b33-b76d-0f0d9fc613d2','SELLER','$2a$10$e8pkeg4UB/b7lUPhnZG3WeHpNfpxW8WQkdIGLNGwEI60QLbfTUd0u'),(18,'400 Admin lane','newadmin@admin.com','newadmin','$2a$10$71p0fxUcAxMMi4OQPcBXv.vsH8PfJVufRjLm8az61FKD1pEIdKvt2','','ADMIN','e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855'),(19,NULL,'amdminn@mail.com','amdminn','$2a$10$XM5KzDP894HquzV1LFDjDec5mmkFPS/wNvyUayGDEnS09pPL2165K',NULL,'ADMIN',NULL),(20,'400 buyer lane','wannabuy@email.com','lookingtobuy','$2a$10$20Mc.Csqh9gHE8E121blKuuLTo3zLukVplnBxPFQJxTsUocDzCk8W','','BUYER','$2a$10$zhp5FKJIbEkH8xcomaagD.C9c8dqZXKPsIVj0oEsJxrm.87Oi6JmO'),(21,'301 Glendale','buyerperson@email.com','buyerperson','$2a$10$.0ExHrU75kSywU1bh.wEe.hgG90V7vEQQIVRz5ZUXV6JGS74oS8Di','','BUYER','$2a$10$4/KTMO4m7gHMSRPGmuPQdewZasdchH0gtPXikZD8e35EXSV4tcI8a'),(22,'1234 Demo Lane','demobuyer@email.com','demobuyer','$2a$10$fG.BiLI/UdqFsDBgv0AEVesmztUJGBb25eCiV0pVwcwyuGYBvW8Mm','https://shoptillyoudrop.s3.amazonaws.com/users/22/ce59d8f3-a27d-4bb5-a82c-235c9398e17e.jpg','BUYER','$2a$10$nJT3FLXSrvbFY3cgIDOE1e8yefxRyow9jtkbo1Tf0qGrUrvPVZc.S'),(23,NULL,'demoseller@email.com','demoseller','$2a$10$BZYi1jEGL4qxGuFtCt8r3OImqSMzuVcpO3F5gr6p4kRPKWmlKSbf.',NULL,'SELLER',NULL),(24,NULL,'newuser@emailex.com','newuserexample','$2a$10$RIHG.bTTwb6vzO.vdFNzd.WmcAlrGNWiYHy3/79d16ytrEgjzTfEK',NULL,'BUYER',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-24 13:46:52
