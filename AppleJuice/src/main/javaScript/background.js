window.urls = [];
chrome.runtime.onMessage.addListener(function (request, sender, sendResponse){

    let req = new XMLHttpRequest();
    req.open("POST", "http://localhost:7000/chromeaddarticle", true);
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    var response = req.send("username=Connor&url=" + request.url + "&topic=Weather");
    window.urls.push(request.url);

});

chrome.browserAction.onClicked.addListener(function(tab){

    chrome.tabs.create({url:"../html/view/popup.html"});

});
