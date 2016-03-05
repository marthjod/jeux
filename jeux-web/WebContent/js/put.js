var createNewGroup = function (groupSubmit, prefix) {
    "use strict";

    var minSets = 0,
            maxSets = 0,
            name = "",
            round = 0,
            active = false,
            sendGroup = {},
            groupForm = null,
            inputOK = false,
            url = "rest/admin/create-group";

    if (prefix && typeof prefix === 'string') {
        url = prefix + '/' + url;
    }

    groupForm = $(groupSubmit).parent();

    try {
        minSets = parseInt($(groupForm).find("#min-sets").val(), 10);
        maxSets = parseInt($(groupForm).find("#max-sets").val(), 10);
        round = parseInt($(groupForm).find("#round").val(), 10);
    } catch (e) {
        alert(e);
    }

    name = $(groupForm).find("#name").val();
    active = $(groupForm).find("#group-active")[0].checked;

    // rudimentary sanity check
    if (minSets && typeof minSets === "number" && minSets !== NaN &&
            maxSets && typeof maxSets === "number" && maxSets !== NaN &&
            round && typeof round === "number" && round !== NaN &&
            name && typeof name === "string" &&
            name.length > 0 &&
            typeof active !== undefined && active !== null && typeof active === "boolean" &&
            minSets <= maxSets) {
        inputOK = true;
    }

    if (inputOK) {

        sendGroup = {
            "minSets": minSets,
            "maxSets": maxSets,
            "name": name,
            "roundId": round,
            // newly-created cannot be completed
            "completed": false,
            "active": active
        };

        // assumption: all input values present
        // see checkSubmitReady()
        $.ajax({
            url: url,
            type: "PUT",
            data: JSON.stringify(sendGroup),
            contentType: "application/json",
            success: function () {
                //$("#user-feedback").html("Group '" + sendGroup['name'] + "' created.");
                //clearForm(groupForm);
                document.location.reload();
            },
            statusCode: {
                403: function () {
                    alert("Operation not permitted (unauthenticated request).");
                },
                500: function () {
                    // TODO feedback label/field
                    $(groupSubmit).attr("value", "Failed to create group!");
                },
                400: function () {
                    alert("Bad request (wrong data format?).");
                }
            }
        });
    }
};

var createNewPlayer = function (playerSubmit, prefix) {
    "use strict";

    var playerForm = null,
            playerName = "",
            sendPlayer = {},
            groupOptionId = "",
            groupId = -1,
            inputOK = false,
            url = "rest/admin/create-player";

    if (prefix && typeof prefix === 'string') {
        url = prefix + '/' + url;
    }

    playerForm = $(playerSubmit).parent();

    groupOptionId = $(playerForm).find("option:selected").attr("id");
    if (groupOptionId && typeof groupOptionId === "string" && /^group-id-.*$/.test(groupOptionId)) {
        try {
            groupId = parseInt(groupOptionId.replace("group-id-", ""), 10);
            // parseInt did not fail
            playerName = $(playerForm).find("input:text[id='name']").val();
            if (playerName && typeof playerName === "string" && playerName.length > 0) {
                inputOK = true;
            }
        } catch (e) {
            alert(e);
        }
    }

    if (inputOK) {
        sendPlayer = {
            "name": playerName,
            "groupId": groupId
        };

        $.ajax({
            url: url,
            type: "PUT",
            data: JSON.stringify(sendPlayer),
            contentType: "application/json",
            success: function () {
                //$("#user-feedback").html("Player '" + sendPlayer['name'] + "' created.");
                //clearForm($(playerSubmit).parent());
                document.location.reload();
            },
            statusCode: {
                403: function () {
                    alert("Operation not permitted (unauthenticated request).");
                },
                500: function () {
                    $(playerSubmit).attr("value", "Failed to create player");
                }
            }
        });
    }
};

var createNewRoundSwitchRule = function (ruleSubmit, prefix) {
    "use strict";

    var ruleForm = null,
            srcGroupId = 0,
            destGroupId = 0,
            startWithRank = 0,
            additionalPlayers = 0,
            inputOK = false,
            sendRule = {},
            url = "rest/admin/create-roundswitchrule";

    if (prefix && typeof prefix === 'string') {
        url = prefix + '/' + url;
    }

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
                "srcGroupId": srcGroupId,
                "destGroupId": destGroupId,
                "startWithRank": startWithRank,
                "additionalPlayers": additionalPlayers
            };

            $.ajax({
                url: url,
                type: "PUT",
                data: JSON.stringify(sendRule),
                contentType: "application/json",
                success: function () {
                    //$("#user-feedback").html("Rule '" + sendRule + "' created.");
                    //clearForm(ruleForm);
                    document.location.reload();
                },
                statusCode: {
                    400: function () {
                        alert("Bad request.");
                    },
                    403: function () {
                        alert("Operation not permitted (unauthenticated request).");
                    },
                    406: function () {
                        alert("Rank too low: must start at least at rank 1.");
                    },
                    409: function () {
                        alert("Source group must not equal destination group.");
                    },
                    412: function () {
                        alert("Source and/or destination group do(es) not exist.");
                    },
                    413: function () {
                        alert("Too many players to be moved from source group.");
                    },
                    416: function () {
                        alert("A rank exceeds the group's size.");
                    },
                    500: function () {
                        alert("Failed to create round-switch rule (unknown error).");
                    }
                }
            });
        }
    }
};
