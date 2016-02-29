<?php

if ( $_SERVER["REQUEST_METHOD"]==="POST" ) {

    $number = @$_POST["cueListNumber"];
    $name = @$_POST["cueListName"];

    if ( $name && $number ) {

        $data = json_encode(["number"=>$number,"name"=>$name]);

        $c = curl_init();
        curl_setopt($c, CURLOPT_URL, "http://192.168.0.70:8001/mediascheduler/schedule/cuelist");
        curl_setopt($c, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($c, CURLOPT_POSTFIELDS, $data);
        curl_setopt($c, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($c, CURLOPT_HTTPHEADER, array(
            'Content-Type: application/json',
            'Content-Length: ' . strlen($data))
        );
        $output = curl_exec($c);

        if ( $output===false ) {
            echo("<script>window.alert('fail!');</script>");
        } else {
            header("Location: /");
        }

    } else {
        header("Location: /");
    }

} else {
    header("Location: /");
}



