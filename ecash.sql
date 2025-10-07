-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 07, 2025 at 04:46 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ecash`
--

-- --------------------------------------------------------

--
-- Table structure for table `linked_accts`
--

CREATE TABLE `linked_accts` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `acct_num` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `linked_accts`
--

INSERT INTO `linked_accts` (`id`, `user_id`, `name`, `acct_num`) VALUES
(1, 1, 'GCash', '1234567'),
(2, 1, 'PayPal', '56789'),
(3, 2, 'GCash', '1234567'),
(4, 3, 'PayPal', '56789');

-- --------------------------------------------------------

--
-- Table structure for table `merchants`
--

CREATE TABLE `merchants` (
  `id` int(11) NOT NULL,
  `name` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `merchants`
--

INSERT INTO `merchants` (`id`, `name`) VALUES
(1, 'GCash'),
(2, 'PayPal'),
(3, 'Debit Card'),
(4, 'AutoSweep'),
(5, 'EasyTrip'),
(6, 'Meralco'),
(7, 'PLDT'),
(11, 'Prime Water'),
(12, 'Globe Tel'),
(13, 'Prime Water'),
(14, 'Globe Tel');

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `id` int(5) NOT NULL,
  `ref_id` varchar(20) NOT NULL,
  `user_id` int(5) NOT NULL,
  `type` varchar(20) NOT NULL,
  `amount` double NOT NULL,
  `merchant_id` int(3) NOT NULL,
  `acct` varchar(20) NOT NULL,
  `balance` double NOT NULL,
  `date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transactions`
--

INSERT INTO `transactions` (`id`, `ref_id`, `user_id`, `type`, `amount`, `merchant_id`, `acct`, `balance`, `date`) VALUES
(1, 'TXN1759800942559', 2, 'Cash In', 500, 3, '1234567', 500, '2025-10-07 09:35:42'),
(2, 'TXN1759800966345', 2, 'Cash Out', 250, 4, '1232142143', 250, '2025-10-07 09:36:06'),
(3, 'TXN1759801100709', 2, 'Cash In', 100, 3, '121344', 350, '2025-10-07 09:38:20'),
(4, 'TXN1759802125529', 1, 'Cash In', 500, 1, '1234567', 500, '2025-10-07 09:55:25'),
(5, 'TXN1759802360886', 1, 'Cash Out', 200, 2, '1234567', 300, '2025-10-07 09:59:20');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `acct_id` int(20) NOT NULL,
  `username` varchar(11) NOT NULL,
  `fname` varchar(20) NOT NULL,
  `lname` varchar(20) NOT NULL,
  `middle` varchar(20) NOT NULL,
  `email` varchar(30) NOT NULL,
  `password` varchar(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `acct_id`, `username`, `fname`, `lname`, `middle`, `email`, `password`) VALUES
(1, 12123324, 'elson', 'Elson', 'Marqueses', 'Caraos', 'elson123456@gmail.com', '1234'),
(2, 2131312, 'ecmarq', 'Tonton', 'Alvarez', '', '', '1234');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `linked_accts`
--
ALTER TABLE `linked_accts`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `merchants`
--
ALTER TABLE `merchants`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ref_id` (`ref_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `acct_id` (`acct_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `linked_accts`
--
ALTER TABLE `linked_accts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `merchants`
--
ALTER TABLE `merchants`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `transactions`
--
ALTER TABLE `transactions`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
