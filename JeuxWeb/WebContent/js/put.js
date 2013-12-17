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

            $(groupSubmit).attr("value", "Created.");
            // disable submit button, clear values and success message
            $(groupSubmit).attr("disabled", "disabled");
            clearForm(groupForm);
            window.setTimeout(function() {
                // reset text
                $(groupSubmit).attr("value", "Create new group");
            }, 3000);

            // refresh
            showAllGroups($("#show-all-groups"), $("#player-select-group"), $("#rule-source-group"), $("#rule-destination-group"));

        },
        statusCode : {
            403 : function() {
                alert("Operation not permitted (unauthenticated request).");
            },
            500 : function() {
                $(groupSubmit).attr("value", "Failed to create group!");
            }
        }
    });
}

function createNewPlayer(playerSubmit) {
    "use strict";

    var playerForm = null, playerName = "", sendPlayer = {}, groupOptionId = "", groupId = -1, inputOK = false;

    playerForm = $(playerSubmit).parent();

    groupOptionId = $(playerForm).find("option:selected").attr("id");
    if (groupOptionId !== undefined && groupOptionId !== null && typeof groupOptionId === "string" && groupOptionId.startsWith("group-id-")) {
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
                showAllPlayers($("#show-all-players"));

                $(playerSubmit).attr("value", "Created.");
                // disable submit button, clear values and success
                // message
                $(playerSubmit).attr("disabled", "disabled");
                clearForm(playerSubmit);
                window.setTimeout(function() {
                    // reset text
                    $(playerSubmit).attr("value", "Create new player");
                }, 3000);
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

function createNewRoundSwitchRule(ruleSubmit) {
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
                    $(ruleSubmit).attr("value", "Created.");
                    // disable submit button, clear values and success message
                    $(ruleSubmit).attr("disabled", "disabled");
                    clearForm(ruleSubmit);
                    window.setTimeout(function() {
                        // reset text
                        $(ruleSubmit).attr("value", "Create new round switch rule");
                    }, 3000);
                },
                statusCode : {
                    403 : function() {
                        alert("Operation not permitted (unauthenticated request).");
                    },
                    500 : function() {
                        $(ruleSubmit).attr("value", "Failed to create round switch rule");
                    }
                }
            });
        }
    }
}

function updateGame(updateSubmit) {

    var inputOK = false, gameId = 0, gameSetId = 0, rows = [], i = 0, set = {}, player1Id = 0, player2Id = 0, player1Score = 0, player2Score = 0, updatedGame = {};

    try {
        gameId = parseInt($(updateSubmit).parent().parent().attr("id").replace("game-id-", ""), 10);
        player1Id = parseInt($(updateSubmit).parent().parent().find("td.player1")[0].id.replace("player-id-", ""), 10);
        player2Id = parseInt($(updateSubmit).parent().parent().find("td.player2")[0].id.replace("player-id-", ""), 10);
        inputOK = true;
    } catch (e) {
        alert(e);
    }

    if (inputOK) {
        // reused
        inputOK = false;

        updatedGame = {
            "id" : gameId,
            "player1Id" : player1Id,
            "player2Id" : player2Id,
            "sets" : []
        };

        // input > td > tr > tbody > table
        rows = $(updateSubmit).parent().parent().parent().parent().find("tr");
        for (i = 0; i < rows.length; i++) {
            if (rows[i].id.startsWith("gameset-id-")) {

                try {
                    gameSetId = parseInt(rows[i].id.replace("gameset-id-", ""), 10);
                    player1Score = parseInt($(rows[i]).find("td.player1-score").find("input").val(), 10);
                    player2Score = parseInt($(rows[i]).find("td.player2-score").find("input").val(), 10);
                    inputOK = true;
                } catch (e) {
                    alert(e);
                }

                if (inputOK) {
                    inputOK = false;
                    set = {
                        "id" : gameSetId,
                        "gameId" : gameId,
                        "player1Score" : player1Score,
                        "player2Score" : player2Score
                    };

                    // add to array
                    updatedGame.sets[updatedGame.sets.length] = set;
                }
            }
        }

        $.ajax({
            url : "rest/admin/update-game/id/" + gameId,
            type : "POST",
            data : JSON.stringify(updatedGame),
            contentType : "application/json",
            success : function() {
                $(updateSubmit).attr("value", "Game updated.").attr("disabled", "disabled");
                window.setTimeout(function() {
                    // reset text
                    $(updateSubmit).attr("value", "Update game").removeAttr("disabled");
                }, 3000);
            },
            statusCode : {
                403 : function() {
                    alert("Operation not permitted (unauthenticated request).");
                },
                500 : function() {
                    alert("Failed to update game");
                }
            }
        });
    }
}
