<?php

    function buildDayPanel($date, $cues) {

        $result = "";

        $weekday = $date->format("w");
        $style = "dayPanelHeader" . ($weekday==0 || $weekday==6 ? " weekend" : "" );
        $result .= wrap("div", ["class"=>$style], $date->format("D d M"));

        $today = ( $date->format("Y-m-d") === (new DateTime())->format("Y-m-d") );

        $containerTop = 100;
        $containerHeight = 500;

        $events = getSchedule($date);
        $startOfDay = new DateTime((new DateTime())->format("Y-m-d"));
        $startOfDayTimeStamp = $startOfDay->getTimeStamp();

        if ( count($events)===0 ) {
            //$result .= "No Events Found" ;
        } else {
            foreach ($events as $event) {

                $startTime = new DateTime($event["startTime"]);
                $endTime = new DateTime($event["endTime"]);

                $startTimeStamp = $startTime->getTimestamp();
                $endTimeStamp = $endTime->getTimestamp();


                $start = $startTimeStamp - $startOfDayTimeStamp;
                $length = $endTimeStamp - $startTimeStamp;

                $name = $cues[$event["cueId"]];

                $top = (( $containerHeight / (24 * 60 * 60) ) * $start) + $containerTop;
                //echo "$length , $start    ";
                $height = ( $containerHeight / (24 * 60 * 60) ) * $length;

                $id = "event".$event["id"];

                $secondClass = "eventOnCue" . $event["cueId"];

                //$startTime = strtotime($event["startTime"]);
                $result .= wrap("div",
                    ["id"=>$id,
                     "onclick"=>"selectEvent({$event["id"]});",
                    "class"=>"event $secondClass $id",
                    "style"=>"top: {$top}px; height: {$height}px;"],
                    $cues[$event["cueId"]]["name"]." (".$startTime->format("H:i")."-".$endTime->format("H:i").")");
            }
        }

        if ( $today ) {
            $nowTimeStamp = (new DateTime())->getTimestamp();
            $now = $nowTimeStamp - $startOfDayTimeStamp;
            $top = (( $containerHeight / (24 * 60 * 60) ) * $now) + $containerTop;
            $result .= wrap("div",
                ["class"=>"nowMarker", "style"=>"top: {$top}px;"], "");
        }

        return $result;

    }

?>
