function redirect(url) {
    window.location.href = url;
}

function reload() {
    location.reload();
}

function hideLoading() {
    $("#loading").modal("hide");
}

function showLoading() {
    $("#loading").modal("show");
}

function showNotice(type, title, message, callback) {
    Swal.fire({
        title: title,
        html: message,
        icon: type,
        showClass: {
            popup: 'animate__animated animate__zoomIn'
        },
        hideClass: {
            popup: 'animate__animated animate__zoomOut'
        }
    }).then(function () {
        callback && callback();
    });
}

function processAuth(url, type, data) {
    $.ajax({
        url: url,
        type: type,
        dataType: 'json',
        data: JSON.stringify(data),
        contentType: 'application/json; charset=utf-8',
        cache: false,
        async: false,
        timeout: 600000,
        success: function (response) {
            let code = response["code"];
            let status = response["status"];
            if (code !== 200) {
                showNotice('error', "Failed", status);
            } else {
                redirect("/dashboard");
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            try {
                let response = JSON.parse(jqXHR.responseText);
                if (response.data && response.data.error) {
                    let errorMessage = response.data.error;
                    showNotice('error', "Error", errorMessage);
                } else {
                    showNotice('error', "Error", textStatus + " : " + errorThrown);
                }
            } catch (e) {
                showNotice('error', "Error", textStatus + " : " + errorThrown);
            }
        }
    });
}

function confirm(url, message, data, callback) {
    Swal.fire({
        title: 'Confirmation',
        text: message,
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: 'Yes',
        cancelButtonText: 'No',
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33'
    }).then(function (ask) {
        if (ask.value) {
            process(url, "POST", data, callback);
        }
    });
}

function process(url, type, data, callback) {
    $.ajax({
        url: url,
        type: type,
        dataType: 'json',
        data: JSON.stringify(data),
        contentType: 'application/json; charset=utf-8',
        cache: false,
        async: false,
        timeout: 600000,
        success: function (response) {
            let code = response["code"];
            let status = response["status"];
            if (code === 200 || code === 201) {
                showNotice('success', "Success", status, callback);
            } else {
                showNotice('error', "Failed", status);
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            try {
                let response = JSON.parse(jqXHR.responseText);
                if (response.data && response.data.error) {
                    let errorMessage = response.data.error;
                    showNotice('error', "Error", errorMessage);
                } else {
                    showNotice('error', "Error", textStatus + " : " + errorThrown);
                }
            } catch (e) {
                showNotice('error', "Error", textStatus + " : " + errorThrown);
            }
        }
    });
}

function get(url, request) {
    let data = {};
    let accessToken = getCookie('access-token');

    $.ajax({
        url: url,
        type: "POST",
        async: false,
        dataType: 'json',
        data: JSON.stringify(request),
        contentType: 'application/json; charset=utf-8',
        cache: false,
        timeout: 600000,
        beforeSend: function (xhr) {
            showLoading();
            if (accessToken) {
                xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
            }
        }
    }).done(function (response) {
        hideLoading();
        data = response;
    }).fail(function (jqXHR, textStatus, errorThrown) {
        hideLoading();
        try {
            let response = JSON.parse(jqXHR.responseText);
            if (response.data && response.data.error) {
                let errorMessage = response.data.error;
                showNotice('error', "Error", errorMessage);
            } else {
                showNotice('error', "Error", textStatus + " : " + errorThrown);
            }
        } catch (e) {
            showNotice('error', "Error", textStatus + " : " + errorThrown);
        }
        data = null;
    });
    return data;
}

function appendOptions(select, data, param) {
    let initOpt = document.createElement("option");
    if (data.data.length > 0) {
        initOpt.setAttribute("value", "");
        initOpt.innerHTML = "All";
        select.appendChild(initOpt);
    }
    for (let x in data.data) {
        let option = document.createElement("option");
        option.setAttribute("value", data.data[x].key);
        option.innerHTML = data.data[x].value;
        if (param !== "" && param != null && param !== "-") {
            if (param === data.data[x].value) {
                option.setAttribute("selected", "selected");
            }
        }
        select.appendChild(option);
    }
}

function getCookie(name) {
    let match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    if (match) {
        return match[2];
    }
    return null;
}

function getValueFromToken(token, key) {
    let parts = token.split('.');
    if (parts.length !== 3) {
        showNotice('error', "Error", "Invalid access token");
        return;
    }

    let payload = parts[1];
    let decodedPayload = atob(payload);
    let jsonPayload = JSON.parse(decodedPayload);

    if (jsonPayload.hasOwnProperty(key)) {
        return jsonPayload[key];
    } else {
        console.log(`Field '${key}' not found`);
        return null;
    }
}

function getAccessList() {
    let token = getCookie('access-token');
    return getValueFromToken(token, "roleMapping");
}