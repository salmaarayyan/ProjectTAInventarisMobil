<?php
/**
 * Read Detail Mobil API
 * Endpoint untuk mengambil detail 1 mobil dengan JOIN ke tabel merk
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

// Get parameter id
if (empty($_GET['id'])) {
    http_response_code(400);
    echo json_encode([
        "success" => false,
        "message" => "Parameter id harus diisi"
    ]);
    exit();
}

$id = $_GET['id'];

// Query detail mobil dengan JOIN ke tabel merk dan stok
$query = "SELECT m.id, m.nama_mobil, m.merk_id, mk.nama_merk, m.tipe, m.tahun, m.harga, m.warna, m.created_at, 
          s.jumlah_stok, s.updated_at as stok_updated_at
          FROM mobil m 
          INNER JOIN merk mk ON m.merk_id = mk.id
          LEFT JOIN stok s ON m.id = s.mobil_id 
          WHERE m.id = :id 
          LIMIT 1";
$stmt = $db->prepare($query);
$stmt->bindParam(":id", $id);
$stmt->execute();

if ($stmt->rowCount() > 0) {
    $row = $stmt->fetch(PDO::FETCH_ASSOC);
    
    http_response_code(200);
    echo json_encode([
        "success" => true,
        "data" => [
            "id" => (int)$row['id'],
            "nama_mobil" => $row['nama_mobil'],
            "merk_id" => (int)$row['merk_id'],
            "nama_merk" => $row['nama_merk'],
            "tipe" => $row['tipe'],
            "tahun" => (int)$row['tahun'],
            "harga" => (float)$row['harga'],
            "warna" => $row['warna'],
            "jumlah_stok" => (int)$row['jumlah_stok'],
            "created_at" => $row['created_at'],
            "stok_updated_at" => $row['stok_updated_at']
        ]
    ]);
} else {
    http_response_code(404);
    echo json_encode([
        "success" => false,
        "message" => "Mobil tidak ditemukan"
    ]);
}
?>