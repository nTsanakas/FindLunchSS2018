-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Table `findlunch`.`swa_sales_person`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`swa_sales_person` (
  `id` INT(11) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `first_name` VARCHAR(60) NOT NULL,
  `second_name` VARCHAR(60) NOT NULL,
  `street` VARCHAR(60) NOT NULL,
  `street_number` VARCHAR(11) NOT NULL,
  `zip` VARCHAR(5) NOT NULL,
  `city` VARCHAR(60) NOT NULL,
  `phone` VARCHAR(60) NOT NULL,
  `country_code` VARCHAR(2),
  `email` VARCHAR(60) NOT NULL,
  `iban` VARCHAR(60) NOT NULL,
  `bic` VARCHAR(60) NOT NULL,
  `salary_percentage` DECIMAL(3,2) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `findlunch`.`swa_todo_request_typ`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`swa_todo_request_typ` (
  `id` INT(11) NOT NULL,
  -- Zweideutig in der MA, die JPA Implementierung erwartet aber VARCHAR
  `todo_request_typ` VARCHAR(60) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `findlunch`.`swa_todo_list`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`swa_todo_list` (
  `id` INT(11) NOT NULL,
  `sales_person_id` INT(11) NOT NULL,
  `offer_id` INT(11),
  `todo_request_typ` INT(11) NOT NULL,
  `datetime` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_salesperson_todo`
    FOREIGN KEY (`sales_person_id`)
    REFERENCES `findlunch`.`swa_sales_person` (`id`)
    ON DELETE NO ACTION
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_offer_todo`
    FOREIGN KEY (`offer_id`)
    REFERENCES `findlunch`.`offer` (`id`)
    ON DELETE SET NULL
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_requesttyp_todo`
    FOREIGN KEY (`todo_request_typ`)
    REFERENCES `findlunch`.`swa_todo_request_typ` (`id`)
    ON DELETE NO ACTION
    ON UPDATE RESTRICT)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `findlunch`.`swa_tour`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`swa_tour` (
  `id` INT(11) NOT NULL,
  `name` VARCHAR(60) NOT NULL,
  `sales_person_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_salesperson_tour`
    FOREIGN KEY (`sales_person_id`)
    REFERENCES `findlunch`.`swa_sales_person` (`id`)
    ON DELETE NO ACTION
    ON UPDATE RESTRICT)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `findlunch`.`swa_tour_details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`swa_tour_details` (
  `tour_id` INT(11) NOT NULL,
  `position` INT NOT NULL,
  `restaurant_id` INT(11) NOT NULL,
  PRIMARY KEY (`tour_id`, `position`),
  CONSTRAINT `fk_tour_tourdetails`
    FOREIGN KEY (`tour_id`)
    REFERENCES `findlunch`.`swa_tour` (`id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `findlunch`.`swa_protocol_contact_way`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`swa_protocol_contact_way` (
  `id` INT(11) NOT NULL,
  `name` VARCHAR(60) NOT NULL,
  -- In der MA soll die id auch FK sein, hat mEn keinen Sinn.
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `findlunch`.`swa_protocol_categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`swa_protocol_categories` (
  `id` INT(11) NOT NULL,
  `name` VARCHAR(60) NOT NULL,
  -- In der MA soll auch diese id auch FK sein, hat mEn auch keinen Sinn.
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `findlunch`.`swa_protocol`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `findlunch`.`swa_protocol` (
  `id` INT(11) NOT NULL,
  `restaurant_id` INT(11) NOT NULL,
  `contact_way_id` INT(11) NOT NULL,
  `categories_id` INT(11) NOT NULL,
  `date` DATE NOT NULL,
  `entry_typ` VARCHAR(60) NOT NULL,
  `comment` TEXT,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_restaurant_protocol`
    FOREIGN KEY (`restaurant_id`)
    REFERENCES `findlunch`.`restaurant` (`id`)
    ON DELETE NO ACTION
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_contactway_protocol`
    FOREIGN KEY (`contact_way_id`)
    REFERENCES `findlunch`.`swa_protocol_contact_way` (`id`)
    ON DELETE NO ACTION
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_categories_protocol`
    FOREIGN KEY (`categories_id`)
    REFERENCES `findlunch`.`swa_protocol_categories` (`id`)
    ON DELETE NO ACTION
    ON UPDATE RESTRICT)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `findlunch`.`offer` erweitern.
-- -----------------------------------------------------
ALTER TABLE `findlunch`.`offer` ADD COLUMN IF NOT EXISTS (
  `swa_change_request_id` INT(11) DEFAULT -1,
  `swa_comment_of_last_change` TEXT DEFAULT NULL,
  `swa_last_changed_by_sales_person_id` INT(11) DEFAULT -1);

ALTER TABLE `findlunch`.`offer`
ADD CONSTRAINT `fk_salesperson_offer`
  FOREIGN KEY (`swa_last_changed_by_sales_person_id`)
  REFERENCES `findlunch`.`swa_sales_person` (`id`)
  ON DELETE SET DEFAULT
  ON UPDATE NO ACTION;

-- -----------------------------------------------------
-- Table `findlunch`.`restaurant` erweitern.
-- -----------------------------------------------------
ALTER TABLE `findlunch`.`restaurant` ADD COLUMN IF NOT EXISTS (
  `swa_offer_modify_permission` TINYINT(1) NOT NULL DEFAULT 0,
  `swa_blocked` TINYINT(1) NOT NULL DEFAULT 0,
  `swa_sales_person_id` INT(11) DEFAULT -1);

ALTER TABLE `findlunch`.`restaurant`
ADD CONSTRAINT `fk_salesperson_restaurant`
  FOREIGN KEY (`swa_sales_person_id`)
  REFERENCES `findlunch`.`swa_sales_person` (`id`)
  ON DELETE SET DEFAULT
  ON UPDATE NO ACTION;

-- -----------------------------------------------------
-- Abschluss des Skriptes
-- -----------------------------------------------------

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
