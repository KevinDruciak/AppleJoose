function passUrl() {
    // here is where we can do some url validation
    const url = document.getElementById("url");
    if (url != null) {
        return true;
    }
    alert("Empty URL");
    return false;
}