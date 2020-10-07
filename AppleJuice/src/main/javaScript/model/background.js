/**
function
chrome.history.search({},
    function (historyItems) {
      const newsWebsites = ['https://www.cnn.com/', 'https://www.foxnews.com'];

      chrome.history.onVisited.addListener(function (result) {
        for (let i = 0; i < newsWebsites.length; i++) {
          if (result.url == newsWebsites[i]) {
            alert("This is a news Website");
          }
        }
      });
});

 */
