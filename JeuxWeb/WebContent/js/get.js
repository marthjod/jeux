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

        if (typeof data !== undefined && data !== null && typeof data === "object" && data.hasOwnProperty("length") && data.length > 0) {

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
                $("<input>").attr("type", "submit").attr("value", "Delete group").appendTo(deletionCell).attr("onclick", "deleteGroup(this);");
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

function showGames(showGamesDiv, status) {
    "use strict";

    var i = 0, j = 0, k = 0, table = null, setsTable = null, row = null, urlPrefix = "", updateCell = null, statusKnown = false;

    if (typeof status !== undefined && status !== null && typeof status === "string") {
        if (status === "played") {
            urlPrefix = "rest/audience/games/played/group/id/";
            statusKnown = true;
        } else if (status === "unplayed") {
            urlPrefix = "rest/audience/games/unplayed/group/id/";
            statusKnown = true;
        }
    }

    if (statusKnown) {

        $(showGamesDiv).empty();

        // first, get all groups

        $.get("rest/audience/groups", function(groupsData) {
            if (typeof groupsData !== undefined && groupsData !== null && typeof groupsData === "object" && groupsData.hasOwnProperty("length") && groupsData.length !== 0) {

                // next, iterate thru groups

                for (k = 0; k < groupsData.length; k++) {

                    // iterate thru individual group's data

                    (function(group, status) {

                        $.get(urlPrefix + group.id, function(gamesData) {

                            if (typeof gamesData !== undefined && gamesData !== null && typeof gamesData === "object" && gamesData.hasOwnProperty("length") && gamesData.length > 0) {

                                $("<span>").attr("class", "title").html(group.name).appendTo(showGamesDiv);
                                $("<br>").appendTo(showGamesDiv);

                                for (i = 0; i < gamesData.length; i++) {

                                    table = $("<table>");
                                    row = $("<tr>");

                                    $("<th>").html("Player 1").appendTo(row);
                                    $("<th>").html("Player 2").appendTo(row);
                                    if (status === "played") {
                                        $("<th>").html("Winner").appendTo(row);
                                    } else if (status === "unplayed") {
                                        $("<th>").appendTo(row);
                                    }
                                    row.appendTo(table);

                                    row = $("<tr>").attr("id", "game-id-" + gamesData[i].id);
                                    $("<td>").attr("class", "player1").attr("id", "player-id-" + gamesData[i].player1Id).html(gamesData[i].player1Name).appendTo(row);
                                    $("<td>").attr("class", "player2").attr("id", "player-id-" + gamesData[i].player2Id).html(gamesData[i].player2Name).appendTo(row);

                                    if (status === "unplayed") {
                                        updateCell = $("<td>");
                                        $("<input>").attr("type", "submit").attr("class", "update-game").attr("value", "Update game").appendTo(updateCell).attr("onclick", "updateGame(this);");
                                        updateCell.appendTo(row);
                                    } else if (status === "played") {
                                        $("<td>").attr("class", "winner").html(gamesData[i].winnerName).appendTo(row);
                                    }

                                    row.appendTo(table);

                                    if (gamesData[i].hasOwnProperty("sets")) {

                                        setsTable = $("<table>");

                                        row = $("<tr>");
                                        $("<th>").attr("class", "gamesets-header").html("Player 1 score").appendTo(row);
                                        $("<th>").attr("class", "gamesets-header").html("Player 2 score").appendTo(row);
                                        row.appendTo(setsTable);

                                        for (j = 0; j < gamesData[i].sets.length; j++) {
                                            row = $("<tr>").attr("id", "gameset-id-" + gamesData[i].sets[j].id);

                                            if (status === "unplayed") {
                                                $("<td>").attr("class", "player1-score").append($("<input>").prop("type", "number").attr("min", "0").val(gamesData[i].sets[j].player1Score)).appendTo(row);
                                                $("<td>").attr("class", "player2-score").append($("<input>").prop("type", "number").attr("min", "0").val(gamesData[i].sets[j].player2Score)).appendTo(row);
                                            } else {
                                                $("<td>").attr("class", "player1-score").html(gamesData[i].sets[j].player1Score).appendTo(row);
                                                $("<td>").attr("class", "player2-score").html(gamesData[i].sets[j].player2Score).appendTo(row);
                                            }

                                            row.appendTo(setsTable);
                                        }

                                        setsTable.appendTo(table);
                                    }

                                    table.appendTo(showGamesDiv);
                                    $("<br>").appendTo(showGamesDiv);
                                }

                            }
                        });
                    }(groupsData[k], status));

                }

            }
        });
    }
}

function showAllPlayers(showPlayersDiv) {
    "use strict";

    var i = 0, k = 0, table = null, row = null, deletionCell = null;

    $(showPlayersDiv).empty();

    // first, get all groups

    $.get("rest/audience/groups", function(groupsData) {
        if (typeof groupsData !== undefined && groupsData !== null && typeof groupsData === "object" && groupsData.hasOwnProperty("length") && groupsData.length !== 0) {

            for (k = 0; k < groupsData.length; k++) {
                (function(group) {
                    $.get("rest/audience/players/group/id/" + group.id, function(playersData) {
                        if (typeof playersData !== undefined && playersData !== null && typeof playersData === "object" && playersData.hasOwnProperty("length") && playersData.length > 0) {

                            $("<span>").attr("class", "title").html(group.name).appendTo(showPlayersDiv);
                            $("<br>").appendTo(showPlayersDiv);
                            table = $("<table>");
                            row = $("<tr>");

                            $("<th>").html("Name").appendTo(row);
                            $("<th>").html("ID").appendTo(row);
                            $("<th>").html("Rank").appendTo(row);
                            $("<th>").html("Points").appendTo(row);
                            $("<th>").html("Score ratio").appendTo(row);
                            $("<th>").appendTo(row);
                            row.appendTo(table);

                            for (i = 0; i < playersData.length; i++) {
                                row = $("<tr>").attr("id", "player-id-" + playersData[i].id);
                                $("<td>").attr("class", "player-name").html(playersData[i].name).appendTo(row);
                                $("<td>").attr("class", "player-id").html(playersData[i].id).appendTo(row);
                                $("<td>").attr("class", "player-rank").html(playersData[i].rank).appendTo(row);
                                $("<td>").attr("class", "player-points").html(playersData[i].points).appendTo(row);
                                $("<td>").attr("class", "player-score-ratio").html(playersData[i].scoreRatio).appendTo(row);
                                deletionCell = $("<td>");
                                $("<input>").attr("type", "submit").attr("value", "Delete player").appendTo(deletionCell).attr("onclick", "deletePlayer(this);");
                                deletionCell.appendTo(row);
                                row.appendTo(table);
                            }

                            table.appendTo(showPlayersDiv);
                            $("<br>").appendTo(showPlayersDiv);
                        } else {
                            // $(showPlayersDiv).html("No or no valid data for
                            // players available for " + group.name + ".<br
                            // />");
                        }

                    });
                }(groupsData[k]));

            }
        }
    });
}
