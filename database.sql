-- Database: showroom_db
CREATE DATABASE IF NOT EXISTS showroom_db;
USE showroom_db;

-- Tabel: user
CREATE TABLE user (
    id INT(11) NOT NULL AUTO_INCREMENT,
    nama_lengkap VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabel: token
CREATE TABLE token (
    id INT(11) NOT NULL AUTO_INCREMENT,
    user_id INT(11) NOT NULL,
    token TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expired_at TIMESTAMP NULL DEFAULT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabel: merk
CREATE TABLE merk (
    id INT(11) NOT NULL AUTO_INCREMENT,
    nama_merk VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabel: mobil
CREATE TABLE mobil (
    id INT(11) NOT NULL AUTO_INCREMENT,
    nama_mobil VARCHAR(100) NOT NULL,
    merk_id INT(11) NOT NULL,
    tipe VARCHAR(50) NOT NULL,
    tahun INT(4) NOT NULL,
    harga DECIMAL(15,2) NOT NULL,
    warna VARCHAR(30) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (merk_id) REFERENCES merk(id) ON DELETE CASCADE,
    UNIQUE KEY unique_mobil (nama_mobil, merk_id, warna, tahun)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabel: stok
CREATE TABLE stok (
    id INT(11) NOT NULL AUTO_INCREMENT,
    mobil_id INT(11) NOT NULL,
    jumlah_stok INT(11) NOT NULL DEFAULT 0,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (mobil_id) REFERENCES mobil(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insert default user (admin)
INSERT INTO user (nama_lengkap, email, password) VALUES 
('Admin Showroom', 'admin1@showroom.com', '$2y$10$qTD7Ij3twSo.5SoEptlvyemyt.yTYrGDCUxUuX2AHMbCeigvcyNEe');
-- Password: admin123 (hashed dengan bcrypt)