function deleteGroup(deleteSubmit) {
    "use strict";

    var groupId = -1, sendOK = false;

    try {
        groupId = parseInt(($(deleteSubmit).parent().parent().attr("id").replace("group-id-", "")), 10);
        sendOK = true;
    } catch (e) {
        alert(e);
    }

    if (sendOK) {

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

                    showAllGroups($("#show-all-groups"));
                    showAllPlayers($("#show-all-players"));
                    // refresh because deletion cascades for games, too
                    showGames($("#show-unplayed-games"), "unplayed");
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
}

function deletePlayer(deleteSubmit) {
    "use strict";

    var playerId = -1, sendOK = false;

    try {
        playerId = parseInt(($(deleteSubmit).parent().parent().attr("id").replace("player-id-", "")), 10);
        sendOK = true;
    } catch (e) {
        alert(e);
    }

    if (sendOK) {
        $.ajax({
            url : "rest/admin/player/id/" + playerId,
            type : "DELETE",
            success : function() {
                showAllPlayers($("#show-all-players"));
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

}