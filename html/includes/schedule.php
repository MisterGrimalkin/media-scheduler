<?php $date=@$_GET["date"] ? @$_GET["date"] : date("Y-m-d"); ?>

<!--<h1>Schedule</h1>-->
<h1>
    <?php
        echo date("F Y", strtotime($date));
    ?>
</h1>

<div class = "dateControls">

<form target="/" method="GET">
<h2>
    <button type="button" onclick="moveDays(-7);"><< WEEK</button>
    <button type="button" onclick="moveDays(-1);">< DAY</button>
    <button type="button" onclick="today();">TODAY</button>
    <button type="button" onclick="moveDays(1);">> DAY</button>
    <button type="button" onclick="moveDays(7);">>> WEEK</button>
    <input id="searchDate" type="date" name="date" value="<?php echo $date ?>">
    <button type="submit" target="_self">Go</button>
</h2>
<form>

</div>


<?php

    include("dayPanel.php");

    $panelDate = new DateTime($date);
    for ( $i = 0; $i < 7; $i++ ) {
        echo wrap("div", ["class"=>"dayPanel"], buildDayPanel($panelDate));
        $panelDate->modify("+1 day");
    }

?>

