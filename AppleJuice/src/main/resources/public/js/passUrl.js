function passValues() {
    // here is where we can do some url validation
    const url = document.getElementById("url");
    const topic = document.getElementById("topic");

    if (url != null && topic != null) {
        return true;
    } else if (url == null) {
        alert("Empty URL");
    } else if (topic == null) {
        alert("No Topic specified");
    }

    return false;
}