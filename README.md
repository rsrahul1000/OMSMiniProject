# Order Management System Mini Project

>- OMS application to track the details of the orders placed by various customers implemented with Core Java Features with JDBC and JUnit 4.
>- Duplicate Entries for Customer and StockItem tables can not be achieved as the insert would be skipped if Consumer has an existing cell number and if StockItem has name existing in the table.
>- Log files are placed in the logs directory, and log handle for console is removed.
>- If a customer is deleted, then all its existing purchase orders and order items will be **cascade deleted**.
>- It a Stock Item is deleted, then all stocks reference present in order items will also be **cascade deleted**.


_Note: This repository is independent of any IDE project files and contains ONLY the relevant code which can be used to implemented on any IDE_

## Application Functions

>1. Populate Customer And Stock Items Table From File ( Once Inserted, Duplicate values would be ignored if ran again ).
>2. Add New Customer
>3. Add new Purchase Order
>4. Add new Stock Item
>5. Fetch customer based on id
>6. Fetch Orders placed by specific customers
>7. Fetch orders placed for a specific duration   
>>  a. fetch all orders placed between from and to date inclusive of both date  
>>  b. fetch all the orders placed for the given date
>8. Update order status to ship and update ship date based on order id.
>9. Fetch delayed orders  (orders which are not shipped even after ship date)
>>  a. By default, every order placed by the customer should get dispatched within the four days after placing the order ( inclusive placed date ). If an order is not dispatched within the four days after placing date, it should be considered as delayed order
>10. Fetch all stock Items
>11. Find month-wise total orders shipped.  
>12. Find the total amount collected based on months
>13. Find the customer who has made maximum orders.
>14. Generate bill for customer for a specific order. ( create a file under bills/customerid directory )



## OMS - ER Diagram
![Alt text](resources/OMS%20Database%20-%20ER%20Diagram.png?raw=true "Order Management System Database ER Diagram")


## Setup Database

>- Database Can be created in 2 Ways: 
  >>  1. Import the **'order-management_db.sql'** file from the resources folder directly to your xampp SQL server. (**Recommended**)
  >>  2. Manually paste the SQL codes to console from **'Queries.txt'** to create Database, Tables, And Populate them with Test Data. (**Run SQL Codes one by one**).
>- (Optional) Populating of Table Customers and Stock Item can also be done via Java Code by choosing the option '0' when running the java application, which will populate tables via files present in the resource directory. Here, No need to populate the tables manually via SQL codes, only creation of tables would be needed as prerequisite.
>- To check the delayed order functionality, add purchaseorder and orderitem table contents via queries provided in the Queries.txt file as it needs orders of shipDate less than current date for them to be delayed. (Not required if imported the whole DB using .sql file)

 
## Setup Application

>- Create a Eclipse Java Project with Name "OMSMiniProject"
>- Change Directory to Project Folder "OMSMiniProject" in local system and Clone [OMS Mini Project](https://github.com/rsrahul1000/OMSMiniProject) into a non-rempty directory(Eclipse Project Directory) using following command in git bash
  >>```bash
  >> git init
  >> git remote add origin https://github.com/rsrahul1000/OMSMiniProject.git
  >> git fetch
  >> git checkout -t origin/main
  >>```
  >> Or manually download and copy all its contents to eclipse "OMSMiniProject" project directory.
>- Add [JDBC jar](https://dev.mysql.com/downloads/file/?id=496255) files to eclipse project to work with JDBC functions.
>- Add JUnit 4 Library in eclipse for this project to test DAO classes.
>- Modify config.properties based on your system for DB connection
>>```bash
>>	mysql.url=jdbc:mysql://localhost:3306/order-management_db
>>	mysql.username=root
>>	mysql.password=
>>```
>- Make sure your Database server is up and running.
>- Run OMSTester.java to start executing the application.


## Application Tested On

> - JDK version: 1.8.0_191
> - Xampp Conrol Pannel Version: 3.2.2
> - Eclipse IDE 2020
