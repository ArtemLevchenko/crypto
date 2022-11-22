CREATE DATABASE  IF NOT EXISTS `crypto` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `crypto`;
-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
--
-- Table structure for table `cryptotable`
--
DROP TABLE IF EXISTS `cryptotable`;

CREATE TABLE `cryptotable` (
  `symbol` text,
  `price` double DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=458 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

