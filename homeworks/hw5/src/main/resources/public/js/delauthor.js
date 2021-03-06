function deleteAuthor(authorName) {
    fetch('http://localhost:7000/delauthor?name=' + authorName, {
            method: 'POST',
        }
    ).then(res => window.location.replace('http://localhost:7000/authors'));
}

let delButtons = document.querySelectorAll("li.author > button")
Array.prototype.forEach.call(delButtons, function(button) {
    button.addEventListener('click', deleteAuthor.bind(null, button.className));
});