<?php
$db = new mysqli('localhost', 'nosqls411_sameer', 'netflix411', 'nosqls411_Chill_DB');

$contentId = $_GET["contentId"];
$get_query = "SELECT Thumbnail FROM ContentMeta WHERE ContentId = '$contentId';";


if ($db->connect_errno > 0) {
    die('Unable to connect to database [' . $db->connect_error . ']');
}
if (!$result = $db->query($get_query)) {
    die('There was an error running the query [' . $db->error . ']');
}
if ($result->num_rows > 0) {
        //the response is the thumbnail url; there should only be one thumbnail result so we don't need a loop and simple response works for our purposes
    exit_simple(200, 'response', $result->fetch_assoc());
}
else{
        exit_simple(409, 'response', 'thumbnail not found');
}

function exit_simple($code, $key, $message) {
    http_response_code($code);
    $response = array(
        'statusCode' => $code,
        $key => $message,
    );
    $json = json_encode($response);
    echo $json;
    exit;
}
?>
