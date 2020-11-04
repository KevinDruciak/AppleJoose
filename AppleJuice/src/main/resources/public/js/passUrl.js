function passUrl() {
    // here is where we can do some url validation
    const url = document.getElementById("url");
    if (url != null) {
        alert("Valid URL")
        return true;
    }
    alert("Empty URL");
    return false;
}