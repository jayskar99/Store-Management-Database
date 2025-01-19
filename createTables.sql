-- Create Suppliers table
CREATE TABLE Suppliers (
    supplierID INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255)
);

-- Create SupplierPhones table with foreign key reference to Suppliers table
CREATE TABLE SupplierPhones (
    number VARCHAR(20),
    supplierID INT,
    FOREIGN KEY (supplierID) REFERENCES Suppliers(supplierID)
);

-- Create Customers table
CREATE TABLE Customers (
    customerID INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    address VARCHAR(255)
);

-- Create CustomerPhones table with foreign key reference to Customers table
CREATE TABLE CustomerPhones (
    number VARCHAR(20),
    customerID INT,
    FOREIGN KEY (customerID) REFERENCES Customers(customerID)
);

-- Create Products table with foreign key reference to Suppliers table
CREATE TABLE Products (
    productID INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    price DECIMAL(10, 2),
    quantity INT,
    supplierID INT,
    FOREIGN KEY (supplierID) REFERENCES Suppliers(supplierID)
);

-- Create Orders table with foreign key reference to Customers table
CREATE TABLE Orders (
    orderID INT AUTO_INCREMENT PRIMARY KEY,
    dateTime DATETIME,
    customerID INT,
    totalPrice DECIMAL(10, 2),
    FOREIGN KEY (customerID) REFERENCES Customers(customerID)
);

-- Create OrderHistory table with foreign key reference to Orders and Products tables
CREATE TABLE OrderHistory (
    orderHistoryID INT AUTO_INCREMENT PRIMARY KEY,
    orderID INT,
    quantity INT,
    productID INT,
    FOREIGN KEY (orderID) REFERENCES Orders(orderID),
    FOREIGN KEY (productID) REFERENCES Products(productID)
);

-- Create Payments table with foreign key reference to Suppliers table
CREATE TABLE Payments (
    paymentID INT AUTO_INCREMENT PRIMARY KEY,
    dateTime DATETIME,
    supplierID INT,
    totalPrice DECIMAL(10, 2),
    FOREIGN KEY (supplierID) REFERENCES Suppliers(supplierID)
);

-- Create PaymentHistory table with foreign key reference to Payments and Products tables
CREATE TABLE PaymentHistory (
    paymentHistoryID INT AUTO_INCREMENT PRIMARY KEY,
    paymentID INT,
    quantity INT,
    productID INT,
    FOREIGN KEY (paymentID) REFERENCES Payments(paymentID),
    FOREIGN KEY (productID) REFERENCES Products(productID)
);
