<?php
/**
 * Kurangi Stok API
 * Endpoint untuk mengurangi stok mobil (-1)
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
if (empty($data->mobil_id)) {
    http_response_code(400);
    echo json_encode([
        "success" => false,
        "message" => "mobil_id harus diisi"
    ]);
    exit();
}

// Cek stok saat ini
$query_stok = "SELECT jumlah_stok FROM stok WHERE mobil_id = :mobil_id LIMIT 1";
$stmt_stok = $db->prepare($query_stok);
$stmt_stok->bindParam(":mobil_id", $data->mobil_id);
$stmt_stok->execute();

if ($stmt_stok->rowCount() == 0) {
    http_response_code(404);
    echo json_encode([
        "success" => false,
        "message" => "Stok tidak ditemukan"
    ]);
    exit();
}

$row = $stmt_stok->fetch(PDO::FETCH_ASSOC);
$stok_sekarang = (int)$row['jumlah_stok'];

// Validasi stok > 0
if ($stok_sekarang <= 0) {
    http_response_code(400);
    echo json_encode([
        "success" => false,
        "message" => "Stok sudah 0, tidak bisa dikurangi"
    ]);
    exit();
}

// Update stok -1
$query_update = "UPDATE stok SET jumlah_stok = jumlah_stok - 1 WHERE mobil_id = :mobil_id";
$stmt_update = $db->prepare($query_update);
$stmt_update->bindParam(":mobil_id", $data->mobil_id);

if ($stmt_update->execute()) {
    // Ambil stok terbaru
    $stmt_stok->execute();
    $row = $stmt_stok->fetch(PDO::FETCH_ASSOC);
    
    http_response_code(200);
    echo json_encode([
        "success" => true,
        "message" => "Stok berhasil dikurangi",
        "jumlah_stok" => (int)$row['jumlah_stok']
    ]);
} else {
    http_response_code(500);
    echo json_encode([
        "success" => false,
        "message" => "Gagal mengurangi stok"
    ]);
}
?>