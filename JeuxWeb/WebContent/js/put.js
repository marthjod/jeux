function createNewGroup(groupSubmit) {
    "use strict";

    var minSets = 0, maxSets = 0, name = "", round = 0, active = false, sendGroup = {}, groupForm = null;

    groupForm = $(groupSubmit).parent();

    minSets = $(groupForm).find("#min-sets").val();
    maxSets = $(groupForm).find("#max-sets").val();
    round = $(groupForm).find("#round").val();
    name = $(groupForm).find("#name").val();
    active = $(groupForm).find("#group-active")[0].checked;

    sendGroup = {
        "minSets" : minSets,
        "maxSets" : maxSets,
        "name" : name,
        "round" : round,
        // newly-created cannot be completed
        "completed" : false,
        "active" : active
    };

    // assumption: all input values present
    // see checkSubmitReady()
    $.ajax({
        url : "create-group",
        type : "PUT",
        data : JSON.stringify(sendGroup),
        contentType : "application/json",
        success : function() {

            $(groupSubmit).attr("value", "Created.");
            // disable submit button, clear values and success message
            $(groupSubmit).attr("disabled", "disabled");
            clearForm(groupForm);
            window.setTimeout(function() {
                // reset text
                $(groupSubmit).attr("value", "Create new group");
            }, 3000);

            // refresh
            showAllGroups($("#show-all-groups"), $("#player-select-group"));

        },
        error : function() {
            $(groupSubmit).attr("value", "Failed to create group!");
        }
    });
}

function createNewPlayer(playerSubmit) {
    "use strict";

    var playerForm = null, playerName = "", sendPlayer = {}, groupOptionId = "", groupId = -1, inputOK = false;

    playerForm = $(playerSubmit).parent();

    groupOptionId = $(playerForm).find("option:selected").attr("id");
    if (groupOptionId !== undefined && groupOptionId !== null
            && typeof groupOptionId === "string"
            && groupOptionId.startsWith("group-id-")) {
        try {
            groupId = parseInt(groupOptionId.replace("group-id-", ""), 10);
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
        sendPlayer = {
            "name" : playerName,
            "groupId" : groupId
        };

        $.ajax({
            url : "create-player",
            type : "PUT",
            data : JSON.stringify(sendPlayer),
            contentType : "application/json",
            success : function() {
                $(playerSubmit).attr("value", "Created.");
                // disable submit button, clear values and success message
                $(playerSubmit).attr("disabled", "disabled");
                clearForm(playerSubmit);
                window.setTimeout(function() {
                    // reset text
                    $(playerSubmit).attr("value", "Create new player");
                }, 3000);
            },
            error : function() {
                $(playerSubmit).attr("value", "Failed to create player");
            }
        });
    }
}
