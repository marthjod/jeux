var deleteGroup = function (groupId, deleteButton, prefix) {
    "use strict";

    var url = "rest/admin/group/id/" + groupId;

    if (prefix && typeof prefix === 'string') {
        url = prefix + '/' + url;
    }

    if (!window.confirm("Really delete group?")) {
        return false;
    }

    if (deleteButton && deleteButton !== null) {
        $(deleteButton).hide();
    }

    $.ajax({
        url: url,
        type: "DELETE",
        success: function () {
            // alert("Group deleted");

            // remove as selectable option
            $("#player-select-group").find("#group-id-" + groupId).remove();
            $("#rule-source-group").find("#rule-source-group-id-" + groupId).remove();
            $("#rule-destination-group").find("#rule-destination-group-id-" + groupId).remove();

            showGroups($("#show-groups"));
            showPlayers($("#show-players"));
            // refresh because deletion cascades for games, too
            showGames($("#show-unplayed-games"), "unplayed", true);
            showGames($("#show-played-games"), "played", true);
            showRules($("#show-rules"));
        },
        // errors
        statusCode: {
            409: function () {
                alert("Conflict trying to delete group (violated constraint).");
            },
            403: function () {
                alert("Operation not permitted (unauthenticated request).");
            },
            500: function () {
                alert("Server error while trying to delete group.");
            }
        }
    });

};

var deletePlayer = function (playerId) {
    "use strict";

    $.ajax({
        url: "rest/admin/player/id/" + playerId,
        type: "DELETE",
        success: function () {
            showPlayers($("#show-players"));
            // refresh because player deletion cascades for his games, too
            showGames($("#show-unplayed-games"), "unplayed", true);
        },
        statusCode: {
            403: function () {
                alert("Operation not permitted (unauthenticated request).");
            },
            500: function () {
                alert("Server error while trying to delete player.");
            }
        }
    });
};

var deleteRule = function (ruleId, deleteButton) {
    "use strict";

    $.ajax({
        url: "rest/admin/roundswitchrule/id/" + ruleId,
        type: "DELETE",
        success: function () {
            showRules($("#show-rules"));
        },
        statusCode: {
            403: function () {
                alert("Operation not permitted (unauthenticated request).");
            },
            500: function () {
                alert("Server error while trying to delete rule.");
            }
        }
    });
};