function searchRole() {
    let requestData = {};
    requestData["name"] = $("#name").val();
    requestData["description"] = $("#desc").val();
    requestData["page"] = 0;
    requestData["size"] = 10;
    requestData["sort"] = "ASC";

    let data = get("/api/role/list", requestData);
    let xtable = $('#example').DataTable();
    xtable.clear().draw();
    let no = 0;
    for (let x in data.data) {
        no++;
        let crtDate = data.data[x].createdDate;
        crtDate = crtDate.replace(crtDate.substring(11, 23), '');

        let active = data.data[x].active;
        let status = active ? "Active" : "Non Active";

        let modifyAction = '<button type="button" data-bs-toggle="modal" data-bs-target="#roleDetail"  class="btn btn-primary btn-icon" ' +
            'onClick="editRole(\'' + data.data[x].id + '\',\'' + data.data[x].name + '\',\'' + data.data[x].description + '\')">' +
            '                <i class="fas fa-edit"></i> Edit' +
            '            </button>';
        let deleteAction = '<button type="button" onClick="deleteRole(\'' + data.data[x].id + '\',\'' + data.data[x].name + '\')" class="btn btn-danger btn-icon">' +
            '                <i class="fas fa-trash"></i> Delete' +
            '            </button>';
        let deactiveAction = '<button type="button" onClick="roleDeactive(\'' + data.data[x].id + '\',\'' + data.data[x].name + '\')" class="btn btn-secondary btn-icon">' +
            '                <i class="fas fa-ban"></i> Deactivate' +
            '            </button>';
        let reactiveAction = '<button type="button" onClick="roleReactive(\'' + data.data[x].id + '\',\'' + data.data[x].name + '\')" class="btn btn-success btn-icon">' +
            '                <i class="fas fa-redo"></i> Reactivate' +
            '            </button>';
        let updStatAction = active ? deactiveAction : reactiveAction;

        let actions = modifyAction + " | " + updStatAction + " | " + deleteAction;

        xtable.row.add([
            no,
            data.data[x].name,
            data.data[x].description,
            status,
            crtDate.substring(0, 10),
            actions
        ]);
    }
    xtable.draw();
}

function saveRole() {
    alert("Save");
}

function addRole() {
    $("#id-modal").val("");
    $("#name-modal").val("");
    $("#desc-modal").val("");
}

function editRole(id, name, description) {
    $("#id-modal").val(id);
    $("#name-modal").val(name);
    $("#desc-modal").val(description);
}

function deleteRole(id, name) {
    confirm("/api/role/delete", "Are you sure want to delete this role: " + name + " ?", roleReq(id), function () {
        pageReload();
    });
}

function roleReactive(id, name) {
    confirm("/api/role/reactivate", "Are you sure want to reactivate this role: " + name + " ?", roleReq(id), function () {
        pageReload();
    });
}

function roleDeactive(id, name) {
    confirm("/api/role/deactivate", "Are you sure want to deactivate this role: " + name + " ?", roleReq(id), function () {
        pageReload();
    });
}

function roleReq(id) {
    let request = {};
    request["roleId"] = id
    return request;
}