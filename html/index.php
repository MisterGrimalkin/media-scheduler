<?php $date=@$_GET["date"] ? @$_GET["date"] : date("Y-m-d"); ?>
<!DOCTYPE html>

<html>

<?php include("includes/header.html"); ?>

<body onload="onLoad();">

<div id="mask" class="maskoff"></div>

<div id="selectedEventId" data-value="-1" class="hidden"></div>

<div id="timePanel" class="timePanel"></div>

<?php
    include("includes/common.php");
    include("includes/cueForm.php");
    include("includes/eventForm.php");
    echo wrap("script", [], "url = '".URL."';");
?>

<div id="sideBar" class="cueMenu">
    <?php
        include("includes/eventMenu.php");
    ?>
    <div class="controlPanel">
        <p>
            <div class="fieldLabel">Brightness</div>
            <input id="brightness" type="range" min="0" max="127"
                   value="<?php echo getBrightness(); ?>" onchange="changeBrightness();">
        </p>
        <p>
            <div class="fieldLabel">Contrast</div>
            <input id="contrast"   type="range" min="0" max="127"
                   value="<?php echo getContrast(); ?>" onchange="changeContrast();">
        </p>
    </div>
    <?php
        include("includes/cueMenu.php");
    ?>
</div>

<div class="schedulePanel">
    <?php
        include("includes/schedule.php");
    ?>
</div>

</body>

</html>