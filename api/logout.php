<?php
/**
 * Logout API
 * Endpoint untuk logout admin (hapus token dari database)
 */

header("Content-Type: application/json");
require_once '../config/database.php';
require_once '../utils/jwt_helper.php';

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

// Hapus token dari database
$query_delete = "DELETE FROM token WHERE token = :token";
$stmt_delete = $db->prepare($query_delete);
$stmt_delete->bindParam(":token", $token);

if ($stmt_delete->execute()) {
    http_response_code(200);
    echo json_encode([
        "success" => true,
        "message" => "Logout berhasil"
    ]);
} else {
    http_response_code(500);
    echo json_encode([
        "success" => false,
        "message" => "Gagal logout"
    ]);
}
?>