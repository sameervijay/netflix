<?php
$username = $_GET["username"];
$get_query = "SELECT ContentId FROM Watches WHERE Username = '$username' ORDER BY UserRating DESC LIMIT 3;";

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
