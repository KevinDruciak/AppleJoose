function deleteBook(isbn) {
    fetch('http://localhost:7000/delbook?isbn=' + isbn, {
            method: 'POST',
        }
    ).then(res => window.location.replace("http://localhost:7000/books"));
}

let delButtons = document.querySelectorAll("li.book > button")
Array.prototype.forEach.call(delButtons, function(button) {
    button.addEventListener('click', deleteBook.bind(null, button.className));
});