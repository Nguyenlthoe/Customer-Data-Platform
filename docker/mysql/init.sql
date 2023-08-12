use `customer-data-platform`;

CREATE TABLE `bookshop_author` (
  `author_id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `description` text,
  `name` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`author_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `bookshop_author_book_association` (
  `book_id` int NOT NULL,
  `author_id` int NOT NULL,
  PRIMARY KEY (`book_id`,`author_id`),
  KEY `FKbvtjw73629sap0u33266cv0i8` (`author_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `bookshop_bill` (
  `bill_id` int NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `status` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `total` int DEFAULT NULL,
  PRIMARY KEY (`bill_id`),
  KEY `FKjwnuayk56ai1o3asn08k5451i` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `bookshop_bill_book_association` (
  `bill_id` int NOT NULL,
  `book_id` int NOT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`bill_id`,`book_id`),
  KEY `FK38vis2ose0df4i1xmqpmta15i` (`book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `bookshop_book` (
  `book_id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `description` text,
  `name` varchar(255) DEFAULT NULL,
  `price` int DEFAULT NULL,
  `release_date` datetime DEFAULT NULL,
  `sales` int DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `url_image` varchar(255) DEFAULT NULL,
  `view_counts` int DEFAULT NULL,
  `publisher_id` int DEFAULT NULL,
  PRIMARY KEY (`book_id`),
  KEY `FK4h9g6lx0ne2vsf623eo9giav3` (`publisher_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `bookshop_cart` (
  `book_id` int NOT NULL,
  `user_id` int NOT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`book_id`,`user_id`),
  KEY `FK5xwowwovqs4pae5xyy0g5pdb4` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `bookshop_category` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `bookshop_category_book_association` (
  `book_id` int NOT NULL,
  `category_id` int NOT NULL,
  PRIMARY KEY (`book_id`,`category_id`),
  KEY `FKruc9vry7qei9lhkr5ye3rom9v` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `bookshop_comment` (
  `comment_id` int NOT NULL AUTO_INCREMENT,
  `content` text,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `user_id` int NOT NULL,
  `book_id` int NOT NULL,
  PRIMARY KEY (`comment_id`),
  KEY `user_id` (`user_id`),
  KEY `book_id` (`book_id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `bookshop_customer` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `birthday` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gender` int DEFAULT NULL,
  `long_hobbies` text,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `url_avatar` varchar(255) DEFAULT NULL,
  `province_code` int DEFAULT NULL,
  `total_book_view` int DEFAULT '0',
  `avg_book_value_view` double DEFAULT '0',
  `total_book_value_view` int DEFAULT '0',
  `min_total_bill` int DEFAULT '0',
  `avg_bill_value` double DEFAULT '0',
  `short_hobbies` text,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1245 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `bookshop_publisher` (
  `publisher_id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`publisher_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `cdp_admin` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `cdp_customer_book_association` (
  `user_id` int NOT NULL,
  `book_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`book_id`),
  KEY `book_id` (`book_id`),
  CONSTRAINT `cdp_customer_book_association_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `bookshop_customer` (`user_id`),
  CONSTRAINT `cdp_customer_book_association_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `bookshop_book` (`book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `cdp_customer_category_association` (
  `user_id` int NOT NULL,
  `category_id` int NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`user_id`,`category_id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `cdp_customer_category_association_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `bookshop_customer` (`user_id`),
  CONSTRAINT `cdp_customer_category_association_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `bookshop_category` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `cdp_segment` (
  `segment_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `rule` longtext,
  `admin_id` int NOT NULL,
  `is_deleted` int NOT NULL DEFAULT '0',
  `status` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`segment_id`),
  KEY `admin_id` (`admin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `cdp_segment_customer_association` (
  `segment_id` int NOT NULL,
  `user_id` int NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`segment_id`,`user_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `cdp_admin` (`user_id`,`name`,`password`,`phone_number`,`email`,`created_at`,`updated_at`) VALUES (1,'Lê Trường Nguyên','7F846A78C17F4F1531BB5B89C42611F2','0862465416','nguyenlthoe@gmail.com','2023-05-21 00:00:00','2023-05-21 08:28:52');
INSERT INTO `cdp_segment` (`segment_id`,`name`,`created_at`,`updated_at`,`rule`,`admin_id`,`is_deleted`) VALUES (8,'filter age > 18','2023-05-21 15:40:50','2023-05-21 15:40:50','[{\"field\":\"birthday\",\"operator\":2,\"value\":\"18\",\"type\":6}]',1,0);
INSERT INTO `cdp_segment` (`segment_id`,`name`,`created_at`,`updated_at`,`rule`,`admin_id`,`is_deleted`) VALUES (9,'filter nhieu hon','2023-05-21 15:41:19','2023-05-21 15:41:19','[{\"field\":\"birthday\",\"operator\":3,\"value\":\"18\",\"type\":6},{\"field\":\"gender\",\"operator\":1,\"value\":\"1\",\"type\":1}]',1,0);
INSERT INTO `cdp_segment` (`segment_id`,`name`,`created_at`,`updated_at`,`rule`,`admin_id`,`is_deleted`) VALUES (10,'filter dia chi, gioi tinh','2023-05-25 13:50:33','2023-05-25 13:50:33','[{\"field\":\"province_code\",\"operator\":1,\"value\":\"1\",\"type\":1},{\"field\":\"gender\",\"operator\":1,\"value\":\"1\",\"type\":1}]',1,0);
INSERT INTO `cdp_segment` (`segment_id`,`name`,`created_at`,`updated_at`,`rule`,`admin_id`,`is_deleted`) VALUES (11,'filter dia chi, gioi tinh, 2','2023-05-25 14:30:13','2023-05-25 14:30:13','[{\"field\":\"gender\",\"operator\":1,\"value\":\"1\",\"type\":1},{\"field\":\"birthday\",\"operator\":2,\"value\":\"18\",\"type\":6}]',1,0);
INSERT INTO `cdp_segment` (`segment_id`,`name`,`created_at`,`updated_at`,`rule`,`admin_id`,`is_deleted`) VALUES (12,'Phân khúc người dùng có giới tính nam, gần đây có quan tâm đến thể loại bigdata ','2023-07-15 17:05:11','2023-07-17 18:55:02','[{\"field\":\"gender\",\"operator\":1,\"value\":\"1\",\"type\":1},{\"field\":\"short_hobbies\",\"operator\":6,\"value\":\"6\",\"type\":5},{\"field\":\"total_book_value_view\",\"operator\":4,\"value\":\"0\",\"type\":1}]',1,0);

INSERT INTO `bookshop_customer` (`user_id`,`address`,`birthday`,`created_at`,`email`,`gender`,`long_hobbies`,`name`,`password`,`phone_number`,`updated_at`,`url_avatar`,`province_code`,`total_book_view`,`avg_book_value_view`,`total_book_value_view`,`min_total_bill`,`avg_bill_value`,`short_hobbies`) VALUES (1234,'85 Vu Trong Phung',NULL,'2023-06-03 13:42:02','nguyennamvan1111@gmail.com',1,' 6 ','Nguyễn Văn Nam','E10ADC3949BA59ABBE56E057F20F883E','0856982486','2023-07-18 09:24:48',NULL,15,14,301428.5714285714,4220000,230000,1095000,' 2 , 4 , 5 , 6 ');
INSERT INTO `bookshop_customer` (`user_id`,`address`,`birthday`,`created_at`,`email`,`gender`,`long_hobbies`,`name`,`password`,`phone_number`,`updated_at`,`url_avatar`,`province_code`,`total_book_view`,`avg_book_value_view`,`total_book_value_view`,`min_total_bill`,`avg_bill_value`,`short_hobbies`) VALUES (1235,'85 Vu Trong Phung',NULL,'2023-06-15 13:36:22','nguyenlthoe2@gmail.com',1,'  ','Lê Trường Nguyên','E10ADC3949BA59ABBE56E057F20F883E','0988855959','2023-07-18 09:24:48',NULL,86,8,370125,2961000,560000,611000,' 5 , 6 ');
INSERT INTO `bookshop_customer` (`user_id`,`address`,`birthday`,`created_at`,`email`,`gender`,`long_hobbies`,`name`,`password`,`phone_number`,`updated_at`,`url_avatar`,`province_code`,`total_book_view`,`avg_book_value_view`,`total_book_value_view`,`min_total_bill`,`avg_bill_value`,`short_hobbies`) VALUES (1236,'85 Vu Trong Phung',NULL,'2023-07-14 13:28:26','nguyenlthoe222@gmail.com',2,'  ','Lê Trường Nguyên','4297F44B13955235245B2497399D7A93','0123456781','2023-07-15 19:07:50',NULL,86,0,0,0,0,0,'  ');
INSERT INTO `bookshop_customer` (`user_id`,`address`,`birthday`,`created_at`,`email`,`gender`,`long_hobbies`,`name`,`password`,`phone_number`,`updated_at`,`url_avatar`,`province_code`,`total_book_view`,`avg_book_value_view`,`total_book_value_view`,`min_total_bill`,`avg_bill_value`,`short_hobbies`) VALUES (1237,'85 Vu Trong Phung',NULL,'2023-07-14 13:29:05','nguyenlthoe2221@gmail.com',1,' 6 , 5 ','Lê Trường Nguyên','4297F44B13955235245B2497399D7A93','0123456782','2023-07-15 19:07:50',NULL,15,8,353875,2831000,2137000,2137000,'  ');
INSERT INTO `bookshop_customer` (`user_id`,`address`,`birthday`,`created_at`,`email`,`gender`,`long_hobbies`,`name`,`password`,`phone_number`,`updated_at`,`url_avatar`,`province_code`,`total_book_view`,`avg_book_value_view`,`total_book_value_view`,`min_total_bill`,`avg_bill_value`,`short_hobbies`) VALUES (1238,'85 Vu Trong Phung',NULL,'2023-07-14 13:57:00','nguyenlthoe3@gmail.com',1,' 6 ','Lê Trường Nguyên','4297F44B13955235245B2497399D7A93','0123456783','2023-07-15 19:07:50',NULL,77,0,0,0,1880000,1880000,'  ');
INSERT INTO `bookshop_customer` (`user_id`,`address`,`birthday`,`created_at`,`email`,`gender`,`long_hobbies`,`name`,`password`,`phone_number`,`updated_at`,`url_avatar`,`province_code`,`total_book_view`,`avg_book_value_view`,`total_book_value_view`,`min_total_bill`,`avg_bill_value`,`short_hobbies`) VALUES (1239,'85 Vu Trong Phung',NULL,'2023-07-14 14:00:44','nguyenlthoe4@gmail.com',1,'  ','Lê Trường Nguyên','4297F44B13955235245B2497399D7A93','0123456784','2023-07-15 19:07:50',NULL,34,0,0,0,0,0,'  ');
INSERT INTO `bookshop_customer` (`user_id`,`address`,`birthday`,`created_at`,`email`,`gender`,`long_hobbies`,`name`,`password`,`phone_number`,`updated_at`,`url_avatar`,`province_code`,`total_book_view`,`avg_book_value_view`,`total_book_value_view`,`min_total_bill`,`avg_bill_value`,`short_hobbies`) VALUES (1240,'85 Vu Trong Phung',NULL,'2023-07-14 14:06:00','nguyenlthoe5@gmail.com',1,'  ','Lê Trường Nguyên','4297F44B13955235245B2497399D7A93','0123456785','2023-07-15 19:07:53',NULL,89,0,0,0,0,0,'  ');
INSERT INTO `bookshop_customer` (`user_id`,`address`,`birthday`,`created_at`,`email`,`gender`,`long_hobbies`,`name`,`password`,`phone_number`,`updated_at`,`url_avatar`,`province_code`,`total_book_view`,`avg_book_value_view`,`total_book_value_view`,`min_total_bill`,`avg_bill_value`,`short_hobbies`) VALUES (1241,'85 Vu Trong Phung',NULL,'2023-07-14 14:06:15','nguyenlthoe6666@gmail.com',1,' 6 , 5 ','Lê Trường Nguyên','4297F44B13955235245B2497399D7A93','0123456786','2023-07-15 19:07:54',NULL,37,0,0,0,679000,679000,'  ');
INSERT INTO `bookshop_customer` (`user_id`,`address`,`birthday`,`created_at`,`email`,`gender`,`long_hobbies`,`name`,`password`,`phone_number`,`updated_at`,`url_avatar`,`province_code`,`total_book_view`,`avg_book_value_view`,`total_book_value_view`,`min_total_bill`,`avg_bill_value`,`short_hobbies`) VALUES (1242,'85 Vu Trong Phung',NULL,'2023-07-14 14:13:27','nguyenlthoe6667@gmail.com',1,'  ','Lê Trường Nguyên','4297F44B13955235245B2497399D7A93','0123456787','2023-07-15 19:07:57',NULL,70,0,0,0,0,0,'  ');
INSERT INTO `bookshop_customer` (`user_id`,`address`,`birthday`,`created_at`,`email`,`gender`,`long_hobbies`,`name`,`password`,`phone_number`,`updated_at`,`url_avatar`,`province_code`,`total_book_view`,`avg_book_value_view`,`total_book_value_view`,`min_total_bill`,`avg_bill_value`,`short_hobbies`) VALUES (1243,'85 Vu Trong Phung',NULL,'2023-07-14 14:23:16','nguyenlthoe21@gmail.com',1,' 5 , 6 ','Lê Trường Nguyên','4297F44B13955235245B2497399D7A93','0123456721','2023-07-15 19:07:59',NULL,15,3,208666.66666666666,626000,5229000,5229000,'  ');
INSERT INTO `bookshop_customer` (`user_id`,`address`,`birthday`,`created_at`,`email`,`gender`,`long_hobbies`,`name`,`password`,`phone_number`,`updated_at`,`url_avatar`,`province_code`,`total_book_view`,`avg_book_value_view`,`total_book_value_view`,`min_total_bill`,`avg_bill_value`,`short_hobbies`) VALUES (1244,'85 Vu Trong Phung',NULL,'2023-07-15 17:59:40','nguyenlthoe61@gmail.com',2,' 6 , 5 ','Nguyễn Văn A','4297F44B13955235245B2497399D7A93','0123456614','2023-07-15 19:08:00',NULL,38,4,362750,1451000,151000,510333.3333333333,' 5 , 6 ');

INSERT INTO `bookshop_category` (`category_id`,`created_at`,`name`,`updated_at`) VALUES (1,'2023-05-31 16:21:19','Truyện trinh thám','2023-05-31 16:21:19');
INSERT INTO `bookshop_category` (`category_id`,`created_at`,`name`,`updated_at`) VALUES (2,'2023-05-31 16:21:29','Sách self-help','2023-05-31 16:21:29');
INSERT INTO `bookshop_category` (`category_id`,`created_at`,`name`,`updated_at`) VALUES (3,'2023-05-31 16:21:38','Truyện kinh dị','2023-05-31 16:21:38');
INSERT INTO `bookshop_category` (`category_id`,`created_at`,`name`,`updated_at`) VALUES (4,'2023-05-31 16:21:58','Phiêu lưu','2023-05-31 16:21:58');
INSERT INTO `bookshop_category` (`category_id`,`created_at`,`name`,`updated_at`) VALUES (5,'2023-05-31 16:24:43','tiểu thuyết','2023-05-31 16:24:43');
INSERT INTO `bookshop_category` (`category_id`,`created_at`,`name`,`updated_at`) VALUES (6,'2023-06-01 16:02:16','bigdata','2023-06-01 16:02:16');

INSERT INTO `cdp_segment_customer_association` (`segment_id`,`user_id`,`updated_at`) VALUES (12,1234,'2023-07-15 17:15:43');
INSERT INTO `cdp_segment_customer_association` (`segment_id`,`user_id`,`updated_at`) VALUES (12,1235,'2023-07-15 17:15:43');
