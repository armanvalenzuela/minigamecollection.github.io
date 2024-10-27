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

// Fetch the top 10 scores
$sql = "SELECT username, score, created_at FROM popit_leaderboard ORDER BY score DESC LIMIT 10";
$result = $conn->query($sql);

$leaderboard = [];

if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $leaderboard[] = [
            "username" => $row["username"],
            "score" => $row["score"],
            "date" => $row["created_at"]
        ];
    }
    echo json_encode(["status" => "success", "leaderboard" => $leaderboard]);
} else {
    echo json_encode(["status" => "success", "leaderboard" => []]);
}

$conn->close();
?>
