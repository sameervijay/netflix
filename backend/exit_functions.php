<?php
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
