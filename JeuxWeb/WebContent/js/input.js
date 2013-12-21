function clearForm(form) {
    "use strict";

    // http://stackoverflow.com/questions/680241/resetting-a-multi-stage-form-with-jquery

    $(form).find("input:text, select, textarea").val("");
    $(form).find("input[type=number]").val("");
    $(form).find("input:radio, input:checkbox").removeAttr("checked").removeAttr("selected");
}
