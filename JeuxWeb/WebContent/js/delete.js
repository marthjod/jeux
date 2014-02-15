function deleteGroup(groupId) {
    "use strict";

    if (window.confirm("Warning: Removing this group\nwill also delete all players\nand games belonging to this group!")) {
        $.ajax({
            url : "rest/admin/group/id/" + groupId,
            type : "DELETE",
            success : function() {
                // alert("Group deleted");

                // remove as selectable option
                $("#player-select-group").find("#group-id-" + groupId).remove();
                $("#rule-source-group").find("#rule-source-group-id-" + groupId).remove();
                $("#rule-destination-group").find("#rule-destination-group-id-" + groupId).remove();

                showGroups($("#show-groups"));
                showPlayers($("#show-players"));
                // refresh because deletion cascades for games, too
                showGames($("#show-unplayed-games"), "unplayed");
                showRules($("#show-rules"));
            },
            statusCode : {
                409 : function() {
                    alert("Conflict trying to delete group (violated constraint).");
                },
                403 : function() {
                    alert("Operation not permitted (unauthenticated request).");
                },
                500 : function() {
                    alert("Server error while trying to delete group.");
                }
            }
        });
    }
}

function deletePlayer(playerId) {
    "use strict";

    $.ajax({
        url : "rest/admin/player/id/" + playerId,
        type : "DELETE",
        success : function() {
            showPlayers($("#show-players"));
            // refresh because player deletion cascades for his games, too
            showGames($("#show-unplayed-games"), "unplayed");
        },
        statusCode : {
            403 : function() {
                alert("Operation not permitted (unauthenticated request).");
            },
            500 : function() {
                alert("Server error while trying to delete player.");
            }
        }
    });
}