<?php
$servername = "localhost"; 
$username = "root"; // Default username for XAMPP is "root"
$password = ""; // Default password is empty
$dbname = "db_minigame";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Get username from request
if (isset($_POST['username'])) {
    $user = $_POST['username'];

    // Check if the username already exists
    $check_sql = "SELECT * FROM users WHERE username='$user'";
    $result = $conn->query($check_sql);

    if ($result->num_rows > 0) {
        // Username already exists
        echo json_encode(["status" => "error", "message" => "Username already taken"]);
    } else {
        // Insert username into the database
        $sql = "INSERT INTO users (username) VALUES ('$user')";

        if ($conn->query($sql) === TRUE) {
            echo json_encode(["status" => "success", "message" => "Username successfully saved!"]);
        } else {
            echo json_encode(["status" => "error", "message" => "Error: " . $conn->error]);
        }
    }
} else {
    echo json_encode(["status" => "error", "message" => "No username provided!"]);
}

$conn->close();
?>