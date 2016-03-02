<?php

include("../includes/common.php");

//$url = "http://192.168.0.70:8001/mediascheduler";

if ( $_SERVER["REQUEST_METHOD"]==="POST" ) {

    $reloadDate = @$_POST["reloadDate"];
    $number = @$_POST["cueNumber"];
    $name = @$_POST["cueName"];

    if ( $name && $number ) {

        $name = htmlspecialchars($name);

        $data = json_encode(["id"=>-1,"number"=>$number,"name"=>$name]);

        $c = curl_init();
        curl_setopt($c, CURLOPT_URL, URL."/cue/create");
        curl_setopt($c, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($c, CURLOPT_POSTFIELDS, $data);
        curl_setopt($c, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($c, CURLOPT_HTTPHEADER, array(
            'Content-Type: application/json',
            'Content-Length: ' . strlen($data))
        );
        $output = curl_exec($c);

        if ( $output!=="Cue created" ) {
            echo errorMessage("Could not create Cue: $output", "/index.php?date=$reloadDate");
        } else {
            header("Location: /index.php?date=$reloadDate");
        }

        curl_close($c);

    } else {
        echo errorMessage("Invalid data", "/index.php?date=$reloadDate");
    }

} else {
    header("Location: /");
}



