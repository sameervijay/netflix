<?php
include 'exit_functions.php';

$db = new mysqli('localhost', 'nosqls411_sameer', 'netflix411', 'nosqls411_Chill_DB');

$name = $_POST["name"];
$deletion = "DELETE FROM Content WHERE Name = '$name';";

// Check if insert failed for some reason
if (!$result = $db->query($deletion)) {
    exit_simple(400, 'response', 'Deletion from Content failed');
}
exit_simple(200, 'response', "$name deleted");

?>
