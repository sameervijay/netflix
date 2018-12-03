<?php
$name = $_GET["name"];
$genre = $_GET["genre"];
$language = $_GET["language"];
$director = $_GET["director"];
$avg_rating = $_GET["avg_rating"];
$release_date = $_GET["release_date"];
$duration = $_GET["duration"];

// If a query parameter was passed in, add it to the array
$params = array();
if (!empty($name)) array_push($params, array("Attr" => "Name", "Value" => "\"%$name%\""));
if (!empty($genre)) array_push($params, array("Attr" => "Genre", "Value" => "\"$genre\""));
if (!empty($language)) array_push($params, array("Attr" => "Language", "Value" => "\"$language\""));
if (!empty($director)) array_push($params, array("Attr" => "Director", "Value" => "\"%$director%\""));
if (!empty($avg_rating)) array_push($params, array("Attr" => "AvgRating", "Value" => $avg_rating));
if (!empty($release_date)) array_push($params, array("Attr" => "ReleaseDate", "Value" => "\"%$release_date%\""));
if (!empty($duration)) array_push($params, array("Attr" => "Duration", "Value" => "\"$duration\""));
$get_query = "SELECT * FROM Content";
for ($i = 0; $i < count($params); $i++) {
    if ($i != 0) $get_query .= " AND";
    else $get_query .= " WHERE";
    $get_query .= " " . $params[$i]["Attr"];
    if ($params[$i]["Attr"] == "Name" || $params[$i]["Attr"] == "Director" || $params[$i]["Attr"] == "ReleaseDate") {
        $get_query .= " LIKE ";
    } else if ($params[$i]["Attr"] == "AvgRating") {
        $get_query .= " >= ";
    } else {
        $get_query .= " = ";
    }
    $get_query .= $params[$i]['Value'];
}
$get_query .= ";";

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
    $contentId = $row['ContentId'];
    $thumbnail_query = "SELECT Thumbnail FROM ContentMeta WHERE ContentId = \"$contentId\";";
    $thumbnail = $db->query($thumbnail_query)->fetch_assoc();
    $row['Thumbnail'] = $thumbnail['Thumbnail'];
    array_push($response['content'], $row);
}
$json = json_encode($response);
echo $json;
?>
