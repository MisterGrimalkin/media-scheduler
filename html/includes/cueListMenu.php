<nav>
    <h3>Cue Lists</h3>

    <?php

    $c = curl_init();
    curl_setopt($c, CURLOPT_URL, "http://192.168.0.70:8001/mediascheduler/schedule/cuelist");
    curl_setopt($c, CURLOPT_RETURNTRANSFER, 1);

    $output = curl_exec($c);

    if ( $output === false ) {
        echo "Media Scheduler Offline";
    } else {
        $cuelists = json_decode($output, true);
        if ( count($cuelists)===0 ) {
            echo "<p>No Cue Lists</p>";
        } else {
            asort($cuelists);
            foreach ( $cuelists as $cuelist ) {
            $number = $cuelist["number"];
            $name = $cuelist["name"];
            echo wrap("button", ["type"=>"button","onclick"=>"showCueListForm(\"$number\", \"$name\");"], "$number: $name", true, true) . "<br>";
        }
    }
        echo "<br><button type='button' onclick='showCueListForm();'>Add...</button>";
    }

    curl_close($c);

    ?>

</nav>
