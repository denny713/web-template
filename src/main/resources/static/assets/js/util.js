function redirect(url) {
    window.location.href = url;
}

function reload() {
    window.location.reload();
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
            let data = response["data"];
            if (code !== 200) {
                showNotice('error', "Failed", status);
            } else {
                showNotice('success', "Success", status, callback);
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
        title: 'Konfirmasi',
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