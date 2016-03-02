<nav>
    <h3>Cues</h3>

    <?php

        $cues = getCues();

        if ( count($cues)===0 ) {

            echo wrap("div", ["class"=>"offline"], "No Cues");

        } else {

            asort($cues);

            foreach ( $cues as $cue ) {

                $id = $cue["id"];
                $number = $cue["number"];
                $name = $cue["name"];

                echo wrap("button", ["type"=>"button","class"=>"eventOnCue$id", "onclick"=>"highlightEvents(\"$id\");"],
                        "$number: $name", true, true) . "<br>";
            }

        }

        echo "<br>". wrap("button", ["type"=>"button", "onclick"=>"showCueForm();"], "Add...");

    ?>

</nav>
