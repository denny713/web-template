function logout() {
    processAuth("/api/auth/logout", "POST", null, function () {
        redirect("/login");
    });
}

function profile() {
    alert("Profile");
}