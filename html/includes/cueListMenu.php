<nav>
    <h3>Cue Lists</h3>

    <?php

    $c = curl_init();
    curl_setopt($c, CURLOPT_URL, "http://192.168.0.70:8001/mediascheduler/schedule/cuelist");
    curl_setopt($c, CURLOPT_RETURNTRANSFER, 1);

    $output = curl_exec($c);

    if ( $output === false ) {

        echo wrap("div", ["class"=>"offline"], "Media Scheduler Offline");

    } else {

        $cuelists = json_decode($output, true);

        if ( count($cuelists)===0 ) {

            echo wrap("div", ["class"=>"offline"], "No Cue Lists");

        } else {

            asort($cuelists);
            foreach ( $cuelists as $cuelist ) {
            $id = $cuelist["id"];
            $number = $cuelist["number"];
            $name = $cuelist["name"];
            echo wrap("button", ["type"=>"button","class"=>"eventOnCueList$id", "onclick"=>"highlightEvents(\"$id\");"],
                    "$number: $name", true, true) . "<br>";

        }

    }
        echo "<br><button type='button' onclick='showCueListForm();'>Add...</button>";
    }

    curl_close($c);

    ?>

</nav>
