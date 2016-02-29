function showCueListForm(number, name) {
    mask(true);
    $("#cueListForm").attr("class", "cueListForm active");
    $("#cueListFormNumber").val(number);
    $("#cueListFormName").val(name);
}

function changeBrightness() {
    var val = $("#brightness").val();
    var rq = $.post("http://192.168.0.70:8001/mediascheduler/control/brightness?value="+val)
    .done(function() {
    })
    .fail(function(err) {
        console.log(err);
    });
}

function changeContrast() {
    var val = $("#brightness").val();
    var rq = $.post("http://192.168.0.70:8001/mediascheduler/control/contrast?value="+val)
    .done(function() {
    })
    .fail(function(err) {
        console.log(err);
    });
}

function mask(on) {
    $("#mask").attr("class", "mask "+(on?"active":"inactive"));
}

function hideCueListForm() {
    $("#cueListForm").attr("class", "cueListForm");
    mask(false);
}

function createCueList() {

    var input = window.prompt("Please enter Cue List number and name, separated by a colon.\n(e.g. 10:My Cue List");


    if ( input ) {
        var pieces = input.split(":");
        if ( pieces.length==2 ) {
            var number = pieces[0].trim();
            var name = pieces[1].trim();

            var rq = $.post("http://192.168.0.70:8001/mediascheduler/schedule/cuelist", "{ \"name\":\""+name+"\", \"number\":\""+number+"\"}")
            .done(function() {
                location.reload();
            })
            .fail(function(err) {
                window.alert("Could not create Cue List " + err.responseText);
                console.log(err);
            });
        }
    }
}

