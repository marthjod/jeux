var getRESTApiStatus = function(statusDiv) {
    "use strict";

    $.get("rest/audience/status", function(data) {
        $(statusDiv).html("ReST API status " + data);
    });
};

var showGroups = function(showGroupsDiv, playerGroupSelect, ruleSrcGroupSelect, ruleDestGroupSelect) {
    "use strict";

    var i = 0, table = null, currentGroup = null, row = null, gameGenerationCell = null, shuffledGameListCell = null, deletionCell = null, playerGroupSelectOK = false, ruleGroupSelectsOK = false, gameGenerationCell = null;

    $.get("rest/audience/groups", function(data) {

        if (playerGroupSelect && playerGroupSelect !== null) {
            playerGroupSelectOK = true;
            $(playerGroupSelect).find("option").remove();
            $("<option>").attr("id", "no-group-selected").text("No group selected").appendTo($(playerGroupSelect));
        }

        if (ruleSrcGroupSelect && ruleSrcGroupSelect !== null && ruleDestGroupSelect && ruleDestGroupSelect !== null) {
            ruleGroupSelectsOK = true;
            $(ruleSrcGroupSelect).find("option").remove();
            $(ruleDestGroupSelect).find("option").remove();
            $("<option>").attr("id", "no-source-group-selected").text("No source group selected").appendTo($(ruleSrcGroupSelect));
            $("<option>").attr("id", "no-destination-group-selected").text("No destination group selected").appendTo($(ruleDestGroupSelect));
        }

        $(showGroupsDiv).empty();
        table = $("<table>").attr("class", "table table-hover table-bordered table-condensed");
        row = $("<tr>");

        $("<th>").html("Name").appendTo(row);
        $("<th>").html("Round").appendTo(row);
        $("<th>").html("Min sets").appendTo(row);
        $("<th>").html("Max sets").appendTo(row);
        $("<th>").html("Active").appendTo(row);
        $("<th>").html("Completed").appendTo(row);
        $("<th>").appendTo(row);
        $("<th>").appendTo(row);
        row.appendTo(table);

        if (data && typeof data === "object" && data.hasOwnProperty("length") && data.length > 0) {

            for (i = 0; i < data.length; i++) {

                // readability
                currentGroup = data[i];

                row = $("<tr>").attr("id", "group-id-" + currentGroup.id);
                $("<td>").attr("class", "group-name").html(currentGroup.name).appendTo(row);
                $("<td>").attr("class", "group-round-id").html(currentGroup.roundId).appendTo(row);
                $("<td>").attr("class", "group-minsets").html(currentGroup.minSets).appendTo(row);
                $("<td>").attr("class", "group-maxsets").html(currentGroup.maxSets).appendTo(row);
                $("<td>").attr("class", "group-active").html(currentGroup.active.toString()).appendTo(row);
                $("<td>").attr("class", "group-completed").html(currentGroup.completed.toString()).appendTo(row);
                
                gameGenerationCell = $("<td>");
                $("<input>").attr("type", "submit").attr("class", "btn btn-success").attr("value", "Generate games").appendTo(gameGenerationCell).attr("onclick", "generateGames(this, " + currentGroup.id + ", false);");
                gameGenerationCell.appendTo(row);
                
                shuffledGameListCell = $("<td>");
                $("<input>").attr("type", "submit").attr("class", "btn btn-warning").attr("value", "Shuffled games list (BETA)").appendTo(shuffledGameListCell).attr("onclick", "getShuffledGames("+currentGroup.id+");");
                shuffledGameListCell.appendTo(row);
                
                deletionCell = $("<td>");
                $("<input>").attr("type", "submit").attr("class", "btn btn-danger").attr("value", "Delete group").appendTo(deletionCell).attr("onclick", "deleteGroup(" + currentGroup.id + ", this);");
                deletionCell.appendTo(row);

                row.appendTo(table);

                if (playerGroupSelectOK) {
                    $("<option>").attr("id", "group-id-" + currentGroup.id).text(currentGroup.name).appendTo($(playerGroupSelect));
                }

                if (ruleGroupSelectsOK) {
                    $("<option>").attr("id", "rule-source-group-id-" + currentGroup.id).text(currentGroup.name).appendTo($(ruleSrcGroupSelect));
                    $("<option>").attr("id", "rule-destination-group-id-" + currentGroup.id).text(currentGroup.name).appendTo($(ruleDestGroupSelect));
                }
            }

            table.appendTo(showGroupsDiv);
        }
    });
};

var showGames = function(showGamesDiv, status) {
    "use strict";

    var i = 0, j = 0, k = 0, gameTable = null, player1Header = null, player2Header = null, setNumberHeader = null, setsTable = null, setsCell = null, row = null, urlPrefix = "", updateCell = null, statusKnown = false, sets = [];

    if (status && status !== null && typeof status === "string") {
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
            if (groupsData && typeof groupsData === "object" && groupsData.hasOwnProperty("length") && groupsData.length > 0) {

                // next, iterate thru groups

                for (k = 0; k < groupsData.length; k++) {

                    // iterate thru individual group's data

                    (function(group, status) {

                        $.get(urlPrefix + group.id, function(gamesData) {

                            if (typeof gamesData !== undefined && gamesData !== null && typeof gamesData === "object" && gamesData.hasOwnProperty("length") && gamesData.length > 0) {

                                $("<h3>").html(group.name).appendTo(showGamesDiv);

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
                                    $("<input>").attr("type", "submit").attr("class", "btn btn-primary update-game").attr("value", "Update game").appendTo(updateCell).attr("onclick", "updateGame(this," + gamesData[i].id + "," + gamesData[i].player1Id + "," + gamesData[i].player2Id + ");");

                                    row.appendTo(gameTable);

                                    if (gamesData[i].hasOwnProperty("sets")) {
                                    	
                                    	sets = gamesData[i].sets;
                                    	// make array.sort() work with numbers
                                    	sets.sort(function (a, b) {
                                    		return a.number - b.number;
                                    	});

                                        setsTable = $("<table>").attr("class", "table table-bordered table-striped");

                                        for (j = 0; j < sets.length; j++) {
                                            row = $("<tr>").attr("id", "gameset-id-" + sets[j].id);

                                            // set number
                                            $("<td>").html($("<em>").html(sets[j].number)).attr("width", "20%").appendTo(row);
                                            
                                            if (status === "unplayed") {
                                            	// make input fields if unplayed
                                                $("<td>").attr("class", "player1-score").append($("<input>").attr("type", "number").attr("class", "form-control").attr("min", "0").val(sets[j].player1Score)).appendTo(row);
                                                $("<td>").attr("class", "player2-score").append($("<input>").attr("type", "number").attr("class", "form-control").attr("min", "0").val(sets[j].player2Score)).appendTo(row);
                                            } else {
                                            	// show text if played
                                                $("<td>").attr("class", "player1-score").html(sets[j].player1Score).attr("width", "40%").appendTo(row);
                                                $("<td>").attr("class", "player2-score").html(sets[j].player2Score).attr("width", "40%").appendTo(row);
                                            }

                                            row.appendTo(setsTable);
                                        }

                                        row = $("<tr>");
                                        setsCell = $("<td>").attr("colspan", "3").appendTo(row);
                                        setsTable.appendTo(setsCell);
                                        row.appendTo(gameTable);

                                        if (status === "unplayed") {
                                        	// show update button if unplayed
                                            row = $("<tr>");
                                            updateCell.appendTo(row);
                                            row.appendTo(gameTable);
                                        }
                                    }

                                    gameTable.appendTo(showGamesDiv);
                                }

                            }
                        });
                    }(groupsData[k], status));

                }

            }
        });
    }
};

var showPlayers = function(showPlayersDiv) {
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

                            $("<h3>").html(group.name).appendTo(showPlayersDiv);
                            $("<br>").appendTo(showPlayersDiv);
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

                            table.appendTo(showPlayersDiv);
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

var getShuffledGames = function (groupId) {
    "use strict";
    
    window.open("rest/admin/shuffled-games/group/id/" + groupId);
};