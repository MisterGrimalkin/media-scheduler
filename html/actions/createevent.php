<?php

$url = "http://192.168.0.70:8001/mediascheduler";

if ( $_SERVER["REQUEST_METHOD"]==="POST" ) {

    $date = $_POST["startDate"];
    $start = $_POST["startTime"];
    $end = $_POST["endTime"];
    $cueList = $_POST["cueList"];

    if ( $date && $start && $end  ) {

        echo "Here";

        $data = json_encode(
        [   "startDate"=>$date,
            "startTime"=>$start,
            "endTime"=>$end,
            "cueListId"=>$cueList,
            "repeatOn"=>[]
        ]);

        var_dump($data);

        $c = curl_init();
        curl_setopt($c, CURLOPT_URL, "$url/schedule/add");
        curl_setopt($c, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($c, CURLOPT_POSTFIELDS, $data);
        curl_setopt($c, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($c, CURLOPT_HTTPHEADER, array(
            'Content-Type: application/json',
            'Content-Length: ' . strlen($data))
        );
        $output = curl_exec($c);

        echo $output;

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



