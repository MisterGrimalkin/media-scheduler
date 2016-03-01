<div id="eventForm" class="form editEvent">

    <form method="POST" action="actions/createevent.php">

        <h3>Create Event</h3>

        <p>
            <div class="fieldLabel">Date</div>
            <input id="eventFormDate" type="date" name="startDate" class="field">
        </p>

        <p>
            <div class="fieldLabel">Start</div>
            <input id="eventFormStartTime" type="time" name="startTime"  class="field" style="width: 70px;">
        </p>

        <p>
            <div class="fieldLabel">End</div>
            <input id="eventFormEndTime" type="time" name="endTime"  class="field" style="width: 70px;">
        </p>

        <p>
        <div class="fieldLabel">Cue List</div>
            <select id="eventCueList" class="field" name="cueList">
                <?php
                    $c = curl_init();
                    curl_setopt($c, CURLOPT_URL, "http://192.168.0.70:8001/mediascheduler/schedule/cuelist");
                    curl_setopt($c, CURLOPT_RETURNTRANSFER, 1);

                    $output = curl_exec($c);

                    if ( $output !== false ) {
                        $cuelists = json_decode($output, true);
                        if ( count($cuelists)>0 ) {
                            foreach ( $cuelists as $cuelist ) {
                                $id = $cuelist["id"];
                                $name = $cuelist["name"];
                                echo wrap("option", ["value"=>$id,"class"=>"field"],$name);
                            }
                        }
                    }
                    curl_close($c);
                ?>
            </select>
        </p>

        <p style="text-align: center">
            <button type="button" onclick="hideEventForm();">Cancel</button>
            <button type="submit">Create</button>
        </p>

    </form>

</div>