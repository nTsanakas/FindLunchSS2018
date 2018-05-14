-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';


-- -----------------------------------------------------
-- Schema findlunch
-- -----------------------------------------------------
-- DROP SCHEMA IF EXISTS `findlunch` ;

-- -----------------------------------------------------
-- Schema findlunch
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `findlunch` DEFAULT CHARACTER SET utf8 ;
-- USE `findlunch` ;

-- -----------------------------------------------------
-- Table `findlunch`.`country`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`country` (
  `country_code` VARCHAR(2) NOT NULL,
  `name` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`country_code`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- ——————————————————————————---------------------------
-- Table `findlunch`.`day_of_week`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`day_of_week` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) NOT NULL,
  `day_number` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `day_number_UNIQUE` (`day_number` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `findlunch`.`restaurant_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`restaurant_type` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `findlunch`.`restaurant`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`restaurant` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `customer_id` INT(11) NOT NULL,
  `name` VARCHAR(60) NOT NULL,
  `street` VARCHAR(60) NOT NULL,
  `street_number` VARCHAR(11) NOT NULL,
  `zip` VARCHAR(5) NOT NULL,
  `city` VARCHAR(60) NOT NULL,
  `country_code` VARCHAR(2) NOT NULL,
  `location_latitude` FLOAT NOT NULL,
  `location_longitude` FLOAT NOT NULL,
  `email` VARCHAR(60) NOT NULL,
  `phone` VARCHAR(60) NOT NULL,
  `url` VARCHAR(60) NULL DEFAULT NULL,
  `restaurant_type_id` INT(11) NULL DEFAULT NULL,
  `restaurant_uuid` VARCHAR(40) NOT NULL,
  `qr_uuid` BLOB NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_restaurant_countries1_idx` (`country_code` ASC),
  INDEX `fk_restaurant_restaurant_type1_idx` (`restaurant_type_id` ASC),
  UNIQUE INDEX `customer_id_UNIQUE` (`customer_id` ASC),
  CONSTRAINT `fk_restaurant_countries1`
    FOREIGN KEY (`country_code`)
    REFERENCES `findlunch`.`country` (`country_code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_restaurant_restaurant_type1`
    FOREIGN KEY (`restaurant_type_id`)
    REFERENCES `findlunch`.`restaurant_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `findlunch`.`user_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`user_type` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `findlunch`.`account_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`account_type` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `findlunch`.`account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`account` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `account_number` INT(11) NOT NULL,
  `account_type_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_account_account_type1_idx` (`account_type_id` ASC),
  CONSTRAINT `fk_account_account_type1`
    FOREIGN KEY (`account_type_id`)
    REFERENCES `findlunch`.`account_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `findlunch`.`course_types`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`course_types` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `restaurant_id` INT(11) NOT NULL,
  `name` VARCHAR(30) NOT NULL,
  `sort_by` INT(11) DEFAULT 1,
  PRIMARY KEY (`id`),
  INDEX `fk_course_restaurant1_idx` (`restaurant_id` ASC),
  CONSTRAINT `fk_course_restaurant1`
    FOREIGN KEY (`restaurant_id`)
    REFERENCES `findlunch`.`restaurant` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)  
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `findlunch`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(60) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `restaurant_id` INT(11) NULL DEFAULT NULL,
  `user_type_id` INT(11) NOT NULL,
  `account_id` INT(11) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC),
  INDEX `fk_user_restaurant1_idx` (`restaurant_id` ASC),
  INDEX `fk_user_user_type1_idx` (`user_type_id` ASC),
  INDEX `fk_user_account1_idx` (`account_id` ASC),
  CONSTRAINT `fk_user_restaurant1`
    FOREIGN KEY (`restaurant_id`)
    REFERENCES `findlunch`.`restaurant` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_user_type1`
    FOREIGN KEY (`user_type_id`)
    REFERENCES `findlunch`.`user_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_account1`
    FOREIGN KEY (`account_id`)
    REFERENCES `findlunch`.`account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `findlunch`.`favorites`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`favorites` (
  `user_id` INT(11) NOT NULL,
  `restaurant_id` INT(11) NOT NULL,
  PRIMARY KEY (`user_id`, `restaurant_id`),
  INDEX `fk_user_has_restaurant_restaurant1_idx` (`restaurant_id` ASC),
  INDEX `fk_user_has_restaurant_user1_idx` (`user_id` ASC),
  CONSTRAINT `fk_user_has_restaurant_restaurant1`
    FOREIGN KEY (`restaurant_id`)
    REFERENCES `findlunch`.`restaurant` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_has_restaurant_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `findlunch`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `findlunch`.`kitchen_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`kitchen_type` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `findlunch`.`offer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`offer` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `restaurant_id` INT(11) NOT NULL,
  `title` VARCHAR(60) NOT NULL,
  `description` TINYTEXT NOT NULL,
  `price` DECIMAL(5,2) NOT NULL,
  `preparation_time` INT(11) NOT NULL,
  `start_date` DATE NULL DEFAULT NULL,
  `end_date` DATE NULL DEFAULT NULL,
  `needed_points` INT NOT NULL,
  `sold_out` TINYINT(1) NOT NULL,
  `course_type` INT(11) NOT NULL,
  `sort` INT(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  INDEX `fk_product_restaurant1_idx` (`restaurant_id` ASC),
  CONSTRAINT `fk_product_restaurant1`
    FOREIGN KEY (`restaurant_id`)
    REFERENCES `findlunch`.`restaurant` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  INDEX `fk_product_course_idx` (`course_type` ASC),
  CONSTRAINT `fk_productcourse1`
    FOREIGN KEY (`course_type`)
    REFERENCES `findlunch`.`course_types` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `findlunch`.`user_pushtoken`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`user_pushtoken` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL,
  `fcm_token` TEXT(4096) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_pushtoken_user1_idx` (`user_id` ASC),
  CONSTRAINT `fk_user_pushtoken_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `findlunch`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `findlunch`.`offer_has_day_of_week`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`offer_has_day_of_week` (
  `offer_id` INT(11) NOT NULL,
  `day_of_week_id` INT(11) NOT NULL,
  PRIMARY KEY (`offer_id`, `day_of_week_id`),
  INDEX `fk_offer_has_day_of_week_day_of_week1_idx` (`day_of_week_id` ASC),
  INDEX `fk_offer_has_day_of_week_offer1_idx` (`offer_id` ASC),
  CONSTRAINT `fk_offer_has_day_of_week_day_of_week1`
    FOREIGN KEY (`day_of_week_id`)
    REFERENCES `findlunch`.`day_of_week` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_offer_has_day_of_week_offer1`
    FOREIGN KEY (`offer_id`)
    REFERENCES `findlunch`.`offer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `findlunch`.`offer_photo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`offer_photo` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `offer_id` INT(11) NOT NULL,
  `photo` MEDIUMBLOB NOT NULL,
  `thumbnail` MEDIUMBLOB NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_offer_photo_offer1_idx` (`offer_id` ASC),
  CONSTRAINT `fk_offer_photo_offer1`
    FOREIGN KEY (`offer_id`)
    REFERENCES `findlunch`.`offer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `findlunch`.`restaurant_logo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`restaurant_logo` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `restaurant_id` INT(11) NOT NULL,
  `logo` MEDIUMBLOB NOT NULL,
  `thumbnail` MEDIUMBLOB NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_restaurant_logo_restaurant1_idx` (`restaurant_id` ASC),
  CONSTRAINT `fk_restaurant_logo_retaurant1`
    FOREIGN KEY (`restaurant_id`)
    REFERENCES `findlunch`.`restaurant` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `findlunch`.`time_schedule`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`time_schedule` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `restaurant_id` INT(11) NOT NULL,
  `offer_start_time` DATETIME NULL DEFAULT NULL,
  `offer_end_time` DATETIME NULL DEFAULT NULL,
  `day_of_week_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_time_schedule_restaurant1_idx` (`restaurant_id` ASC),
  INDEX `fk_time_schedule_day_of_week1_idx` (`day_of_week_id` ASC),
  CONSTRAINT `fk_time_schedule_day_of_week1`
    FOREIGN KEY (`day_of_week_id`)
    REFERENCES `findlunch`.`day_of_week` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_time_schedule_restaurant1`
    FOREIGN KEY (`restaurant_id`)
    REFERENCES `findlunch`.`restaurant` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `findlunch`.`opening_time`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`opening_time` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `opening_time` DATETIME NOT NULL,
  `closing_time` DATETIME NOT NULL,
  `time_schedule_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_opening_time_time_schedule1_idx` (`time_schedule_id` ASC),
  CONSTRAINT `fk_opening_time_time_schedule1`
    FOREIGN KEY (`time_schedule_id`)
    REFERENCES `findlunch`.`time_schedule` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `findlunch`.`push_notification`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`push_notification` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL,
  `title` VARCHAR(60) NULL DEFAULT NULL,
  `latitude` FLOAT NOT NULL,
  `longitude` FLOAT NOT NULL,
  `radius` INT(11) NOT NULL,
  `fcm_token` TEXT(4096) NOT NULL,
  `sns_token` TEXT(4096) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_push_notification_user1_idx` (`user_id` ASC),
  CONSTRAINT `fk_push_notification_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `findlunch`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `findlunch`.`push_notification_has_day_of_week`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`push_notification_has_day_of_week` (
  `push_notification_id` INT(11) NOT NULL,
  `day_of_week_id` INT(11) NOT NULL,
  PRIMARY KEY (`push_notification_id`, `day_of_week_id`),
  INDEX `fk_push_notification_has_day_of_week_day_of_week1_idx` (`day_of_week_id` ASC),
  INDEX `fk_push_notification_has_day_of_week_push_notification1_idx` (`push_notification_id` ASC),
  CONSTRAINT `fk_push_notification_has_day_of_week_day_of_week1`
    FOREIGN KEY (`day_of_week_id`)
    REFERENCES `findlunch`.`day_of_week` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_push_notification_has_day_of_week_push_notification1`
    FOREIGN KEY (`push_notification_id`)
    REFERENCES `findlunch`.`push_notification` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `findlunch`.`push_notification_has_kitchen_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`push_notification_has_kitchen_type` (
  `push_notification_id` INT(11) NOT NULL,
  `kitchen_type_id` INT(11) NOT NULL,
  PRIMARY KEY (`push_notification_id`, `kitchen_type_id`),
  INDEX `fk_push_notification_has_kitchen_type_kitchen_type1_idx` (`kitchen_type_id` ASC),
  INDEX `fk_push_notification_has_kitchen_type_push_notification1_idx` (`push_notification_id` ASC),
  CONSTRAINT `fk_push_notification_has_kitchen_type_kitchen_type1`
    FOREIGN KEY (`kitchen_type_id`)
    REFERENCES `findlunch`.`kitchen_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_push_notification_has_kitchen_type_push_notification1`
    FOREIGN KEY (`push_notification_id`)
    REFERENCES `findlunch`.`push_notification` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `findlunch`.`restaurant_has_kitchen_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`restaurant_has_kitchen_type` (
  `restaurant_id` INT(11) NOT NULL,
  `kitchen_type_id` INT(11) NOT NULL,
  PRIMARY KEY (`restaurant_id`, `kitchen_type_id`),
  INDEX `fk_restaurant_has_kitchen_type_kitchen_type1_idx` (`kitchen_type_id` ASC),
  INDEX `fk_restaurant_has_kitchen_type_restaurant1_idx` (`restaurant_id` ASC),
  CONSTRAINT `fk_restaurant_has_kitchen_type_kitchen_type1`
    FOREIGN KEY (`kitchen_type_id`)
    REFERENCES `findlunch`.`kitchen_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_restaurant_has_kitchen_type_restaurant1`
    FOREIGN KEY (`restaurant_id`)
    REFERENCES `findlunch`.`restaurant` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `findlunch`.`points`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`points` (
  `user_id` INT(11) NOT NULL,
  `restaurant_id` INT(11) NOT NULL,
  `points` INT(11) NOT NULL,
  PRIMARY KEY (`user_id`, `restaurant_id`),
  INDEX `fk_points_restaurant1_idx` (`restaurant_id` ASC),
  CONSTRAINT `fk_points_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `findlunch`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_points_restaurant1`
    FOREIGN KEY (`restaurant_id`)
    REFERENCES `findlunch`.`restaurant` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
-- -----------------------------------------------------
-- Table `findlunch`.`euro_per_point`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`euro_per_point` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `euro` DECIMAL(3,2) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `findlunch`.`minimum_profit`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`minimum_profit` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `profit` DECIMAL(5,2) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `findlunch`.`bill`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`bill` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `bill_number` VARCHAR(12) NOT NULL,
  `start_date` DATE NOT NULL,
  `end_date` DATE NOT NULL,
  `paid` TINYINT(1) NOT NULL,
  `minimum_profit_id` INT NOT NULL,
  `restaurant_id` INT(11) NOT NULL,
  `bill_pdf` MEDIUMBLOB NOT NULL,
  `total_price` DECIMAL(6,2) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_bill_minimum_profit1_idx` (`minimum_profit_id` ASC),
  INDEX `fk_bill_restaurant1_idx` (`restaurant_id` ASC),
  CONSTRAINT `fk_bill_minimum_profit1`
    FOREIGN KEY (`minimum_profit_id`)
    REFERENCES `findlunch`.`minimum_profit` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_bill_restaurant1`
    FOREIGN KEY (`restaurant_id`)
    REFERENCES `findlunch`.`restaurant` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `findlunch`.`reservation_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`reservation_status` (
  `id` INT(11) NOT NULL,
  `status` VARCHAR(255) NULL DEFAULT NULL,
  `statkey` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `findlunch`.`reservation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`reservation` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `reservation_number` INT(11) NOT NULL,
  `total_price` DECIMAL(5,2) NOT NULL,
  `donation` DECIMAL(5,2) NOT NULL,
  `used_points` TINYINT(1) NOT NULL,
  `points_collected` TINYINT(1) NOT NULL,
  `points` DECIMAL(5,2) DEFAULT 0.0,
  `user_id` INT(11) NOT NULL,
  `euro_per_point_id` INT NOT NULL,
  `bill_id` INT(11) NULL DEFAULT NULL,
  `restaurant_id` INT(11) NOT NULL,
  `collect_time` DATETIME NULL DEFAULT NULL,
  `timestamp_received` DATETIME NULL DEFAULT NULL,
  `timestamp_responded` DATETIME NULL DEFAULT NULL,
  `reservation_status_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_reservation_user1_idx` (`user_id` ASC),
  INDEX `fk_reservation_euro_per_point1_idx` (`euro_per_point_id` ASC),
  INDEX `fk_reservation_bill1_idx` (`bill_id` ASC),
  INDEX `fk_reservation_restaurant1_idx` (`restaurant_id` ASC),
  INDEX `fk_reservation_reservation_status1_idx` (`reservation_status_id` ASC),
  UNIQUE INDEX `reservation_number_UNIQUE` (`reservation_number` ASC),
  CONSTRAINT `fk_reservation_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `findlunch`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_reservation_euro_per_point1`
    FOREIGN KEY (`euro_per_point_id`)
    REFERENCES `findlunch`.`euro_per_point` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_reservation_bill1`
    FOREIGN KEY (`bill_id`)
    REFERENCES `findlunch`.`bill` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_reservation_restaurant1`
    FOREIGN KEY (`restaurant_id`)
    REFERENCES `findlunch`.`restaurant` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_reservation_reservation_status1`
    FOREIGN KEY (`reservation_status_id`)
    REFERENCES `findlunch`.`reservation_status` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `findlunch`.`reservation_offers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`reservation_offers` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `reservation_id` INT(11) NOT NULL,
  `offer_id` INT(11) NOT NULL,
  `amount` INT(3) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_reservation_idx` (`reservation_id` ASC),
  INDEX `fk__offer1_idx` (`offer_id` ASC),
  CONSTRAINT `fk_reservation1`
    FOREIGN KEY (`reservation_id`)
    REFERENCES `findlunch`.`reservation` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_offer1`
    FOREIGN KEY (`offer_id`)
    REFERENCES `findlunch`.`offer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `findlunch`.`booking_reason`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`booking_reason` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `reason` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `findlunch`.`booking`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`booking` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `book_id` INT NOT NULL,
  `booking_time` DATETIME NOT NULL,
  `amount` DECIMAL(6,2) NOT NULL,
  `booking_reason_id` INT(11) NOT NULL,
  `account_id` INT(11) NOT NULL,
  `bill_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_booking_booking_reason1_idx` (`booking_reason_id` ASC),
  INDEX `fk_booking_account1_idx` (`account_id` ASC),
  INDEX `fk_booking_bill1_idx` (`bill_id` ASC),
  CONSTRAINT `fk_booking_booking_reason1`
    FOREIGN KEY (`booking_reason_id`)
    REFERENCES `findlunch`.`booking_reason` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_booking_account1`
    FOREIGN KEY (`account_id`)
    REFERENCES `findlunch`.`account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_booking_bill1`
    FOREIGN KEY (`bill_id`)
    REFERENCES `findlunch`.`bill` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `findlunch`.`donation_per_month`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`donation_per_month` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `date` DATE NOT NULL,
  `amount` DECIMAL(5,2) NOT NULL,
  `restaurant_id` INT(11) NOT NULL,
  `datetime_of_update` DATETIME NOT NULL,
  `bill_id` INT(11) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_donation_per_month_restaurant1_idx` (`restaurant_id` ASC),
  INDEX `fk_donation_per_month_bill1_idx` (`bill_id` ASC),
  CONSTRAINT `fk_donation_per_month_restaurant1`
    FOREIGN KEY (`restaurant_id`)
    REFERENCES `findlunch`.`restaurant` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_donation_per_month_bill1`
    FOREIGN KEY (`bill_id`)
    REFERENCES `findlunch`.`bill` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `findlunch`.`bill_counter`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`bill_counter` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `counter` INT NOT NULL,
  `date` DATE NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `findlunch`.`reset_password`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`reset_password` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `token` VARCHAR(45) NOT NULL,
  `date` DATETIME NULL,
  `user_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`, `user_id`),
  INDEX `fk_reset_password_user1_idx` (`user_id` ASC),
  CONSTRAINT `fk_reset_password_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `findlunch`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `findlunch`.`allergenic`
-- -----------------------------------------------------
-- DROP TABLE IF EXISTS `findlunch`.`offer_has_allergenic` ;
-- DROP TABLE IF EXISTS `findlunch`.`allergenic` ;

CREATE TABLE IF NOT EXISTS `findlunch`.`allergenic` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(255) NULL,
  `short` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `findlunch`.`offer_has_allergenic`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `findlunch`.`offer_has_allergenic` (
  `offer_id` INT(11) NOT NULL,
  `allergenic_id` INT NOT NULL,
  PRIMARY KEY (`allergenic_id`, `offer_id`),
  INDEX `fk_offer_has_allergenic_offer1_idx` (`offer_id` ASC),
  INDEX `fk_offer_has_allergenic_allergenic1_idx` (`allergenic_id` ASC),
  CONSTRAINT `fk_offer_has_allergenic_offer1`
    FOREIGN KEY (`offer_id`)
    REFERENCES `findlunch`.`offer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_offer_has_allergenic_allergenic1`
    FOREIGN KEY (`allergenic_id`)
    REFERENCES `findlunch`.`allergenic` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `findlunch`.`additives`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `findlunch`.`offer_has_additives` ;
DROP TABLE IF EXISTS `findlunch`.`additives` ;

CREATE TABLE IF NOT EXISTS `findlunch`.`additives` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(255) NULL,
  `short` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `findlunch`.`offer_has_additives`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `findlunch`.`offer_has_additives` (
  `additives_id` INT NOT NULL,
  `offer_id` INT(11) NOT NULL,
  PRIMARY KEY (`additives_id`, `offer_id`),
  INDEX `fk_offer_has_additives_additives1_idx` (`additives_id` ASC),
  INDEX `fk_offer_has_additives_offer1_idx` (`offer_id` ASC),
  CONSTRAINT `fk_offer_has_additives_additives1`
    FOREIGN KEY (`additives_id`)
    REFERENCES `findlunch`.`additives` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_offer_has_additives_offer1`
    FOREIGN KEY (`offer_id`)
    REFERENCES `findlunch`.`offer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

INSERT IGNORE INTO `findlunch`.`country` (`country_code`, `name`) VALUES ('DE', 'Deutschland');

INSERT IGNORE INTO `findlunch`.`day_of_week` (`id`, `name`, `day_number`) VALUES (1, 'Montag', 2);
INSERT IGNORE INTO `findlunch`.`day_of_week` (`id`, `name`, `day_number`) VALUES (2, 'Dienstag', 3);
INSERT IGNORE INTO `findlunch`.`day_of_week` (`id`, `name`, `day_number`) VALUES (3, 'Mittwoch', 4);
INSERT IGNORE INTO `findlunch`.`day_of_week` (`id`, `name`, `day_number`) VALUES (4, 'Donnerstag', 5);
INSERT IGNORE INTO `findlunch`.`day_of_week` (`id`, `name`, `day_number`) VALUES (5, 'Freitag', 6);
INSERT IGNORE INTO `findlunch`.`day_of_week` (`id`, `name`, `day_number`) VALUES (6, 'Samstag', 7);
INSERT IGNORE INTO `findlunch`.`day_of_week` (`id`, `name`, `day_number`) VALUES (7, 'Sonntag', 1);

INSERT IGNORE INTO `findlunch`.`kitchen_type` (`id`, `name`) VALUES (1, 'Italienisch');
INSERT IGNORE INTO `findlunch`.`kitchen_type` (`id`, `name`) VALUES (2, 'Indisch');
INSERT IGNORE INTO `findlunch`.`kitchen_type` (`id`, `name`) VALUES (3, 'Griechisch');
INSERT IGNORE INTO `findlunch`.`kitchen_type` (`id`, `name`) VALUES (4, 'Asiatisch');
INSERT IGNORE INTO `findlunch`.`kitchen_type` (`id`, `name`) VALUES (5, 'Bayerisch');

INSERT IGNORE INTO `findlunch`.`course_types` (`id`, `name`) VALUES (1, 'Vorspeise');
INSERT IGNORE INTO `findlunch`.`course_types` (`id`, `name`) VALUES (2, 'Hauptspeise');
INSERT IGNORE INTO `findlunch`.`course_types` (`id`, `name`) VALUES (3, 'Nachspeise');
INSERT IGNORE INTO `findlunch`.`course_types` (`id`, `name`) VALUES (4, 'Getränk');
INSERT IGNORE INTO `findlunch`.`course_types` (`id`, `name`) VALUES (5, 'Beilage');

INSERT IGNORE INTO `findlunch`.`restaurant_type` (`id`, `name`) VALUES (1, 'Imbiss');
INSERT IGNORE INTO `findlunch`.`restaurant_type` (`id`, `name`) VALUES (2, 'Restaurant');
INSERT IGNORE INTO `findlunch`.`restaurant_type` (`id`, `name`) VALUES (3, 'Bäckerei');
INSERT IGNORE INTO `findlunch`.`restaurant_type` (`id`, `name`) VALUES (4, 'Sonstiges');

INSERT IGNORE INTO `findlunch`.`user_type` (`id`,`name`) VALUES (1, "Anbieter");
INSERT IGNORE INTO `findlunch`.`user_type` (`id`,`name`) VALUES (2, "Kunde");
INSERT IGNORE INTO `findlunch`.`user_type` (`id`,`name`) VALUES (3, "Betreiber");

INSERT IGNORE INTO `findlunch`.`euro_per_point` (`id`,`euro`) VALUES (1, 1.0);

INSERT IGNORE INTO `findlunch`.`minimum_profit` (`id`,`profit`) VALUES (1, 10);

-- account type
INSERT IGNORE INTO `findlunch`.`account_type` (`id`,`name`) VALUES (1, 'Forderungskonto');
INSERT IGNORE INTO `findlunch`.`account_type` (`id`,`name`) VALUES (2, 'Kundenkonto');

-- booking reason
INSERT IGNORE INTO `findlunch`.`booking_reason` (`id`,`reason`) VALUES (1, 'Forderung');
INSERT IGNORE INTO `findlunch`.`booking_reason` (`id`,`reason`) VALUES (2, 'Einzahlung');

INSERT IGNORE INTO `additives` (`id`, `name`, `description`, `short`) VALUES
(1, 'Farbstoffe E 100 - E 180', 'mit Farbstoff', 'a'),
(2, 'Konservierungsstoffe E 200 - E 219, E 230 - E 235, E 239, E 249 - E 252, E 280 - E 285, E 1105', 'mit Konservierungsstoff', 'b'),
(3, 'Geschmacksverstärker E 620 - E 635', 'mit Geschmacksverstärker', 'c'),
(4, 'Schwefeldioxid/Sulfite E 220 - E 228 ab 10 mg/kg', 'geschwefelt', 'd'),
(5, 'Eisensalze E 579, E 585', 'geschwärzt', 'e'),
(6, 'Stoffe zur Oberflächenbehandlung E 901 - E 904, E 912, E 914', 'gewachst', 'f'),
(7, 'Süßstoffe E 950 - E 952, E 954, E 955, E 957, E 959, E 962', 'mit Süßungsmittel(n)', 'g'),
(8, 'andere Süßungsmittel mit mehr als 10% Gehalt (Zuckeralkohole) E 420, E 421, E 953, E 965 - E 967', 'kann bei übermäßigem Verzehr abführend wirken', 'h'),
(9, 'Phosphate E 338 - 341, E 450 - E 452', 'mit Phosphat', 'i'),
(10, 'Coffein', 'coffeinhaltig', 'j'),
(11, 'Chinin, Chininsalze', 'chininhaltig', 'k'),
(12, 'Taurin', 'taurinhaltig', 'l'),
(13, 'Zutaten mit gentechnisch veränderten Organismen', 'gentechnisch verändert', 'm'),
(14, 'Gentechnisch veränderte Organismen', 'enthält Sojaöl, aus gentechnisch veränderter Soja hergestellt', 'n');

INSERT IGNORE INTO `allergenic` (`id`, `name`, `description`, `short`) VALUES
(1, 'Getreideprodukte (Glutenhaltig)', 'Weizen, Roggen, Gerste, Hafer, Dinkel, Kamut und daraus hergestellte Erzeugnisse, also Stärke, Brot, Nudeln, Panaden, Wurstwaren, Desserts etc.. Ausgenommen sind Glukosesirup auf Weizen- und Gerstenbasis. ', '1'),
(2, 'Fisch', 'Betroffen sind alle Süß- und Salzwasserfischarten, Kaviar, Fischextrakte, Würzpasten, Saucen etc.. Ganz genau genommen müsste auch ausgewiesen werden, wenn Produkte von Tieren verarbeitet werden, die mit Fischmehl gefüttert wurden.', '2'),
(3, 'Krebstiere', 'Garnelen, Hummer, Krebse, Scampi, Shrimps, Langusten und sämtliche daraus gewonnenen Erzeugnisse. Wer also in seinen Gerichten asiatische Gewürzmischung oder Paste mit Extrakten aus Krebstieren verwendet, muss das deklarieren. ', '3'),
(4, 'Schwefeldioxide und Sulfite', 'Wie sie in Softdrinks, Bier, Wein, Essig, Trockenfrüchten und bei diversen Fleisch-, Fisch- und Gemüseprodukten entstehen oder zugesetzt werden, in Konzentrationen von mehr als 10 mg/Kg oder 10 mg/l als insgesamt vorhandenes Schwefeldioxid.', '4'),
(5, 'Sellerie', 'Sowohl Knolle als auch Staude müssen deklariert werden, egal in welchem Aggregatzustand sie zum Gast wandern. Als Gewürz in Fertiggerichten, in Dressings, Ketchup, Saucen.', '5'),
(6, 'Milch und Laktose', 'Erzeugnisse wie Butter, Käse, Margarine etc. und Produkte, in denen Milch und /oder Laktose vorkommt, also etwa Brot-, Backwaren, Wurstwaren, Pürees, Suppen oder Saucen. Ausgenommen sind Molke zur Herstellung von alkoholischen Destillaten und Lactit.', '6'),
(7, 'Sesamsamen', 'Sesam, egal ob im Rohzustand, als Öl oder Paste, in Gebäck, Marinaden, Dressings, Falafel, Müsli, Hummus.', '7'),
(8, 'Nüsse', 'Mandeln, Haselnüsse, Walnüsse, Kaschunüsse / Cashewnüsse, Pekannüsse, Paranüsse, Pistazien, Macadamia- / Queenslandnüsse sowie sämtliche daraus gewonnenen Erzeugnisse außer Nüssen zur Herstellung von alkoholischen Destillaten.', '8'),
(9, 'Erdnüsse', 'Alle Erzeugnisse aus Erdnüssen wie Erdnussöl und Butter; Vorkommen in Gebäck und Kuchen, Desserts, vorfrittierten Produkten wie Pommes Frites oder Rösti, Aufstrichen, Füllungen etc. ', '9'),
(10, 'Eier', 'Als Flüssigei, Lecithin oder (Ov) Albumin und so, wie es in Mayonnaise, Panaden, Dressings, Kuchen, Suppen, Saucen, Nudeln, Glasuren und natürlich generell bei allen Eier-Speisen vorkommt.', '10'),
(11, 'Lupinen', 'Lupinensamen, Lupinenmehl, Milch, Tofu und Konzentrat, wie es sich in Brot- und Backwaren, Nudeln, Gewürzen, Würsten, Aufstrichen oder Süßspeisen findet. ', '11'),
(12, 'Senf', 'Betroffen sind hier sowohl Senfkörner als auch Pulver und alle daraus gewonnenen Erzeugnisse wie Dressings, Marinaden, Currys, Wurstwaren, Aufstriche, Gewürzmischungen etc.', '12'),
(13, 'Soja', 'Sojabohnen und daraus gewonnene Erzeugnisse, also etwa Miso, Sojasauce, Sojaöl, Gebäck, Marinaden, Kaffeeweißer, Suppen, Saucen, Dressings. Ausgenommen ist vollständig raffiniertes Sojabohnen-Öl und -Fett.', '13'),
(14, 'Weichtiere', 'Schnecken, Tintenfisch, Austern, Muscheln und alle Erzeugnisse, in denen Weichtiere oder Spuren von ihnen enthalten sind, also Gewürzmischungen, Saucen, asiatische Spezialitäten, Salate oder Pasten.', '14');

INSERT IGNORE INTO `findlunch`.`reservation_status` (`id`, `status`, `statKey`) VALUES
(0, 'neu angelegt', 0),
(1, 'bestätigt', 1),
(2, 'abgelehnt, Der Anbieter hat die Bestellung abgelehnt.', 2),
(3, 'abgelehnt, Die gewünschte Wartezeit kann nicht eingehalten werden.', 2),
(4, 'abgelehnt, Das Produkt ist leider ausverkauft.', 2),
(5, 'abgelehnt, Der Anbieter hat momentan geschlossen. ', 2),
(6, 'abgelehnt, Der Anbieter ist bereits ausgebucht.', 2),
(7, 'abgelehnt, Der Anbieter ist im Urlaub.', 2),
(8, 'abgelehnt, Das Restaurant bei dem Sie bestellen ist umgezogen.', 2),
(9, 'nicht abgeholt', 3);
-- -----------------------------------------------------
-- Abschluss des Skriptes
-- -----------------------------------------------------

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
