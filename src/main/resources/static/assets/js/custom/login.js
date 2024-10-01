document.addEventListener('DOMContentLoaded', function () {
    const loginForm = document.querySelector('form');

    loginForm.addEventListener('keydown', function (event) {
        if (event.key === 'Enter') {
            event.preventDefault();
            doLogin();
        }
    });
});

function doLogin() {
    let username = $("#username").val();
    let password = $("#password").val();
    if (username === "" || username == null) {
        showNotice('info', "Warning", "Please fill username");
    } else if (password === "" || password == null) {
        showNotice('info', "Warning", "Please fill password");
    } else {
        let data = {};
        data["username"] = username;
        data["password"] = password;

        processAuth("/api/auth/login", "POST", data, function () {
            redirect("/dashboard");
        });
    }
}