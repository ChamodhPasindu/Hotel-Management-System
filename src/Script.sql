DROP DATABASE IF EXISTS Hotel;
CREATE DATABASE IF NOT EXISTS Hotel;
USE Hotel;

DROP TABLE IF EXISTS Customer;
CREATE TABLE IF NOT EXISTS Customer(
    custId VARCHAR (6),
    custTittle VARCHAR (5),
    custName VARCHAR (30) NOT NULL DEFAULT 'unknown',
    custNIC VARCHAR (15),
    custNumber VARCHAR (15),
    custEmail VARCHAR (50),
    custAddress VARCHAR (100),
    custProvince VARCHAR (15),
    CONSTRAINT PRIMARY KEY (custId)
);
SHOW TABLES;
DESC Customer;

#---------------------------------------

DROP TABLE IF EXISTS Room;
CREATE TABLE IF NOT EXISTS Room(
    roomId VARCHAR (6),
    roomFloor INT (2),
    roomType VARCHAR (30),
    roomDescription VARCHAR (50),
    RoomPrice DECIMAL(10,2)DEFAULT 0.00,
    RoomDiscount DECIMAL(10,2)DEFAULT 0.00,
    roomStatus VARCHAR (10),
    CONSTRAINT PRIMARY KEY (roomId)
);
SHOW TABLES;
DESC Room;

#---------------------------------------

DROP TABLE IF EXISTS Complain;
CREATE TABLE IF NOT EXISTS Complain(
    complainId VARCHAR(10),
    custId VARCHAR(10),
    description VARCHAR (50),
    time VARCHAR (10) ,
    date DATE ,
    status VARCHAR(10),
    CONSTRAINT PRIMARY KEY (complainId),
    CONSTRAINT FOREIGN KEY (custId) REFERENCES Customer (custId) ON DELETE CASCADE ON UPDATE CASCADE
);
SHOW TABLES;
DESC Complain;

#----------------------------------------

DROP TABLE IF EXISTS Service;
CREATE TABLE IF NOT EXISTS Service(
    serviceId VARCHAR (6),
    serviceName VARCHAR (15),
    description VARCHAR (50),
    servicePrice DECIMAL (6,2) DEFAULT 0.00,
    CONSTRAINT PRIMARY KEY (serviceId)
);
SHOW TABLES;
DESC Service;

#---------------------------------------

DROP TABLE IF EXISTS MealPackage;
CREATE TABLE IF NOT EXISTS MealPackage(
    packageId VARCHAR (6),
    packageType VARCHAR (30),
    description VARCHAR (50),
    packagePrice DECIMAL(10,2)DEFAULT 0.00,
    CONSTRAINT PRIMARY KEY (packageId)
);
SHOW TABLES;
DESC Service;

#---------------------------------------

DROP TABLE IF EXISTS Hall;
CREATE TABLE IF NOT EXISTS Hall(
    hallId VARCHAR (6),
    hallFloor INT (2),
    hallType VARCHAR (30),
    description VARCHAR (50),
    HallPrice DECIMAL(10,2)DEFAULT 0.00,
    HallDiscount DECIMAL(10,2)DEFAULT 0.00,
    hallStatus VARCHAR (10),
    CONSTRAINT PRIMARY KEY (hallId)
);
SHOW TABLES;
DESC Service;

#--------------------------------------

DROP TABLE IF EXISTS RoomReservation;
CREATE TABLE IF NOT EXISTS RoomReservation(
    reservationId VARCHAR(10),
    custId VARCHAR (10),
    reserveDate DATE ,
    reserveTime VARCHAR (10),
    checkIn DATE ,
    checkOut DATE,
    advance DECIMAL (10,2) DEFAULT 0.00,
    cost DECIMAL (10,2) DEFAULT 0.00,
    status VARCHAR (10),
    noOfGuest INT (5),
    CONSTRAINT PRIMARY KEY (reservationId),
    CONSTRAINT FOREIGN KEY (custId) REFERENCES Customer (custId) ON DELETE CASCADE ON UPDATE CASCADE
);
SHOW TABLES;
DESC RoomReservation;

#--------------------------------------

DROP TABLE IF EXISTS HallReservation;
CREATE TABLE IF NOT EXISTS HallReservation(
    reservationId VARCHAR (10),
    hallId VARCHAR (10),
    custId VARCHAR (10),
    reserveDate DATE ,
    reserveTime VARCHAR (10),
    checkIn DATE ,
    checkOut DATE,
    advance DECIMAL (10,2) DEFAULT 0.00,
    cost DECIMAL (10,2) DEFAULT 0.00,
    status VARCHAR (10),
    CONSTRAINT PRIMARY KEY (reservationId),
    CONSTRAINT FOREIGN KEY (hallId) REFERENCES Hall (hallId) ON DELETE CASCADE ON UPDATE CASCADE ,
    CONSTRAINT FOREIGN KEY (custId) REFERENCES Customer (custId) ON DELETE CASCADE ON UPDATE CASCADE
);
SHOW TABLES;
DESC HallReservation;

#-------------------------------------

DROP TABLE IF EXISTS ServiceDetail;
CREATE TABLE IF NOT EXISTS ServiceDetail(
    reservationId VARCHAR (10),
    serviceId VARCHAR (6),
    noOfDay INT(10),
    price DECIMAL (6,2) DEFAULT 0.00,
    CONSTRAINT PRIMARY KEY (reservationId,serviceId),
    CONSTRAINT FOREIGN KEY (reservationId) REFERENCES RoomReservation (reservationId) ON DELETE CASCADE ON UPDATE CASCADE ,
    CONSTRAINT FOREIGN KEY (serviceId) REFERENCES Service (serviceId) ON DELETE CASCADE ON UPDATE CASCADE
);
SHOW TABLES;
DESC ServiceDetail;

#------------------------------------

DROP TABLE IF EXISTS MealpackageDetail;
CREATE TABLE IF NOT EXISTS MealpackageDetail(
    reservationId VARCHAR (10),
    packageId VARCHAR (6),
    breakfast INT(1) DEFAULT 0,
    lunch INT(1) DEFAULT 0,
    dinner INT(1) DEFAULT 0,
    price DECIMAL (10,2) DEFAULT 0.00,
    availability VARCHAR(12),
    CONSTRAINT PRIMARY KEY (reservationId,packageId),
    CONSTRAINT FOREIGN KEY (reservationId) REFERENCES RoomReservation (reservationId) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY (packageid) REFERENCES MealPackage (packageId) ON DELETE CASCADE ON UPDATE CASCADE
);
SHOW TABLES;
DESC MealPackageDetail;

#-------------------------------------

DROP TABLE IF EXISTS RoomDetail;
CREATE TABLE IF NOT EXISTS RoomDetail(
    roomId VARCHAR (6),
    reservationId VARCHAR (10),
    CONSTRAINT PRIMARY KEY (roomId,reservationId),
    CONSTRAINT FOREIGN KEY (roomId) REFERENCES Room(roomId) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY (reservationId) REFERENCES RoomReservation(reservationId)  ON DELETE CASCADE ON UPDATE CASCADE
);
SHOW TABLES;
DESC RoomDetail;

#------------------------------------

DROP TABLE IF EXISTS Bill;
CREATE TABLE IF NOT EXISTS Bill(
    billId VARCHAR (10),
    custid VARCHAR (10),
    date DATE ,
    description VARCHAR (50),
    amount DECIMAL (10,2),
    CONSTRAINT PRIMARY KEY (billId),
    CONSTRAINT FOREIGN KEY (custId) REFERENCES Customer (custId) ON DELETE CASCADE ON UPDATE CASCADE
);
SHOW TABLES;
DESC Bill;

#-----------------------------------

DROP TABLE IF EXISTS Department;
CREATE TABLE IF NOT EXISTS Department(
    departmentId VARCHAR (6),
    departmentName VARCHAR (20),
    CONSTRAINT PRIMARY KEY (departmentId)
);
SHOW TABLES;
DESC Department;

INSERT INTO Department VALUES ('D-01','Front Office');
INSERT INTO Department VALUES ('D-02','Housekeeping');
INSERT INTO Department VALUES ('D-03','Security');
INSERT INTO Department VALUES ('D-04','Food and Beverage Service');
INSERT INTO Department VALUES ('D-05','Engineering and Maintenance');
#-----------------------------------


DROP TABLE IF EXISTS Employee;
CREATE TABLE IF NOT EXISTS Employee(
    employeeId VARCHAR (6),
    departmentId VARCHAR (6),
    employeeName VARCHAR(50) NOT NULL DEFAULT 'Unknown',
    dob DATE ,
    nic VARCHAR (15),
    address VARCHAR (100),
    number VARCHAR (15),
    email VARCHAR (50),
    salary DECIMAL (6,2) DEFAULT 0.00,
    joinDate DATE ,
    gender VARCHAR (10),
    CONSTRAINT PRIMARY KEY (employeeId),
    CONSTRAINT FOREIGN KEY (departmentId) REFERENCES Department (departmentId) ON DELETE CASCADE ON UPDATE CASCADE
);
SHOW TABLES;
DESC Employee;

#-----------------------------------------

DROP TABLE IF EXISTS Food;
CREATE TABLE IF NOT EXISTS Food(
    foodId VARCHAR (6),
    description VARCHAR (50),
    unitPrice DECIMAL (6,2) DEFAULT 0.00,
    CONSTRAINT PRIMARY KEY (foodId)
);
SHOW TABLES;
DESC Food;

#-----------------------------------------

DROP TABLE IF EXISTS Orders;
CREATE TABLE IF NOT EXISTS Orders(
    orderId VARCHAR (6),
    date DATE,
    CONSTRAINT PRIMARY KEY (orderId)
);
SHOW TABLES;
DESC Orders;

#----------------------------------------

DROP TABLE IF EXISTS OrderDetail;
CREATE TABLE IF NOT EXISTS OrderDetail(
    orderId VARCHAR (6),
    foodId VARCHAR (6),
    quantity INT,
    price DECIMAL (10,2) DEFAULT 0.00,
    CONSTRAINT PRIMARY KEY (orderid,foodId),
    CONSTRAINT FOREIGN KEY (orderId) REFERENCES Orders(orderId) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY (foodId) REFERENCES Food(foodId) ON DELETE CASCADE ON UPDATE CASCADE
);
SHOW TABLES;
DESC OrderDetail;

#---------------------------------------

DROP TABLE IF EXISTS OrderBill;
CREATE TABLE IF NOT EXISTS OrderBill(
    billId VARCHAR (6),
    orderId VARCHAR (6),
    amount DECIMAL (6,2) DEFAULT 0.00,
    CONSTRAINT PRIMARY KEY (billId),
    CONSTRAINT FOREIGN KEY (orderId) REFERENCES Orders(orderId) ON DELETE CASCADE ON UPDATE CASCADE
);
SHOW TABLES;
DESC OrderBill;

#####################/

DROP TABLE IF EXISTS Users;
CREATE TABLE IF NOT EXISTS Users(
    userId INT NOT NULL AUTO_INCREMENT ,
    userName VARCHAR(20),
    userPassword VARCHAR(20),
    userRole VARCHAR (10),
    CONSTRAINT PRIMARY KEY (userId)
);
SHOW TABLES;
DESC users;

#----------------------------------------
#--------------------------------------


SELECT Employee.employeeId, Department.departmentName,Employee.employeeName
FROM Employee
JOIN Department
ON Employee.departmentId = Department.departmentId
AND employeeId
LIKE 'd%'
ORDER BY Employee.employeeId;


SELECT Customer.custName, Complain.date,Complain.time,Complain.description
FROM Complain
LEFT JOIN Customer
ON Complain.custid = Customer.custId
AND status='Active'
ORDER BY Complain.complainId;

SELECT Complain.complainId, Customer.custName, Complain.date,Complain.time,Complain.description
FROM Complain
JOIN Customer
ON Complain.custId = Customer.custId
AND Complain.status='Active'
ORDER BY Complain.complainId;


SELECT employeeId
FROM Employee
WHERE joinDate>='2020-09-25'
AND dob<='2020-09-25';
SELECT * FROM Room
WHERE NOT EXISTS (
SELECT * FROM RoomDetail
INNER JOIN RoomReservation
ON RoomDetail.reservationId=RoomReservation.reservationId
AND RoomReservation.checkIn<=? AND RoomReservation.checkOut>=?
);

SELECT RoomDetail.roomId
FROM RoomDetail
JOIN RoomReservation
ON RoomDetail.reservationId=RoomReservation.reservationId
AND RoomReservation.checkIn<='2021-09-01'
AND RoomReservation.checkOut>='2021-09-01';

SELECT Room.roomId,Room.roomType
FROM Room
WHERE roomStatus='Active'
AND Room.roomId
NOT IN (
SELECT RoomDetail.roomId
FROM RoomDetail
JOIN RoomReservation
ON RoomDetail.reservationId=RoomReservation.reservationId
AND RoomReservation.checkIn<='2021-9-2' AND RoomReservation.checkOut>='2021-9-2');


 SELECT * FROM Hall WHERE HallStatus='Active' AND hallId NOT IN (SELECT HallId FROM HallReservation WHERE HallReservation.checkIn<='2021-9-2' AND HallReservation.checkOut>='2021-9-2');

SELECT HallReservation.reservationId,Customer.custName,HallReservation.reserveDate,HallReservation.reserveTime
            FROM HallReservation JOIN Customer ON HallReservation.custId=Customer.custId AND HallReservation.status='Pending' AND HallReservation.reservationId LIKE 'HR%';

            SELECT RoomReservation.reservationId,RoomReservation.custId,RoomReservation.reserveDate,
                RoomReservation.reserveTime,RoomReservation.checkIn,RoomReservation.checkOut,RoomReservation.advance,RoomReservation.cost,
                RoomReservation.status,RoomReservation.noOfGuest,Customer.custName FROM RoomReservation JOIN Customer ON RoomReservation.CustId=Customer.custId WHERE  status='Pending';


                SELECT RoomReservation.reservationId,Customer.custTittle,Customer.custName
                ,Customer.custNIC,Customer.custNumber,Customer.custEmail,Customer.custAddress,Customer.custProvince FROM RoomReservation JOIN Customer ON
                RoomReservation.custId=Customer.custId WHERE status='CheckOut';

    SELECT Food.description, SUM(quantity) AS "Total sales"
FROM orderDetail JOIN Food ON OrderDetail.foodId=Food.foodId
GROUP BY food.description ORDER BY food.description DESC LIMIT 1 ;

    SELECT * FROM Room WHERE roomStatus='Active' AND Room.roomId NOT IN
                    (SELECT RoomDetail.roomId FROM RoomDetail JOIN RoomReservation
                    ON RoomDetail.reservationId=RoomReservation.reservationId
                    WHERE (RoomReservation.checkIn<='2021-09-01' AND RoomReservation.checkOut>='2021-09-01')
                    AND(RoomReservation.checkIn<='2021-09-05' AND RoomReservation.checkOut>='2021-09-01'));


                    SELECT RoomDetail.roomId,Room.roomPrice,Room.roomDiscount FROM RoomDetail JOIN Room ON RoomDetail.roomId=room.RoomId
                    WHERE RoomDetail.reservationId='RR-00003';

                    SELECT MealPackage.packageType,MealPackageDetail.price FROM MealPackageDetail JOIN MealPackage
                    ON MealPackage.packageId=MealPackageDetail.packageId WHERE MealPackageDetail.reservationId='RR-00003';

                    SELECT Service.serviceName,ServiceDetail.noOfDay,ServiceDetail.price
                FROM ServiceDetail JOIN Service ON ServiceDetail.serviceId=Service.serviceId WHERE ServiceDetail.reservationId='RR-00003';


                SELECT Food.description, SUM(quantity) AS TotalSales
                FROM orderDetail JOIN Food ON OrderDetail.foodId=Food.foodId
                GROUP BY food.description ORDER BY food.description ASC LIMIT 1;
#------------------------------------------------------------------------------------------------


