document.addEventListener("DOMContentLoaded", function(){
    const bg = chrome.extension.getBackgroundPage();
    for(let i = 0; i < bg.urls.length; i++){

        const div = document.createElement('div');
        div.textContent = bg.urls[i];
        document.body.appendChild(div);

    }
}, false);
