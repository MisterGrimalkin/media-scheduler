<?php

    function buildDayPanel($date) {

        $result = "";

        $weekday = $date->format("w");
        $style = "dayPanelHeader" . ($weekday==0 || $weekday==6 ? " weekend" : "" );
        $result .= wrap("div", ["class"=>$style], $date->format("D d M"));

        $today = ( $date->format("Y-m-d") === (new DateTime())->format("Y-m-d") );

        $cueLists = getCueLists();


        $containerTop = 100;
        $containerHeight = 500;

        $events = getSchedule($date);

        if ( count($events)===0 ) {
            //$result .= "No Events Found" ;
        } else {
            foreach ($events as $event) {

                $startTime = new DateTime($event["startTime"]);
                $endTime = new DateTime($event["endTime"]);

                $startTimeStamp = $startTime->getTimestamp();
                $endTimeStamp = $endTime->getTimestamp();

                $startOfDay = new DateTime((new DateTime())->format("Y-m-d"));
                $startOfDayTimeStamp = $startOfDay->getTimeStamp();

                $start = $startTimeStamp - $startOfDayTimeStamp;
                $length = $endTimeStamp - $startTimeStamp;

                $name = $cueLists[$event["cueListId"]];

                $top = (( $containerHeight / (24 * 60 * 60) ) * $start) + $containerTop;
                //echo "$length , $start    ";
                $height = ( $containerHeight / (24 * 60 * 60) ) * $length;

                $id = "event".$event["id"];

                $secondClass = "eventOnCueList" . $event["cueListId"];

                //$startTime = strtotime($event["startTime"]);
                $result .= wrap("div",
                    ["id"=>$id,
                     "onclick"=>"selectEvent({$event["id"]});",
                    "class"=>"event $secondClass $id",
                    "style"=>"top: {$top}px; height: {$height}px;"],
                    $cueLists[$event["cueListId"]]["name"]." (".$startTime->format("H:i")."-".$endTime->format("H:i").")");
            }
            if ( $today ) {
                $nowTimeStamp = (new DateTime())->getTimestamp();
                $now = $nowTimeStamp - $startOfDayTimeStamp;
                $top = (( $containerHeight / (24 * 60 * 60) ) * $now) + $containerTop;
                $result .= wrap("div",
                    ["class"=>"nowMarker", "style"=>"top: {$top}px;"], "");
            }
        }

        return $result;

    }

    function getSchedule($date) {

        $dateStr = $date->format("Y-m-d");

        $c = curl_init();
        curl_setopt($c, CURLOPT_URL, "http://192.168.0.70:8001/mediascheduler/schedule?date=$dateStr");
        curl_setopt($c, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($c, CURLOPT_CUSTOMREQUEST, "GET");

        $output = curl_exec($c);

        return json_decode($output, true);

    }
?>
