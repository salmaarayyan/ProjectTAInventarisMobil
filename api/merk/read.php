<?php
/**
 * Read Merk API
 * Endpoint untuk mengambil daftar semua merk dari tabel merk
 */

header("Content-Type: application/json");
require_once '../../config/database.php';
require_once '../../utils/jwt_helper.php';

// Only accept GET method
if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
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

// Query untuk mengambil daftar merk dengan jumlah mobil per merk
$query = "SELECT m.id, m.nama_merk, m.created_at, 
          COUNT(mb.id) as jumlah_mobil 
          FROM merk m 
          LEFT JOIN mobil mb ON m.id = mb.merk_id 
          GROUP BY m.id 
          ORDER BY m.nama_merk ASC";
$stmt = $db->prepare($query);
$stmt->execute();

$merk_list = [];
while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
    $merk_list[] = [
        "id" => (int)$row['id'],
        "nama_merk" => $row['nama_merk'],
        "jumlah_mobil" => (int)$row['jumlah_mobil'],
        "created_at" => $row['created_at']
    ];
}

http_response_code(200);
echo json_encode([
    "success" => true,
    "data" => $merk_list
]);
?>