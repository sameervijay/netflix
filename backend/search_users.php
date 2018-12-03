<?php
$db = new mysqli('localhost', 'nosqls411_sameer', 'netflix411', 'nosqls411_Chill_DB');

$username = $_GET["username"]; //change to POST if implement password since GET will just append the query in the url which is insecure for passwords
$get_query = "SELECT Username FROM Users WHERE Username = '$username';";


if ($db->connect_errno > 0) {
    die('Unable to connect to database [' . $db->connect_error . ']');
}
if (!$result = $db->query($get_query)) {
    die('There was an error running the query [' . $db->error . ']');
}
if ($result->num_rows > 0) {
        //does not check password; if later impl. then add here to check if both are correct rather than just checking if a row was returned
    exit_simple(200, 'response', 'username exists');
}
else{
        exit_simple(409, 'response', 'username not found');
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
