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
                reload();
            });
        } else {
            request["userId"] = userId;
            confirm("/api/user/update", "Are you sure want to update this user: " + username + " ?", request, function () {
                reload();
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

function generateActions(response) {
    let upd = $("#edit-access").val();
    if (upd === "" || upd == null) {
        upd = "false";
    }

    let del = $("#delete-access").val();
    if (del === "" || del == null) {
        del = "false";
    }

    let active = response.active;
    let modifyAction = '<button type="button" data-bs-toggle="modal" data-bs-target="#userDetail" class="btn btn-primary btn-icon btn-sm" ' +
        'onClick="edit(\'' + response.id + '\',\'' + response.username + '\',\'' + response.name + '\',\'' + response.email + '\',\'' + response.roleDescription + '\')">' +
        '                <i class="fas fa-edit"></i> Edit' +
        '            </button>';
    let deactiveAction = '<button type="button" onClick="deactive(\'' + response.id + '\',\'' + response.username + '\')" class="btn btn-secondary btn-icon btn-sm">' +
        '                <i class="fas fa-ban"></i> Deactivate' +
        '            </button>';
    let reactiveAction = '<button type="button" onClick="reactive(\'' + response.id + '\',\'' + response.username + '\')" class="btn btn-success btn-icon btn-sm">' +
        '                <i class="fas fa-redo"></i> Reactivate' +
        '            </button>';
    let updStatAction = active ? deactiveAction : reactiveAction;
    let resetAction = '<button type="button" onClick="resetPass(\'' + response.id + '\',\'' + response.username + '\')" class="btn btn-warning btn-icon btn-sm">' +
        '                <i class="fas fa-lock"></i> Reset Password' +
        '            </button>';
    let deleteAction = '<button type="button" onClick="deleteUser(\'' + response.id + '\',\'' + response.username + '\')" class="btn btn-danger btn-icon btn-sm">' +
        '                <i class="fas fa-trash"></i> Delete' +
        '            </button>';

    let actions;
    if (upd === "true" && del === "true") {
        actions = modifyAction + " | " + updStatAction + " | " + resetAction + " | " + deleteAction;
    } else if (upd === "true" && del === "false") {
        actions = modifyAction + " | " + updStatAction + " | " + resetAction;
    } else if (upd === "false" && del === "true") {
        actions = deleteAction;
    } else {
        actions = "-";
    }

    return actions;
}

function searchUsers() {
    let role = $("#role option:selected").text();
    if (role === "All") {
        role = $("#role").val();
    }

    $('#example').DataTable({
        "destroy": true,
        "serverSide": true,
        "processing": true,
        "ajax": {
            "url": "/api/user/list",
            "type": "POST",
            "contentType": "application/json",
            "data": function (d) {
                return JSON.stringify({
                    "draw": d.draw,
                    "username": $("#username").val(),
                    "name": $("#name").val(),
                    "email": $("#email").val(),
                    "role": role,
                    "active": true,
                    "page": Math.ceil(d.start / d.length),
                    "size": d.length,
                    "sort": "ASC"
                });
            },
            "dataSrc": function (json) {
                return json.data;
            }
        },
        "columns": [
            {"data": "username"},
            {"data": "name"},
            {"data": "email"},
            {"data": "role"},
            {
                "data": "active", "render": function (data) {
                    return data ? "Active" : "Non Active";
                }
            },
            {
                "data": "createdDate", "render": function (data) {
                    return data.substring(0, 10);
                }
            },
            {
                "data": null, "render": function (data) {
                    return generateActions(data);
                }
            }
        ],
        "pageLength": 10,
        "paging": true,
        "searching": false,
        "ordering": true,
        "info": true,
        "autoWidth": false
    });
}

function resetPass(id, username) {
    confirm("/api/user/password/reset", "Are you sure want to reset password this user: " + username + " ?", generateRequest(id), function () {
        reload();
    });
}

function deleteUser(id, username) {
    confirm("/api/user/delete", "Are you sure want to delete this user: " + username + " ?", generateRequest(id), function () {
        reload();
    });
}

function reactive(id, username) {
    confirm("/api/user/reactivate", "Are you sure want to reactivate this user: " + username + " ?", generateRequest(id), function () {
        reload();
    });
}

function deactive(id, username) {
    confirm("/api/user/deactivate", "Are you sure want to deactivate this user: " + username + " ?", generateRequest(id), function () {
        reload();
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

function fillProfile() {
    let data = get("/api/user/me");
    let status = data.data.active;
    document.getElementById("username-profile").innerHTML = data.data.username;
    document.getElementById("name-profile").innerHTML = data.data.name;
    document.getElementById("email-profile").innerHTML = data.data.email;
    document.getElementById("role-profile").innerHTML = data.data.role;
    document.getElementById("desc-profile").innerHTML = data.data.roleDescription;
    document.getElementById("stat-profile").innerHTML = (status === true ? "Active" : "Non Active");
}

function setCreatePermission() {
    let createAccess = $("#create-access").val();
    let btnAdd = document.getElementById("btn-add");

    btnAdd.style.display = "none";
    if (createAccess !== "false") {
        btnAdd.style.display = "block";
    }
}