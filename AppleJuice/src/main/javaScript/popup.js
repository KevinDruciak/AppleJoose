document.addEventListener("DOMContentLoaded", function(){
    const bg = chrome.extension.getBackgroundPage();
    for(let i = 0; i < bg.bears.length; i++){

        const div = document.createElement('div');
        div.textContent = bg.bears[i];
        document.body.appendChild(div);

    }
}, false);
