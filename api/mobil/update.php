<?php
/**
 * Update Mobil API
 * Endpoint untuk mengubah data mobil dengan merk_id
 */

header("Content-Type: application/json");
require_once '../../config/database.php';
require_once '../../utils/jwt_helper.php';

// Only accept PUT method
if ($_SERVER['REQUEST_METHOD'] !== 'PUT') {
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

// Get PUT data
$data = json_decode(file_get_contents("php://input"));

// Validasi input
if (empty($data->id) || empty($data->nama_mobil) || empty($data->merk_id) || 
    empty($data->tipe) || empty($data->tahun) || empty($data->harga) || empty($data->warna)) {
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

// Cek duplikasi mobil (exclude mobil yang sedang diedit) - REQ-8.4
$query_dup = "SELECT id FROM mobil WHERE LOWER(nama_mobil) = LOWER(:nama_mobil) 
              AND merk_id = :merk_id AND LOWER(warna) = LOWER(:warna) 
              AND tahun = :tahun AND id != :id LIMIT 1";
$stmt_dup = $db->prepare($query_dup);
$stmt_dup->bindParam(":nama_mobil", $data->nama_mobil);
$stmt_dup->bindParam(":merk_id", $data->merk_id);
$stmt_dup->bindParam(":warna", $data->warna);
$stmt_dup->bindParam(":tahun", $data->tahun);
$stmt_dup->bindParam(":id", $data->id);
$stmt_dup->execute();

if ($stmt_dup->rowCount() > 0) {
    http_response_code(400);
    echo json_encode([
        "success" => false,
        "message" => "Mobil sudah ada!"
    ]);
    exit();
}

// Update mobil
$query_update = "UPDATE mobil SET nama_mobil = :nama_mobil, merk_id = :merk_id, tipe = :tipe, 
                 tahun = :tahun, harga = :harga, warna = :warna WHERE id = :id";
$stmt_update = $db->prepare($query_update);
$stmt_update->bindParam(":nama_mobil", $data->nama_mobil);
$stmt_update->bindParam(":merk_id", $data->merk_id);
$stmt_update->bindParam(":tipe", $data->tipe);
$stmt_update->bindParam(":tahun", $data->tahun);
$stmt_update->bindParam(":harga", $data->harga);
$stmt_update->bindParam(":warna", $data->warna);
$stmt_update->bindParam(":id", $data->id);

if ($stmt_update->execute()) {
    if ($stmt_update->rowCount() > 0) {
        http_response_code(200);
        echo json_encode([
            "success" => true,
            "message" => "Mobil berhasil diperbarui"
        ]);
    } else {
        http_response_code(404);
        echo json_encode([
            "success" => false,
            "message" => "Mobil tidak ditemukan atau tidak ada perubahan"
        ]);
    }
} else {
    http_response_code(500);
    echo json_encode([
        "success" => false,
        "message" => "Gagal memperbarui mobil"
    ]);
}
?>