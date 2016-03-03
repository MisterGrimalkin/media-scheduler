<nav>
    <h3>Events</h3>

    <?php
        echo "<button type='button' onclick='addEvent();'>Add...</button>";
        echo "<br><button id='editEvent' type='button' onclick='editSelectedEvent();' disabled>Edit</button>";
        echo "<br><button id='removeEvent' type='button' onclick='removeEvent();' disabled>Remove</button>";
    ?>

</nav>
