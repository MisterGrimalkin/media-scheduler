<div id="eventForm" class="form editEvent">

    <form method="POST" action="actions/createEvent.php">

        <h3 id="eventFormHeader">Create Event</h3>

        <input id="eventFormHiddenDate" type="date" name="reloadDate" class="hidden" value="<?php echo $date; ?>">
        <input id="eventHiddenId" type="number" name="id" class="hidden">

        <p>
            <div class="fieldLabel">Date</div>
            <input id="eventFormDate" type="date" name="startDate" class="field" style="width: 210px;">
        </p>

        <p>
            <div class="fieldLabel">Start</div>
            <input id="eventFormStartTime" type="time" name="startTime"  class="field" style="width: 80px;">
            <div class="fieldLabel">End</div>
            <input id="eventFormEndTime" type="time" name="endTime"  class="field" style="width: 80px;">
        </p>

        <p>
        <div class="fieldLabel">Cue</div>
            <select id="eventCueId" class="field" name="cueId" style="width: 210px;">
                <?php
                    $cues = getCues();
                    if ( count($cues)>0 ) {
                        foreach ( $cues as $cue ) {
                            $id = $cue["id"];
                            $name = $cue["name"];
                            echo wrap("option", ["value"=>$id,"class"=>"field"],$name);
                        }
                    }
                ?>
            </select>
        </p>

        <p style="text-align: center">
            <button type="button" onclick="hideEventForm();">Cancel</button>
            <button id="eventFormOK" type="submit">OK</button>
        </p>

    </form>

</div>