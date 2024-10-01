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
            if (code !== 200) {
                showNotice('error', "Failed", status);
            } else {
                callback(this.success);
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

$.extend(true, $.fn.dataTable.defaults, {
    "scrollX": true,
    "scrollY": "400px",
    "language": {
        "search": "Searching Data : ",
        "loadingRecords": "Please Wait...",
        "processing": "Processing...",
        "lengthMenu": "Showing _MENU_ data",
        "infoEmpty": "Showing 0 to 0 from 0 data",
        "info": "Showing _START_ to _END_ from _TOTAL_ data",
        "emptyTable": "No data shown",
        "zeroRecords": "No search available",
        "infoFiltered": "(filter from _MAX_ total data)",
        "paginate": {
            "first": "<i class='fas fa-angle-double-left'></i>",
            "last": "<i class='fas fa-angle-double-right'></i>",
            "next": "<i class='fas fa-angle-right'></i>",
            "previous": "<i class='fas fa-angle-left'></i>"
        }
    }
});