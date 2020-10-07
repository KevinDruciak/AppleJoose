let checkPageButton = document.getElementById('checkPage');

checkPageButton.addEventListener('click', function() {
    chrome.tabs.create({url: "localhost:7000"}, {});
    }, true);
