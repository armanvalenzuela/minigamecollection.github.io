<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "db_minigame";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Get score and username from request
if (isset($_POST['username']) && isset($_POST['game_name']) && isset($_POST['score'])) {
    $username = $_POST['username'];
    $game_name = $_POST['game_name'];
    $score = $_POST['score'];

    // Get user_id from username
    $user_query = "SELECT id FROM users WHERE username='$username'";
    $user_result = $conn->query($user_query);

    if ($user_result->num_rows > 0) {
        $user_row = $user_result->fetch_assoc();
        $user_id = $user_row['id'];

        // Insert score into the database
        $sql = "INSERT INTO scores (user_id, game_name, score) VALUES ('$user_id', '$game_name', '$score')";

        if ($conn->query($sql) === TRUE) {
            echo json_encode(["status" => "success", "message" => "Score submitted successfully!"]);
        } else {
            echo json_encode(["status" => "error", "message" => "Error: " . $conn->error]);
        }
    } else {
        echo json_encode(["status" => "error", "message" => "User not found"]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Incomplete data provided!"]);
}

$conn->close();
?>