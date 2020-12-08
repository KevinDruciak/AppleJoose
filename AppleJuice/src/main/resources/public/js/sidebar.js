/* Set the width of the sidebar to 250px and the left margin of the page content to 250px */

let isOpen = false;

function deleteAllCookies() {
    var cookies = document.cookie.split(";");

    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        var eqPos = cookie.indexOf("=");
        var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
        document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    }
}

function toggle() {
    if (isOpen) {
        if (document.getElementById("title").innerText === "General Statistics") {
            document.getElementById("news-chart").style.marginLeft = "50px";
            document.getElementById("topics-chart").style.marginLeft = "50px";
            document.getElementById("bias-chart").style.marginLeft = "50px";
        } else {
            document.getElementById("main2").style.marginLeft = "50px";
        }
        document.getElementById("mySidebar").style.width = "50px";

        var str1 = document.getElementById("sbbutton").innerHTML;
        document.getElementById("sbbutton").innerHTML = str1.replace("&lt;", "&gt;");

        str1 = document.getElementById("dashboard").innerHTML;
        document.getElementById("dashboard").style.display = "none";//.innerHTML = str1.replace("Dashboard", " ");

        //str1 = document.getElementById("userstats").innerHTML;
        //document.getElementById("userstats").style.display = "none";//.innerHTML = str1.replace("User Statistics", " ");

        str1 = document.getElementById("genstats").innerHTML;
        document.getElementById("genstats").style.display = "none";//.innerHTML = str1.replace("General Statistics", " ");

        str1 = document.getElementById("about").innerHTML;
        document.getElementById("about").style.display = "none";//.innerHTML = str1.replace("About", " ");

        str1 = document.getElementById("signout").innerHTML;
        document.getElementById("signout").style.display = "none";//.innerHTML = str1.replace("Logout", " ");

        isOpen = false;
    } else {
        if (document.getElementById("title").innerText === "General Statistics") {
            document.getElementById("news-chart").style.marginLeft = "250px";
            document.getElementById("topics-chart").style.marginLeft = "250px";
            document.getElementById("bias-chart").style.marginLeft = "250px";

        } else {
            document.getElementById("main2").style.marginLeft = "250px";
        }
        document.getElementById("mySidebar").style.width = "250px";
        var str2 = document.getElementById("sbbutton").innerHTML;
        document.getElementById("sbbutton").innerHTML = str2.replace("&gt;", "&lt;");

        str2 = document.getElementById("dashboard").innerHTML;
        document.getElementById("dashboard").style.display = "block";//.innerHTML = "Dashboard";

        //str2 = document.getElementById("userstats").innerHTML;
        //document.getElementById("userstats").style.display = "block";//.innerHTML = "User Statistics";

        str2 = document.getElementById("genstats").innerHTML;
        document.getElementById("genstats").style.display = "block";//.innerHTML = "General Statistics";

        str2 = document.getElementById("about").innerHTML;
        document.getElementById("about").style.display = "block";//.innerHTML = "About";

        str2 = document.getElementById("signout").innerHTML;
        document.getElementById("signout").style.display = "block";//.innerHTML = "Logout";

        isOpen = true;
    }
}