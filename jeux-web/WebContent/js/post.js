var updateGame = function (updateSubmit, gameId, player1Id, player2Id, action) {
    "use strict";

    var inputOK = false,
            gameSetId = 0,
            rows = [],
            set = {},
            player1Score = 0,
            player2Score = 0,
            updatedGame = {};

    if (updateSubmit && updateSubmit !== null) {
        $(updateSubmit).attr("disabled", "disabled");
        if (action && typeof action === "string") {
            if (action === "save") {
                $(updateSubmit).attr("value", "Saving...");
            } else if (action === "update") {
                $(updateSubmit).attr("value", "Updating...");
            }
        } else {
            $(updateSubmit).attr("value", "...");
        }
    }

    updatedGame = {
        "id": gameId,
        "player1Id": player1Id,
        "player2Id": player2Id,
        "sets": []
    };

    // input > td > tr > tbody
    // yikes!
    rows = $(updateSubmit).parent().parent().parent().find("tr");

    $.each(rows, function (id, row) {
        if (/^gameset-id-.*$/.test(row.id)) {

            try {
                gameSetId = parseInt(row.id.replace("gameset-id-", ""), 10);
                player1Score = parseInt($(row).find("td.player1-score").find("input").val(), 10);
                player2Score = parseInt($(row).find("td.player2-score").find("input").val(), 10);
                inputOK = true;
            } catch (e) {
                alert(e);
            }

            if (inputOK) {
                // reset
                inputOK = false;
                set = {
                    "id": gameSetId,
                    "gameId": gameId,
                    "player1Score": player1Score,
                    "player2Score": player2Score
                };

                // add to array
                updatedGame.sets.push(set);
            }
        }
    });

    $.ajax({
        url: "rest/admin/update-game",
        type: "POST",
        data: JSON.stringify(updatedGame),
        contentType: "application/json",
        success: function () {
            $(updateSubmit).attr("value", "Update game").removeAttr("disabled");
            showGames($("#show-unplayed-games"), "unplayed", true);
            showGames($("#show-played-games"), "played", true);
            // also update group view in case one has been set to
            // completed/inactive
            showGroups($("#show-groups"));
        },
        statusCode: {
            403: function () {
                alert("Operation not permitted (unauthenticated request).");
            },
            500: function () {
                alert("Failed to update game");
            }
        }
    });
};

var generateGames = function (generateButton, groupId, shuffledMode) {
    "use strict";

    if (!shuffledMode || typeof shuffledMode !== "boolean") {
        shuffledMode = false;
    }

    if (generateButton && generateButton !== null) {
        $(generateButton).attr("disabled", "disabled").attr("value", "Generating...");
    }

    $.ajax({
        // access with @MatrixParam
        url: "rest/admin/generate-games/group/id/" + groupId + ";shuffledMode=" + shuffledMode.toString(),
        type: "POST",
        statusCode: {
            201: function () {
                $(generateButton).remove();
                showGames($("#show-unplayed-games"), "unplayed", true);
            },
            409: function () {
                alert("Conflict: one or more game(s) already exist(s) in this group.");
                // disable button
                $(generateButton).remove();
            },
            500: function () {
                alert("Unknown error.");
            },
            501: function () {
                alert("Error during game calculation.");
            },
            428: function () {
                alert("Cannot calculate games: Too few group members.");
                $(generateButton).removeAttr("disabled").attr("value", "Generate games");
            }
        }
    });
};
