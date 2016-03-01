url = "http://192.168.0.70:8001/mediascheduler";

function onLoad() {

    setInterval(function() {
        updateTime();
    }, 250);

    $("#eventForm").draggable();
    $("#cueListForm").draggable();
}

var selectedEvent = -1;

function removeEvent() {
    var response = window.confirm("Really delete this event?");
    if ( response ) {
        if ( selectedEvent>=0 ) {
            $.ajax({
                url: 'actions/removeevent.php',
                type: 'POST',
                data: ""+selectedEvent,
                success: function(response) {
                    location.reload();
                }
            });
        }
    }
}

function selectEvent(id) {
    console.log(id);
    if ( selectedEvent>=0 ) {
        $(".event"+selectedEvent).css("background-color", "");
        $("#removeEvent").prop("disabled", "disabled");
    }
    if ( selectedEvent!==id ) {
        $(".event"+id).css("background-color", "red");
        $("#removeEvent").prop("disabled", "");
        selectedEvent = id;
    } else {
        selectedEvent = -1;
    }
}

var highlightedId = -1;

function highlightEvents(id) {
    if ( highlightedId>=0 ) {
        $(".eventOnCueList"+highlightedId).css("background-color", "");
        $(".eventOnCueList"+highlightedId).css("color", "");
    }
    if ( highlightedId===id ) {
        highlightedId = -1;
    } else {
        highlightedId = id;
        $(".eventOnCueList"+id).css("color","black");
        $(".eventOnCueList"+id).css("background-color","yellow");
    }
}

var offline = false;

function updateTime() {
    var rq = $.get(url + "/schedule/time");
    rq.done(function(response) {
        $("#timePanel").attr("class", "timePanel");
        $("#timePanel").html(response.substr(0,8));
        if ( offline ) {
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

function dateString(date) {
    return date.getFullYear() + "-" + ("0"+(date.getMonth()+1)).slice(-2) + "-" + ("0"+date.getDate()).slice(-2);
}

function redirect(target) {
    window.location.assign(target);
}

function today() {
    mask(true);
    console.log(dateString(new Date()))
    redirect("/?date="+dateString(new Date()));
}

function moveDays(days) {
    var date = new Date($("#searchDate").val());
    date = new Date(date.getTime() + (days*24*60*60*1000))
    mask(true);
    redirect("/?date="+dateString(date));
}

function showCueListForm(number, name) {
    mask(true);
    $("#cueListForm").attr("class", "form shown cueList");
    $("#cueListFormNumber").val(number);
    $("#cueListFormName").val(name);
    $("#cueListFormNumber").focus();
}

function showEventForm() {
    mask(true);
    $("#eventForm").attr("class", "form shown editEvent");
    $("#eventCueList").val(highlightedId);
}

function hideEventForm() {
    $("#eventForm").attr("class", "form editEvent");
    mask(false);
}

function hideCueListForm() {
    $("#cueListForm").attr("class", "form cueList");
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
    var val = $("#brightness").val();
    var rq = $.post(url + "/control/contrast?value="+val)
    .done(function() {
    })
    .fail(function(err) {
        console.log(err);
    });
}
