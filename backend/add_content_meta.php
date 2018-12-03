<?php
include 'exit_functions.php';

$db = new mysqli('localhost', 'nosqls411_sameer', 'netflix411', 'nosqls411_Chill_DB');

$contentId = $_POST["contentId"];
$imdb = $_POST["imdb"];
$thumbnail = $_POST["thumbnail"];
$insertion = "INSERT INTO ContentMeta(ContentId, Imdb, Thumbnail) "
                . "VALUES(\"$contentId\", \"$imdb\", \"$thumbnail\");";

// Check if insert failed for some reason
if (!$result = $db->query($insertion)) {
    exit_simple(400, 'response', 'Insertion into ContentMeta failed');
}
// Make sure the inserted tuple is actually there
$worked_query = "SELECT * FROM ContentMeta WHERE ContentId = \"$contentId\";";
$result = $db->query($worked_query);
$db->close();
if ($result->num_rows > 0) {
    exit_simple(200, 'response', 'content meta added');
} else {
    exit_simple(500, 'response', 'content meta not added');
}

?>
