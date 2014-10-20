function widthOf(element) {
    return Number(window.getComputedStyle(element).width.replace(/[^\d\.\-]/g, ''));
}
function heightOf(element) {
    return Number(window.getComputedStyle(element).height.replace(/[^\d\.\-]/g, ''));
}
function parseNanoTime(input) {
    var module = Math.abs(input);
    var sign = input / module;
    var seconds = (module - (module % (1000 * 1000 * 1000))) / (1000 * 1000 * 1000);
    var minutes = (seconds - (seconds % 60)) / 60;
    seconds = seconds % 60;
    if (seconds < 10) {
        seconds = "0" + seconds;
    }
    if (sign < 0) {
        minutes = "-" + minutes;
    }
    return minutes + ":" + seconds;
}
function readapt(id) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open("GET", "/chrono?get=1&id=" + id, false);
    xmlhttp.send();
    var answer = JSON.parse(xmlhttp.responseText);
    var displayString = parseNanoTime(answer.mainTime)
    if (answer.secondaryTime > 0) {
        displayString += " + " + parseNanoTime(answer.secondaryTime);
    }
    document.getElementById("display").innerHTML = displayString;
    document.getElementById("name").innerHTML = answer.name;
    document.getElementById("stance").innerHTML = answer.stance;

    var display = document.getElementById("display");
    var chrono = document.getElementById("chrono");
    var fs = 10;
    display.style.fontSize = fs;
    while (widthOf(display) < 0.65 * window.innerWidth && heightOf(chrono) < 0.8 * window.innerHeight) {
        display.style.fontSize = fs + "px";
        fs *= 1.1;
    }
    chrono.style.marginTop = ((window.innerHeight - heightOf(chrono)) * 0.4) + "px";
}

function control(id, command) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open("GET", "/chrono?control=1&id=" + id + "&manage=1&" + command, false);
    xmlhttp.send();
    controlTags(id);
}
function controlReset(id) {
    var timeString = document.getElementById("reset").value.split(":");
    var minutes = parseInt(timeString[0]);
    var seconds = parseInt(timeString[1]);
    var time = minutes * 60 + seconds;
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open("GET", "/chrono?control=1&id=" + id + "&manage=1&type=main&action=reset&time=" + time, false);
    xmlhttp.send();
    controlTags(id);
}
function controlTags(id) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open("GET", "/chrono?get=1&id=" + id, false);
    xmlhttp.send();
    var answer = JSON.parse(xmlhttp.responseText);
    document.getElementById("main").innerHTML = answer.mainTag;
    document.getElementById("secondary").innerHTML = answer.secondaryTag;
    document.getElementById("reset").value = parseNanoTime(answer.resetTime * 1000 * 1000 * 1000);
}