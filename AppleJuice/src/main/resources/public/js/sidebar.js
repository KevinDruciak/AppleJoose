/* Set the width of the sidebar to 250px and the left margin of the page content to 250px */

let isOpen = false;

function toggle() {
    if (isOpen) {
        document.getElementById("mySidebar").style.width = "50px";
        document.getElementById("main2").style.marginLeft = "50px";
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
        document.getElementById("mySidebar").style.width = "250px";
        document.getElementById("main2").style.marginLeft = "250px";
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