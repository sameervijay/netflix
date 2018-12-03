<?php
$db = new mysqli('localhost', 'nosqls411_sameer', 'netflix411', 'nosqls411_Chill_DB');

// Check if username already exists in database; if so, reject new user
$username = $_POST["username"];
$check_query = "SELECT Username FROM Users WHERE Username = '$username';";
if ($db->connect_errno > 0) {
    die('Unable to connect to database [' . $db->connect_error . ']');
}
if (!$result = $db->query($check_query)) {
    die('There was an error running the query [' . $db->error . ']');
}
if ($result->num_rows > 0) {
    exit_simple(409, 'response', 'username taken');
}

// Extract parameters
$name = $_POST["name"];
$age = $_POST["age"];
$language = $_POST["language"];
$insertion = "INSERT INTO Users(FullName, Username, Age, Language) VALUES('$name', '$username', '$age', '$language');";

// Check if insert failed for some reason
if (!$result = $db->query($insertion)) {
    exit_simple(400, 'response', 'user not added');
}
// Make sure the inserted tuple is actually in Users
$worked_query = "SELECT Username FROM Users WHERE Username = '$username'";
$result = $db->query($worked_query);
$db->close();
if ($result->num_rows == 1) {
    // Insert succeeded
    exit_simple(200, 'response', 'user added');
} else {
    exit_simple(500, 'response', 'user not added');
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
[nosqls411@web public_html]$ cat add_watches.php
<?php
$db = new mysqli('localhost', 'nosqls411_sameer', 'netflix411', 'nosqls411_Chill_DB');

$username = $_POST["username"];
$contentId = $_POST["contentId"];
$rating = $_POST["rating"];
$language = $_POST["language"];
$date = $_POST["date"];

$insertion = "INSERT INTO Watches(Username, ContentId, UserRating, ViewingLanguage, TimeStamp) VALUES(\"$username\", \"$contentId\", \"$rating\", \"$language\", \"$date\");";

// Check if insert failed for some reason
if (!$result = $db->query($insertion)) {
    exit_simple(400, 'response', 'insertion into Watches failed');
}
// Make sure the inserted tuple is actually in Users
$worked_query = "SELECT Username FROM Watches WHERE Username = '$username'";
$result = $db->query($worked_query);
$db->close();
if ($result->num_rows == 1) {
    exit_simple(200, 'response', 'user added');
} else {

    exit_simple(500, 'response', 'user not added');
}

?>
