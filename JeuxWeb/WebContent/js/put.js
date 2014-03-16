function createNewGroup(groupSubmit) {
    "use strict";

    var minSets = 0, maxSets = 0, name = "", round = 0, active = false, sendGroup = {}, groupForm = null;

    groupForm = $(groupSubmit).parent();

    minSets = $(groupForm).find("#min-sets").val();
    maxSets = $(groupForm).find("#max-sets").val();
    try {
        round = parseInt($(groupForm).find("#round").val(), 10);
    } catch (e) {
        alert(e);
    }
    name = $(groupForm).find("#name").val();
    active = $(groupForm).find("#group-active")[0].checked;

    sendGroup = {
        "minSets" : minSets,
        "maxSets" : maxSets,
        "name" : name,
        "roundId" : round,
        // newly-created cannot be completed
        "completed" : false,
        "active" : active
    };

    // assumption: all input values present
    // see checkSubmitReady()
    $.ajax({
        url : "rest/admin/create-group",
        type : "PUT",
        data : JSON.stringify(sendGroup),
        contentType : "application/json",
        success : function() {

            clearForm(groupForm);

            // refresh
            showGroups($("#show-groups"), $("#player-select-group"), $("#rule-source-group"), $("#rule-destination-group"));

        },
        statusCode : {
            403 : function() {
                alert("Operation not permitted (unauthenticated request).");
            },
            500 : function() {
                $(groupSubmit).attr("value", "Failed to create group!");
            },
            400 : function() {
                alert("Bad request (wrong data format?).");
            }
        }
    });
}

function createNewPlayer(playerSubmit) {
    "use strict";

    var playerForm = null, playerName = "", sendPlayer = {}, groupOptionId = "", groupId = -1, inputOK = false;

    playerForm = $(playerSubmit).parent();

    groupOptionId = $(playerForm).find("option:selected").attr("id");
    if (groupOptionId !== undefined && groupOptionId !== null && typeof groupOptionId === "string" && /^group-id-.*$/.test(groupOptionId)) {
        try {
            groupId = parseInt(groupOptionId.replace("group-id-", ""), 10);
            // parseInt did not fail
            playerName = $(playerForm).find("input:text[id='name']").val();
            if (playerName !== undefined && playerName !== null && typeof playerName === "string" && playerName.length > 0) {
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
            url : "rest/admin/create-player",
            type : "PUT",
            data : JSON.stringify(sendPlayer),
            contentType : "application/json",
            success : function() {
                showPlayers($("#show-players"));
                clearForm(playerSubmit);
            },
            statusCode : {
                403 : function() {
                    alert("Operation not permitted (unauthenticated request).");
                },
                500 : function() {
                    $(playerSubmit).attr("value", "Failed to create player");
                }
            }
        });
    }
}

function createNewRoundSwitchRule(ruleSubmit, rulesDiv) {
    "use strict";

    var ruleForm = null, srcGroupId = 0, destGroupId = 0, startWithRank = 0, additionalPlayers = 0, inputOK = false, sendRule = {};

    ruleForm = $(ruleSubmit).parent();

    if (ruleForm !== undefined && ruleForm !== null) {
        try {
            srcGroupId = parseInt($(ruleForm).find("#rule-source-group").find("option:selected").attr("id").replace("rule-source-group-id-", ""), 10);
            destGroupId = parseInt($(ruleForm).find("#rule-destination-group").find("option:selected").attr("id").replace("rule-destination-group-id-", ""), 10);
            startWithRank = parseInt($(ruleForm).find("#rule-start-rank").val(), 10);

            if ($(ruleForm).find("#rule-additional-players").val() !== "") {
                additionalPlayers = parseInt($(ruleForm).find("#rule-additional-players").val(), 10);
            }

            inputOK = true;
        } catch (e) {
            alert(e);
        }

        if (inputOK) {
            sendRule = {
                "srcGroupId" : srcGroupId,
                "destGroupId" : destGroupId,
                "startWithRank" : startWithRank,
                "additionalPlayers" : additionalPlayers
            };

            $.ajax({
                url : "rest/admin/create-roundswitchrule",
                type : "PUT",
                data : JSON.stringify(sendRule),
                contentType : "application/json",
                success : function() {
                    clearForm(ruleSubmit);
                    showRules($(rulesDiv));
                },
                statusCode : {
                    403 : function() {
                        alert("Operation not permitted (unauthenticated request).");
                    },
                    406 : function() {
                        alert("Rank too low: must start at least at rank 1.");
                    },
                    409 : function() {
                        alert("Source group must not equal destination group.");
                    },
                    412 : function() {
                        alert("Source and/or destination group do(es) not exist.");
                    },
                    413 : function() {
                        alert("Too many players to be moved from source group.");
                    },
                    416 : function() {
                        alert("A rank exceeds the group's size.");
                    },
                    500 : function() {
                        alert("Failed to create round-switch rule (unknown error).");
                    }
                }
            });
        }
    }
}
