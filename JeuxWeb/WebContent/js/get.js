function getRESTApiStatus(statusDiv) {
    "use strict";

    $.get("rest/audience/status", function(data) {
        $(statusDiv).html("REST API status " + data);
    });
}

function showAllGroups(showAllGroupsDiv, playerGroupSelect, ruleSrcGroupSelect, ruleDestGroupSelect) {
    "use strict";

    var i = 0, table = null, row = null, deletionCell = null, playerGroupSelectOK = false, ruleGroupSelectsOK = false;

    $.get("rest/audience/groups", function(data) {

        if (playerGroupSelect !== undefined && playerGroupSelect !== null) {
            playerGroupSelectOK = true;
            $(playerGroupSelect).find("option").remove();
            $("<option>").attr("id", "no-group-selected").text("No group selected").appendTo($(playerGroupSelect));
        }

        if (ruleSrcGroupSelect !== undefined && ruleSrcGroupSelect !== null && ruleDestGroupSelect !== undefined && ruleDestGroupSelect !== null) {
            ruleGroupSelectsOK = true;
            $(ruleSrcGroupSelect).find("option").remove();
            $(ruleDestGroupSelect).find("option").remove();
            $("<option>").attr("id", "no-source-group-selected").text("No source group selected").appendTo($(ruleSrcGroupSelect));
            $("<option>").attr("id", "no-destination-group-selected").text("No destination group selected").appendTo($(ruleDestGroupSelect));
        }

        // showAllGroupsDiv
        $(showAllGroupsDiv).empty();
        $("<span>").attr("class", "title").html("Existing groups").appendTo(showAllGroupsDiv);
        $("<br>").appendTo(showAllGroupsDiv);

        if (typeof data !== undefined && data !== null && typeof data === "object" && data.hasOwnProperty("length") && data.length > 0) {

            table = $("<table>");
            row = $("<tr>");

            $("<th>").html("Name").appendTo(row);
            $("<th>").html("ID").appendTo(row);
            $("<th>").html("Round").appendTo(row);
            $("<th>").html("Min sets").appendTo(row);
            $("<th>").html("Max sets").appendTo(row);
            $("<th>").html("Active").appendTo(row);
            $("<th>").html("Completed").appendTo(row);
            $("<th>").appendTo(row);
            row.appendTo(table);

            for (i = 0; i < data.length; i++) {
                row = $("<tr>").attr("id", "group-id-" + data[i].id);
                $("<td>").attr("class", "group-name").html(data[i].name).appendTo(row);
                $("<td>").attr("class", "group-id").html(data[i].id).appendTo(row);
                $("<td>").attr("class", "group-round-id").html(data[i].roundId).appendTo(row);
                $("<td>").attr("class", "group-minsets").html(data[i].minSets).appendTo(row);
                $("<td>").attr("class", "group-maxsets").html(data[i].maxSets).appendTo(row);
                $("<td>").attr("class", "group-active").html(data[i].active.toString()).appendTo(row);
                $("<td>").attr("class", "group-completed").html(data[i].completed.toString()).appendTo(row);
                deletionCell = $("<td>");
                $("<input>").attr("type", "submit").attr("value", "Delete group").attr("class", "delete-button").appendTo(deletionCell).attr("onclick", "deleteGroup(this);");
                deletionCell.appendTo(row);
                row.appendTo(table);

                // populate playerGroupSelect as well
                // so that <option
                // id="GROUP_ID">GROUP_NAME</option>
                // are appended

                if (playerGroupSelectOK) {
                    $("<option>").attr("id", "group-id-" + data[i].id).text(data[i].name).appendTo($(playerGroupSelect));
                }

                if (ruleGroupSelectsOK) {
                    $("<option>").attr("id", "rule-source-group-id-" + data[i].id).text(data[i].name).appendTo($(ruleSrcGroupSelect));
                    $("<option>").attr("id", "rule-destination-group-id-" + data[i].id).text(data[i].name).appendTo($(ruleDestGroupSelect));
                }
            }

            table.appendTo(showAllGroupsDiv);

        } else {
            $(showAllGroupsDiv).html("No or no valid group data available.");
        }

    });
}

function showPlayedGames(showPlayedGamesDiv) {
    "use strict";

    var i = 0, j = 0, k = 0, table = null, row = null, group = [];

    $(showPlayedGamesDiv).empty();
    $("<span>").attr("class", "title").html("Played games").appendTo(showPlayedGamesDiv);
    $("<br>").appendTo(showPlayedGamesDiv);
    $("<br>").appendTo(showPlayedGamesDiv);

    // first, get all groups

    $.get("rest/audience/groups", function(groupsData) {
        if (typeof groupsData !== undefined && groupsData !== null && typeof groupsData === "object" && groupsData.hasOwnProperty("length") && groupsData.length !== 0) {

            for (k = 0; k < groupsData.length; k++) {
                (function(group) {
                    $.get("rest/audience/games/played/group/id/" + group.id, function(gamesData) {

                        if (typeof gamesData !== undefined && gamesData !== null && typeof gamesData === "object" && gamesData.hasOwnProperty("length") && gamesData.length > 0) {

                            $("<span>").attr("class", "title").html(group.name).appendTo(showPlayedGamesDiv);
                            $("<br>").appendTo(showPlayedGamesDiv);
                            table = $("<table>");
                            row = $("<tr>");

                            $("<th>").html("Player 1").appendTo(row);
                            $("<th>").html("Player 2").appendTo(row);
                            $("<th>").html("Winner").appendTo(row);
                            row.appendTo(table);

                            for (i = 0; i < gamesData.length; i++) {
                                row = $("<tr>").attr("id", "game-id-" + gamesData[i].id);
                                $("<td>").attr("class", "player1").html(gamesData[i].player1Name).appendTo(row);
                                $("<td>").attr("class", "player2").html(gamesData[i].player2Name).appendTo(row);
                                $("<td>").attr("class", "winner").html(gamesData[i].winnerName).appendTo(row);
                                row.appendTo(table);

                                if (gamesData[i].hasOwnProperty("sets")) {

                                    row = $("<tr>");
                                    $("<th>").attr("class", "gamesets-header").html("Player 1 score").appendTo(row);
                                    $("<th>").attr("class", "gamesets-header").html("Player 2 score").appendTo(row);
                                    $("<th>").attr("class", "gamesets-header").html("Winner").appendTo(row);
                                    row.appendTo(table);

                                    for (j = 0; j < gamesData[i].sets.length; j++) {
                                        row = $("<tr>").attr("id", "gameset-id-" + gamesData[i].sets[j].id);
                                        $("<td>").attr("class", "player1-score").html(gamesData[i].sets[j].player1Score).appendTo(row);
                                        $("<td>").attr("class", "player2-score").html(gamesData[i].sets[j].player2Score).appendTo(row);
                                        $("<td>").attr("class", "winner").html(gamesData[i].sets[j].winnerName).appendTo(row);
                                        row.appendTo(table);
                                    }
                                }
                            }

                            table.appendTo(showPlayedGamesDiv);
                            $("<br>").appendTo(showPlayedGamesDiv);
                        } else {
                            $(showPlayedGamesDiv).html("No or no valid data for played games available.");
                        }
                    });
                }(groupsData[k]));
            }

        }
    });
}
