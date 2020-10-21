window.bears = [];
chrome.runtime.onMessage.addListener(function (request, sender, sendResponse){

    window.bears.push(request.url);

});

chrome.browserAction.onClicked.addListener(function(tab){

    chrome.tabs.create({url:"../html/view/popup.html"});

});