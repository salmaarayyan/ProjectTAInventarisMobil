<?php
/**
 * Read Mobil API
 * Endpoint untuk mengambil daftar mobil per merk dengan JOIN ke tabel merk
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

// Get parameter merk_id
if (empty($_GET['merk_id'])) {
    http_response_code(400);
    echo json_encode([
        "success" => false,
        "message" => "Parameter merk_id harus diisi"
    ]);
    exit();
}

$merk_id = $_GET['merk_id'];

// Query mobil dengan JOIN ke tabel merk dan stok
$query = "SELECT m.id, m.nama_mobil, m.merk_id, mk.nama_merk, m.tipe, m.tahun, m.harga, m.warna, m.created_at, 
          s.jumlah_stok 
          FROM mobil m 
          INNER JOIN merk mk ON m.merk_id = mk.id
          LEFT JOIN stok s ON m.id = s.mobil_id 
          WHERE m.merk_id = :merk_id
          ORDER BY m.created_at DESC";
$stmt = $db->prepare($query);
$stmt->bindParam(":merk_id", $merk_id);
$stmt->execute();

$mobil_list = [];
while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
    $mobil_list[] = [
        "id" => (int)$row['id'],
        "nama_mobil" => $row['nama_mobil'],
        "merk_id" => (int)$row['merk_id'],
        "nama_merk" => $row['nama_merk'],
        "tipe" => $row['tipe'],
        "tahun" => (int)$row['tahun'],
        "harga" => (float)$row['harga'],
        "warna" => $row['warna'],
        "jumlah_stok" => (int)$row['jumlah_stok'],
        "created_at" => $row['created_at']
    ];
}

http_response_code(200);
echo json_encode([
    "success" => true,
    "data" => $mobil_list
]);
?>