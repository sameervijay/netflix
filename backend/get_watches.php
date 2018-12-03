<?php
// This is a GET request, but if it were a POST, could retrieve query parameters here
$username = $_GET["username"];
//mysql_real_escape_string($username);

$query = "SELECT * FROM Watches WHERE Username = '$username';";
$db = new mysqli('localhost', 'nosqls411_sameer', 'netflix411', 'nosqls411_Chill_DB');

if($db->connect_errno > 0){
    die('Unable to connect to database [' . $db->connect_error . ']');
}
if(!$results = $db->query($query)){
    die('There was an error running the query [' . $db->error . ']');
}

$response = array();
$code = 200;
http_response_code($code);
$response['statusCode'] = $code;
$response['watches'] = array();
while ($row = $results->fetch_assoc()) {
    array_push($response['watches'], $row);
}
$json = json_encode($response);
echo $json;
?>
