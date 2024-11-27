-- Step 1: Create the Database
CREATE DATABASE motorverse;
USE motorverse;

-- Step 2: Create the Users Table
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(15),
    address TEXT,
    role ENUM('Admin', 'Buyer', 'Seller', 'Renter') NOT NULL
);

-- Step 3: Create the Admin Table
CREATE TABLE Admin (
    admin_id INT PRIMARY KEY,
    dashboard_access BOOLEAN DEFAULT TRUE,
    report_access BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (admin_id) REFERENCES Users(user_id)
);

-- Step 4: Create the Vehicles Table
CREATE TABLE Vehicles (
    vehicle_id INT AUTO_INCREMENT PRIMARY KEY,
    make VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    year INT NOT NULL,
    status ENUM('Available', 'Sold', 'Rented') NOT NULL,
    price DECIMAL(10, 2),
    rental_price DECIMAL(10, 2),
    average_rating DECIMAL(3, 2) DEFAULT 0
);

-- Step 5: Create the Listings Table
CREATE TABLE Listings (
    listing_id INT AUTO_INCREMENT PRIMARY KEY,
    seller_id INT NOT NULL,
    vehicle_id INT NOT NULL,
    price DECIMAL(10, 2), -- For Selling
    rental_price DECIMAL(10, 2), -- For Renting
    description TEXT,
    listing_type ENUM('Selling', 'Renting') NOT NULL,
    FOREIGN KEY (seller_id) REFERENCES Users(user_id),
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id)
);

-- Step 6: Create the Rental Agreements Table
CREATE TABLE RentalAgreements (
    rental_id INT AUTO_INCREMENT PRIMARY KEY,
    renter_id INT NOT NULL,
    vehicle_id INT NOT NULL,
    rental_period VARCHAR(50) NOT NULL,
    rental_cost DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (renter_id) REFERENCES Users(user_id),
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id)
);

-- Step 7: Create the Purchase Agreements Table
CREATE TABLE PurchaseAgreements (
    purchase_id INT AUTO_INCREMENT PRIMARY KEY,
    buyer_id INT NOT NULL,
    vehicle_id INT NOT NULL,
    purchase_date DATE NOT NULL,
    purchase_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (buyer_id) REFERENCES Users(user_id),
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id)
);

-- Step 8: Create the Insurance Table
CREATE TABLE Insurance (
    insurance_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    coverage_details TEXT,
    premium DECIMAL(10, 2),
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id)
);

-- Step 9: Create the Payments Table
CREATE TABLE Payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_method ENUM('Credit Card', 'Debit Card', 'PayPal', 'Bank Transfer') NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- Step 10: Create the Customer Support Tickets Table
CREATE TABLE CustomerSupportTickets (
    support_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    issue_description TEXT NOT NULL,
    status ENUM('Open', 'In Progress', 'Resolved', 'Closed') DEFAULT 'Open',
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- Step 11: Create the Test Drive Requests Table
CREATE TABLE TestDrives (
    test_drive_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    vehicle_id INT NOT NULL,
    schedule_date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id)
);

-- Step 12: Create the Reviews Table
CREATE TABLE Reviews (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    vehicle_id INT NOT NULL,
    comments TEXT,
    rating DECIMAL(2, 1) NOT NULL CHECK (rating BETWEEN 1 AND 5),
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id)
);

-- Step 13: Create the Auto Parts Table
CREATE TABLE AutoParts (
    part_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    vehicle_compatibility TEXT
);

-- End of Script
-- Insert data into Users
INSERT INTO Users (name, email, password, phone_number, address, role) VALUES
('Alice Johnson', 'alice@example.com', 'hashed_pw_1', '1234567890', '123 Main St', 'Admin'),
('Bob Smith', 'bob@example.com', 'hashed_pw_2', '2345678901', '456 Elm St', 'Buyer'),
('Charlie Brown', 'charlie@example.com', 'hashed_pw_3', '3456789012', '789 Oak St', 'Seller'),
('Diana Prince', 'diana@example.com', 'hashed_pw_4', '4567890123', '101 Pine St', 'Renter'),
('Eve Adams', 'eve@example.com', 'hashed_pw_5', '5678901234', '202 Cedar St', 'Admin'),
('Frank White', 'frank@example.com', 'hashed_pw_6', '6789012345', '303 Maple St', 'Buyer'),
('Grace Hopper', 'grace@example.com', 'hashed_pw_7', '7890123456', '404 Birch St', 'Seller'),
('Hank Pym', 'hank@example.com', 'hashed_pw_8', '8901234567', '505 Spruce St', 'Renter'),
('Ivy Green', 'ivy@example.com', 'hashed_pw_9', '9012345678', '606 Fir St', 'Seller'),
('Jack Black', 'jack@example.com', 'hashed_pw_10', '0123456789', '707 Walnut St', 'Buyer');


-- Insert two admins into the Admin table
INSERT INTO Admin (admin_id, dashboard_access, report_access) VALUES
(1, TRUE, TRUE), -- Assuming Alice Johnson (user_id = 1) is an Admin
(5, TRUE, TRUE); -- Assuming Eve Adams (user_id = 5) is an Admin
use motorverse;
select * from Users;


ALTER TABLE Vehicles ADD COLUMN image_path VARCHAR(255);
INSERT INTO Vehicles (make, model, year, status, price, rental_price, average_rating, image_path) VALUES
('Toyota', 'Camry', 2021, 'Available', 30000.00, 100.00, 4.5, 'toyota_camry.jpg'),
('Honda', 'Civic', 2020, 'Sold', 25000.00, NULL, 4.7, 'honda_civic.jpg'),
('Ford', 'Focus', 2019, 'Rented', 20000.00, 80.00, 4.2, 'ford_focus.jpg'),
('Tesla', 'Model 3', 2022, 'Available', 50000.00, 150.00, 4.8, 'tesla_model3.jpg'),
('BMW', 'X5', 2020, 'Available', 45000.00, 140.00, 4.6, 'bmw_x5.jpg'),
('Audi', 'A4', 2018, 'Sold', 35000.00, NULL, 4.4, 'audi_a4.jpg'),
('Mercedes', 'C-Class', 2021, 'Available', 55000.00, 160.00, 4.9, 'mercedes_cclass.jpg'),
('Hyundai', 'Elantra', 2017, 'Available', 18000.00, 70.00, 4.1, 'hyundai_elantra.jpg'),
('Kia', 'Sorento', 2019, 'Rented', 30000.00, 110.00, 4.3, 'kia_sorento.jpg'),
('Chevrolet', 'Malibu', 2021, 'Sold', 28000.00, NULL, 4.5, 'chevrolet_malibu.jpg');

UPDATE Vehicles SET image_path = 'images/toyota_camry_2021.jpg' WHERE make = 'Toyota' AND model = 'Camry' AND year = 2021 and Vehicle_id=1;
UPDATE Vehicles SET image_path = 'images/honda_civic_2020.jpg' WHERE make = 'Honda' AND model = 'Civic' AND year = 2020 and Vehicle_id=2;
UPDATE Vehicles SET image_path = 'images/ford_focus_2019.jpg' WHERE make = 'Ford' AND model = 'Focus' AND year = 2019 and Vehicle_id=3;
UPDATE Vehicles SET image_path = 'images/tesla_model3_2022.jpg' WHERE make = 'Tesla' AND model = 'Model 3' AND year = 2022 and Vehicle_id=4;
UPDATE Vehicles SET image_path = 'images/bmw_x5_2020.jpg' WHERE make = 'BMW' AND model = 'X5' AND year = 2020 and Vehicle_id=5;
UPDATE Vehicles SET image_path = 'images/audi_a4_2018.jpg' WHERE make = 'Audi' AND model = 'A4' AND year = 2018 and Vehicle_id=6;
UPDATE Vehicles SET image_path = 'images/mercedes_cclass_2021.jpg' WHERE make = 'Mercedes' AND model = 'C-Class' AND year = 2021 and Vehicle_id=7;
UPDATE Vehicles SET image_path = 'images/hyundai_elantra_2017.jpg' WHERE make = 'Hyundai' AND model = 'Elantra' AND year = 2017 and Vehicle_id=8;
UPDATE Vehicles SET image_path = 'images/kia_sorento_2019.jpg' WHERE make = 'Kia' AND model = 'Sorento' AND year = 2019 and Vehicle_id=9;
UPDATE Vehicles SET image_path = 'images/chevrolet_malibu_2021.jpg' WHERE make = 'Chevrolet' AND model = 'Malibu' AND year = 2021 and Vehicle_id=10;


-- Insert data into Vehicles
INSERT INTO Vehicles (make, model, year, status, price, rental_price, average_rating) VALUES
('Toyota', 'Camry', 2021, 'Available', 30000.00, 100.00, 4.5),
('Honda', 'Civic', 2020, 'Sold', 25000.00, NULL, 4.7),
('Ford', 'Focus', 2019, 'Rented', 20000.00, 80.00, 4.2),
('Tesla', 'Model 3', 2022, 'Available', 50000.00, 150.00, 4.8),
('BMW', 'X5', 2020, 'Available', 45000.00, 140.00, 4.6),
('Audi', 'A4', 2018, 'Sold', 35000.00, NULL, 4.4),
('Mercedes', 'C-Class', 2021, 'Available', 55000.00, 160.00, 4.9),
('Hyundai', 'Elantra', 2017, 'Available', 18000.00, 70.00, 4.1),
('Kia', 'Sorento', 2019, 'Rented', 30000.00, 110.00, 4.3),
('Chevrolet', 'Malibu', 2021, 'Sold', 28000.00, NULL, 4.5);

select * from Vehicles;
-- Insert data into Listings
INSERT INTO Listings (seller_id, vehicle_id, price, rental_price, description, listing_type) VALUES
(3, 1, 30000.00, NULL, 'Great car, low mileage.', 'Selling'), -- Seller is Charlie Brown
(3, 2, 25000.00, NULL, 'Excellent condition.', 'Selling'), -- Seller is Charlie Brown
(3, 4, NULL, 150.00, 'Available for rent.', 'Renting'), -- Seller is Charlie Brown
(9, 5, 45000.00, NULL, 'Luxury SUV.', 'Selling'), -- Seller is Ivy Green
(7, 7, 55000.00, NULL, 'Premium sedan.', 'Selling'), -- Seller is Grace Hopper
(3, 8, NULL, 70.00, 'Affordable rental.', 'Renting'), -- Seller is Charlie Brown
(9, 9, NULL, 110.00, 'Spacious and comfortable.', 'Renting'), -- Seller is Ivy Green
(7, 3, NULL, 80.00, 'Reliable vehicle for rent.', 'Renting'), -- Seller is Grace Hopper
(3, 6, 35000.00, NULL, 'Sporty sedan.', 'Selling'), -- Seller is Charlie Brown
(7, 10, 28000.00, NULL, 'Family car, well-maintained.', 'Selling'); -- Seller is Grace Hopper


-- Insert data into CustomerSupportTickets
INSERT INTO CustomerSupportTickets (user_id, issue_description, status) VALUES
(1, 'Unable to log in to my account. Password reset not working.', 'Open'), -- Admin Alice Johnson
(2, 'I was charged twice for the same payment. Need assistance.', 'In Progress'), -- Buyer Bob Smith
(4, 'The vehicle I rented is not available at the agreed location.', 'Open'), -- Renter Diana Prince
(5, 'I need to update my email address but cannot find the option.', 'Resolved'), -- Admin Eve Adams
(6, 'The purchased vehicle is not showing in my account dashboard.', 'In Progress'), -- Buyer Frank White
(7, 'Requesting cancellation of my current insurance plan.', 'Open'), -- Seller Grace Hopper
(8, 'Test drive request is pending for over a week. Please review.', 'In Progress'), -- Renter Hank Pym
(9, 'My review for a vehicle was deleted. Can it be restored?', 'Closed'), -- Seller Ivy Green
(3, 'The rental agreement contains incorrect dates.', 'Open'), -- Seller Charlie Brown
(10, 'I received a damaged vehicle upon delivery. Need immediate help.', 'Open'); -- Buyer Jack Black



INSERT INTO TestDrives (user_id, vehicle_id, schedule_date) VALUES
(1, 2, '2024-11-20'),
(2, 3, '2024-11-22'),
(3, 1, '2024-11-25'),
(4, 4, '2024-11-18'),
(5, 5, '2024-11-21'),
(6, 2, '2024-11-19'),
(7, 3, '2024-11-23'),
(8, 1, '2024-11-24'),
(9, 4, '2024-11-26'),
(10, 5, '2024-11-27');


-- Insert data into Reviews
INSERT INTO Reviews (user_id, vehicle_id, comments, rating) VALUES
(2, 2, 'Amazing car, loved it!', 5.0), -- Bob Smith reviewing Honda Civic
(4, 3, 'Good but could be better.', 4.0), -- Diana Prince reviewing Ford Focus
(10, 6, 'Very satisfied!', 4.5), -- Jack Black reviewing Audi A4
(6, 1, 'Smooth ride and great value.', 4.8), -- Frank White reviewing Toyota Camry
(7, 5, 'Luxury at its finest.', 5.0), -- Grace Hopper reviewing BMW X5
(8, 4, 'Tesla never disappoints.', 4.9), -- Hank Pym reviewing Tesla Model 3
(3, 7, 'Exceeded expectations.', 5.0), -- Charlie Brown reviewing Mercedes C-Class
(2, 8, 'Affordable and reliable.', 4.2), -- Bob Smith reviewing Hyundai Elantra
(4, 9, 'Perfect for road trips.', 4.7), -- Diana Prince reviewing Kia Sorento
(10, 10, 'Great family car.', 4.6); -- Jack Black reviewing Chevrolet Malibu

-- Insert data into AutoParts
INSERT INTO AutoParts (name, price, vehicle_compatibility) VALUES
('Brake Pads', 200.00, 'Toyota Camry 2021'), -- Compatible with Toyota Camry
('Engine Oil', 50.00, 'All vehicles'), -- Universal compatibility
('Air Filter', 30.00, 'Hyundai Elantra 2017'), -- Compatible with Hyundai Elantra
('Tires', 400.00, 'Kia Sorento 2019'), -- Compatible with Kia Sorento
('Battery', 150.00, 'Tesla Model 3 2022'), -- Compatible with Tesla Model 3
('Headlights', 120.00, 'Chevrolet Malibu 2021'), -- Compatible with Chevrolet Malibu
('Windshield Wipers', 40.00, 'BMW X5 2020'), -- Compatible with BMW X5
('Fuel Pump', 250.00, 'Ford Focus 2019'), -- Compatible with Ford Focus
('Clutch Plate', 300.00, 'Audi A4 2018'), -- Compatible with Audi A4
('Exhaust System', 500.00, 'Mercedes C-Class 2021'); -- Compatible with Mercedes C-Class


INSERT INTO RentalAgreements (renter_id, vehicle_id, rental_period, rental_cost) VALUES
(4, 3, '1 week', 400.00), -- Diana Prince renting Ford Focus
(8, 9, '2 weeks', 1100.00), -- Hank Pym renting Kia Sorento
(4, 1, '3 days', 200.00), -- Diana Prince renting Toyota Camry
(8, 6, '1 month', 1200.00), -- Hank Pym renting Audi A4
(8, 3, '10 days', 500.00), -- Hank Pym renting Ford Focus again
(4, 7, '5 days', 450.00), -- Diana Prince renting Mercedes C-Class
(8, 8, '2 weeks', 600.00), -- Hank Pym renting Hyundai Elantra
(4, 5, '1 day', 100.00), -- Diana Prince renting BMW X5
(8, 4, '1 week', 700.00), -- Hank Pym renting Tesla Model 3
(4, 9, '3 days', 300.00); -- Diana Prince renting Kia Sorento again

ALTER TABLE RentalAgreements
ADD COLUMN insurance_id INT,
ADD COLUMN total_premium FLOAT;
ALTER TABLE RentalAgreements
ADD COLUMN seller_id INT;

-- Add a foreign key to link the Insurance table
ALTER TABLE RentalAgreements
ADD CONSTRAINT fk_insurance
FOREIGN KEY (insurance_id) REFERENCES Insurance(insurance_id);

select * from rentalagreements;

INSERT INTO PurchaseAgreements (buyer_id, vehicle_id, purchase_date, purchase_price) VALUES
(2, 2, '2024-01-10', 25000.00), -- Bob Smith bought Honda Civic
(10, 6, '2024-01-15', 35000.00), -- Jack Black bought Audi A4
(6, 10, '2024-02-01', 28000.00), -- Frank White bought Chevrolet Malibu
(2, 8, '2024-02-20', 18000.00), -- Bob Smith bought Hyundai Elantra
(10, 5, '2024-03-05', 45000.00), -- Jack Black bought BMW X5
(6, 9, '2024-03-15', 30000.00), -- Frank White bought Kia Sorento
(10, 7, '2024-04-10', 55000.00), -- Jack Black bought Mercedes C-Class
(6, 3, '2024-04-20', 20000.00), -- Frank White bought Ford Focus
(10, 4, '2024-05-05', 50000.00), -- Jack Black bought Tesla Model 3
(2, 1, '2024-05-15', 30000.00); -- Bob Smith bought Toyota Camry

select * from purchaseagreements;

ALTER TABLE purchaseagreements
ADD COLUMN insurance_id INT,
ADD COLUMN total_premium FLOAT;
ALTER TABLE purchaseagreements
ADD COLUMN seller_id INT;

-- Add a foreign key to link the Insurance table
ALTER TABLE purchaseagreements
ADD CONSTRAINT fk_insurance2
FOREIGN KEY (insurance_id) REFERENCES Insurance(insurance_id);



INSERT INTO Insurance (vehicle_id, coverage_details, premium) VALUES
(1, 'Full coverage with roadside assistance', 1200.00), -- Toyota Camry
(2, 'Liability-only insurance', 800.00), -- Honda Civic
(3, 'Comprehensive coverage for theft and collision', 1500.00), -- Ford Focus
(4, 'Full coverage with zero depreciation', 2000.00), -- Tesla Model 3
(5, 'Premium coverage with high liability limits', 1800.00), -- BMW X5
(6, 'Basic coverage with limited benefits', 900.00), -- Audi A4
(7, 'Comprehensive with glass and windscreen coverage', 2200.00), -- Mercedes C-Class
(8, 'Liability-only plan', 600.00), -- Hyundai Elantra
(9, 'Full coverage with discounted premiums for low mileage', 1700.00), -- Kia Sorento
(10, 'Basic coverage with minimum liability', 1000.00); -- Chevrolet Malibu

Drop table Insurance;
CREATE TABLE Insurance (
    insurance_id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    coverage TEXT NOT NULL,
    premium_rate FLOAT NOT NULL
);
INSERT INTO Insurance (type, coverage, premium_rate) VALUES
('Collision', 'Covers damage to your car caused by collisions with another vehicle or object.', 10.0),
('PIP', 'Personal Injury Protection covers medical expenses for the driver and passengers.', 7.0),
('Engine Protection', 'Covers damage to the engine due to mechanical or water ingress issues.', 5.0);
INSERT INTO Insurance (type, coverage, premium_rate) VALUES('None','None',0);



INSERT INTO Payments (user_id, amount, payment_method, payment_date) VALUES
(2, 25000.00, 'Credit Card', '2024-01-02 10:30:00'), -- Bob Smith for Honda Civic
(10, 35000.00, 'PayPal', '2024-01-16 15:45:00'), -- Jack Black for Audi A4
(6, 28000.00, 'Bank Transfer', '2024-02-02 08:15:00'), -- Frank White for Chevrolet Malibu
(10, 18000.00, 'Debit Card', '2024-02-21 12:00:00'), -- Jack Black for Hyundai Elantra
(2, 45000.00, 'Credit Card', '2024-03-06 14:50:00'), -- Bob Smith for BMW X5
(6, 30000.00, 'PayPal', '2024-03-16 11:30:00'), -- Frank White for Kia Sorento
(10, 55000.00, 'Bank Transfer', '2024-04-11 09:45:00'), -- Jack Black for Mercedes C-Class
(6, 20000.00, 'Debit Card', '2024-04-21 10:15:00'), -- Frank White for Ford Focus
(10, 50000.00, 'Credit Card', '2024-05-06 13:20:00'), -- Jack Black for Tesla Model 3
(2, 30000.00, 'PayPal', '2024-05-16 16:40:00'); -- Bob Smith for Toyota Camry


#new addition:
ALTER TABLE Listings ADD COLUMN image_path VARCHAR(255);
Alter TABLE rentalagreements ADD COLUMN return_status VARCHAR(255) default 'Not Returned';
select * from rentalagreements;

CREATE TABLE AutoPartPurchases (
    purchase_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    part_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    total_price DECIMAL(10, 2) NOT NULL,
    purchase_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (part_id) REFERENCES AutoParts(part_id) ON DELETE CASCADE
);

ALTER TABLE autoparts ADD COLUMN quantity INT DEFAULT 10;














