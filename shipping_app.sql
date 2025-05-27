CREATE DATABASE IF NOT EXISTS shipping_app DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE shipping_app;

select * from users;
select * from products;
select * from packages;
select * from package_products;

   SELECT CONSTRAINT_NAME, CHECK_CLAUSE
   FROM INFORMATION_SCHEMA.CHECK_CONSTRAINTS
   WHERE CONSTRAINT_NAME = 'products_chk_2';

-- Xóa bảng liên kết trước (do có foreign key đến các bảng khác)
DROP TABLE IF EXISTS package_products;

-- Xóa bảng packages (phụ thuộc users)
DROP TABLE IF EXISTS packages;

-- Xóa bảng products (phụ thuộc users)
DROP TABLE IF EXISTS products;

-- Xóa bảng users (gốc)
DROP TABLE IF EXISTS users;


-- 1.1. Bảng users
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tel VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255),
    name VARCHAR(255),
    address VARCHAR(500),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status TINYINT(1) NOT NULL DEFAULT 0 CHECK (status IN (0, 1))
);

ALTER TABLE users
ADD COLUMN role ENUM('USER', 'ADMIN', 'SHIPPER') NOT NULL DEFAULT 'USER';


-- 1.2. Bảng products
CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    barcode VARCHAR(100) NOT NULL,
    image VARCHAR(500),
    weight DECIMAL(10,2) NOT NULL DEFAULT 0,
    height DECIMAL(10,2) NOT NULL DEFAULT 0,
    length DECIMAL(10,2) NOT NULL DEFAULT 0,
    width DECIMAL(10,2) NOT NULL DEFAULT 0,
    price DECIMAL(15,2) NOT NULL DEFAULT 0,
    users_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 CHECK (is_deleted IN (0, 1)),
    status TINYINT(1) NOT NULL DEFAULT 0 CHECK (status IN (0, 1)),
    FOREIGN KEY (users_id) REFERENCES users(id)
);

-- 1.3. Bảng packages
CREATE TABLE packages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pick_address VARCHAR(500),
    customer_name VARCHAR(255) NOT NULL,
    customer_address VARCHAR(500) NOT NULL,
    customer_tel VARCHAR(20) NOT NULL,
    pick_money DECIMAL(15,2),
    value DECIMAL(15,2),
    ship_money DECIMAL(15,2),
    extra_fee DECIMAL(15,2),
    weight DECIMAL(10,2),
    users_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status INT NOT NULL DEFAULT 0 CHECK (status IN (0, 3, 5, 7, 11, 14, 17, -1, 20)),
    FOREIGN KEY (users_id) REFERENCES users(id)
);

-- 1.4. Bảng package_products
CREATE TABLE package_products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    package_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    FOREIGN KEY (package_id) REFERENCES packages(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

ALTER TABLE package_products
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

-- insert users
INSERT INTO users (tel, password, name, address, status, role)
VALUES
('0912345670', 'pass1', 'Nguyen Van A', 'Hà Nội', 1, 'ADMIN'),
('0912345671', 'pass2', 'Tran Thi B', 'Hồ Chí Minh', 1, 'USER'),
('0912345672', 'pass3', 'Le Van C', 'Đà Nẵng', 0, 'SHIPPER'),
('0912345673', 'pass4', 'Pham Thi D', 'Cần Thơ', 0, 'USER'),
('0912345674', 'pass5', 'Vo Van E', 'Hải Phòng', 1, 'ADMIN'),
('0912345675', 'pass6', 'Hoang Thi F', 'Huế', 1, 'USER'),
('0912345676', 'pass7', 'Dang Van G', 'Biên Hòa', 0, 'SHIPPER'),
('0912345677', 'pass8', 'Bui Thi H', 'Vũng Tàu', 1, 'USER'),
('0912345678', 'pass9', 'Ngo Van I', 'Nam Định', 0, 'USER'),
('0912345679', 'pass10', 'Do Thi J', 'Nghệ An', 1, 'ADMIN');


-- insert products
INSERT INTO products (name, barcode, image, weight, height, length, width, price, users_id, status)
VALUES
('Áo thun', 'BC001', 'ao-thun.jpg', 0.5, 10, 30, 25, 150000, 1, 1),
('Quần jean', 'BC002', 'quan-jean.jpg', 0.8, 15, 35, 30, 250000, 2, 1),
('Balo', 'BC003', 'balo.jpg', 1.2, 40, 30, 15, 350000, 3, 1),
('Giày thể thao', 'BC004', 'giay.jpg', 1.0, 20, 30, 12, 500000, 4, 1),
('Mũ bảo hiểm', 'BC005', 'mu.jpg', 0.9, 25, 25, 25, 200000, 5, 0),
('Sách lập trình', 'BC006', 'sach.jpg', 0.4, 3, 25, 20, 100000, 1, 1),
('Tai nghe', 'BC007', 'tainghe.jpg', 0.2, 5, 10, 10, 300000, 2, 1),
('Bình nước', 'BC008', 'binhnuoc.jpg', 0.6, 25, 8, 8, 120000, 3, 0),
('Đèn pin', 'BC009', 'denpin.jpg', 0.3, 12, 5, 5, 80000, 4, 1),
('Chuột máy tính', 'BC010', 'chuot.jpg', 0.25, 8, 12, 7, 250000, 5, 1);

-- insert packages
INSERT INTO packages (pick_address, customer_name, customer_address, customer_tel, pick_money, value, ship_money, extra_fee, weight, users_id, status)
VALUES
('Kho Hà Nội', 'Nguyen A', 'Hà Nội', '0987654321', 100000, 200000, 30000, 10000, 2.2, 1, 0),
('Kho Sài Gòn', 'Tran B', 'Hồ Chí Minh', '0987654322', 150000, 250000, 40000, 0, 1.8, 2, 3),
('Kho Đà Nẵng', 'Le C', 'Đà Nẵng', '0987654323', 80000, 180000, 25000, 10000, 2.3, 3, 5),
('Kho Cần Thơ', 'Pham D', 'Cần Thơ', '0987654324', 0, 300000, 35000, 0, 1.5, 4, 7),
('Kho Huế', 'Vo E', 'Huế', '0987654325', 120000, 220000, 28000, 0, 1.9, 5, 11),
('Kho Hải Phòng', 'Hoang F', 'Hải Phòng', '0987654326', 200000, 350000, 45000, 10000, 2.5, 1, 14),
('Kho Vũng Tàu', 'Dang G', 'Vũng Tàu', '0987654327', 90000, 190000, 22000, 0, 1.6, 2, 17),
('Kho Biên Hòa', 'Bui H', 'Biên Hòa', '0987654328', 0, 160000, 18000, 0, 1.4, 3, -1),
('Kho Nam Định', 'Ngo I', 'Nam Định', '0987654329', 70000, 170000, 26000, 10000, 2.1, 4, 20),
('Kho Nghệ An', 'Do J', 'Nghệ An', '0987654330', 110000, 210000, 29000, 0, 1.7, 5, 0);

-- insert package_product
INSERT INTO package_products (package_id, product_id, quantity)
VALUES
(1, 1, 2),
(1, 6, 1),
(2, 2, 1),
(3, 3, 1),
(4, 4, 1),
(5, 5, 1),
(6, 7, 2),
(7, 8, 1),
(8, 9, 3),
(9, 10, 1);

SELECT count(*)
FROM package_products pp
JOIN packages p ON pp.package_id = p.id
WHERE pp.product_id = 3 AND p.status IN (5, 7, 11);






