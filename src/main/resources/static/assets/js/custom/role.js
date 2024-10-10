function searchRole() {
    let upd = $("#edit-access").val();
    if (upd === "" || upd == null) {
        upd = "false";
    }

    let del = $("#delete-access").val();
    if (del === "" || del == null) {
        del = "false";
    }

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

        let modifyAction = '<button type="button" data-bs-toggle="modal" data-bs-target="#roleDetail" class="btn btn-primary btn-icon btn-sm" ' +
            'onClick="editRole(\'' + data.data[x].id + '\')">' +
            '                <i class="fas fa-edit"></i> Edit' +
            '            </button>';
        let deleteAction = '<button type="button" onClick="deleteRole(\'' + data.data[x].id + '\',\'' + data.data[x].name + '\')" class="btn btn-danger btn-icon btn-sm">' +
            '                <i class="fas fa-trash"></i> Delete' +
            '            </button>';
        let deactiveAction = '<button type="button" onClick="roleDeactive(\'' + data.data[x].id + '\',\'' + data.data[x].name + '\')" class="btn btn-secondary btn-icon btn-sm">' +
            '                <i class="fas fa-ban"></i> Deactivate' +
            '            </button>';
        let reactiveAction = '<button type="button" onClick="roleReactive(\'' + data.data[x].id + '\',\'' + data.data[x].name + '\')" class="btn btn-success btn-icon btn-sm">' +
            '                <i class="fas fa-redo"></i> Reactivate' +
            '            </button>';
        let updStatAction = active ? deactiveAction : reactiveAction;

        let actions = null;
        if (upd === "true" && del === "true") {
            actions = modifyAction + " | " + updStatAction + " | " + deleteAction;
        } else if (upd === "true" && del === "false") {
            actions = modifyAction + " | " + updStatAction;
        } else if (upd === "false" && del === "true") {
            actions = deleteAction;
        } else {
            actions = "-";
        }

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
    let foundDuplicate = false;
    let noChooseMenu = false;
    let roleId = $("#id-modal").val();
    let roleName = $("#name-modal").val();
    let roleDesc = $("#desc-modal").val();
    if (roleDesc === "" || roleDesc == null) {
        roleDesc = "-";
    }

    let size = $("#no-modal").val();
    let listMenu = [];
    let listMap = [];
    for (let x = 0; x <= size; x++) {
        try {
            let menu = $("#opt-map-" + x).val();
            if (menu === "") {
                noChooseMenu = true;
                break;
            }

            if (listMenu.includes(menu)) {
                foundDuplicate = true;
                break;
            }

            let req = {};
            req["menuId"] = menu;
            req["view"] = document.getElementById("view-" + x).checked;
            req["create"] = document.getElementById("create-" + x).checked;
            req["edit"] = document.getElementById("edit-" + x).checked;
            req["delete"] = document.getElementById("delete-" + x).checked;
            listMap.push(req);
            listMenu.push(menu);
        } catch (err) {
            continue;
        }
    }

    if (noChooseMenu) {
        showNotice('warning', "Warning", "Please choose menu to mapping");
    } else if (foundDuplicate) {
        showNotice('warning', "Warning", "There are duplicate mapping menus, please select one");
    } else {
        let request = {};
        request["name"] = roleName;
        request["description"] = roleDesc;
        request["roleMapping"] = listMap;

        if (roleId === "" || roleId == null) {
            confirm("/api/role/register", "Are you sure want to register this role: " + roleName + " ?", request, function () {
                reload();
            });
        } else {
            request["roleId"] = roleId;
            confirm("/api/role/update", "Are you sure want to update this role: " + roleName + " ?", request, function () {
                reload();
            });
        }
    }
}

function addRole() {
    clearTableOnModal();
    $("#id-modal").val("");
    $("#name-modal").val("");
    $("#desc-modal").val("");
    $("#no-modal").val("");
}

function editRole(id) {
    clearTableOnModal();
    let listMenu = get("/api/menu/options");
    let data = get("/api/role/detail", roleReq(id));
    let dataSize = data.data.roleMapping.length;

    $("#id-modal").val(id);
    $("#name-modal").val(data.data.name);
    $("#desc-modal").val(data.data.description);
    $("#no-modal").val(dataSize);

    let tableBody = document.querySelector('#permissionsTable tbody');
    for (let x = 0; x < dataSize; x++) {
        let dataMapping = data.data.roleMapping[x];
        let newRow = document.createElement('tr');

        let menuDropdown = document.createElement('select');
        menuDropdown.setAttribute("class", "form-select");
        menuDropdown.setAttribute("id", `opt-map-${x}`);

        let viewCheckbox = document.createElement('input');
        viewCheckbox.setAttribute("type", "checkbox");
        viewCheckbox.setAttribute("id", `view-${x}`);
        viewCheckbox.setAttribute("class", "form-check-input");
        if (dataMapping.view) {
            viewCheckbox.setAttribute("checked", "checked");
        }

        let createCheckbox = document.createElement('input');
        createCheckbox.setAttribute("type", "checkbox");
        createCheckbox.setAttribute("id", `create-${x}`);
        createCheckbox.setAttribute("class", "form-check-input");
        if (dataMapping.create) {
            createCheckbox.setAttribute("checked", "checked");
        }

        let editCheckbox = document.createElement('input');
        editCheckbox.setAttribute("type", "checkbox");
        editCheckbox.setAttribute("id", `edit-${x}`);
        editCheckbox.setAttribute("class", "form-check-input");
        if (dataMapping.edit) {
            editCheckbox.setAttribute("checked", "checked");
        }

        let deleteCheckbox = document.createElement('input');
        deleteCheckbox.setAttribute("type", "checkbox");
        deleteCheckbox.setAttribute("id", `delete-${x}`);
        deleteCheckbox.setAttribute("class", "form-check-input");
        if (dataMapping.delete) {
            deleteCheckbox.setAttribute("checked", "checked");
        }

        let removeButton = document.createElement('button');
        removeButton.setAttribute("type", "button");
        removeButton.setAttribute("class", "btn btn-danger btn-sm removeRowBtn");
        removeButton.innerHTML = `<i class="fas fa-remove"></i>`;

        removeButton.addEventListener('click', function () {
            tableBody.removeChild(newRow);
        });

        appendOptions(menuDropdown, listMenu, dataMapping.menuName);
        newRow.appendChild(addCell(menuDropdown));
        newRow.appendChild(addCell(viewCheckbox, 'text-center'));
        newRow.appendChild(addCell(createCheckbox, 'text-center'));
        newRow.appendChild(addCell(editCheckbox, 'text-center'));
        newRow.appendChild(addCell(deleteCheckbox, 'text-center'));
        newRow.appendChild(addCell(removeButton, 'text-center'));

        tableBody.appendChild(newRow);
    }
}

function clearTableOnModal() {
    let tableBody = document.querySelector('#permissionsTable tbody');

    while (tableBody.firstChild) {
        tableBody.removeChild(tableBody.firstChild);
    }

    $("#no-modal").val(0);
}

function deleteRole(id, name) {
    confirm("/api/role/delete", "Are you sure want to delete this role: " + name + " ?", roleReq(id), function () {
        reload();
    });
}

function roleReactive(id, name) {
    confirm("/api/role/reactivate", "Are you sure want to reactivate this role: " + name + " ?", roleReq(id), function () {
        reload();
    });
}

function roleDeactive(id, name) {
    confirm("/api/role/deactivate", "Are you sure want to deactivate this role: " + name + " ?", roleReq(id), function () {
        reload();
    });
}

function roleReq(id) {
    let request = {};
    request["roleId"] = id
    return request;
}

function addMappingValue() {
    let listMenu = get("/api/menu/options");
    let idx = $("#no-modal").val();
    if (idx === "" || idx == null) {
        idx = 0;
    } else {
        idx++;
    }

    let tableBody = document.querySelector('#permissionsTable tbody');
    let newRow = document.createElement('tr');

    let menuDropdown = document.createElement('select');
    menuDropdown.setAttribute("class", "form-select");
    menuDropdown.setAttribute("id", `opt-map-${idx}`);

    let viewCheckbox = document.createElement('input');
    viewCheckbox.setAttribute("type", "checkbox");
    viewCheckbox.setAttribute("id", `view-${idx}`);
    viewCheckbox.setAttribute("class", "form-check-input");

    let createCheckbox = document.createElement('input');
    createCheckbox.setAttribute("type", "checkbox");
    createCheckbox.setAttribute("id", `create-${idx}`);
    createCheckbox.setAttribute("class", "form-check-input");

    let editCheckbox = document.createElement('input');
    editCheckbox.setAttribute("type", "checkbox");
    editCheckbox.setAttribute("id", `edit-${idx}`);
    editCheckbox.setAttribute("class", "form-check-input");

    let deleteCheckbox = document.createElement('input');
    deleteCheckbox.setAttribute("type", "checkbox");
    deleteCheckbox.setAttribute("id", `delete-${idx}`);
    deleteCheckbox.setAttribute("class", "form-check-input");

    let removeButton = document.createElement('button');
    removeButton.setAttribute("type", "button");
    removeButton.setAttribute("class", "btn btn-danger btn-sm removeRowBtn");
    removeButton.innerHTML = `<i class="fas fa-remove"></i>`;

    removeButton.addEventListener('click', function () {
        tableBody.removeChild(newRow);
    });

    appendOptions(menuDropdown, listMenu);
    newRow.appendChild(addCell(menuDropdown));
    newRow.appendChild(addCell(viewCheckbox, 'text-center'));
    newRow.appendChild(addCell(createCheckbox, 'text-center'));
    newRow.appendChild(addCell(editCheckbox, 'text-center'));
    newRow.appendChild(addCell(deleteCheckbox, 'text-center'));
    newRow.appendChild(addCell(removeButton, 'text-center'));

    tableBody.appendChild(newRow);
    $("#no-modal").val(idx);
}

function addCell(element, className = '') {
    let td = document.createElement('td');
    if (className) {
        td.setAttribute('class', className);
    }

    td.appendChild(element);
    return td;
}

function setRoleCreatePermission() {
    let createAccess = $("#create-access").val();
    let btnAdd = document.getElementById("btn-add");

    btnAdd.style.display = "none";
    if (createAccess !== "false") {
        btnAdd.style.display = "block";
    }
}