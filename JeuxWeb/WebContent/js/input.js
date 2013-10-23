// https://bugs.eclipse.org/bugs/show_bug.cgi?id=355036

function prefillMaxSets(minSetsInput) {
    "use strict";

    var minSets = $(minSetsInput).val();

    if (minSets === "1") {
        $("#max-sets").val("1");
    } else if (minSets === "2") {
        $("#max-sets").val("3");
    }
}

function clearForm(form) {
    "use strict";

    // http://stackoverflow.com/questions/680241/resetting-a-multi-stage-form-with-jquery

    $(form).find("input:text, select, textarea").val("");
    $(form).find("input[type=number]").val("");
    $(form).find("input:radio, input:checkbox").removeAttr("checked")
            .removeAttr("selected");
}

function checkSubmitReady(inputElement) {
    "use strict";

    var groupForm = null, minSets = "", maxSets = "", name = "";

    groupForm = $(inputElement).parent();

    minSets = $(groupForm).find("#min-sets").val();
    maxSets = $(groupForm).find("#max-sets").val();
    name = $(groupForm).find("#name").val();

    if (minSets !== "" && maxSets !== "" && name !== "") {
        $("#submit-create-group").removeAttr("disabled");
    }

}

// function checkAddNextPlayer(playerInput) {
// "use strict";
//
// if ($(playerInput).val() !== "") {
// $("#add-player").removeAttr("disabled");
// }
// }

// function createAdditionalPlayer(newPlayersElement) {
// "use strict";
//
// var createPlayersDiv = null;
//
// createPlayersDiv = $(newPlayersElement).parent();
//
// $("<br>").prependTo($(createPlayersDiv));
// $("<input>").attr("class", "create-player").attr("type", "text").attr(
// "onkeyup", "checkAddNextPlayer(this);").prependTo(
// $(createPlayersDiv)).focus();
// $("#add-player").attr("disabled", "disabled");
// }
