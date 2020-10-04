-- phpMyAdmin SQL Dump
-- version 4.8.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 04, 2020 at 09:43 AM
-- Server version: 10.1.34-MariaDB
-- PHP Version: 7.2.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `order-management_db`
--
CREATE DATABASE `order-management_db`;
USE `order-management_db`;
-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `customer_no` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `home_phone` varchar(15) DEFAULT NULL,
  `cell_phone` varchar(15) NOT NULL,
  `work_phone` varchar(15) DEFAULT NULL,
  `street` varchar(100) NOT NULL,
  `city` varchar(50) NOT NULL,
  `state` varchar(50) NOT NULL,
  `zip` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`customer_no`, `name`, `home_phone`, `cell_phone`, `work_phone`, `street`, `city`, `state`, `zip`) VALUES
(103, 'Carine Schmitt', '2319999', '8888838383', '7321456712', '54, rue Royale', 'Nantes', 'Ouest', '44000'),
(104, 'Justin Patel', '2311123', '8887738813', '9320006718', '11, Heritage', 'Ponda', 'Goa', '403401'),
(108, 'Prerna Desai', '9564325978', '9654875321', '2229747452', '99, HarbhajanGunj', 'Srinagar', 'Jammu and Kashmir', '297490'),
(172, 'Rahul Sharma', '08322536498', '9881876854', '08332659845', 'Mangor Hills', 'Vasco', 'Goa', '403802'),
(174, 'Kunal Singh', '08652315468', '8652314985', '02256548654', '82, Chhaya Nagar', 'Darjeeling', 'West Bengal', '734101');

-- --------------------------------------------------------

--
-- Table structure for table `orderitem`
--

CREATE TABLE `orderitem` (
  `order_item_number` int(11) NOT NULL,
  `stock_item_number` int(11) NOT NULL,
  `number_of_items` int(11) NOT NULL,
  `po_number` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `orderitem`
--

INSERT INTO `orderitem` (`order_item_number`, `stock_item_number`, `number_of_items`, `po_number`) VALUES
(1, 3, 50, 1),
(2, 5, 2, 1),
(3, 2, 2, 2),
(4, 1, 3, 3),
(5, 4, 3, 10),
(15, 36, 3, 24),
(16, 36, 1, 25),
(17, 5, 1, 26),
(18, 4, 1, 27),
(19, 1, 1, 27),
(20, 3, 2, 27),
(21, 3, 1, 29);

-- --------------------------------------------------------

--
-- Table structure for table `purchaseorder`
--

CREATE TABLE `purchaseorder` (
  `po_number` int(11) NOT NULL,
  `customer_no` int(11) NOT NULL,
  `order_date` date DEFAULT NULL,
  `ship_date` date DEFAULT NULL,
  `is_shipped` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `purchaseorder`
--

INSERT INTO `purchaseorder` (`po_number`, `customer_no`, `order_date`, `ship_date`, `is_shipped`) VALUES
(1, 103, '2020-07-11', '2020-10-03', 1),
(2, 104, '2020-07-03', '2020-07-11', 0),
(3, 103, '2020-09-25', '2020-09-29', 0),
(10, 103, '2020-08-24', '2020-09-29', 1),
(24, 108, '2020-10-03', '2020-10-07', 0),
(25, 108, '2020-10-03', '2020-10-07', 0),
(26, 108, '2020-10-03', '2020-10-07', 0),
(27, 104, '2020-10-03', '2020-10-07', 0),
(28, 104, '2020-10-03', '2020-10-07', 0),
(29, 103, '2020-10-03', '2020-10-07', 0);

-- --------------------------------------------------------

--
-- Table structure for table `stockitems`
--

CREATE TABLE `stockitems` (
  `stock_item_number` int(11) NOT NULL,
  `item_description` varchar(50) NOT NULL,
  `item_price` double NOT NULL,
  `unit` varchar(50) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `stockitems`
--

INSERT INTO `stockitems` (`stock_item_number`, `item_description`, `item_price`, `unit`, `quantity`) VALUES
(1, 'Chicken', 200, 'KG', 99),
(2, 'Egg', 70, 'DOZEN', 50),
(3, 'Apple', 50, 'KG', 37),
(4, 'Orange', 30, 'KG', 27),
(5, 'Bread', 15, 'NUMBERS', 19),
(36, 'Maggie', 24, 'NUMBERS', 29);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`customer_no`),
  ADD UNIQUE KEY `unique_customer` (`cell_phone`) USING BTREE;

--
-- Indexes for table `orderitem`
--
ALTER TABLE `orderitem`
  ADD PRIMARY KEY (`order_item_number`),
  ADD KEY `stock_item_number` (`stock_item_number`),
  ADD KEY `po_number` (`po_number`);

--
-- Indexes for table `purchaseorder`
--
ALTER TABLE `purchaseorder`
  ADD PRIMARY KEY (`po_number`),
  ADD KEY `customer_no` (`customer_no`);

--
-- Indexes for table `stockitems`
--
ALTER TABLE `stockitems`
  ADD PRIMARY KEY (`stock_item_number`),
  ADD UNIQUE KEY `unique_stock` (`item_description`) USING BTREE;

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `customer_no` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=175;

--
-- AUTO_INCREMENT for table `orderitem`
--
ALTER TABLE `orderitem`
  MODIFY `order_item_number` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `purchaseorder`
--
ALTER TABLE `purchaseorder`
  MODIFY `po_number` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT for table `stockitems`
--
ALTER TABLE `stockitems`
  MODIFY `stock_item_number` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `orderitem`
--
ALTER TABLE `orderitem`
  ADD CONSTRAINT `orderitem_ibfk_1` FOREIGN KEY (`stock_item_number`) REFERENCES `stockitems` (`stock_item_number`) ON DELETE CASCADE,
  ADD CONSTRAINT `orderitem_ibfk_2` FOREIGN KEY (`po_number`) REFERENCES `purchaseorder` (`po_number`) ON DELETE CASCADE;

--
-- Constraints for table `purchaseorder`
--
ALTER TABLE `purchaseorder`
  ADD CONSTRAINT `purchaseorder_ibfk_1` FOREIGN KEY (`customer_no`) REFERENCES `customer` (`customer_no`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
