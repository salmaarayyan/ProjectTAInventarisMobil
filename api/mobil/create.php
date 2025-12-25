<?php
/**
 * Create Mobil API
 * Endpoint untuk menambah mobil baru dengan merk_id sebagai FK
 */

header("Content-Type: application/json");
require_once '../../config/database.php';
require_once '../../utils/jwt_helper.php';

// Only accept POST method
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode(["success" => false, "message" => "Method not allowed"]);
    exit();
}

// Validasi token
$token = JWTHelper::getBearerToken();
if (!$token) {
    http_response_code(401);
    echo json_encode(["success" => false, "message" => "Token tidak ditemukan"]);
    exit();
}

$validation = JWTHelper::validateToken($token);
if (!$validation['valid']) {
    http_response_code(401);
    echo json_encode(["success" => false, "message" => "Token tidak valid"]);
    exit();
}

// Koneksi database
$database = new Database();
$db = $database->getConnection();

// Cek token di database
$query_check = "SELECT id FROM token WHERE token = :token LIMIT 1";
$stmt_check = $db->prepare($query_check);
$stmt_check->bindParam(":token", $token);
$stmt_check->execute();

if ($stmt_check->rowCount() == 0) {
    http_response_code(401);
    echo json_encode(["success" => false, "message" => "Token tidak valid"]);
    exit();
}

// Get POST data
$data = json_decode(file_get_contents("php://input"));

// Validasi input
if (empty($data->nama_mobil) || empty($data->merk_id) || empty($data->tipe) || 
    empty($data->tahun) || empty($data->harga) || empty($data->warna)) {
    http_response_code(400);
    echo json_encode([
        "success" => false,
        "message" => "Semua field harus diisi"
    ]);
    exit();
}

// Validasi tahun
if ($data->tahun < 2000) {
    http_response_code(400);
    echo json_encode([
        "success" => false,
        "message" => "Tahun mobil minimal 2000"
    ]);
    exit();
}

// Validasi harga
if ($data->harga <= 0) {
    http_response_code(400);
    echo json_encode([
        "success" => false,
        "message" => "Harga harus lebih dari 0"
    ]);
    exit();
}

// Validasi stok awal
$stok_awal = isset($data->jumlah_stok) ? $data->jumlah_stok : 0;
if ($stok_awal < 0) {
    http_response_code(400);
    echo json_encode([
        "success" => false,
        "message" => "Stok tidak boleh negatif"
    ]);
    exit();
}

// Cek apakah merk_id ada di tabel merk
$query_merk = "SELECT id FROM merk WHERE id = :merk_id LIMIT 1";
$stmt_merk = $db->prepare($query_merk);
$stmt_merk->bindParam(":merk_id", $data->merk_id);
$stmt_merk->execute();

if ($stmt_merk->rowCount() == 0) {
    http_response_code(400);
    echo json_encode([
        "success" => false,
        "message" => "Merk tidak ditemukan"
    ]);
    exit();
}

// Cek duplikasi mobil (nama + merk_id + warna + tahun) - REQ-6.5
$query_dup = "SELECT id FROM mobil WHERE LOWER(nama_mobil) = LOWER(:nama_mobil) 
              AND merk_id = :merk_id AND LOWER(warna) = LOWER(:warna) AND tahun = :tahun LIMIT 1";
$stmt_dup = $db->prepare($query_dup);
$stmt_dup->bindParam(":nama_mobil", $data->nama_mobil);
$stmt_dup->bindParam(":merk_id", $data->merk_id);
$stmt_dup->bindParam(":warna", $data->warna);
$stmt_dup->bindParam(":tahun", $data->tahun);
$stmt_dup->execute();

if ($stmt_dup->rowCount() > 0) {
    http_response_code(400);
    echo json_encode([
        "success" => false,
        "message" => "Mobil sudah ada!"
    ]);
    exit();
}

try {
    // Begin transaction
    $db->beginTransaction();
    
    // Insert mobil dengan merk_id
    $query_mobil = "INSERT INTO mobil (nama_mobil, merk_id, tipe, tahun, harga, warna) 
                    VALUES (:nama_mobil, :merk_id, :tipe, :tahun, :harga, :warna)";
    $stmt_mobil = $db->prepare($query_mobil);
    $stmt_mobil->bindParam(":nama_mobil", $data->nama_mobil);
    $stmt_mobil->bindParam(":merk_id", $data->merk_id);
    $stmt_mobil->bindParam(":tipe", $data->tipe);
    $stmt_mobil->bindParam(":tahun", $data->tahun);
    $stmt_mobil->bindParam(":harga", $data->harga);
    $stmt_mobil->bindParam(":warna", $data->warna);
    $stmt_mobil->execute();
    
    // Get last insert ID
    $mobil_id = $db->lastInsertId();
    
    // Insert stok
    $query_stok = "INSERT INTO stok (mobil_id, jumlah_stok) VALUES (:mobil_id, :jumlah_stok)";
    $stmt_stok = $db->prepare($query_stok);
    $stmt_stok->bindParam(":mobil_id", $mobil_id);
    $stmt_stok->bindParam(":jumlah_stok", $stok_awal);
    $stmt_stok->execute();
    
    // Commit transaction
    $db->commit();
    
    http_response_code(200);
    echo json_encode([
        "success" => true,
        "message" => "Mobil berhasil ditambahkan",
        "id" => (int)$mobil_id
    ]);
    
} catch (Exception $e) {
    // Rollback transaction jika ada error
    $db->rollBack();
    http_response_code(500);
    echo json_encode([
        "success" => false,
        "message" => "Gagal menambahkan mobil: " . $e->getMessage()
    ]);
}
?>