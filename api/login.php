<?php
/**
 * Login API
 * Endpoint untuk login admin
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

// Get POST data
$data = json_decode(file_get_contents("php://input"));

// Validasi input
if (empty($data->email) || empty($data->password)) {
    http_response_code(400);
    echo json_encode([
        "success" => false,
        "message" => "Email dan password harus diisi"
    ]);
    exit();
}

// Koneksi database
$database = new Database();
$db = $database->getConnection();

// Query user berdasarkan email
$query = "SELECT id, nama_lengkap, email, password FROM user WHERE email = :email LIMIT 1";
$stmt = $db->prepare($query);
$stmt->bindParam(":email", $data->email);
$stmt->execute();

if ($stmt->rowCount() > 0) {
    $row = $stmt->fetch(PDO::FETCH_ASSOC);
    
    // Verify password
    if (password_verify($data->password, $row['password'])) {
        // Generate JWT token
        $token = JWTHelper::generateToken($row['id'], $row['email']);
        
        // Simpan token ke database
        $query_token = "INSERT INTO token (user_id, token) VALUES (:user_id, :token)";
        $stmt_token = $db->prepare($query_token);
        $stmt_token->bindParam(":user_id", $row['id']);
        $stmt_token->bindParam(":token", $token);
        $stmt_token->execute();
        
        // Response sukses
        http_response_code(200);
        echo json_encode([
            "success" => true,
            "message" => "Login berhasil",
            "token" => $token,
            "user" => [
                "id" => $row['id'],
                "nama_lengkap" => $row['nama_lengkap'],
                "email" => $row['email']
            ]
        ]);
    } else {
        // Password salah
        http_response_code(401);
        echo json_encode([
            "success" => false,
            "message" => "Email atau password salah"
        ]);
    }
} else {
    // Email tidak ditemukan
    http_response_code(401);
    echo json_encode([
        "success" => false,
        "message" => "Email atau password salah"
    ]);
}
?>