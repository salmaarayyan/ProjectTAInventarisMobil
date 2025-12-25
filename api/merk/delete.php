<?php
/**
 * Delete Merk API
 * Endpoint untuk menghapus merk dari tabel merk
 * CASCADE DELETE akan otomatis menghapus semua mobil dengan merk tersebut
 */

header("Content-Type: application/json");
require_once '../../config/database.php';
require_once '../../utils/jwt_helper.php';

// Only accept DELETE method
if ($_SERVER['REQUEST_METHOD'] !== 'DELETE') {
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

// Get DELETE parameter
if (empty($_GET['id'])) {
    http_response_code(400);
    echo json_encode([
        "success" => false,
        "message" => "Parameter id harus diisi"
    ]);
    exit();
}

$id = $_GET['id'];

// Hapus merk dari tabel merk (CASCADE akan otomatis hapus mobil & stok terkait)
$query_delete = "DELETE FROM merk WHERE id = :id";
$stmt_delete = $db->prepare($query_delete);
$stmt_delete->bindParam(":id", $id);

if ($stmt_delete->execute()) {
    if ($stmt_delete->rowCount() > 0) {
        http_response_code(200);
        echo json_encode([
            "success" => true,
            "message" => "Merk berhasil dihapus"
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
        "message" => "Gagal menghapus merk"
    ]);
}
?>