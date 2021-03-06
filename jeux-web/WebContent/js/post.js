var updateGame = function (updateSubmit, gameId, player1Id, player2Id, action, prefix) {
    "use strict";

    var inputOK = false,
            gameSetId = 0,
            rows = [],
            set = {},
            player1Score = 0,
            player2Score = 0,
            updatedGame = {},
            url = "rest/admin/update-game";

    if (prefix && typeof prefix === 'string') {
        url = prefix + '/' + url;
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
        url: url,
        type: "POST",
        data: JSON.stringify(updatedGame),
        contentType: "application/json",
        success: function () {
            document.location.reload();
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

var generateGames = function (generateButton, groupId, shuffledMode, prefix) {
    "use strict";

    var url = "rest/admin/generate-games/group/id/" + groupId + ";shuffledMode=" + shuffledMode.toString();

    if (prefix && typeof prefix === 'string') {
        url = prefix + '/' + url;
    }

    if (!shuffledMode || typeof shuffledMode !== "boolean") {
        shuffledMode = false;
    }

    $.ajax({
        // access with @MatrixParam
        url: url,
        type: "POST",
        statusCode: {
            201: function () {
                document.location.reload();
            },
            409: function () {
                alert("Conflict: one or more game(s) already exist(s) in this group.");
            },
            500: function () {
                alert("Unknown error.");
            },
            501: function () {
                alert("Error during game calculation.");
            },
            428: function () {
                alert("Cannot calculate games: Too few group members.");
            }
        }
    });
};


var switchRound = function (switchButton, url) {
    "use strict";

    $(switchButton).attr("disabled", "disabled");
    window.setTimeout(function () {
        $(switchButton).removeAttr("disabled");
    }, 5000);

    $.ajax({
        url: url,
        type: 'POST',
        statusCode: {
            204: function () {
                alert("Current round not over yet.");
            },
            201: function () {
                $(switchButton).attr("class", "btn btn-lg btn-success");
                window.setTimeout(function () {
                    $(switchButton).attr("class", "btn btn-lg btn-danger");
                }, 3000);
            }
        }
    });
};

var doTakeover = function (groupId, prefix) {
    "use strict";

    var url = "rest/admin/takeover/group/" + groupId;

    if (prefix && typeof prefix === 'string') {
        url = prefix + '/' + url;
    }

    $.ajax({
        url: url,
        type: "POST",
        statusCode: {
            200: function () {
                document.location.reload();
            },
            204: function () {
                alert("Nothing to do.");
            },
            403: function () {
                alert("Operation not permitted (unauthenticated request).");
            },
            409: function () {
                alert("Destination group conflict.");
            },
            428: function () {
                alert("Source group conflict.");
            },
            500: function () {
                alert("Failed to do takeover for group.");
            }
        }
    });
};
