<!DOCTYPE html>

<html>

<head lang="en">
    <meta charset="UTF-8">
    <title>Cue Lists</title>
    <link rel="stylesheet" type="text/css" href="scheduler.css">
    <script src="scheduler.js"></script>
    <!--<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular.min.js"></script>-->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
    <script src="//code.jquery.com/jquery-1.10.2.js"></script>
    <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
    <script>
    $(function() {
        $("#cueListForm").draggable();
    });
    </script>
</head>

<div id="mask" class="mask inactive"></div>

<?php
include("includes/common.php");
include("includes/cueListForm.php");
?>

<div style="width: 160px; height: 100%;">
    <?php
        include("includes/cueListMenu.php");
        include("includes/controls.php");
    ?>
</div>

</body>
</html>