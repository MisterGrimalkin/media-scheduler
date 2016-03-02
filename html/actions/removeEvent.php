<?php

include("../includes/common.php");

if ( $_SERVER["REQUEST_METHOD"]==="POST" ) {

    error_log("wyt?");

    $id = file_get_contents("php://input");

    if ( $id>-1 ) {

        $c = curl_init();
        curl_setopt($c, CURLOPT_URL, URL."/schedule/remove");
        curl_setopt($c, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($c, CURLOPT_POSTFIELDS, $id);
        curl_setopt($c, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($c, CURLOPT_HTTPHEADER, array(
            'Content-Type: application/json',
            'Content-Length: ' . strlen($id))
        );
        $output = curl_exec($c);
        curl_close($c);

        if ( $output!=="Event removed" ) {
            echo errorMessage("Could not remove event: $output", "index.php");
        } else {
            header("Location: /");
        }

    } else {
        header("Location: /");
    }

} else {
   header("Location: /");
}
