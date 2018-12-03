<?php
$username = $_POST["username"];
$contentId = $_POST["contentId"];
$delete_query = "DELETE FROM Watches WHERE Username = \"$username\" AND ContentId = \"$contentId\";";

$db = new mysqli('localhost', 'nosqls411_sameer', 'netflix411', 'nosqls411_Chill_DB');
if ($db->connect_errno > 0) {
    die('Unable to connect to database [' . $db->connect_error . ']');
}
if (!$results = $db->query($delete_query)) {
    die('There was an error running the query [' . $db->error . ']');
}
$code = 200;
http_response_code($code);
$response['statusCode'] = $code;
$response['response'] = $db->affected_rows;
$json = json_encode($response);
echo $json;
$db->close();
?>
