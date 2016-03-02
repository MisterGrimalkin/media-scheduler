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

<iframe id="schedule" width="1000" height="820" frameborder="0"></iframe>
