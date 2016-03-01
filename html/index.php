<!DOCTYPE html>

<html>

<head lang="en">

    <title>Amarantha Media Scheduler</title>

    <meta charset="UTF-8">

    <link rel="stylesheet" type="text/css" href="stylesheets/scheduler.css">

    <script src="scheduler.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
    <script src="//code.jquery.com/jquery-1.10.2.js"></script>
    <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
    <!--<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular.min.js"></script>-->

</head>

<body onload="onLoad();">

<div id="mask" class="maskoff"></div>

<div id="timePanel" class="timePanel"></div>

<?php
    include("includes/common.php");
    include("includes/cueListForm.html");
    include("includes/eventForm.php");
?>

<div class="cueListMenu">
    <?php
        include("includes/eventMenu.php");
        include("includes/cueListMenu.php");
    ?>
    <div class="controlPanel">
        <input id="brightness" type="range" min="0" max="127" onchange="changeBrightness();">
        <input id="contrast"   type="range" min="0" max="127" onchange="changeContrast();">
    </div>
</div>

<div class="schedulePanel">
    <?php
        include("includes/schedule.php");
    ?>
</div>

</body>

</html>