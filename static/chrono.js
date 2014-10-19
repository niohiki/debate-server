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
    var minutes = sign * (seconds - (seconds % 60)) / 60;
    seconds = seconds % 60;
    if (seconds < 10) {
        seconds = "0" + seconds;
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