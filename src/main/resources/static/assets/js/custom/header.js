function logout() {
    processAuth("/api/auth/logout", "POST", null, function () {
        redirect("/login");
    });
}

function profile() {
    redirect("/profile");
}

function fillName() {
    let name = getValueFromToken(getCookie('access-token'), "name");
    if (name === "" || name == null) {
        name = "-";
    }

    let nameElement = document.getElementById("name-login");
    nameElement.innerHTML = "Welcome, " + name;
}