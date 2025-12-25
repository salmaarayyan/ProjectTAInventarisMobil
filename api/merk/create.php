<?php
/**
 * Create Merk API
 * Endpoint untuk menambah merk baru ke tabel merk
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

// Validasi input - TERIMA BAIK "merk" MAUPUN "nama_merk"
$nama_merk = !empty($data->merk) ? $data->merk : (!empty($data->nama_merk) ? $data->nama_merk : null);

if (empty($nama_merk)) {
    http_response_code(400);
    echo json_encode([
        "success" => false,
        "message" => "Nama merk harus diisi"
    ]);
    exit();
}

// Cek duplikasi merk di tabel merk (case-insensitive)
$query_dup = "SELECT id FROM merk WHERE LOWER(nama_merk) = LOWER(:nama_merk) LIMIT 1";
$stmt_dup = $db->prepare($query_dup);
$stmt_dup->bindParam(":nama_merk", $nama_merk);
$stmt_dup->execute();

if ($stmt_dup->rowCount() > 0) {
    // Merk sudah ada
    http_response_code(400);
    echo json_encode([
        "success" => false,
        "message" => "Merk sudah ada!"
    ]);
    exit();
}

// Insert merk baru ke tabel merk
$query_insert = "INSERT INTO merk (nama_merk) VALUES (:nama_merk)";
$stmt_insert = $db->prepare($query_insert);
$stmt_insert->bindParam(":nama_merk", $nama_merk);

if ($stmt_insert->execute()) {
    http_response_code(200);
    echo json_encode([
        "success" => true,
        "message" => "Merk berhasil ditambahkan",
        "id" => (int)$db->lastInsertId()
    ]);
} else {
    http_response_code(500);
    echo json_encode([
        "success" => false,
        "message" => "Gagal menambahkan merk"
    ]);
}
?>