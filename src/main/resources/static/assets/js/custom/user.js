function searchUsers() {
    let requestData = {};
    requestData["username"] = $("#username").val();
    requestData["name"] = $("#name").val();
    requestData["email"] = $("#email").val();
    requestData["role"] = $("#role").val();
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
        let updStatAction = active ? '<a href="#" onClick="deactive(\'' + data.data[x].id + '\')">Deactivate&nbsp;User</a>'
            : '<a href="#" onClick="reactive(\'' + data.data[x].id + '\')">Reactivate&nbsp;User</a>';
        let resetAction = '<a href="#" onClick="resetPass(\'' + data.data[x].id + '\')">Reset&nbsp;Password</a>';
        let deleteAction = '<a href="#" onClick="deleteUser(\'' + data.data[x].id + '\')">Delete&nbsp;User</a>';
        let actions = updStatAction + " | " + resetAction + " | " + deleteAction

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

function resetPass(id) {
    alert("Reset " + id);
}

function deleteUser(id) {
    alert("Delete " + id);
}

function reactive(id) {
    alert("Reactivate " + id);
}

function deactive(id) {
    alert("Deactivate " + id);
}