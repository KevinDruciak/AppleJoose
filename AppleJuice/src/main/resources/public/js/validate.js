function validateUsername() {
    const name = document.getElementById("username");
    if (name.value.length < 1) {
        alert("Username cannot be empty!");
        return false;
    } else {
        return true;
    }
}

function validateSignup() {
    const username = document.getElementById("username");
    const password = document.getElementById("password");
    const confirmPW = document.getElementById("confirmPW");
    if (username.value.length < 1) {
        alert("Username cannot be empty!");
        return false;
    }
    else if (password.value.length < 1) {
        alert("Password cannot be empty!");
        return false;
    }
    if (password.value !== confirmPW.value) {
        alert("Passwords must match!")
        return false;
    }

    return true;
}