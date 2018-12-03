<?php
include 'exit_functions.php';

$db = new mysqli('localhost', 'nosqls411_sameer', 'netflix411', 'nosqls411_Chill_DB');

$name = $_POST["name"];
$genre = $_POST["genre"];
$rating = $_POST["rating"];
$release = $_POST["release_date"];
$language = $_POST["language"];
$director = $_POST["director"];
$duration = $_POST["duration"];
$insertion = "INSERT INTO Content(Name, Genre, AvgRating, ReleaseDate, Language, Director, Duration) "
                . "VALUES(\"$name\", \"$genre\", \"$rating\", \"$release\", \"$language\", \"$director\", \"$duration\");";

// Check if content with given name already exists
$check = "SELECT * FROM Content WHERE Name = \"$name\";";
$result = $db->query($check);
if (!$result || $result->num_rows > 0) {
    exit_simple(400, 'response', "Content with name $name already exists");
}

// Check if insert failed for some reason
if (!$result = $db->query($insertion)) {
    exit_simple(400, 'response', 'Insertion into Content failed');
}
// Make sure the inserted tuple is actually in Users
$worked_query = "SELECT ContentId FROM Content WHERE Name = \"$name\";";
$result = $db->query($worked_query);
$db->close();
if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $contentId = $row['ContentId'];
    exit_simple(200, 'response', "$contentId content added");
} else {
    exit_simple(500, 'response', 'content not added');
}

?>
