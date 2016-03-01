<?php

$url = "http://192.168.0.70:8001/mediascheduler";

function getCueLists() {

    $c = curl_init();
    curl_setopt($c, CURLOPT_URL, "http://192.168.0.70:8001/mediascheduler/schedule/cuelist");
    curl_setopt($c, CURLOPT_RETURNTRANSFER, 1);

    $output = curl_exec($c);

    $result = [];

    if ( $output !== false ) {

        $cuelists = json_decode($output, true);

        if ( count($cuelists)>0 ) {

            foreach($cuelists as $cueList) {
                $result[$cueList["id"]] = $cueList;
            }
        }
    }

    curl_close($c);

    return $result;
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