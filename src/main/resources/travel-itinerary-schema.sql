-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema travel-itinerary
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema travel-itinerary
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `travel-itinerary` DEFAULT CHARACTER SET utf8 ;
USE `travel-itinerary` ;

-- -----------------------------------------------------
-- Table `travel-itinerary`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travel-itinerary`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `email` VARCHAR(100) UNIQUE NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `origin_city` VARCHAR(50) NULL,
  `dob` DATE NULL,
    PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `travel-itinerary`.`locationcode`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travel-itinerary`.`locationcode` (
  `code_id` CHAR(3) NOT NULL,
  `city_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`code_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `travel-itinerary`.`flight`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travel-itinerary`.`flight` (
  `flight_id` INT NOT NULL AUTO_INCREMENT,
  `price` DECIMAL(7,2),
  `duration` TIME NOT NULL,
  `date` DATE NOT NULL,
  `departure_time` TIME NOT NULL,
  `is_nonstop` TINYINT(1) NOT NULL,
  `next_flight_id` INT NULL,
  `origin_code` CHAR(3) NOT NULL,
  `dest_code` CHAR(3) NOT NULL,
  `flight_type` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`flight_id`),
  INDEX `fk_flight_flight_idx` (`next_flight_id` ASC) VISIBLE,
  INDEX `fk_flight_locationcode1_idx` (`origin_code` ASC) VISIBLE,
  INDEX `fk_flight_locationcode2_idx` (`dest_code` ASC) VISIBLE,
  CONSTRAINT `fk_flight_flight`
    FOREIGN KEY (`next_flight_id`)
    REFERENCES `travel-itinerary`.`flight` (`flight_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_flight_locationcode1`
    FOREIGN KEY (`origin_code`)
    REFERENCES `travel-itinerary`.`locationcode` (`code_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_flight_locationcode2`
    FOREIGN KEY (`dest_code`)
    REFERENCES `travel-itinerary`.`locationcode` (`code_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `travel-itinerary`.`hotel`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travel-itinerary`.`hotel` (
  `hotel_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `price` DECIMAL(7,2) NOT NULL,
  `checkin_date` DATE NOT NULL,
  `checkout_date` DATE NOT NULL,
  `address` VARCHAR(100) NOT NULL,
  `board_type` VARCHAR(15) NOT NULL,
  `latitude` DECIMAL(9,6) NOT NULL,
  `longitude` DECIMAL(9,6) NOT NULL,
  PRIMARY KEY (`hotel_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `travel-itinerary`.`activity`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travel-itinerary`.`activity` (
  `activity_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `rating` DECIMAL(3,2) NOT NULL,
  `price` DECIMAL(6,2) NOT NULL,
  `latitude` DECIMAL(9,6) NOT NULL,
  `longitude` DECIMAL(9,6) NOT NULL,
  `description` MEDIUMTEXT NULL,
  PRIMARY KEY (`activity_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `travel-itinerary`.`itinerary`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travel-itinerary`.`itinerary` (
  `itinerary_id` INT NOT NULL AUTO_INCREMENT,
  `num_adults` SMALLINT NOT NULL,
  `price_range_flight` DECIMAL(7,2) NULL,
  `price_range_hotel` DECIMAL(7,2) NULL,
  `price_range_activity` DECIMAL(6,2) NULL,
  `is_confirmed` TINYINT(1) NOT NULL,
  `total_price` DECIMAL(9,2) NULL,
  `start_date` DATE NOT NULL,
  `end_date` DATE NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`itinerary_id`),
  INDEX `fk_itinerary_user1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_itinerary_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `travel-itinerary`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `travel-itinerary`.`itinerary_flight`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travel-itinerary`.`itinerary_flight` (
  `itinerary_id` INT NOT NULL,
  `flight_id` INT NOT NULL,
  PRIMARY KEY (`itinerary_id`, `flight_id`),
  INDEX `fk_itinerary_has_flight_flight1_idx` (`flight_id` ASC) VISIBLE,
  INDEX `fk_itinerary_has_flight_itinerary1_idx` (`itinerary_id` ASC) VISIBLE,
  CONSTRAINT `fk_itinerary_has_flight_itinerary1`
    FOREIGN KEY (`itinerary_id`)
    REFERENCES `travel-itinerary`.`itinerary` (`itinerary_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_itinerary_has_flight_flight1`
    FOREIGN KEY (`flight_id`)
    REFERENCES `travel-itinerary`.`flight` (`flight_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `travel-itinerary`.`hotel_itinerary`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travel-itinerary`.`hotel_itinerary` (
  `hotel_id` INT NOT NULL,
  `itinerary_id` INT NOT NULL,
  PRIMARY KEY (`hotel_id`, `itinerary_id`),
  INDEX `fk_hotel_has_itinerary_itinerary1_idx` (`itinerary_id` ASC) VISIBLE,
  INDEX `fk_hotel_has_itinerary_hotel1_idx` (`hotel_id` ASC) VISIBLE,
  CONSTRAINT `fk_hotel_has_itinerary_hotel1`
    FOREIGN KEY (`hotel_id`)
    REFERENCES `travel-itinerary`.`hotel` (`hotel_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_hotel_has_itinerary_itinerary1`
    FOREIGN KEY (`itinerary_id`)
    REFERENCES `travel-itinerary`.`itinerary` (`itinerary_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `travel-itinerary`.`activity_itinerary`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travel-itinerary`.`activity_itinerary` (
  `activity_id` INT NOT NULL,
  `itinerary_id` INT NOT NULL,
  PRIMARY KEY (`activity_id`, `itinerary_id`),
  INDEX `fk_activity_has_itinerary_itinerary1_idx` (`itinerary_id` ASC) VISIBLE,
  INDEX `fk_activity_has_itinerary_activity1_idx` (`activity_id` ASC) VISIBLE,
  CONSTRAINT `fk_activity_has_itinerary_activity1`
    FOREIGN KEY (`activity_id`)
    REFERENCES `travel-itinerary`.`activity` (`activity_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_activity_has_itinerary_itinerary1`
    FOREIGN KEY (`itinerary_id`)
    REFERENCES `travel-itinerary`.`itinerary` (`itinerary_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
