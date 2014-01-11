function updateGame(updateSubmit, gameId, player1Id, player2Id) {
    "use strict";

    var inputOK = false, gameSetId = 0, rows = [], i = 0, set = {}, player1Score = 0, player2Score = 0, updatedGame = {};

    $(updateSubmit).attr("value", "Updating...").attr("disabled", "disabled");

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
        url : "rest/admin/update-game",
        type : "POST",
        data : JSON.stringify(updatedGame),
        contentType : "application/json",
        success : function() {
            $(updateSubmit).attr("value", "Update game").removeAttr("disabled");
            showGames($("#show-unplayed-games"), "unplayed");
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

function generateGames(groupId, shuffledMode) {
    "use strict";

    if (typeof shuffledMode === undefined || shuffledMode === null || typeof shuffledMode !== "boolean") {
        shuffledMode = false;
    }

    $.ajax({
        // access with @MatrixParam
        url : "rest/admin/generate-games/group/id/" + groupId + ";shuffledMode=" + shuffledMode.toString(),
        type : "POST",
        success : function() {

        },
        statusCode : {
            409 : function() {
                alert("Conflict: games already exist for group.");
            },
            500 : function() {
                alert("Unknown error.");
            },
            501 : function() {
                alert("Calculation error.");
            }
        }
    });

}
