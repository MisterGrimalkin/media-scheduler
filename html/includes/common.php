<?php

define("URL", "http://192.168.0.70:8001/mediascheduler");

function getBrightness() {
    return get("/control/brightness");
}

function getContrast() {
    return get("/control/contrast");
}

function getSchedule($date) {
    $dateStr = $date->format("Y-m-d");
    $output = get("/schedule?date=$dateStr");
    return json_decode($output, true);
}

function getCues() {
    $result = [];
    $output = get("/cue");
    if ( $output !== false ) {
        $cues = json_decode($output, true);
        if ( count($cues)>0 ) {
            foreach($cues as $cue) {
                $result[$cue["id"]] = $cue;
            }
        }
    }
    return $result;
}

function get($url) {
    $c = curl_init();
    curl_setopt($c, CURLOPT_URL, URL.$url);
    curl_setopt($c, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($c, CURLOPT_CUSTOMREQUEST, "GET");
    $output = curl_exec($c);
    curl_close($c);
    return $output;
}

function errorMessage($message, $redirect) {
    return "<script>
                window.alert('$message');
                window.location.assign('$redirect');
            </script>";
}

/////////
// DOM //
/////////

function div($content, $class="", $style="") {
    if ( $content ) {
        return wrap("div", asAttributes($class, $style), $content, false, true);
    } else {
        return wrap("div", asAttributes($class, $style), $content, true, true);
    }
}

function section($content, $class="", $style="") {
    return wrap("section", asAttributes($class, $style), $content);
}

function article($content, $class="", $style="") {
    return wrap("article", asAttributes($class, $style), $content);
}

function wrap($element, $attributes, $content, $inline=false, $tight=false) {
    $extras = compileAttributes($attributes);
    $i = $inline ? "" : "\n";
    $t = $tight ? "" : "\n";
    return "$t<$element$extras>$i$content$i</$element>$t";
}

function wrapStandalone($element, $attributes = [], $tight=false) {
    $extras = compileAttributes($attributes);
    $t = $tight ? "" : "\n";
    return "$t<$element$extras />$t";
}

function wrapStart($element, $attributes = []) {
    $extras = compileAttributes($attributes);
    return "\n<$element$extras>\n";
}

function wrapEnd($element) {
    return "\n</$element>\n";
}

function indent($content, $chars=4) {
    $indent = str_repeat(" ", $chars);
    return $indent . str_replace("\n", "\n$indent", $content);
}

function compileAttributes($attributes) {
    $extras = "";
    foreach ( $attributes as $name=>$value ) {
        $extras .= " $name='$value'";
    }
    return $extras;
}

function asAttributes($class, $style) {
    $result = [];
    if ( $class ) {
        $result["class"] = $class;
    }
    if ( $style) {
        $result["style"] = $style;
    }
    return $result;
}