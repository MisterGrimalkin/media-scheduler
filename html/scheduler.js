var url;

function onLoad() {

    $("#eventForm").draggable();
    $("#cueForm").draggable();
    loadSchedule();

    setInterval(function() {
        if ( !timePaused ) {
            updateTime();
        }
    }, 250);

    setInterval(function() {
        $("#schedule").contents().find("#nowMarker").css("opacity",cursorOpacity);
        cursorOpacity += cursorOpacityDelta;
        if ( cursorOpacity >= 1 ) {
            cursorOpacity = 1;
            cursorOpacityDelta = -cursorOpacityDelta;
        }
        if ( cursorOpacity <= 0 ) {
            cursorOpacity = 0;
            cursorOpacityDelta = -cursorOpacityDelta;
        }
    }, 100);
}

var cursorOpacity = 0;
var cursorOpacityDelta = 0.2;

var timePaused = false;

function loadSchedule() {
    var w = window.innerWidth - 240;
    var h = Math.max(window.innerHeight - 150, parseInt($("#sideBar").css("height")));
    $("#schedule").attr("width", w+20);
    $("#schedule").attr("height", window.innerHeight - 130);
    $("#schedule").attr("src", "viewSchedule.php?date="+$("#searchDate").val()+"&width="+w+"&height="+h);
}

////////////
// Events //
////////////

function showEventForm() {
    mask(true);
    $("#eventFormHeader").html("Create Event");
    $("#eventForm").attr("class", "form shown editEvent");
    $("#eventCueId").val(highlightedId);
    $("#eventFormDate").val($("#searchDate").val());
    $("#eventFormOK").html("Create");
    $("#eventFormStartTime").focus();
}

function hideEventForm() {
    $("#eventForm").attr("class", "form editEvent");
    mask(false);
}

var selectedEvent = -1;


function editEvents() {
    window.alert("cunts are nice");
}

function editSelectedEvent() {
    console.log("eidt");
    editEvent($("#selectedEventId").prop("data-value"), url+"/schedule/event");
}

function editEvent(id, uri) {
    $.ajax({
        url: uri,
        type: 'GET',
        data: {
            id: ""+id
        },
        success: function(response) {
            var event = JSON.parse(response);
            console.log(event["cueId"]);
            showEventForm();
            $("#eventFormHeader").html("Edit Event");
            $("#eventHiddenId").val(event["id"]);
            $("#eventFormDate").val(event["startDate"]);
            $("#eventFormStartTime").val(event["startTime"])
            $("#eventFormEndTime").val(event["endTime"])
            $("#eventCueId").val(event["cueId"]);
            $("#eventFormOK").html("Update");
            $("#eventFormStartDate").focus();
        },
        fail: function(response) {
            window.alert(response);
        }
    });

}

function removeEvent() {
    var id = $("#selectedEventId").prop("data-value");
    var response = window.confirm("Really delete this event?");
    if ( response ) {
        if ( id>=0 ) {
            $.ajax({
                url: 'actions/removeEvent.php',
                type: 'POST',
                data: ""+id,
                success: function(response) {
                    location.reload();
                },
                fail: function(response) {
                    window.alert(response);
                }
            });
        }
    }
}

function selectEvent(id) {
    if ( selectedEvent>=0 ) {
        $(".event"+selectedEvent).css("background-color", "");
        $("#removeEvent", parent.document).prop("disabled", "disabled");
        $("#editEvent", parent.document).prop("disabled", "disabled");
    }
    if ( id>=0 && selectedEvent!==id ) {
        $(".event"+id).css("background-color", "red");
        $("#removeEvent", parent.document).prop("disabled", "");
        $("#editEvent", parent.document).prop("disabled", "");
        highlightEvents(-1);
        selectedEvent = id;
    } else {
        selectedEvent = -1;
    }
    console.log("select " + selectedEvent);
    $("#selectedEventId", parent.document).prop("data-value", selectedEvent);
    console.log($("#selectedEventId", parent.document).prop("data-value"));
}

var highlightedId = -1;

function highlightEvents(id) {
    if ( highlightedId>=0 ) {
        $(".eventOnCue"+highlightedId).css("background-color", "");
        $("#schedule").contents().find(".eventOnCue"+highlightedId).css("background-color", "");
        $("#schedule").contents().find(".eventOnCue"+highlightedId).css("color", "");
    }
    if ( highlightedId===id ) {
        highlightedId = -1;
    } else {
        highlightedId = id;
        $(".eventOnCue"+highlightedId).css("background-color", "yellow");
        $("#schedule").contents().find(".eventOnCue"+id).css("color","black");
        $("#schedule").contents().find(".eventOnCue"+id).css("background-color","yellow");
    }
}


//////////
// Time //
//////////

var offline = false;

function updateTime() {
    var rq = $.get(url + "/schedule/time");
    rq.done(function(response) {
        $("#timePanel").attr("class", "timePanel");
        $("#timePanel").html(response.substr(0,8));
        if ( offline ) {
            timePaused = true;
            location.reload();
        }
    })
    .fail(function(err) {
        $("#timePanel").attr("class", "timePanel offline");
        $("#timePanel").html("Offline");
        if ( !offline ) {
            mask(true);
        }
        offline = true;
    });
}


//////////
// Date //
//////////

function dateString(date) {
    return date.getFullYear() + "-" + ("0"+(date.getMonth()+1)).slice(-2) + "-" + ("0"+date.getDate()).slice(-2);
}

function redirect(target) {
    window.location.assign(target);
}

function today() {
    mask(true);
    redirect("/?date="+dateString(new Date()));
}

function moveDays(days) {
    var date = new Date($("#searchDate").val());
    date = new Date(date.getTime() + (days*24*60*60*1000))
    mask(true);
    redirect("/?date="+dateString(date));
}

//////////
// Cues //
//////////

function showCueForm(number, name) {
    mask(true);
    $("#cueForm").attr("class", "form shown cue");
    $("#cueFormNumber").val(number);
    $("#cueFormName").val(name);
    $("#cueFormNumber").focus();
}

function hideCueForm() {
    $("#cueForm").attr("class", "form cue");
    mask(false);
}

function mask(on) {
    $("#mask").attr("class", "mask"+(on?"on":"off"));
}

function changeBrightness() {
    var val = $("#brightness").val();
    var rq = $.post(url + "/control/brightness?value="+val)
    .done(function() {
    })
    .fail(function(err) {
        console.log(err);
    });
}

function changeContrast() {
    var val = $("#contrast").val();
    var rq = $.post(url + "/control/contrast?value="+val)
    .done(function() {
    })
    .fail(function(err) {
        console.log(err);
    });
}
