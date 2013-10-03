function createNewGroup(groupSubmit) {
    "use strict";

    var minSets = 0, maxSets = 0, name = "", round = 0, sendGroup = {}, groupForm = null;

    groupForm = $(groupSubmit).parent();

    minSets = $(groupForm).find("#min-sets").val();
    maxSets = $(groupForm).find("#max-sets").val();
    round = $(groupForm).find("#round").val();
    name = $(groupForm).find("#name").val();

    sendGroup = {
        "minSets" : minSets,
        "maxSets" : maxSets,
        "name" : name,
        "round" : round,
        "completed" : false,
        "active" : true
    // TODO user input
    };

    // assumption: all input values present
    // see checkSubmitReady()
    $.ajax({
        url : "create-group",
        type : "PUT",
        data : JSON.stringify(sendGroup),
        contentType : "application/json",
        success : function() {

            // create any players first before clearing fields
            createNewPlayersForGroup(groupSubmit);

            $("#creation-result").html("Created.");
            // disable submit button, clear values and success message
            $(groupForm).find("#submit-create-group").attr("disabled",
                    "disabled");
            $(groupForm).find("#min-sets").val("");
            $(groupForm).find("#max-sets").val("");
            $(groupForm).find("#name").val("");
            $(groupForm).find("#round").val("");

            window.setTimeout(function() {
                $("#creation-result").html("");
            }, 5000);

        },
        error : function() {
            $("#creation-result").html("Failed to create group");
        }
    });
}

function createNewPlayersForGroup(groupSubmit) {
    "use strict";

    var groupForm = null, i = 0, createdPlayers = [], sendPlayers = [], groupName = "";

    groupForm = $(groupSubmit).parent();
    groupName = $(groupForm).find("#name").val();

    createdPlayers = groupForm.find($(".create-player"));
    if (createdPlayers.length > 0) {
        for (i = 0; i < createdPlayers.length; i += 1) {
            // non-empty player name?
            if ($(createdPlayers[i]).val() !== "") {
                // add player object with name attribute
                // to group data
                sendPlayers.push({
                    "name" : $(createdPlayers[i]).val(),
                    "groupName" : groupName
                });
            }

            // remove parsed input element
            $(createdPlayers[i]).remove();
        }

        $.ajax({
            url : "create-players",
            type : "PUT",
            data : JSON.stringify(sendPlayers),
            contentType : "application/json",
            success : function() {

            },
            error : function() {
                $("#creation-result").html("Failed to create players");
            }
        });
    }
}