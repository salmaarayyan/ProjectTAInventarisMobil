<?php
/**
 * JWT Helper
 * File ini berisi fungsi untuk generate dan validasi JWT token
 */

require_once __DIR__ . '/../vendor/autoload.php';
use Firebase\JWT\JWT;
use Firebase\JWT\Key;

class JWTHelper {
    // Gunakan key minimal 32 chars untuk HS256 (disarankan lebih panjang)
    private static $secret_key = "showroom_secret_key_2025_change_me_to_a_longer_random_string";
    private static $algorithm = "HS256";

    // Generate JWT token
    public static function generateToken($user_id, $email) {
        $payload = [
            'user_id' => $user_id,
            'email' => $email,
            'iat' => time() // issued at
        ];

        return JWT::encode($payload, self::$secret_key, self::$algorithm);
    }

    // Validasi JWT token
    public static function validateToken($token) {
        try {
            $decoded = JWT::decode($token, new Key(self::$secret_key, self::$algorithm));
            return [
                'valid' => true,
                'data' => $decoded
            ];
        } catch (Exception $e) {
            return [
                'valid' => false,
                'message' => $e->getMessage()
            ];
        }
    }

    // Extract token dari Authorization header
    public static function getBearerToken() {
        $headers = getallheaders();
        
        if (isset($headers['Authorization'])) {
            $matches = [];
            if (preg_match('/Bearer\s(\S+)/', $headers['Authorization'], $matches)) {
                return $matches[1];
            }
        }
        
        return null;
    }
}
?>