function saveUser() {
    let userId = $("#id-modal").val();
    let username = $("#username-modal").val();
    let name = $("#name-modal").val();
    let email = $("#email-modal").val();
    let role = $("#role-modal").val();

    if (username === "" || username == null) {
        showNotice('warning', "Warning", "Please insert username");
    } else if (name === "" || name == null) {
        showNotice('warning', "Warning", "Please insert name");
    } else if (role === "" || role == null) {
        showNotice('warning', "Warning", "Please choose role");
    } else {
        if (email === "" || email == null) {
            email = "-";
        }

        let request = {};
        request["username"] = username;
        request["name"] = name;
        request["email"] = email;
        request["roleId"] = role;

        if (userId === "" || userId == null) {
            confirm("/api/user/register", "Are you sure want to register this user: " + username + " ?", request, function () {
                pageReload();
            });
        } else {
            request["userId"] = userId;
            confirm("/api/user/update", "Are you sure want to update this user: " + username + " ?", request, function () {
                pageReload();
            });
        }
    }
}

function fillRoles(param) {
    $("#role option").remove();
    appendOptions(
        document.getElementById("role"),
        get("/api/role/options"),
        param
    );
}

function fillRolesModal(param) {
    $("#role-modal option").remove();
    appendOptions(
        document.getElementById("role-modal"),
        get("/api/role/options"),
        param
    );
}

function searchUsers() {
    let role = $("#role option:selected").text();
    if (role === "All") {
        role = $("#role").val();
    }

    let requestData = {};
    requestData["username"] = $("#username").val();
    requestData["name"] = $("#name").val();
    requestData["email"] = $("#email").val();
    requestData["role"] = role;
    requestData["page"] = 0;
    requestData["size"] = 10;
    requestData["sort"] = "ASC";

    let data = get("/api/user/list", requestData);
    let xtable = $('#example').DataTable();
    xtable.clear().draw();
    let no = 0;
    for (let x in data.data) {
        no++;
        let crtDate = data.data[x].createdDate;
        crtDate = crtDate.replace(crtDate.substring(11, 23), '');

        let active = data.data[x].active;
        let status = active ? "Active" : "Non Active";

        let modifyAction = '<button type="button" data-bs-toggle="modal" data-bs-target="#userDetail"  class="btn btn-primary btn-icon" ' +
            'onClick="edit(\'' + data.data[x].id + '\',\'' + data.data[x].username + '\',\'' + data.data[x].name + '\',\'' + data.data[x].email + '\',\'' + data.data[x].role + '\')">' +
            '                <i class="fas fa-edit"></i> Edit' +
            '            </button>';
        let deactiveAction = '<button type="button" onClick="deactive(\'' + data.data[x].id + '\',\'' + data.data[x].username + '\')" class="btn btn-secondary btn-icon">' +
            '                <i class="fas fa-ban"></i> Deactivate' +
            '            </button>';
        let reactiveAction = '<button type="button" onClick="reactive(\'' + data.data[x].id + '\',\'' + data.data[x].username + '\')" class="btn btn-success btn-icon">' +
            '                <i class="fas fa-redo"></i> Reactivate' +
            '            </button>';
        let updStatAction = active ? deactiveAction : reactiveAction;
        let resetAction = '<button type="button" onClick="resetPass(\'' + data.data[x].id + '\',\'' + data.data[x].username + '\')" class="btn btn-warning btn-icon">' +
            '                <i class="fas fa-lock"></i> Reset Password' +
            '            </button>';
        let deleteAction = '<button type="button" onClick="deleteUser(\'' + data.data[x].id + '\',\'' + data.data[x].username + '\')" class="btn btn-danger btn-icon">' +
            '                <i class="fas fa-trash"></i> Delete' +
            '            </button>';

        let actions = modifyAction + " | " + updStatAction + " | " + resetAction + " | " + deleteAction;

        xtable.row.add([
            no,
            data.data[x].username,
            data.data[x].name,
            data.data[x].email,
            data.data[x].role,
            status,
            crtDate.substring(0, 10),
            actions
        ]);
    }
    xtable.draw();
}

function resetPass(id, username) {
    confirm("/api/user/password/reset", "Are you sure want to reset password this user: " + username + " ?", generateRequest(id), function () {
        pageReload();
    });
}

function deleteUser(id, username) {
    confirm("/api/user/delete", "Are you sure want to delete this user: " + username + " ?", generateRequest(id), function () {
        pageReload();
    });
}

function reactive(id, username) {
    confirm("/api/user/reactivate", "Are you sure want to reactivate this user: " + username + " ?", generateRequest(id), function () {
        pageReload();
    });
}

function deactive(id, username) {
    confirm("/api/user/deactivate", "Are you sure want to deactivate this user: " + username + " ?", generateRequest(id), function () {
        pageReload();
    });
}

function edit(id, username, name, email, role) {
    $("#id-modal").val(id);
    $("#username-modal").val(username);
    $("#name-modal").val(name);
    $("#email-modal").val(email);
    fillRolesModal(role);
}

function addNew() {
    $("#id-modal").val("");
    $("#username-modal").val("");
    $("#name-modal").val("");
    $("#email-modal").val("");
    fillRolesModal();
}

function generateRequest(id) {
    let request = {};
    request["userId"] = id;
    return request;
}