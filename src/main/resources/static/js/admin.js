const COLUMN_ID = 0;
const COLUMN_FIRST_NAME = 1;
const COLUMN_LAST_NAME = 2;
const COLUMN_AGE = 3;
const COLUMN_EMAIL = 4;
const COLUMN_ROLES = 5;

$("#tableAllUsers .btn-user-edit").click(function() {fillModalEdit(this);});
$("#tableAllUsers .btn-user-delete").click(function() {fillModalDelete(this);});
$("#formUserAdd").submit(function() {return userAddSubmit(this)});
$("#formUserEdit").submit(function() {return userEditSubmit(this)});
$("#userEditEmail, #userAddEmail").change(function() {$(this).removeClass("is-invalid")});

function fillModalEdit(button) {
    let $row = $(button).parents("tr").children("td").not(":has(button)");
    let $values = $.map($row, function(v) {return $(v).text()});

    $("#userEditId").val($values[COLUMN_ID]);
    $("#userEditFirstName").val($values[COLUMN_FIRST_NAME]);
    $("#userEditLastName").val($values[COLUMN_LAST_NAME]);
    $("#userEditAge").val($values[COLUMN_AGE]);
    $("#userEditEmail").val($values[COLUMN_EMAIL]).data("originalEmail", $values[COLUMN_EMAIL])
        .removeClass("is-invalid");
    $("#userEditPassword").val("");
    $("#userEditRoles > option").each(function(index, option) {
        $(option).prop("selected", $.inArray(parseInt($(option).val()), $($row[COLUMN_ROLES]).data()['roles']) !== -1);
    });
}

function fillModalDelete(button) {
    let $row = $(button).parents("tr").children("td").not(":has(button)");
    let $values = $.map($row, function(v) {return $(v).text()});

    $("#userDeleteId").val($values[COLUMN_ID]);
    $("#userDeleteFirstName").val($values[COLUMN_FIRST_NAME]);
    $("#userDeleteLastName").val($values[COLUMN_LAST_NAME]);
    $("#userDeleteAge").val($values[COLUMN_AGE]);
    $("#userDeleteEmail").val($values[COLUMN_EMAIL]);
    $("#userDeleteRoles > option").each(function(index, option) {
        $(option).attr("disabled", true);
        $(option).prop("selected", $.inArray(parseInt($(option).val()), $($row[COLUMN_ROLES]).data()['roles']) !== -1);
    });
}

function userAddSubmit(form) {
    let $field = $("#userAddEmail");
    let url = $(form).data('check') + "?email=" + $field.val();
    let result = true;

    $.ajax({
        url: url,
        success: function (data) {
            if (data === false) {
                $field.addClass("is-invalid");
            } else {
                $field.removeClass("is-invalid")
            }
            result = data;
        },
        async: false
    });

    return result;
}

function userEditSubmit(form) {
    let $field = $("#userEditEmail");
    let result = true;

    if ($field.data("originalEmail") !== $field.val()) {
        let url = $(form).data('check') + "?email=" + $field.val();
        $.ajax({
            url: url,
            success: function (data) {
                if (data === false) {
                    $field.addClass("is-invalid");
                } else {
                    $field.removeClass("is-invalid")
                }
                result = data;
            },
            async: false
        });
    }

    return result;
}