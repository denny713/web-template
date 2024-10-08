function doLogin() {
    let username = $("#username").val();
    let password = $("#password").val();
    if (username === "" || username == null) {
        showNotice('info', "Warning", "Please fill username");
    } else if (password === "" || password == null) {
        showNotice('info', "Warning", "Please fill password");
    } else {
        doAuth(username, password);
    }
}

function changePassExecute() {
    $("#current-pass-modal").val("");
    $("#new-pass-modal").val("");
    $("#confirm-pass-modal").val("");
    $("#username-modal").val($("#username-profile").val());
}

function doChangePass() {
    let oldPass = $("#current-pass").val();
    let newPass = $("#new-pass").val();
    let confirmPass = $("#confirm-pass").val();
    let username = $("#username").val();
    if (username === "" || username == null) {
        username = "-";
    }

    if (oldPass === "" || oldPass == null) {
        showNotice('warning', "Warning", "Old password cannot be null or empty");
    } else if (newPass === "" || newPass == null) {
        showNotice('warning', "Warning", "New password cannot be null or empty");
    } else if (confirmPass === "" || confirmPass == null) {
        showNotice('warning', "Warning", "Confirmation password cannot be null or empty");
    } else if (oldPass === newPass || oldPass === confirmPass) {
        showNotice('warning', "Warning", "Old password cannot same as new password");
    } else if (newPass !== confirmPass) {
        showNotice('warning', "Warning", "Invalid confirmation password");
    } else {
        let request = {};
        request["oldPassword"] = oldPass;
        request["newPassword"] = newPass;

        confirm("/api/auth/update-pass", "Are yut sure want to change this password?", request, function () {
            doAuth(username, newPass);
        });
    }
}

function doChangePassFromProfile() {
    let oldPass = $("#current-pass-modal").val();
    let newPass = $("#new-pass-modal").val();
    let confirmPass = $("#confirm-pass-modal").val();
    let username = $("#username-modal").val();
    if (username === "" || username == null) {
        username = "-";
    }

    if (oldPass === "" || oldPass == null) {
        showNotice('warning', "Warning", "Old password cannot be null or empty");
    } else if (newPass === "" || newPass == null) {
        showNotice('warning', "Warning", "New password cannot be null or empty");
    } else if (confirmPass === "" || confirmPass == null) {
        showNotice('warning', "Warning", "Confirmation password cannot be null or empty");
    } else if (oldPass === newPass || oldPass === confirmPass) {
        showNotice('warning', "Warning", "Old password cannot same as new password");
    } else if (newPass !== confirmPass) {
        showNotice('warning', "Warning", "Invalid confirmation password");
    } else {
        let request = {};
        request["oldPassword"] = oldPass;
        request["newPassword"] = newPass;

        confirm("/api/auth/update-pass", "Are yut sure want to change this password?", request, function () {
            reload();
        });
    }
}

function doAuth(user, pass) {
    let data = {};
    data["username"] = user;
    data["password"] = pass;

    processAuth("/api/auth/login", "POST", data);
}