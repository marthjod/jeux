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

function checkGroupSubmitReady(inputElement) {
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

function checkPlayerSubmitReady(inputElement) {
    "use strict";

    var playerForm = null, playerName = "", groupOptionId = "", inputOK = false;

    playerForm = $(inputElement).parent();

    groupOptionId = $(playerForm).find("option:selected").attr("id");

    if (groupOptionId !== undefined && groupOptionId !== null
            && typeof groupOptionId === "string"
            && groupOptionId.startsWith("group-id-")) {
        try {
            parseInt(groupOptionId.replace("group-id-", ""), 10);
            // parseInt did not fail
            playerName = $(playerForm).find("input:text[id='name']").val();
            if (playerName !== undefined && playerName !== null
                    && typeof playerName === "string" && playerName.length > 0) {
                inputOK = true;
            }
        } catch (e) {
            alert(e);
        }
    }

    if (inputOK) {
        $(playerForm).find("#submit-create-player").removeAttr("disabled");
    }
}
