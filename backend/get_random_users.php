<?php
$username = $_GET["username"];
$username1 = $_GET["username1"];
$username2 = $_GET["username2"];

if($username1 == null and $username2 == null){
    $get_query = "SELECT DISTINCT Watches.Username, Users.FBProfile FROM Watches Join Users on Watches.Username = Users.Username WHERE Watches.Username <> '$username' ORDER BY RAND() LIMIT 3 ;";
}
else if($username2 == null){
    $get_query = "SELECT DISTINCT Watches.Username, Users.FBProfile FROM Watches Join Users on Watches.Username = Users.Username WHERE Watches.Username <> '$username' AND Watches.Username <> '$username1' ORDER BY RAND() LIMIT 2 ;";
}
else{
    $get_query = "SELECT  Watches.Username, Users.FBProfile FROM Watches Join Users on Watches.Username = Users.Username WHERE Watches.Username <> '$username' AND Watches.Username <> '$username1' AND Watches.Username <> '$username2' ORDER BY RAND() LIMIT 1 ;";
}

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
