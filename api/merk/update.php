<?php
/**
 * Update Merk API
 * Endpoint untuk mengubah nama merk di tabel merk
 * ON UPDATE CASCADE akan otomatis update merk_id di tabel mobil
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
if (empty($data->id) || empty($data->nama_merk)) {
    http_response_code(400);
    echo json_encode([
        "success" => false,
        "message" => "ID dan nama merk harus diisi"
    ]);
    exit();
}

// Cek apakah nama merk baru sudah digunakan merk lain (case-insensitive)
$query_dup = "SELECT id FROM merk WHERE LOWER(nama_merk) = LOWER(:nama_merk) AND id != :id LIMIT 1";
$stmt_dup = $db->prepare($query_dup);
$stmt_dup->bindParam(":nama_merk", $data->nama_merk);
$stmt_dup->bindParam(":id", $data->id);
$stmt_dup->execute();

if ($stmt_dup->rowCount() > 0) {
    http_response_code(400);
    echo json_encode([
        "success" => false,
        "message" => "Merk sudah ada!"
    ]);
    exit();
}

// Update nama merk di tabel merk
$query_update = "UPDATE merk SET nama_merk = :nama_merk WHERE id = :id";
$stmt_update = $db->prepare($query_update);
$stmt_update->bindParam(":nama_merk", $data->nama_merk);
$stmt_update->bindParam(":id", $data->id);

if ($stmt_update->execute()) {
    if ($stmt_update->rowCount() > 0) {
        http_response_code(200);
        echo json_encode([
            "success" => true,
            "message" => "Merk berhasil diperbarui"
        ]);
    } else {
        http_response_code(404);
        echo json_encode([
            "success" => false,
            "message" => "Merk tidak ditemukan"
        ]);
    }
} else {
    http_response_code(500);
    echo json_encode([
        "success" => false,
        "message" => "Gagal memperbarui merk"
    ]);
}
?>