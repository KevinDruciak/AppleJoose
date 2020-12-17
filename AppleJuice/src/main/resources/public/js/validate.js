function validateUsername() {
    const name = document.getElementById("username");
    if (name.value.length < 1) {
        alert("Username cannot be empty!");
        return false;
    }

    const pw = document.getElementById("password");
    if (pw.value.length < 1) {
        alert("Password cannot be empty!");
        return false;
    }

    return true;
}

function validateSignup() {
    const username = document.getElementById("username");
    const password = document.getElementById("password");
    const confirmPW = document.getElementById("confirmPW");
    if (username.value.length < 1) {
        alert("Username cannot be empty!");
        return false;
    }
    else if (username.value.length > 20) {
        alert("Username exceeds 20 character limit!");
        return false;
    }
    else {
        let usernameRegex = new RegExp("^[a-zA-Z0-9_]{1,20}$");
        if (!usernameRegex.test(username.value)) {
            alert("Username can contain only letters, numbers, or underscores!");
            return false;
        }
    }

    if (password.value.length < 1) {
        alert("Password cannot be empty!");
        return false;
    }
    else if (password.value.length < 6) {
        alert("Password must be at least 6 characters long!");
        return false;
    }
    else if (password.value.length > 20) {
        alert("Password exceeds 20 character limit!")
    }
    else {
        let passwordRegex = new RegExp("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$");
        if (!passwordRegex.test(password.value)) {
            alert("Password can contain only letters and numbers, and must include" +
                " at least one letter and one number!");
            return false;
        }
    }

    if (password.value !== confirmPW.value) {
        alert("Passwords must match!")
        return false;
    }

    return true;
}