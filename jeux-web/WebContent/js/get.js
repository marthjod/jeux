var getRESTApiStatus = function(statusDiv) {
    "use strict";

    $.get("rest/audience/status", function(data) {
        $(statusDiv).html("ReST API status " + data);
    });
};

var showGroups = function(showGroupsDiv, playerGroupSelect, ruleSrcGroupSelect, ruleDestGroupSelect) {
    "use strict";

    var i = 0, table = null, currentGroup = null, row = null, gameGenerationCell = null, shuffledGamesDefaultCell = null, shuffledGamesLaTeXCell = null, deletionCell = null, playerGroupSelectOK = false, ruleGroupSelectsOK = false, gameGenerationCell = null;

    $.get("rest/audience/groups", function(data) {

        if (playerGroupSelect && playerGroupSelect !== null) {
            playerGroupSelectOK = true;
            $(playerGroupSelect).find("option").remove();
            $(playerGroupSelect).append($("<option>").attr("id", "no-group-selected").text("No group selected"));
        }

        if (ruleSrcGroupSelect && ruleSrcGroupSelect !== null && ruleDestGroupSelect && ruleDestGroupSelect !== null) {
            ruleGroupSelectsOK = true;
            $(ruleSrcGroupSelect).find("option").remove();
            $(ruleDestGroupSelect).find("option").remove();
            $(ruleSrcGroupSelect).append($("<option>").attr("id", "no-source-group-selected").text("No source group selected"));
            $(ruleDestGroupSelect).append($("<option>").attr("id", "no-destination-group-selected").text("No destination group selected"));
        }

        $(showGroupsDiv).empty();
        table = $("<table>").attr("class", "table table-hover table-bordered table-condensed");
        row = $("<tr>");

        row.append($("<th>").html("Name"));
        row.append($("<th>").html("Round"));
        row.append($("<th>").html("Min sets"));
        row.append($("<th>").html("Max sets"));
        row.append($("<th>").html("Active"));
        row.append($("<th>").html("Completed"));
        row.append($("<th>"));
        row.append($("<th>"));
        row.append($("<th>"));
        table.append(row);

        if (data && typeof data === "object" && data.hasOwnProperty("length") && data.length > 0) {

            for (i = 0; i < data.length; i++) {

                // readability
                currentGroup = data[i];

                row = $("<tr>").attr("id", "group-id-" + currentGroup.id);
                row.append($("<td>").attr("class", "group-name").html(currentGroup.name));
                row.append($("<td>").attr("class", "group-round-id").html(currentGroup.roundId));
                row.append($("<td>").attr("class", "group-minsets").html(currentGroup.minSets));
                row.append($("<td>").attr("class", "group-maxsets").html(currentGroup.maxSets));
                row.append($("<td>").attr("class", "group-active").html(currentGroup.active.toString()));
                row.append($("<td>").attr("class", "group-completed").html(currentGroup.completed.toString()));

                gameGenerationCell = $("<td>");
                gameGenerationCell.append($("<input>").attr("type", "submit").attr("class", "btn btn-success").attr("value", "Generate games").attr("onclick", "generateGames(this, " + currentGroup.id + ", false);"));

                shuffledGamesDefaultCell = $("<td>");
                shuffledGamesDefaultCell.append($("<input>").attr("type", "submit").attr("class", "btn btn-warning").attr("value", "Games list").attr("onclick", "getShuffledGames(" + currentGroup.id + ");"));

                shuffledGamesLaTeXCell = $("<td>");
                shuffledGamesLaTeXCell.append($("<input>").attr("type", "submit").attr("class", "btn btn-warning").attr("value", "Scoresheets (LaTeX)").attr("onclick", "getShuffledGames(" + currentGroup.id + ", 'latex');"));

                deletionCell = $("<td>");
                deletionCell.append($("<input>").attr("type", "submit").attr("class", "btn btn-danger").attr("value", "Delete group").attr("onclick", "deleteGroup(" + currentGroup.id + ", this);"));

                row.append(gameGenerationCell);
                row.append(shuffledGamesDefaultCell);
                row.append(shuffledGamesLaTeXCell);
                row.append(deletionCell);
                table.append(row);

                if (playerGroupSelectOK) {
                    $("<option>").attr("id", "group-id-" + currentGroup.id).text(currentGroup.name).appendTo($(playerGroupSelect));
                }

                if (ruleGroupSelectsOK) {
                    $("<option>").attr("id", "rule-source-group-id-" + currentGroup.id).text(currentGroup.name).appendTo($(ruleSrcGroupSelect));
                    $("<option>").attr("id", "rule-destination-group-id-" + currentGroup.id).text(currentGroup.name).appendTo($(ruleDestGroupSelect));
                }
            }

            showGroupsDiv.append(table);
        }
    });
};

var showGames = function(showGamesDiv, status, editable) {
    "use strict";

    var urlPrefixes = {
        "played" : "rest/audience/games/played/group/id/",
        "unplayed" : "rest/audience/games/unplayed/group/id/"
    }, k = 0, groupDiv = null;

    $(showGamesDiv).empty();

    $.get("rest/audience/groups", function(groupsData) {
        if (groupsData && typeof groupsData === "object" && groupsData.hasOwnProperty("length") && groupsData.length > 0) {

            // iterate thru groups

            for (k = 0; k < groupsData.length; k++) {

                // iterate thru individual group's data

                (function(group, status) {

                    $.get(urlPrefixes[status] + group.id, function(gamesData) {
                        groupDiv = $("<div>").attr("class", "games-in-group").attr("id", group.name);
                        $("<h3>").html(group.name).appendTo(groupDiv);
                        showGamesData(gamesData, groupDiv, status, editable);
                        $(groupDiv).appendTo(showGamesDiv);
                    });
                }(groupsData[k], status));

            }

        }
    });

};

var showGamesData = function(gamesData, groupDiv, status, editable) {
    "use strict";

    var i = 0, j = 0, k = 0, gameTable = null, player1Header = null, player2Header = null, setNumberHeader = null, setsTable = null, setsCell = null, row = null, updateCell = null, updateButton = null, sets = [];

    if (gamesData && typeof gamesData === "object" && gamesData.hasOwnProperty("length") && gamesData.length > 0) {

        for (i = 0; i < gamesData.length; i++) {

            gameTable = $("<table>").attr("class", "table table-bordered table-condensed");

            row = $("<tr>").attr("id", "game-id-" + gamesData[i].id);
            setNumberHeader = $("<th>").html("Set #").attr("width", "20%");
            player1Header = $("<th>").attr("class", "player1").attr("id", "player-id-" + gamesData[i].player1Id).html(gamesData[i].player1Name).attr("width", "40%");
            player2Header = $("<th>").attr("class", "player2").attr("id", "player-id-" + gamesData[i].player2Id).html(gamesData[i].player2Name).attr("width", "40%");

            // mark winner name
            if (gamesData[i].winnerName === gamesData[i].player1Name) {
                player1Header.html($("<em>").html(gamesData[i].player1Name + " *"));
            } else if (gamesData[i].winnerName === gamesData[i].player2Name) {
                player2Header.html($("<em>").html(gamesData[i].player2Name + " *"));
            }

            setNumberHeader.appendTo(row);
            player1Header.appendTo(row);
            player2Header.appendTo(row);

            updateCell = $("<td>");
            updateButton = $("<input>").attr("type", "submit").attr("class", "btn btn-primary update-game").appendTo(updateCell);

            if (status === "played") {
                updateButton.attr("value", "Update game").attr("onclick", "updateGame(this," + gamesData[i].id + "," + gamesData[i].player1Id + "," + gamesData[i].player2Id + ", \"update\");");
            } else if (status === "unplayed") {
                updateButton.attr("value", "Save game").attr("onclick", "updateGame(this," + gamesData[i].id + "," + gamesData[i].player1Id + "," + gamesData[i].player2Id + ", \"save\");");
            }

            row.appendTo(gameTable);

            if (gamesData[i].hasOwnProperty("sets")) {

                sets = gamesData[i].sets;
                // make array.sort() work with numbers
                sets.sort(function(a, b) {
                    return a.number - b.number;
                });

                setsTable = $("<table>").attr("class", "table table-bordered table-striped");

                for (j = 0; j < sets.length; j++) {
                    row = $("<tr>").attr("id", "gameset-id-" + sets[j].id);

                    if (typeof editable === "boolean" && editable === true) {
                        // set number
                        $("<td>").html($("<em>").html(sets[j].number)).attr("width", "20%").appendTo(row);
                        $("<td>").attr("class", "player1-score").append($("<input>").attr("type", "number").attr("class", "form-control").attr("min", "0").val(sets[j].player1Score)).appendTo(row);
                        $("<td>").attr("class", "player2-score").append($("<input>").attr("type", "number").attr("class", "form-control").attr("min", "0").val(sets[j].player2Score)).appendTo(row);
                    } else {
                        if (status === "played" && !(sets[j].player1Score === 0 && sets[j].player2Score === 0)) {
                            // set number
                            $("<td>").html($("<em>").html(sets[j].number)).attr("width", "20%").appendTo(row);
                            $("<td>").attr("class", "player1-score").html(sets[j].player1Score).attr("width", "40%").appendTo(row);
                            $("<td>").attr("class", "player2-score").html(sets[j].player2Score).attr("width", "40%").appendTo(row);
                        }
                    }

                    row.appendTo(setsTable);
                }

                row = $("<tr>");
                setsCell = $("<td>").attr("colspan", "3").appendTo(row);
                setsTable.appendTo(setsCell);
                row.appendTo(gameTable);

                // always show update button
                if (typeof editable === "boolean" && editable === true) {
                    row = $("<tr>");
                    updateCell.appendTo(row);
                    row.appendTo(gameTable);
                }
            }

            gameTable.appendTo(groupDiv);
        }

    }
};

var showPlayers = function(showPlayersDiv) {
    "use strict";

    var i = 0, k = 0, table = null, row = null, deletionCell = null, groupPlayersDiv = null;

    $(showPlayersDiv).empty();

    // first, get all groups

    $.get("rest/audience/groups", function(groupsData) {
        if (typeof groupsData !== undefined && groupsData !== null && typeof groupsData === "object" && groupsData.hasOwnProperty("length") && groupsData.length !== 0) {

            for (k = 0; k < groupsData.length; k++) {
                (function(group) {
                    $.get("rest/audience/players/group/id/" + group.id, function(playersData) {
                        if (typeof playersData !== undefined && playersData !== null && typeof playersData === "object" && playersData.hasOwnProperty("length") && playersData.length > 0) {

                            groupPlayersDiv = $("<div>").attr("class", "group-players");
                            groupPlayersDiv.appendTo(showPlayersDiv);
                            $("<h3>").html(group.name).appendTo(groupPlayersDiv);
                            $("<br>").appendTo(groupPlayersDiv);
                            table = $("<table>").attr("class", "table table-hover table-bordered table-condensed");
                            row = $("<tr>");

                            $("<th>").html("Name").appendTo(row);
                            $("<th>").html("Rank").appendTo(row);
                            $("<th>").html("Points").appendTo(row);
                            $("<th>").html("Score ratio").appendTo(row);
                            $("<th>").appendTo(row);
                            row.appendTo(table);

                            for (i = 0; i < playersData.length; i++) {
                                row = $("<tr>").attr("id", "player-id-" + playersData[i].id);
                                $("<td>").attr("class", "player-name").html(playersData[i].name).appendTo(row);
                                $("<td>").attr("class", "player-rank").html(playersData[i].rank).appendTo(row);
                                $("<td>").attr("class", "player-points").html(playersData[i].points).appendTo(row);
                                $("<td>").attr("class", "player-score-ratio").html(playersData[i].scoreRatio).appendTo(row);
                                deletionCell = $("<td>");
                                $("<input>").attr("type", "submit").attr("class", "btn btn-danger").attr("value", "Delete player").appendTo(deletionCell).attr("onclick", "deletePlayer(" + playersData[i].id + ");");
                                deletionCell.appendTo(row);
                                row.appendTo(table);
                            }

                            table.appendTo(groupPlayersDiv);
                        }
                    });
                }(groupsData[k]));
            }
        }
    });
};

var rankSorter = function(firstPlayer, secondPlayer) {
    "use strict";

    var retval = 0;

    /*
     * If the return value is less than zero, the index of a is before b, and if
     * it is greater than zero it's vice-versa. If the return value is zero, the
     * elements' index is equal.
     */

    if (firstPlayer && secondPlayer && firstPlayer.hasOwnProperty("rank") && secondPlayer.hasOwnProperty("rank")) {
        // smaller = better
        if (firstPlayer.rank < secondPlayer.rank) {
            retval = -1;
        } else if (firstPlayer.rank > secondPlayer.rank) {
            retval = 1;
        }
    }

    return retval;
};

var showRankings = function(rankingsDiv) {
    "use strict";

    var i = 0, k = 0, table = null, row = null;

    $(rankingsDiv).empty();

    // first, get all groups

    $.get("rest/audience/groups", function(groupsData) {
        if (groupsData && typeof groupsData === "object" && groupsData.hasOwnProperty("length") && groupsData.length > 0) {

            // next, iterate thru groups

            for (k = 0; k < groupsData.length; k++) {
                (function(group) {

                    // for each group, fetch its rankings data

                    $.get("rest/audience/rankings/group/id/" + group.id, function(rankingsData) {
                        if (typeof rankingsData !== undefined && rankingsData !== null && typeof rankingsData === "object" && rankingsData.hasOwnProperty("length") && rankingsData.length > 0) {

                            $("<h3>").html(group.name).appendTo(rankingsDiv);
                            $("<br>").appendTo(rankingsDiv);
                            table = $("<table>").attr("class", "table table-hover table-bordered table-condensed");
                            row = $("<tr>");

                            $("<th>").html("Rank").appendTo(row);
                            $("<th>").html("Name").appendTo(row);
                            $("<th>").html("Won games").appendTo(row);
                            $("<th>").html("Points").appendTo(row);
                            $("<th>").html("Score ratio").appendTo(row);
                            row.appendTo(table);

                            rankingsData.sort(rankSorter);
                            // rankings...
                            for (i = 0; i < rankingsData.length; i++) {
                                row = $("<tr>").attr("id", "player-id-" + rankingsData[i].id);
                                $("<td>").attr("class", "player-rank").html(rankingsData[i].rank).appendTo(row);
                                $("<td>").attr("class", "player-name").html(rankingsData[i].name).appendTo(row);
                                $("<td>").attr("class", "player-won-games").html(rankingsData[i].wonGames).appendTo(row);
                                $("<td>").attr("class", "player-points").html(rankingsData[i].points).appendTo(row);
                                $("<td>").attr("class", "player-score-ratio").html(rankingsData[i].scoreRatio).appendTo(row);
                                row.appendTo(table);
                            }

                            table.appendTo(rankingsDiv);
                        }
                    });
                }(groupsData[k]));
            }
        }
    });
};

var showRules = function(showRulesDiv) {
    "use strict";

    var i = 0, table = null, row = null, startWithRank = 0, additionalPlayers = 0, lastRank = 0, ruleId = 0, deletionCell = null;

    if (showRulesDiv && showRulesDiv != null) {
        $(showRulesDiv).empty();

        $.get("rest/audience/roundswitchrules", function(rulesData) {
            if (rulesData && typeof rulesData === "object" && rulesData.hasOwnProperty("length") && rulesData.length > 0) {

                table = $("<table>").attr("class", "table table-hover table-bordered table-condensed");
                row = $("<tr>");
                $("<th>").html("Source group").appendTo(row);
                $("<th>").html("Destination group").appendTo(row);
                $("<th>").html("Ranks").appendTo(row);
                $("<th>").html("&Sigma;").appendTo(row);
                // $("<th>").html("Start with rank").appendTo(row);
                // $("<th>").html("Additional players").appendTo(row);
                $("<th>").appendTo(row);
                row.appendTo(table);

                for (i = 0; i < rulesData.length; i++) {

                    ruleId = rulesData[i].id;
                    startWithRank = parseInt(rulesData[i].startWithRank, 10);
                    additionalPlayers = parseInt(rulesData[i].additionalPlayers, 10);
                    lastRank = startWithRank + additionalPlayers;

                    row = $("<tr>");
                    $("<td>").html(rulesData[i].srcGroupName).appendTo(row);
                    $("<td>").html(rulesData[i].destGroupName).appendTo(row);
                    $("<td>").html(startWithRank + "-" + lastRank).appendTo(row);
                    $("<td>").html(additionalPlayers + 1).appendTo(row);
                    // $("<td>").html(startWithRank).appendTo(row);
                    // $("<td>").html(additionalPlayers).appendTo(row);
                    deletionCell = $("<td>");
                    $("<input>").attr("type", "submit").attr("class", "btn btn-danger").attr("value", "Delete rule").appendTo(deletionCell).attr("onclick", "deleteRule(" + ruleId + ", this);");
                    deletionCell.appendTo(row);
                    row.appendTo(table);
                }

                table.appendTo(showRulesDiv);
            }
        });
    }
};

var getShuffledGames = function(groupId, format) {
    "use strict";

    var url = "rest/admin/shuffled-games/group/id/" + groupId;

    if (format && format !== null && typeof format === 'string') {
        url += ";format=" + format;
    }

    window.open(url);
};