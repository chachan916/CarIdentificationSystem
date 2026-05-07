-- ==========================================
-- 1. CUSTOMER MODULE 
-- ==========================================

-- Table to store owner/customer details
CREATE TABLE Customer (
    customer_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address TEXT,
    phone VARCHAR(20),
    email VARCHAR(100)
);

-- ==========================================
-- 2. WORKSHOP MODULE 
-- ==========================================

-- Table to store vehicle registration details
CREATE TABLE Vehicle (
    vehicle_id SERIAL PRIMARY KEY,
    registration_number VARCHAR(50) UNIQUE NOT NULL,
    make VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    year INTEGER NOT NULL,
    owner_id INTEGER REFERENCES Customer(customer_id) ON DELETE SET NULL
);

-- Table to store service information for vehicles
CREATE TABLE ServiceRecord (
    service_id SERIAL PRIMARY KEY,
    vehicle_id INTEGER REFERENCES Vehicle(vehicle_id) ON DELETE CASCADE,
    service_date DATE NOT NULL DEFAULT CURRENT_DATE,
    service_type VARCHAR(100),
    description TEXT,
    cost DECIMAL(10, 2)
);

-- ==========================================
-- 3. CUSTOMER MODULE (Interactions)
-- ==========================================

-- Table for owner queries regarding vehicle condition
CREATE TABLE CustomerQuery (
    query_id SERIAL PRIMARY KEY,
    customer_id INTEGER REFERENCES Customer(customer_id) ON DELETE CASCADE,
    vehicle_id INTEGER REFERENCES Vehicle(vehicle_id) ON DELETE CASCADE,
    query_date DATE NOT NULL DEFAULT CURRENT_DATE,
    query_text TEXT NOT NULL,
    response_text TEXT
);

-- ==========================================
-- 4. POLICE MODULE 
-- ==========================================

-- Table to track police-related records (Accidents, Theft, etc.)
CREATE TABLE PoliceReport (
    report_id SERIAL PRIMARY KEY,
    vehicle_id INTEGER REFERENCES Vehicle(vehicle_id) ON DELETE CASCADE,
    report_date DATE NOT NULL DEFAULT CURRENT_DATE,
    report_type VARCHAR(100), -- e.g., Accident, Theft
    description TEXT,
    officer_name VARCHAR(255)
);

-- Table to track traffic violations and fines
CREATE TABLE Violation (
    violation_id SERIAL PRIMARY KEY,
    vehicle_id INTEGER REFERENCES Vehicle(vehicle_id) ON DELETE CASCADE,
    violation_date DATE NOT NULL DEFAULT CURRENT_DATE,
    violation_type VARCHAR(100),
    fine_amount DECIMAL(10, 2),
    status VARCHAR(20) CHECK (status IN ('Paid', 'Unpaid')) DEFAULT 'Unpaid'
);

-- ==========================================
-- INDEXES FOR PERFORMANCE
-- ==========================================
CREATE INDEX idx_vehicle_reg ON Vehicle(registration_number);
CREATE INDEX idx_service_vehicle ON ServiceRecord(vehicle_id);
CREATE INDEX idx_police_vehicle ON PoliceReport(vehicle_id);