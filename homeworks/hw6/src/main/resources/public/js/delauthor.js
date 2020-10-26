function deleteAuthor(authorName) {
    fetch('https://secret-tor-59728.herokuapp.com/delauthor?name=' + authorName, {
            method: 'POST',
        }
    ).then(res => window.location.replace('https://secret-tor-59728.herokuapp.com/authors'));
}

let delButtons = document.querySelectorAll("li.author > button")
Array.prototype.forEach.call(delButtons, function(button) {
    button.addEventListener('click', deleteAuthor.bind(null, button.className));
});