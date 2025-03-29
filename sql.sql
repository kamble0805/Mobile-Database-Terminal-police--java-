create database pol_db;
use pol_db;

CREATE TABLE IF NOT EXISTS `mdt_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cid` VARCHAR(20) NOT NULL,  -- Changed from DEFAULT NULL to NOT NULL
  `information` MEDIUMTEXT DEFAULT NULL,
  `tags` TEXT NOT NULL,
  `gallery` TEXT NOT NULL,
  `jobtype` VARCHAR(25) DEFAULT 'police',
  `pfp` TEXT DEFAULT NULL,
  `fingerprint` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (`id`),  -- Use `id` as the primary key
  UNIQUE KEY `cid` (`cid`)  -- Ensure `cid` is unique
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
ALTER TABLE `mdt_data` 
ADD COLUMN `username` VARCHAR(50) UNIQUE NOT NULL AFTER `id`,
ADD COLUMN `password` VARCHAR(255) NOT NULL AFTER `username`;
ALTER TABLE `mdt_data` 
ADD COLUMN `name` VARCHAR(100) NOT NULL AFTER `cid`,
ADD COLUMN `dob` DATE NOT NULL AFTER `name`;
ALTER TABLE `mdt_data` 
ADD COLUMN `clockedin` ENUM('yes', 'no') DEFAULT 'no' AFTER `dob`;


CREATE TABLE IF NOT EXISTS `mdt_bulletin` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `title` TEXT NOT NULL,
    `description` TEXT NOT NULL,
    `author` VARCHAR(50) NOT NULL,
    `time` VARCHAR(20) NOT NULL,
    `jobtype` VARCHAR(25) DEFAULT 'police',
    PRIMARY KEY (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE IF NOT EXISTS `mdt_reports` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `author` varchar(50) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `details` LONGTEXT DEFAULT NULL,
  `tags` text DEFAULT NULL,
  `officersinvolved` text DEFAULT NULL,
  `civsinvolved` text DEFAULT NULL,
  `gallery` text DEFAULT NULL,
  `time` varchar(20) DEFAULT NULL,
  `jobtype` varchar(25) DEFAULT 'police',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `mdt_bolos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `author` varchar(50) DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `plate` varchar(50) DEFAULT NULL,
  `owner` varchar(50) DEFAULT NULL,
  `individual` varchar(50) DEFAULT NULL,
  `detail` text DEFAULT NULL,
  `tags` text DEFAULT NULL,
  `gallery` text DEFAULT NULL,
  `officersinvolved` text DEFAULT NULL,
  `time` varchar(20) DEFAULT NULL,
  `jobtype` varchar(25) NOT NULL DEFAULT 'police',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `mdt_cases` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `case_type` ENUM('incident', 'conviction', 'warrant') NOT NULL,  -- Distinguish case types
  `title` VARCHAR(100) NOT NULL DEFAULT '',  -- Case title
  `details` LONGTEXT NOT NULL,  -- Incident or conviction details
  `officers_involved` TEXT NOT NULL,  -- Officers in the case
  `civs_involved` TEXT NOT NULL,  -- Civilians involved
  `charges` TEXT DEFAULT NULL,  -- Charges (for convictions)
  `fine` INT(11) DEFAULT 0,  -- Fine amount
  `sentence` INT(11) DEFAULT 0,  -- Sentence duration
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Auto-generated creation time
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- Create the mdt_calls table
CREATE TABLE IF NOT EXISTS `mdt_calls` (
  `call_id` INT(11) NOT NULL AUTO_INCREMENT,
  `timestamp` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `caller_cid` VARCHAR(20) NOT NULL,
  `caller_name` VARCHAR(100) NOT NULL,
  `call_details` TEXT NOT NULL,
  `status` ENUM('active', 'completed', 'failed') NOT NULL,
  PRIMARY KEY (`call_id`),
  FOREIGN KEY (`caller_cid`) REFERENCES `mdt_data`(`cid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `mdt_vehicleinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `plate` varchar(50) DEFAULT NULL,
  `information` text NOT NULL,  -- Removed DEFAULT ''
  `stolen` tinyint(1) NOT NULL DEFAULT 0,
  `code5` tinyint(1) NOT NULL DEFAULT 0,
  `image` text NOT NULL,  -- Removed DEFAULT ''
  `points` int(11) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
ALTER TABLE `mdt_vehicleinfo`
ADD COLUMN `cid` VARCHAR(20) NOT NULL,  -- Ensure the data type matches `mdt_data.cid`
ADD CONSTRAINT `fk_vehicle_cid` 
FOREIGN KEY (`cid`) 
REFERENCES `mdt_data` (`cid`) 
ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `mdt_vehicleinfo`
ADD COLUMN `name` VARCHAR(100) NOT NULL AFTER `cid`;


CREATE TABLE IF NOT EXISTS `mdt_weaponinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `serial` varchar(50) DEFAULT NULL,
  `owner` varchar(50) DEFAULT NULL,
  `information` text NOT NULL,  -- Removed DEFAULT ''
  `weapClass` varchar(50) DEFAULT NULL,
  `weapModel` varchar(50) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `serial` (`serial`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE IF NOT EXISTS `mdt_impound` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vehicleid` int(11) NOT NULL,
  `linkedreport` int(11) NOT NULL,
  `fee` int(11) DEFAULT NULL,
  `time` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `mdt_clocking` (
  `id` INT(10) NOT NULL AUTO_INCREMENT,
  `cid` VARCHAR(20) NOT NULL,  -- User identifier
  `name` VARCHAR(255) NOT NULL, -- User's full name
  `status` ENUM('clocked_in', 'clocked_out') NOT NULL, -- Status of the user
  `clock_in_time` DATETIME DEFAULT NULL, -- Time when the user clocked in
  `clock_out_time` DATETIME DEFAULT NULL, -- Time when the user clocked out
  PRIMARY KEY (`id`),
  KEY `cid` (`cid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
ALTER TABLE `mdt_clocking`
ADD COLUMN `officer_status` ENUM('Available', 'Unavailable', 'Responding to Call', 'Break/Report Writing') NOT NULL DEFAULT 'Available';


CREATE TABLE mdt_warrants (
    id INT AUTO_INCREMENT PRIMARY KEY,
    civilian_name VARCHAR(100) NOT NULL,  -- Stores civilian's name
    charges TEXT NOT NULL,                -- Stores charges
    officer_name VARCHAR(100) NOT NULL,   -- Stores issuing officer's name
    status ENUM('Active', 'Served') DEFAULT 'Active',
    date_issued TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS penal_code_charges (
    id INT AUTO_INCREMENT PRIMARY KEY,          -- Unique identifier for each charge
    description VARCHAR(255) NOT NULL,          -- Description of the charge
    severity ENUM('Misdemeanor', 'Felony', 'Infraction') NOT NULL, -- Severity of the charge
    penal_code VARCHAR(50) NOT NULL,            -- Penal code associated with the charge
    notes TEXT DEFAULT NULL,                     -- Additional notes or comments
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp for when the charge was created
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Timestamp for when the charge was last updated
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
