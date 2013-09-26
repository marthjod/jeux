SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `jeuxdb` ;
CREATE SCHEMA IF NOT EXISTS `jeuxdb` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `jeuxdb` ;

-- -----------------------------------------------------
-- Table `jeuxdb`.`Group`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jeuxdb`.`Group` (
  `id` INT(3) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL,
  `roundId` INT(2) NOT NULL COMMENT 'Round this group belongs to',
  `minSets` INT(1) NOT NULL DEFAULT '1' COMMENT 'Minimum number of sets to be played in each of the group\'s games',
  `maxSets` INT(2) NOT NULL DEFAULT '1' COMMENT 'Maximum number of sets to be played in each of the group\'s games',
  `active` TINYINT(1) NOT NULL DEFAULT '0' COMMENT 'Is this group\'s games currently being played?',
  `completed` TINYINT(1) NOT NULL DEFAULT '0' COMMENT 'Have all this group\'s games been played?',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `jeuxdb`.`Player`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jeuxdb`.`Player` (
  `id` INT(3) NOT NULL AUTO_INCREMENT,
  `groupId` INT(3) NOT NULL COMMENT 'Group this player currently belongs to',
  `name` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL,
  `points` INT(3) NOT NULL DEFAULT '0' COMMENT 'Total points (0-n) player has been awarded for won sets. May be 1 per won game if only 1 set is played per game',
  `scoreRatio` INT(4) NULL DEFAULT NULL COMMENT 'Difference (-n...0...n) between player\'s own and opponent\'s scores (in games which this player participated in)',
  `rank` INT(2) NOT NULL DEFAULT '0' COMMENT 'Rank in group. Updated after all this group\'s games have been completed',
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Player_Group1`
    FOREIGN KEY (`groupId`)
    REFERENCES `jeuxdb`.`Group` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;

CREATE INDEX `fk_Player_Group1_idx` ON `jeuxdb`.`Player` (`groupId` ASC);


-- -----------------------------------------------------
-- Table `jeuxdb`.`Game`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jeuxdb`.`Game` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `groupId` INT(11) NOT NULL COMMENT 'Group in which this game is played',
  `winnerId` INT(11) NOT NULL COMMENT 'Player who won the entire game, i.e. all its sets',
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Game_Player`
    FOREIGN KEY (`winnerId`)
    REFERENCES `jeuxdb`.`Player` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Game_Group1`
    FOREIGN KEY (`groupId`)
    REFERENCES `jeuxdb`.`Group` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;

CREATE INDEX `fk_Game_Player_idx` ON `jeuxdb`.`Game` (`winnerId` ASC);

CREATE INDEX `fk_Game_Group1_idx` ON `jeuxdb`.`Game` (`groupId` ASC);


-- -----------------------------------------------------
-- Table `jeuxdb`.`GameSet`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jeuxdb`.`GameSet` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `player1Score` INT(3) NOT NULL DEFAULT '0',
  `player2Score` INT(3) NOT NULL DEFAULT '0',
  `winnerId` INT(11) NOT NULL DEFAULT '0' COMMENT 'Player who has scored more',
  `gameId` INT(11) NOT NULL COMMENT 'Game this set is part of',
  `player1Id` INT(11) NOT NULL,
  `player2Id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_GameSet_Game1`
    FOREIGN KEY (`gameId`)
    REFERENCES `jeuxdb`.`Game` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_GameSet_Player1`
    FOREIGN KEY (`player1Id`)
    REFERENCES `jeuxdb`.`Player` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_GameSet_Player2`
    FOREIGN KEY (`player2Id`)
    REFERENCES `jeuxdb`.`Player` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_GameSet_Player3`
    FOREIGN KEY (`winnerId`)
    REFERENCES `jeuxdb`.`Player` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;

CREATE INDEX `fk_GameSet_Game1_idx` ON `jeuxdb`.`GameSet` (`gameId` ASC);

CREATE INDEX `fk_GameSet_Player1_idx` ON `jeuxdb`.`GameSet` (`player1Id` ASC);

CREATE INDEX `fk_GameSet_Player2_idx` ON `jeuxdb`.`GameSet` (`player2Id` ASC);

CREATE INDEX `fk_GameSet_Player3_idx` ON `jeuxdb`.`GameSet` (`winnerId` ASC);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
