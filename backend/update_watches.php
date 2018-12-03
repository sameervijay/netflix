<?php
$username = $_POST["username"];
$contentId = $_POST["contentId"];
$rating = $_POST["rating"];
$language = $_POST["language"];
$date = $_POST["date"];

$db = new mysqli('localhost', 'nosqls411_sameer', 'netflix411', 'nosqls411_Chill_DB');
$get_query = "SELECT * FROM Watches WHERE Username = \"$username\" AND ContentId = \"$contentId\";";
if (!$results = $db->query($get_query)) {
    die('There was an error running the query [' . $db->error . ']');
}

$response = array();
if ($results->num_rows > 0) {
    $params = array();
    if (!empty($date)) array_push($params, array("Attr" => "TimeStamp", "Value" => "\"$date\""));
    if (!empty($language)) array_push($params, array("Attr" => "ViewingLanguage", "Value" => "\"$language\""));
    if (!empty($rating)) array_push($params, array("Attr" => "UserRating", "Value" => $rating));
    $update_query = "UPDATE Watches SET";
    for ($i = 0; $i < count($params); $i++) {
        if ($i != 0) $update_query .= ",";
        $update_query .= " " . $params[$i]["Attr"] . " = " . $params[$i]['Value'];
    }
    $update_query .= " WHERE Username = \"$username\" AND ContentId = \"$contentId\";";
    if ($db->connect_errno > 0) {
        die('Unable to connect to database [' . $db->connect_error . ']');
    }
    if (!$results = $db->query($update_query)) {
        die('There was an error running the query [' . $db->error . ']');
    }
    $code = 201;
    http_response_code($code);
    $response['statusCode'] = $code;
    $response['response'] = "row updated";
    $json = json_encode($response);
    echo $json;
} else {
    $insertion = "INSERT INTO Watches(Username, ContentId, UserRating, ViewingLanguage, TimeStamp) VALUES(\"$username\", \"$contentId\", \"$rating\", \"$language\", \"$date\");";

    // Check if insert failed for some reason
    if (!$result = $db->query($insertion)) {
        exit_simple(409, 'response', 'insertion into Watches failed');
    }
    // Make sure the inserted tuple is actually in Watches
    $worked_query = "SELECT Username FROM Watches WHERE Username = '$username' AND ContentId = '$contentId';";
    $result = $db->query($worked_query);
    $code = 200;
    if ($result->num_rows == 0) {
        $code = 409;
    }
    http_response_code($code);
    $response['statusCode'] = $code;
    $response['response'] = "row inserted";
    $json = json_encode($response);
    echo $json;
}
$db->close();
?>
