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

// Get scores for a specific game
if (isset($_GET['game'])) {
    $game = $_GET['game'];

    $sql = "SELECT username, score FROM scores WHERE game='$game' ORDER BY score DESC LIMIT 10";
    $result = $conn->query($sql);

    $scores = [];
    if ($result->num_rows > 0) {
        while ($row = $result->fetch_assoc()) {
            $scores[] = $row;
        }
    }

    echo json_encode($scores);
} else {
    echo json_encode(["status" => "error", "message" => "No game specified!"]);
}

$conn->close();
?>
