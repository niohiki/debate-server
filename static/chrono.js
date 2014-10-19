function widthOf(element) {
    return Number(window.getComputedStyle(element).width.replace(/[^\d\.\-]/g, ''));
}
function heightOf(element) {
    return Number(window.getComputedStyle(element).height.replace(/[^\d\.\-]/g, ''));
}
function readapt() {
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