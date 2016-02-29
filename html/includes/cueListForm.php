<div id="cueListForm" class="cueListForm">
    <form method="POST" action="actions/createcuelist.php">
        <h3>Create Cue List</h3>
        <p>
        <div class="fieldLabel">Number</div>
        <input id="cueListFormNumber" type="text" name="cueListNumber" class="field" style="width: 50px;">
        </p>
        <p>

        <div class="fieldLabel">Name</div>
        <input id="cueListFormName" type="text" name="cueListName"  class="field">
        </p>
        <p style="text-align: center">

        <button type="button" onclick="hideCueListForm();">Cancel</button>
        <button type="submit">Create</button>
        </p>
    </form>
</div>