<?php
$username = $_GET["username"];
$genre = $_GET["genre"];
$director = $_GET["director"];
$language = $_GET["language"];
$get_query = "SELECT DISTINCT Watches.Username, Users.FBProfile FROM Watches Join Content On Watches.ContentId = Content.ContentId Join Users On Watches.Username = Users.Username WHERE Genre = '$genre' AND Director = '$director' AND ViewingLanguage = '$language' AND UserRating >= 7 AND Watches.Username <> '$username' ;";

$db = new mysqli('localhost', 'nosqls411_sameer', 'netflix411', 'nosqls411_Chill_DB');
if ($db->connect_errno > 0) {
    die('Unable to connect to database [' . $db->connect_error . ']');
}
if (!$results = $db->query($get_query)) {
    die('There was an error running the query [' . $db->error . ']');
}

$response = array();
$code = 200;
http_response_code($code);
$response['statusCode'] = $code;
$response['content'] = array();
while ($row = $results->fetch_assoc()) {
    array_push($response['content'], $row);
}
$json = json_encode($response);
echo $json;
?>
