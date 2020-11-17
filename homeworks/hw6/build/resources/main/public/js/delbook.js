function deleteBook(isbn) {
    fetch('https://secret-tor-59728.herokuapp.com/delbook?isbn=' + isbn, {
            method: 'POST',
        }
    ).then(res => window.location.replace("https://secret-tor-59728.herokuapp.com/books"));
}

let delButtons = document.querySelectorAll("li.book > button")
Array.prototype.forEach.call(delButtons, function(button) {
    button.addEventListener('click', deleteBook.bind(null, button.className));
});