function clearForm(form) {
    "use strict";

    // http://stackoverflow.com/questions/680241/resetting-a-multi-stage-form-with-jquery

    $(form).find("input:text, textarea").val("");
    // reset to first drop-down list value
    $(form).find("select").prop("selectedIndex", 0);
    $(form).find("input[type=number]").val("");
    $(form).find("input:radio, input:checkbox").removeAttr("checked").removeAttr("selected");
}
