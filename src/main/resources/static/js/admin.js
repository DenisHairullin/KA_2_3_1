const csrfToken = document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, "$1");

/* Events */
$(document).ready(() => {
    fillUsersTable();
    fillFormAddUser();
});
$("#formUserAdd").submit(userAddSubmit);
$("#formUserEdit").submit(userEditSubmit);
$("#formUserDelete").submit(userDeleteSubmit);
$("#modalEdit").on("hide.bs.modal", () => {
    $("#userEditAlert").collapse("hide");
});
$("#modalDelete").on("hide.bs.modal", () => {
    $("#userDeleteAlert").collapse("hide");
});


/* Util functions */
function fetchJson(url) {
    return new Promise((resolve, reject) => {
        fetch(url).then(response => {
            response.json().then(data => {
                if (response.status !== 200) {
                    reject(data.message);
                } else {
                    resolve(data);
                }
            });
        });
    });
}

function postJson(url, json, method = "POST") {
    return new Promise((resolve, reject) => {
        fetch(url, {
            method: method,
            headers: {
                "Content-type": "application/json",
                "X-XSRF-TOKEN": csrfToken
            },
            body: JSON.stringify(json),
        }).then(response => {
            if (response.status !== 200) {
                response.json().then(data => {
                    reject(data.message);
                }).catch(message => {
                    reject(message);
                });
            } else {
                resolve();
            }
        });
    });
}

function formToJson(form) {
    let json = {};

    $(form).find("input").each((i, e) => {
        json[$(e).attr("name")] = $(e).val();
    });

    json["roles"] = [];
    $(form).find("option").each((i, e) => {
        if ($(e).prop("selected")) {
            json["roles"].push({
                id: $(e).val(),
                name: $(e).text(),
            });
        }
    });

    return json;
}

/* Data fetch functions */
function getUser(id) {
    return fetchJson(`/users/get?id=${id}`);
}

function getUsers() {
    return fetchJson("/users/list");
}

function getRoles() {
    return fetchJson("/users/roles");
}

/* Form fill functions */
function fillUsersTable() {
    getUsers().then(users => {
        $("#tableAllUsersAlert").collapse("hide");
        let $table = $("#tableAllUsers");
        $table.find("*").remove();
        $table.append("<thead><tr>"
            + "<th>ID</th>"
            + "<th>First Name</th>"
            + "<th>Last Name</th>"
            + "<th>Login</th>"
            + "<th>Roles</th>"
            + "<th>Edit</th>"
            + "<th>Delete</th>"
            + "</tr></thead><tbody>");
        users.forEach(user => {
            $table.append("<tr>"
                + `<td>${user.id}</td>`
                + `<td>${user.firstName}</td>`
                + `<td>${user.lastName}</td>`
                + `<td>${user.login}</td>`
                + `<td data-roles=[${user.roles.map(role => role.id).join(",")}]>`
                + `${user.roles.map(role => role.name).join(" ")}</td>`
                + `<td><button class="btn btn-info btn-sm btn-user-edit" type="button" data-toggle="modal" `
                + `data-target="#modalEdit" data-id="${user.id}">Edit</button></td>`
                + `<td><button class="btn btn-danger btn-sm btn-user-delete" type="button" data-toggle="modal" `
                + `data-target="#modalDelete" data-id="${user.id}">Delete</button></td>`
                + "</tr>")
        });
        $table.append("</tbody>");
        $table.find(".btn-user-edit").click((event) => fillModalEdit(event.currentTarget));
        $table.find(".btn-user-delete").click((event) => fillModalDelete(event.currentTarget));
    }).catch(() => {
        $("#tableAllUsersAlert").text("Error fetching data").collapse("show");
    });
}

function fillModalEdit(button) {
    getRoles().then(roles => {
        $("#userEditRoles").attr("size", roles.length).find("option").remove();
        roles.forEach(role => {
            $("#userEditRoles").append(`<option value=\"${role.id}\">${role.name}</option>`);
        })
        getUser($(button).data("id")).then(user => {
            $("#userEditId").val(user.id);
            $("#userEditFirstName").val(user.firstName);
            $("#userEditLastName").val(user.lastName);
            $("#userEditLogin").val(user.login);
            $("#userEditPassword").val("");
            let roleIds = user.roles.map(role => role.id);
            $("#userEditRoles > option").each((i, o) => {
                $(o).prop("selected", $.inArray(parseInt($(o).val()), roleIds) !== -1);
            });
        }).catch(message => {
            $("#userEditAlert").html(`Error fetching user<br>${message}`).collapse("show");
        });
    }).catch(message => {
        $("#userEditAlert").html(`Error fetching roles<br>${message}`).collapse("show");
    });
}

function fillModalDelete(button) {
    getRoles().then(roles => {
        $("#userDeleteRoles").attr("size", roles.length).find("option").remove();
        roles.forEach(role => {
            $("#userDeleteRoles").append(`<option value=\"${role.id}\">${role.name}</option>`);
        })
        getUser($(button).data("id")).then(user => {
            $("#userDeleteId").val(user.id);
            $("#userDeleteFirstName").val(user.firstName);
            $("#userDeleteLastName").val(user.lastName);
            $("#userDeleteLogin").val(user.login);
            $("#userDeletePassword").val("");
            let roleIds = user.roles.map(role => role.id);
            $("#userDeleteRoles > option").each((i, o) => {
                $(o).prop("disabled", true);
                $(o).prop("selected", $.inArray(parseInt($(o).val()), roleIds) !== -1);
            });
        }).catch(message => {
            $("#userDeleteAlert").html(`Error fetching user<br>${message}`).collapse("show");
        });
    }).catch(message => {
        $("#userDeleteAlert").html(`Error fetching roles<br>${message}`).collapse("show");
    });
}

function fillFormAddUser() {
    getRoles().then(roles => {
        $("#userAddRoles").attr("size", roles.length).find("option").remove();
        roles.forEach(role => {
            $("#userAddRoles").append(`<option value=\"${role.id}\">${role.name}</option>`);
        })
    }).catch(message => {
        $("#userAddAlert").html(`Error fetching roles<br>${message}`).collapse("show");
    });
}

/* Submit functions */
function userAddSubmit(event) {
    event.preventDefault();

    postJson("/users/add", formToJson($("#formUserAdd"))).then(() => {
        $("#userAddAlert").collapse("hide");
        fillUsersTable();
        let $form = $("#formUserAdd");
        $form.find("input").val("");
        $form.find("option").prop("selected", false);
    }).catch(message => {
        $("#userAddAlert").text(message).collapse("show");
    });
}

function userEditSubmit(event) {
    event.preventDefault();

    postJson("/users/update", formToJson($("#formUserEdit")), "PUT").then(() => {
        $("#modalEdit").modal("hide");
        fillUsersTable();
    }).catch(message => {
        $("#userEditAlert").text(message).collapse("show");
    });
}

function userDeleteSubmit(event) {
    event.preventDefault();

    postJson(`/users/remove?id=${$("#userDeleteId").val()}`, formToJson($("#formUserEdit")), "DELETE")
            .then(() => {
        $("#modalDelete").modal("hide");
        fillUsersTable();
    }).catch(message => {
        $("#userDeleteAlert").text(message).collapse("show");
    });
}