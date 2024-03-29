CREATE TABLE IF NOT EXISTS Users (
    user_id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255),
    chat_id BIGINT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    uid VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS Roles (
    role_id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL,
    description VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS Users_roles (
    user_role_id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id INT NOT NULL REFERENCES Users(user_id) ON DELETE CASCADE,
    role_id INT NOT NULL REFERENCES Roles(role_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Addresses (
    address_id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    city VARCHAR(50) NOT NULL,
    street VARCHAR(255) NOT NULL,
    house_number INT NOT NULL
);

CREATE TABLE IF NOT EXISTS Vendors (
    vendor_id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    company_name VARCHAR(255) NOT NULL,
    phone_support_service VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS Stations (
    station_id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    station_name VARCHAR(50) NOT NULL,
    login VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    manager_id INT REFERENCES Users(user_id) ON DELETE SET NULL,
    vendor_id INT REFERENCES Vendors(vendor_id) ON DELETE SET NULL,
    address_id INT REFERENCES Addresses(address_id) ON DELETE SET NULL,
    station_state VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS Charge_sessions (
    session_id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id INT NOT NULL REFERENCES Users(user_id) ON DELETE SET NULL,
    station_id INT NOT NULL REFERENCES Stations(station_id) ON DELETE SET NULL,
    start_time TIMESTAMP NOT NULL,
    stop_time TIMESTAMP,
    stop_reason VARCHAR(20)
);
