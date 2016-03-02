<?php

include("../includes/common.php");

if ( $_SERVER["REQUEST_METHOD"]==="POST" ) {

    $reloadDate = @$_POST["reloadDate"];
    $id = $_POST["id"] ? $_POST["id"] : -1;
    error_log("ID=$id");
    $path = URL;
    if ( $id >= 0 ) {
        $path .= "/schedule/update";
    } else {
        $path .= "/schedule/add";
    }
    error_log($path);

    $date = $_POST["startDate"];
    $start = $_POST["startTime"];
    $end = $_POST["endTime"];
    $cueId = $_POST["cueId"];

    if ( $date && $start && $end  ) {

        $data = json_encode(
        [   "id"=>$id,
            "startDate"=>$date,
            "startTime"=>$start,
            "endTime"=>$end,
            "cueId"=>$cueId,
            "repeatOn"=>[]
        ]);


        $c = curl_init();
        curl_setopt($c, CURLOPT_URL, $path);
        curl_setopt($c, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($c, CURLOPT_POSTFIELDS, $data);
        curl_setopt($c, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($c, CURLOPT_HTTPHEADER, array(
            'Content-Type: application/json',
            'Content-Length: ' . strlen($data))
        );
        $output = curl_exec($c);

        if ( $output!=="Event created" ) {
            echo errorMessage("Could not create Event: $output", "/index.php?date=$reloadDate");
        } else {
            header("Location: /index.php?date=$reloadDate");
        }

    } else {
        echo errorMessage("Invalid data", "/index.php?date=$reloadDate");
    }

} else {
    header("Location: /");
}



