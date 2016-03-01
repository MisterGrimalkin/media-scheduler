<?php

$url = "http://192.168.0.70:8001/mediascheduler";


if ( $_SERVER["REQUEST_METHOD"]==="POST" ) {

    $id = file_get_contents("php://input");

    if ( $id ) {

        $c = curl_init();
        curl_setopt($c, CURLOPT_URL, "$url/schedule/remove");
        curl_setopt($c, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($c, CURLOPT_POSTFIELDS, $id);
        curl_setopt($c, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($c, CURLOPT_HTTPHEADER, array(
            'Content-Type: application/json',
            'Content-Length: ' . strlen($id))
        );
        $output = curl_exec($c);

        echo $output;

        if ( $output===false ) {
            echo("<script>window.alert('fail!');</script>");
        } else {
           header("Location: /");
        }

    } else {
  //      header("Location: /");
    }

} else {
    //header("Location: /");
}
