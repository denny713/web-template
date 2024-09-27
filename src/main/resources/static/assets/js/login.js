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

        process("/api/auth/login", "POST", data, function () {
            redirect("/dashboard");
        });
    }
}