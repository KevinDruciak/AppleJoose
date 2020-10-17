function validateUsername() {
    const name = document.getElementById("username");
    if (name.value.length < 1) {
        alert("Username cannot be empty!");
        return false;
    } else {
        return true;
    }
}

function validateAuthor() {
    let name = document.getElementById("name");
    let nameRegex = new RegExp("[a-zA-Z]*\\s[a-zA-Z]*");
    if (!nameRegex.test(name.value)) {
        alert("Not a valid name!");
        return false;
    }
    return true;
}

function validateBook() {
    let isbn = document.getElementById("isbn");
    let isbnRegex = new RegExp("[0-9]{10}|[0-9]{3}[-][0-9]{10}");
    if (!isbnRegex.test(isbn.value)) {
        alert("Not a valid ISBN!");
        return false;
    }

    let title = document.getElementById("title");
    if (title.value.length < 1) {
        alert("Book title cannot be empty!");
        return false;
    }

    return true;
}



