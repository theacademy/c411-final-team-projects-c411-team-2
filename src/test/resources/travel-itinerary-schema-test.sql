-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema travel-itinerary-test
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema travel-itinerary-test
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `travel-itinerary-test` DEFAULT CHARACTER SET utf8 ;
USE `travel-itinerary-test` ;

-- -----------------------------------------------------
-- Table `travel-itinerary-test`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travel-itinerary-test`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `origin_city` VARCHAR(50) NULL,
  `dob` DATE NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `travel-itinerary-test`.`locationcode`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travel-itinerary-test`.`locationcode` (
  `code_id` CHAR(3) NOT NULL,
  `city_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`code_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `travel-itinerary-test`.`flight`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travel-itinerary-test`.`flight` (
  `flight_id` INT NOT NULL AUTO_INCREMENT,
  `price` DECIMAL(7,2) NOT NULL,
  `duration` TIME NOT NULL,
  `date` DATE NOT NULL,
  `departure_time` TIME NOT NULL,
  `is_nonstop` TINYINT(1) NOT NULL,
  `next_flight_id` INT NULL,
  `origin_code` CHAR(3) NOT NULL,
  `dest_code` CHAR(3) NOT NULL,
  PRIMARY KEY (`flight_id`),
  CONSTRAINT `fk_flight_flight`
    FOREIGN KEY (`next_flight_id`)
    REFERENCES `travel-itinerary-test`.`flight` (`flight_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_flight_locationcode1`
    FOREIGN KEY (`origin_code`)
    REFERENCES `travel-itinerary-test`.`locationcode` (`code_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_flight_locationcode2`
    FOREIGN KEY (`dest_code`)
    REFERENCES `travel-itinerary-test`.`locationcode` (`code_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `travel-itinerary-test`.`boardType`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travel-itinerary`.`boardType` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `travel-itinerary-test`.`hotel`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travel-itinerary`.`hotel` (
  `hotel_id` CHAR(8) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `price` DECIMAL(7,2) NOT NULL,
  `checkin_date` DATE NOT NULL,
  `checkout_date` DATE NOT NULL,
  `address` VARCHAR(100) NOT NULL,
  `latitude` DECIMAL(9,6) NOT NULL,
  `longitude` DECIMAL(9,6) NOT NULL,
  `board_type` INT NOT NULL,
  PRIMARY KEY (`hotel_id`),
  CONSTRAINT `fk_hotel_boardType`
    FOREIGN KEY (`board_type`)
    REFERENCES `boardType` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `travel-itinerary-test`.`activity`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travel-itinerary-test`.`activity` (
  `activity_id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `rating` DECIMAL(3,2) NOT NULL,
  `price` DECIMAL(6,2) NOT NULL,
  `latitude` DECIMAL(9,6) NOT NULL,
  `longitude` DECIMAL(9,6) NOT NULL,
  `description` MEDIUMTEXT NULL,
  PRIMARY KEY (`activity_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `travel-itinerary-test`.`activityTypes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travel-itinerary-test`.`activityTypes` (
  `activity_type_id` INT NOT NULL,
  `name` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`activity_type_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `travel-itinerary-test`.`activity_activityTypes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travel-itinerary-test`.`activity_activityTypes` (
  `activity_id` INT NOT NULL,
  `activity_type_id` INT NOT NULL,
  PRIMARY KEY (`activity_id`, `activity_type_id`),
  CONSTRAINT `fk_activity_has_activityTypes_activity1`
    FOREIGN KEY (`activity_id`)
    REFERENCES `travel-itinerary-test`.`activity` (`activity_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_activity_has_activityTypes_activityTypes1`
    FOREIGN KEY (`activity_type_id`)
    REFERENCES `travel-itinerary-test`.`activityTypes` (`activity_type_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `travel-itinerary-test`.`pointOfInterest`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travel-itinerary-test`.`pointOfInterest` (
  `poi_id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `latitude` DECIMAL(9,6) NOT NULL,
  `longitude` DECIMAL(9,6) NOT NULL,
  PRIMARY KEY (`poi_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `travel-itinerary-test`.`pointOfInterest_activityTypes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travel-itinerary-test`.`pointOfInterest_activityTypes` (
  `activity_type_id` INT NOT NULL,
  `poi_id` INT NOT NULL,
  PRIMARY KEY (`activity_type_id`, `poi_id`),
  CONSTRAINT `fk_activityTypes_has_pointOfInterest_activityTypes1`
    FOREIGN KEY (`activity_type_id`)
    REFERENCES `travel-itinerary-test`.`activityTypes` (`activity_type_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_activityTypes_has_pointOfInterest_pointOfInterest1`
    FOREIGN KEY (`poi_id`)
    REFERENCES `travel-itinerary-test`.`pointOfInterest` (`poi_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `travel-itinerary-test`.`itinerary`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travel-itinerary-test`.`itinerary` (
  `itinerary_id` INT NOT NULL,
  `num_adults` SMALLINT NOT NULL,
  `price_range_flight` DECIMAL(7,2) NULL,
  `price_range_hotel` DECIMAL(7,2) NULL,
  `price_range_activity` DECIMAL(6,2) NULL,
  `is_confirmed` TINYINT(1) NOT NULL,
  `total_price` DECIMAL(9,2) NULL,
  `start_date` DATE NOT NULL,
  `end_date` DATE NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`itinerary_id`, `user_id`),
  CONSTRAINT `fk_itinerary_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `travel-itinerary-test`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `travel-itinerary-test`.`itinerary_flight`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travel-itinerary-test`.`itinerary_flight` (
  `itinerary_id` INT NOT NULL,
  `flight_id` INT NOT NULL,
  PRIMARY KEY (`itinerary_id`, `flight_id`),
  CONSTRAINT `fk_itinerary_has_flight_itinerary1`
    FOREIGN KEY (`itinerary_id`)
    REFERENCES `travel-itinerary-test`.`itinerary` (`itinerary_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_itinerary_has_flight_flight1`
    FOREIGN KEY (`flight_id`)
    REFERENCES `travel-itinerary-test`.`flight` (`flight_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `travel-itinerary-test`.`hotel_itinerary`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travel-itinerary-test`.`hotel_itinerary` (
  `hotel_id` CHAR(8) NOT NULL,
  `itinerary_id` INT NOT NULL,
  PRIMARY KEY (`hotel_id`, `itinerary_id`),
  CONSTRAINT `fk_hotel_has_itinerary_hotel1`
    FOREIGN KEY (`hotel_id`)
    REFERENCES `travel-itinerary-test`.`hotel` (`hotel_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_hotel_has_itinerary_itinerary1`
    FOREIGN KEY (`itinerary_id`)
    REFERENCES `travel-itinerary-test`.`itinerary` (`itinerary_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `travel-itinerary-test`.`activity_itinerary`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travel-itinerary-test`.`activity_itinerary` (
  `activity_id` INT NOT NULL,
  `itinerary_id` INT NOT NULL,
  PRIMARY KEY (`activity_id`, `itinerary_id`),
  CONSTRAINT `fk_activity_has_itinerary_activity1`
    FOREIGN KEY (`activity_id`)
    REFERENCES `travel-itinerary-test`.`activity` (`activity_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_activity_has_itinerary_itinerary1`
    FOREIGN KEY (`itinerary_id`)
    REFERENCES `travel-itinerary-test`.`itinerary` (`itinerary_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `travel-itinerary-test`.`itinerary_pointOfInterest`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travel-itinerary-test`.`itinerary_pointOfInterest` (
  `itinerary_id` INT NOT NULL,
  `poi_id` INT NOT NULL,
  PRIMARY KEY (`itinerary_id`, `poi_id`),
  CONSTRAINT `fk_itinerary_has_pointOfInterest_itinerary1`
    FOREIGN KEY (`itinerary_id`)
    REFERENCES `travel-itinerary-test`.`itinerary` (`itinerary_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_itinerary_has_pointOfInterest_pointOfInterest1`
    FOREIGN KEY (`poi_id`)
    REFERENCES `travel-itinerary-test`.`pointOfInterest` (`poi_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
