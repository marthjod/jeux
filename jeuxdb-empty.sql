-- phpMyAdmin SQL Dump
-- version 4.1.12
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 20, 2014 at 11:37 
-- Server version: 5.6.16
-- PHP Version: 5.5.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `jeuxdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `Game`
--

CREATE TABLE IF NOT EXISTS `Game` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupId` int(11) NOT NULL COMMENT 'Group in which this game is played',
  `winnerId` int(11) DEFAULT NULL COMMENT 'Player who won the entire game, i.e. all its sets',
  `player1Id` int(11) NOT NULL,
  `player2Id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Game_Player_idx` (`winnerId`),
  KEY `fk_Game_Group1_idx` (`groupId`),
  KEY `fk_Game_Player1` (`player1Id`),
  KEY `fk_Game_Player2` (`player2Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `GameSet`
--

CREATE TABLE IF NOT EXISTS `GameSet` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `player1Score` int(3) NOT NULL DEFAULT '0',
  `player2Score` int(3) NOT NULL DEFAULT '0',
  `winnerId` int(11) DEFAULT NULL COMMENT 'Player who has scored more',
  `gameId` int(11) NOT NULL COMMENT 'Game this set is part of',
  `number` int(3) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_GameSet_Game1_idx` (`gameId`),
  KEY `fk_GameSet_Player3_idx` (`winnerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `Group_`
--

CREATE TABLE IF NOT EXISTS `Group_` (
  `id` int(3) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `roundId` int(2) NOT NULL COMMENT 'Round this group belongs to',
  `minSets` int(1) NOT NULL DEFAULT '1' COMMENT 'Minimum number of sets to be played in each of the group''s games',
  `maxSets` int(2) NOT NULL DEFAULT '1' COMMENT 'Maximum number of sets to be played in each of the group''s games',
  `active` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Is this group''s games currently being played?',
  `completed` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Have all this group''s games been played?',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=5 ;

-- --------------------------------------------------------

--
-- Table structure for table `Player`
--

CREATE TABLE IF NOT EXISTS `Player` (
  `id` int(3) NOT NULL AUTO_INCREMENT,
  `groupId` int(3) NOT NULL COMMENT 'Group this player currently belongs to',
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `points` int(3) NOT NULL DEFAULT '0' COMMENT 'Total points (0-n) player has been awarded for won sets. May be 1 per won game if only 1 set is played per game',
  `scoreRatio` int(4) DEFAULT NULL COMMENT 'Difference (-n...0...n) between player''s own and opponent''s scores (in games which this player participated in)',
  `rank` int(2) NOT NULL DEFAULT '0' COMMENT 'Rank in group. Updated after all this group''s games have been completed',
  `wonGames` int(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_Player_Group1_idx` (`groupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `RoundSwitchRule`
--

CREATE TABLE IF NOT EXISTS `RoundSwitchRule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `srcGroupId` int(11) NOT NULL COMMENT 'Group where players originate from.',
  `destGroupId` int(11) NOT NULL COMMENT 'Group players should be in in the next round.',
  `startWithRank` int(3) NOT NULL COMMENT 'Player with this rank in source group is the first to be moved from source group to destination group.',
  `additionalPlayers` int(3) NOT NULL COMMENT 'How many additional players should be moved from source to destination group?',
  `previousRoundId` int(3) NOT NULL COMMENT '(For safety. Remove if unused.) ',
  PRIMARY KEY (`id`),
  KEY `fk_rule_src_group` (`srcGroupId`),
  KEY `fk_rule_dest_group` (`destGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Game`
--
ALTER TABLE `Game`
  ADD CONSTRAINT `fk_Game_Group` FOREIGN KEY (`groupId`) REFERENCES `Group_` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Game_Player1` FOREIGN KEY (`player1Id`) REFERENCES `Player` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Game_Player2` FOREIGN KEY (`player2Id`) REFERENCES `Player` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Game_Winner` FOREIGN KEY (`winnerId`) REFERENCES `Player` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `GameSet`
--
ALTER TABLE `GameSet`
  ADD CONSTRAINT `fk_GameSet_Game` FOREIGN KEY (`gameId`) REFERENCES `Game` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_GameSet_Winner` FOREIGN KEY (`winnerId`) REFERENCES `Player` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `Player`
--
ALTER TABLE `Player`
  ADD CONSTRAINT `fk_player_groupid` FOREIGN KEY (`groupId`) REFERENCES `Group_` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `RoundSwitchRule`
--
ALTER TABLE `RoundSwitchRule`
  ADD CONSTRAINT `fk_rule_dest_group` FOREIGN KEY (`destGroupId`) REFERENCES `Group_` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_rule_src_group` FOREIGN KEY (`srcGroupId`) REFERENCES `Group_` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
