var getRESTApiStatus = function (statusDiv) {
    "use strict";
    // only show message in case of error
    $.ajax({
        url: "rest/audience/status",
        error: function (err) {
            $(statusDiv).html("REST API status " + err);
        }
    });
};
var showGroups = function (showGroupsDiv, playerGroupSelect, ruleSrcGroupSelect, ruleDestGroupSelect) {
    "use strict";
    var table = null,
            row = null,
            playerGroupSelectOK = false,
            ruleGroupSelectsOK = false;
    $.get("rest/audience/groups", function (data) {

        if (playerGroupSelect && playerGroupSelect !== null) {
            playerGroupSelectOK = true;
            $(playerGroupSelect).find("option").remove();
            $(playerGroupSelect)
                    .append($("<option>")
                            .attr("id", "no-group-selected")
                            .text("No group selected"));
        }

        if (ruleSrcGroupSelect && ruleSrcGroupSelect !== null &&
                ruleDestGroupSelect && ruleDestGroupSelect !== null) {
            ruleGroupSelectsOK = true;
            $(ruleSrcGroupSelect).find("option").remove();
            $(ruleDestGroupSelect).find("option").remove();
            $(ruleSrcGroupSelect)
                    .append($("<option>")
                            .attr("id", "no-source-group-selected")
                            .text("No source group selected"));
            $(ruleDestGroupSelect)
                    .append($("<option>")
                            .attr("id", "no-destination-group-selected")
                            .text("No destination group selected"));
        }

        $(showGroupsDiv).empty();
        table = $("<table>")
                .attr("class", "table table-hover table-bordered table-condensed");
        table.append($("<tr>")
                .append($("<th>").html("Name"))
                .append($("<th>").html("Round"))
                .append($("<th>").html("Min sets"))
                .append($("<th>").html("Max sets"))
                .append($("<th>").html("Active"))
                .append($("<th>").html("Completed"))
                .append($("<th>"))
                .append($("<th>"))
                .append($("<th>"))
                .append($("<th>")));
        if ($.isArray(data) && data.length > 0) {

            $.each(data, function (id, group) {

                row = $("<tr>").attr("id", "group-id-" + group.id);
                row.append($("<td>").attr("class", "group-name").html(group.name))
                        .append($("<td>").attr("class", "group-round-id").html(group.roundId))
                        .append($("<td>").attr("class", "group-minsets").html(group.minSets))
                        .append($("<td>").attr("class", "group-maxsets").html(group.maxSets))
                        .append($("<td>").attr("class", "group-active").html(group.active.toString()))
                        .append($("<td>").attr("class", "group-completed").html(group.completed.toString()));
                row.append($("<td>")
                        .append($("<input>")
                                .attr("type", "submit")
                                .attr("class", "btn btn-success")
                                .attr("value", "Generate games")
                                .attr("onclick", "generateGames(this, " + group.id + ", false);")));
                row.append($("<td>")
                        .append($("<input>")
                                .attr("type", "submit")
                                .attr("class", "btn btn-default")
                                .attr("value", "Games list")
                                .attr("onclick", "getShuffledGames(" + group.id + ");")));
                row.append($("<td>")
                        .append($("<input>")
                                .attr("type", "submit")
                                .attr("class", "btn btn-default")
                                .attr("value", "Scoresheets (XeTeX)")
                                .attr("onclick", "getShuffledGames(" + group.id + ", 'latex');")));
                row.append($("<td>")
                        .append($("<input>")
                                .attr("type", "submit")
                                .attr("class", "btn btn-danger")
                                .attr("value", "Delete group")
                                .attr("onclick", "deleteGroup(" + group.id + ", this);")));
                table.append(row);
                if (playerGroupSelectOK) {
                    $(playerGroupSelect).append($("<option>")
                            .attr("id", "group-id-" + group.id)
                            .text(group.name));
                }

                if (ruleGroupSelectsOK) {
                    $(ruleSrcGroupSelect)
                            .append($("<option>")
                                    .attr("id", "rule-source-group-id-" + group.id)
                                    .text(group.name));
                    $(ruleDestGroupSelect)
                            .append($("<option>")
                                    .attr("id", "rule-destination-group-id-" + group.id)
                                    .text(group.name));
                }
            });
            showGroupsDiv.append(table);
        }
    });
};
var showGames = function (showGamesDiv, status, editable) {
    "use strict";
    var urlPrefixes = {
        "played": "rest/audience/games/played/group/id/",
        "unplayed": "rest/audience/games/unplayed/group/id/"
    },
    groupDiv = null;
    $(showGamesDiv).empty();
    $.get("rest/audience/groups", function (groupsData) {
        if ($.isArray(groupsData) && groupsData.length > 0) {

            // TODO this does not guarantee array order == display order, however!
            groupsData = sortGroupsByName(groupsData);
            $.each(groupsData, function (id, group) {

                (function (group, status, editable) {

                    $.get(urlPrefixes[status] + group.id, function (gamesData) {
                        // only show if games available
                        if ($.isArray(gamesData) && gamesData.length > 0) {
                            groupDiv = $("<div>").attr("class", "games-in-group").attr("id", group.name);
                            groupDiv.append($("<h3>").html(group.name));
                            showGamesDiv.append($(groupDiv));
                            showGamesData(gamesData, groupDiv, status, editable);
                        }
                    });
                }(group, status, editable));
            });
        }
    });
};
var showGamesData = function (gamesData, groupDiv, status, editable) {
    "use strict";

    var gameTable = null,
            row = null,
            updateButton = null,
            player1Out = null,
            player2Out = null;

    if ($.isArray(gamesData) && gamesData.length > 0) {

        $.each(gamesData, function (id, game) {

            if ($.isArray(game.sets) && game.sets.length > 0) {

                // overall game info (1 row)

                gameTable = $("<table>").attr("class", "table table-bordered table-condensed");
                row = $("<tr>").attr("id", "game-id-" + game.id);
                row.append($("<th>").html("Set #"));

                // mark winner
                player1Out = game.player1Name;
                player2Out = game.player2Name;
                if (game.winnerId === game.player1Id) {
                    player1Out = $("<em>").html(game.player1Name + " *");
                }
                else if (game.winnerId === game.player2Id) {
                    player2Out = $("<em>").html(game.player2Name + " * ");
                }

                row.append($("<th>")
                        .attr("class", "player1")
                        .attr("id", "player-id-" + game.player1Id)
                        .html(player1Out));
                row.append($("<th>")
                        .attr("class", "player2")
                        .attr("id", "player-id-" + game.player2Id)
                        .html(player2Out));
                gameTable.append(row);
                // more detailed game sets info (1-n row(s))

                game.sets.sort(function (a, b) {
                    return a.number - b.number;
                });

                $.each(game.sets, function (id, set) {

                    row = $("<tr>").attr("id", "gameset-id-" + set.id);
                    if (typeof editable === "boolean" && editable === true) {

                        row.append($("<td>").html(set.number));
                        row.append($("<td>").attr("class", "player1-score")
                                .append($("<input>")
                                        .attr("type", "number")
                                        .attr("class", "form-control")
                                        .attr("min", "0")
                                        .val(set.player1Score)));
                        row.append($("<td>").attr("class", "player2-score")
                                .append($("<input>")
                                        .attr("type", "number")
                                        .attr("class", "form-control")
                                        .attr("min", "0")
                                        .val(set.player2Score)));
                    } else {
                        if (status === "played" && !(set.player1Score === 0 &&
                                set.player2Score === 0)) {
                            row.append($("<td>")
                                    .html(set.number));
                            row.append($("<td>").attr("class", "player1-score")
                                    .html(set.player1Score));
                            row.append($("<td>").attr("class", "player2-score")
                                    .html(set.player2Score));
                        }
                    }
                    gameTable.append(row);
                });
                // append save/update button
                if (typeof editable === "boolean" && editable === true &&
                        (status === "played" || status === "unplayed")) {

                    updateButton = $("<input>").attr("type", "submit");
                    if (status === "played") {
                        updateButton
                                .attr("value", "Update game")
                                .attr("class", "btn btn-warning update-game")
                                .attr("onclick", "updateGame(this," + game.id + "," + game.player1Id + "," + game.player2Id + ", \"update\");");
                    } else {
                        updateButton
                                .attr("value", "Save game")
                                .attr("class", "btn btn-primary update-game")
                                .attr("onclick", "updateGame(this," + game.id + "," + game.player1Id + "," + game.player2Id + ", \"save\");");
                    }
                    gameTable.append($("<tr>").append(updateButton));
                }

                groupDiv.append(gameTable);
            }
        });
    }
};

var showPlayers = function (showPlayersDiv) {
    "use strict";
    var i = 0,
            table = null,
            row = null,
            groupPlayersDiv = null;

    $(showPlayersDiv).empty();

    $.get("rest/audience/groups", function (groupsData) {
        if ($.isArray(groupsData) && groupsData.length !== 0) {

            $.each(groupsData, function (id, group) {
                (function (group) {
                    $.get("rest/audience/players/group/id/" + group.id, function (playersData) {
                        if ($.isArray(playersData) && playersData.length > 0) {

                            groupPlayersDiv = $("<div>").attr("class", "group-players");
                            showPlayersDiv.append(groupPlayersDiv);
                            groupPlayersDiv
                                    .append($("<h3>")
                                            .html(group.name))
                                    .append("<br>");

                            table = $("<table>").attr("class", "table table-hover table-bordered table-condensed");
                            table.append($("<tr>")
                                    .append($("<th>").html("Name"))
                                    .append($("<th>").html("Rank"))
                                    .append($("<th>").html("Points"))
                                    .append($("<th>").html("Score ratio"))
                                    .append($("<th>")));

                            $.each(playersData, function (id, player) {
                                row = $("<tr>")
                                        .attr("id", "player-id-" + player.id);
                                row
                                        .append($("<td>")
                                                .attr("class", "player-name")
                                                .html(player.name))
                                        .append($("<td>")
                                                .attr("class", "player-rank")
                                                .html(player.rank))
                                        .append($("<td>")
                                                .attr("class", "player-points")
                                                .html(player.points))
                                        .append($("<td>")
                                                .attr("class", "player-score-ratio")
                                                .html(player.scoreRatio))
                                        .append($("<td>")
                                                .append($("<input>")
                                                        .attr("type", "submit")
                                                        .attr("class", "btn btn-danger")
                                                        .attr("value", "Delete player")
                                                        .attr("onclick", "deletePlayer(" + playersData[i].id + ");")));
                                table.append(row);
                            });

                            groupPlayersDiv.append(table);
                        }
                    });
                }(group));
            });
        }
    });
};
var rankSorter = function (firstPlayer, secondPlayer) {
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
var showRankings = function (rankingsDiv) {
    "use strict";
    var i = 0,
            table = null,
            row = null;

    $(rankingsDiv).empty();

    $.get("rest/audience/groups", function (groupsData) {
        if ($.isArray(groupsData) && groupsData.length > 0) {

            $.each(groupsData, function (id, group) {
                (function (group) {

                    // for each group, fetch its rankings data

                    $.get("rest/audience/rankings/group/id/" + group.id, function (rankingsData) {
                        if ($.isArray(rankingsData) && rankingsData.length > 0) {

                            rankingsDiv.append($("<h3>").html(group.name)).append("<br>");

                            table = $("<table>").attr("class", "table table-hover table-bordered table-condensed");
                            row = $("<tr>");
                            row.append($("<th>").html("Rank"))
                                    .append($("<th>").html("Name"))
                                    .append($("<th>").html("Won games"))
                                    .append($("<th>").html("Points"))
                                    .append($("<th>").html("Score ratio"));
                            table.append(row);

                            rankingsData.sort(rankSorter);
                            // rankings...
                            $.each(rankingsData, function (id, ranking) {
                                row = $("<tr>").attr("id", "player-id-" + ranking.id);
                                row.append($("<td>")
                                        .attr("class", "player-rank")
                                        .html(ranking.rank))
                                        .append($("<td>")
                                                .attr("class", "player-name")
                                                .html(ranking.name))
                                        .append($("<td>")
                                                .attr("class", "player-won-games")
                                                .html(ranking.wonGames))
                                        .append($("<td>")
                                                .attr("class", "player-points")
                                                .html(ranking.points))
                                        .append($("<td>")
                                                .attr("class", "player-score-ratio")
                                                .html(ranking.scoreRatio));
                                table.append(row);
                            });

                            rankingsDiv.append(table);
                        }
                    });
                }(group));
            });
        }
    });
};
var showRules = function (showRulesDiv) {
    "use strict";
    var table = null,
            startWithRank = 0,
            additionalPlayers = 0,
            lastRank = 0;
    if (showRulesDiv && showRulesDiv !== null) {
        $(showRulesDiv).empty();
        $.get("rest/audience/roundswitchrules", function (rulesData) {
            if ($.isArray(rulesData) && rulesData.length > 0) {

                table = $("<table>")
                        .attr("class", "table table-hover table-bordered table-condensed");
                table.append($("<tr>")
                        .append($("<th>").html("Source group"))
                        .append($("<th>").html("Destination group"))
                        .append($("<th>").html("Ranks"))
                        .append($("<th>").html("&Sigma;"))
                        .append("<th>"));
                $.each(rulesData, function (id, rule) {

                    startWithRank = parseInt(rule.startWithRank, 10);
                    additionalPlayers = parseInt(rule.additionalPlayers, 10);
                    lastRank = startWithRank + additionalPlayers;
                    table.append($("<tr>")
                            .append($("<td>").html(rule.srcGroupName))
                            .append($("<td>").html(rule.destGroupName))
                            .append($("<td>").html(startWithRank + "-" + lastRank))
                            .append($("<td>").html(additionalPlayers + 1))
                            .append($("<input>")
                                    .attr("type", "submit")
                                    .attr("class", "btn btn-danger")
                                    .attr("value", "Delete rule")
                                    .attr("onclick", "deleteRule(" + rule.id + ", this);")));
                });
                showRulesDiv.append(table);
            }
        });
    }
};
var getShuffledGames = function (groupId, format) {
    "use strict";
    var url = "rest/admin/shuffled-games/group/id/" + groupId;
    if (format && format !== null && typeof format === 'string') {
        url += ";format=" + format;
    }

    window.open(url);
};
var sortGroupsByName = function (groups) {
    "use strict;"

    if ($.isArray(groups)) {
        groups.sort(function (a, b) {
            if (a.name < b.name) {
                return -1;
            } else if (a.name > b.name) {
                return 1;
            } else {
                return 0;
            }
        });
    }

    // unaltered if no array
    return groups;
};
var sortGamesByTime = function (games) {
    "use strict;"

    if ($.isArray(games) && games.length > 0) {
        // TODO
    }

    // unaltered if no array
    return games;
};