function logout() {
    processAuth("/api/auth/logout", "POST", null, function () {
        redirect("/login");
    });
}

function profile() {
    redirect("/profile");
}