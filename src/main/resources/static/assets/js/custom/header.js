function logout() {
    process("/api/auth/logout", "POST", null, function () {
        redirect("/login");
    });
}

function profile() {
    alert("Profile");
}