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


//var getRankings = function (callback) {
//    "use strict";
//
//    var rankings = [];
//
//    $.get("rest/audience/groups", function (groupsData) {
//        if ($.isArray(groupsData) && groupsData.length > 0) {
//            $.each(groupsData, function (id, group) {
//                $.get("rest/audience/rankings/group/id/" + group.id, function (rankingsData) {
//                    rankings.push({
//                        "groupName": group.name,
//                        "roundId": group.roundId,
//                        "rankings": rankingsData
//                    });
//                    // hacky
//                    if (rankings.length === groupsData.length) {
//                        if (callback && typeof callback === 'function') {
//                            callback(rankings);
//                        }
//                    }
//                });
//            });
//        }
//    });
//};
//
//var getGames = function (status, editable, callback) {
//    "use strict";
//
//    var games = [],
//            urlPrefixes = {
//                "played": "rest/audience/games/played/group/id/",
//                "unplayed": "rest/audience/games/unplayed/group/id/"
//            };
//
//    $.get("rest/audience/groups", function (groupsData) {
//        if ($.isArray(groupsData) && groupsData.length > 0) {
//            $.each(groupsData, function (id, group) {
//                $.get(urlPrefixes[status] + group.id, function (gamesData) {
//                    games.push({
//                        "groupName": group.name,
//                        "games": gamesData
//                    });
//                    // hacky
//                    if (games.length === groupsData.length) {
//                        if (callback && typeof callback === 'function') {
//                            callback(games);
//                        }
//                    }
//                });
//            });
//        }
//    });
//};

//// fetch rankings data, sort it, and display
//var showRankings = function (rankingsDiv) {
//    "use strict";
//
//    getRankings(function (rankings) {
//        rankings = sortArrayByKeyNames(rankings, ["roundId", "groupName"]);
//        rankingsDiv.empty();
//        $.each(rankings, function (id, ranking) {
//            rankingsDiv
//                    .append($("<h3>").html(ranking.groupName))
//                    .append(buildRankingsTable(ranking.rankings));
//        });
//    });
//
//};
//
//
//var showGames = function (gamesDiv, status, editable, button) {
//    "use strict";
//
//    if (button) {
//        $(button).attr("disabled", "disabled");
//    }
//    gamesDiv.removeAttr("hidden");
//
//    getGames(status, editable, function (games) {
//        games = sortArrayByKeyNames(games, ["groupName"]);
//        gamesDiv.empty();
//        $.each(games, function (id, game) {
//            gamesDiv.append($("<h3>").html(game.groupName));
//            game.games = sortGamesByTime(game.games);
//            $.each(buildGamesTables(game.games, status, editable), function (id, table) {
//                gamesDiv.append(table);
//            });
//        });
//
//        if (button) {
//            $(button).removeAttr("disabled");
//        }
//    });
//};
//
//var showGroups = function (showGroupsDiv, playerGroupSelect, ruleSrcGroupSelect, ruleDestGroupSelect, button) {
//    "use strict";
//    var table = null,
//            row = null,
//            playerGroupSelectOK = false,
//            ruleGroupSelectsOK = false;
//
//    if (button) {
//        $(button).attr("disabled", "disabled");
//    }
//
//    $.get("rest/audience/groups", function (data) {
//
//        if (playerGroupSelect && playerGroupSelect !== null) {
//            playerGroupSelectOK = true;
//            $(playerGroupSelect).find("option").remove();
//            $(playerGroupSelect)
//                    .append($("<option>")
//                            .attr("id", "no-group-selected")
//                            .text("No group selected"));
//        }
//
//        if (ruleSrcGroupSelect && ruleSrcGroupSelect !== null &&
//                ruleDestGroupSelect && ruleDestGroupSelect !== null) {
//            ruleGroupSelectsOK = true;
//            $(ruleSrcGroupSelect).find("option").remove();
//            $(ruleDestGroupSelect).find("option").remove();
//            $(ruleSrcGroupSelect)
//                    .append($("<option>")
//                            .attr("id", "no-source-group-selected")
//                            .text("No source group selected"));
//            $(ruleDestGroupSelect)
//                    .append($("<option>")
//                            .attr("id", "no-destination-group-selected")
//                            .text("No destination group selected"));
//        }
//
//        $(showGroupsDiv).empty();
//        table = $("<table>")
//                .attr("class", "table table-hover table-bordered table-condensed");
//        table.append($("<tr>")
//                .append($("<th>").html("Name"))
//                .append($("<th>").html("Round"))
//                .append($("<th>").html("Min sets"))
//                .append($("<th>").html("Max sets"))
//                .append($("<th>").html("Active"))
//                .append($("<th>").html("Completed"))
//                .append($("<th>"))
//                .append($("<th>"))
//                .append($("<th>"))
//                .append($("<th>")));
//        if ($.isArray(data) && data.length > 0) {
//
//            $.each(data, function (id, group) {
//
//                row = $("<tr>").attr("id", "group-id-" + group.id);
//                row.append($("<td>").attr("class", "group-name").html(group.name))
//                        .append($("<td>").attr("class", "group-round-id").html(group.roundId))
//                        .append($("<td>").attr("class", "group-minsets").html(group.minSets))
//                        .append($("<td>").attr("class", "group-maxsets").html(group.maxSets))
//                        .append($("<td>").attr("class", "group-active").html(group.active.toString()))
//                        .append($("<td>").attr("class", "group-completed").html(group.completed.toString()));
//                row.append($("<td>")
//                        .append($("<input>")
//                                .attr("type", "submit")
//                                .attr("class", "btn btn-success")
//                                .attr("value", "Generate games")
//                                .attr("onclick", "generateGames(this, " + group.id + ", false);")));
//                row.append($("<td>")
//                        .append($("<input>")
//                                .attr("type", "submit")
//                                .attr("class", "btn btn-default")
//                                .attr("value", "Games list")
//                                .attr("onclick", "getShuffledGames(" + group.id + ");")));
//                row.append($("<td>")
//                        .append($("<input>")
//                                .attr("type", "submit")
//                                .attr("class", "btn btn-default")
//                                .attr("value", "Scoresheets (XeTeX)")
//                                .attr("onclick", "getShuffledGames(" + group.id + ", 'latex');")));
//                row.append($("<td>")
//                        .append($("<input>")
//                                .attr("type", "submit")
//                                .attr("class", "btn btn-danger")
//                                .attr("value", "Delete group")
//                                .attr("onclick", "deleteGroup(" + group.id + ", this);")));
//                table.append(row);
//                if (playerGroupSelectOK) {
//                    $(playerGroupSelect).append($("<option>")
//                            .attr("id", "group-id-" + group.id)
//                            .text(group.name));
//                }
//
//                if (ruleGroupSelectsOK) {
//                    $(ruleSrcGroupSelect)
//                            .append($("<option>")
//                                    .attr("id", "rule-source-group-id-" + group.id)
//                                    .text(group.name));
//                    $(ruleDestGroupSelect)
//                            .append($("<option>")
//                                    .attr("id", "rule-destination-group-id-" + group.id)
//                                    .text(group.name));
//                }
//            });
//            showGroupsDiv.append(table);
//        }
//
//        if (button) {
//            $(button).removeAttr("disabled");
//        }
//    });
//};

//var showPlayers = function (showPlayersDiv, button) {
//    "use strict";
//    var i = 0,
//            table = null,
//            row = null,
//            groupPlayersDiv = null;
//
//    if (button) {
//        $(button).attr("disabled", "disabled");
//    }
//
//    $(showPlayersDiv).empty();
//
//    $.get("rest/audience/groups", function (groupsData) {
//        if ($.isArray(groupsData) && groupsData.length !== 0) {
//
//            $.each(groupsData, function (id, group) {
//                (function (group) {
//                    $.get("rest/audience/players/group/id/" + group.id, function (playersData) {
//                        if ($.isArray(playersData) && playersData.length > 0) {
//
//                            groupPlayersDiv = $("<div>").attr("class", "group-players");
//                            showPlayersDiv.append(groupPlayersDiv);
//                            groupPlayersDiv
//                                    .append($("<h3>")
//                                            .html(group.name))
//                                    .append("<br>");
//
//                            table = $("<table>").attr("class", "table table-hover table-bordered table-condensed");
//                            table.append($("<tr>")
//                                    .append($("<th>").html("Name"))
//                                    .append($("<th>").html("Rank"))
//                                    .append($("<th>").html("Points"))
//                                    .append($("<th>").html("Score ratio"))
//                                    .append($("<th>")));
//
//                            $.each(playersData, function (id, player) {
//                                row = $("<tr>")
//                                        .attr("id", "player-id-" + player.id);
//                                row
//                                        .append($("<td>")
//                                                .attr("class", "player-name")
//                                                .html(player.name))
//                                        .append($("<td>")
//                                                .attr("class", "player-rank")
//                                                .html(player.rank))
//                                        .append($("<td>")
//                                                .attr("class", "player-points")
//                                                .html(player.points))
//                                        .append($("<td>")
//                                                .attr("class", "player-score-ratio")
//                                                .html(player.scoreRatio))
//                                        .append($("<td>")
//                                                .append($("<input>")
//                                                        .attr("type", "submit")
//                                                        .attr("class", "btn btn-danger")
//                                                        .attr("value", "Delete player")
//                                                        .attr("onclick", "deletePlayer(" + playersData[i].id + ");")));
//                                table.append(row);
//                            });
//
//                            groupPlayersDiv.append(table);
//                        }
//                    });
//                }(group));
//            });
//        }
//
//        if (button) {
//            $(button).removeAttr("disabled");
//        }
//    });
//};
//
//
//
//
//
//var showRules = function (showRulesDiv, button) {
//    "use strict";
//    var table = null,
//            startWithRank = 0,
//            additionalPlayers = 0,
//            lastRank = 0;
//
//    if (button) {
//        $(button).attr("disabled", "disabled");
//    }
//
//    if (showRulesDiv && showRulesDiv !== null) {
//        $(showRulesDiv).empty();
//        $.get("rest/audience/roundswitchrules", function (rulesData) {
//            if ($.isArray(rulesData) && rulesData.length > 0) {
//
//                table = $("<table>")
//                        .attr("class", "table table-hover table-bordered table-condensed");
//                table.append($("<tr>")
//                        .append($("<th>").html("Source group"))
//                        .append($("<th>").html("Destination group"))
//                        .append($("<th>").html("Ranks"))
//                        .append($("<th>").html("&Sigma;"))
//                        .append("<th>"));
//                $.each(rulesData, function (id, rule) {
//
//                    startWithRank = parseInt(rule.startWithRank, 10);
//                    additionalPlayers = parseInt(rule.additionalPlayers, 10);
//                    lastRank = startWithRank + additionalPlayers;
//                    table.append($("<tr>")
//                            .append($("<td>").html(rule.srcGroupName))
//                            .append($("<td>").html(rule.destGroupName))
//                            .append($("<td>").html(startWithRank + "-" + lastRank))
//                            .append($("<td>").html(additionalPlayers + 1))
//                            .append($("<td>").append($("<input>")
//                                    .attr("type", "submit")
//                                    .attr("class", "btn btn-danger")
//                                    .attr("value", "Delete rule")
//                                    .attr("onclick", "deleteRule(" + rule.id + ", this);"))));
//                });
//                showRulesDiv.append(table);
//            }
//
//            if (button) {
//                $(button).removeAttr("disabled");
//            }
//        });
//    }
//};

var getShuffledGames = function (groupId, format, prefix) {
    "use strict";

    var url = "rest/admin/shuffled-games/group/id/" + groupId;

    if (prefix && typeof prefix === 'string') {
        url = prefix + '/' + url;
    }

    if (format && format !== null && typeof format === 'string') {
        url += ";format=" + format;
    }
    window.open(url);
};

var sortArrayByKeyNames = function (array, names) {
    "use strict";

    var i,
            comp;

    if ($.isArray(array) && $.isArray(names)) {
        array.sort(function (a, b) {
            for (i = 0; i < names.length; i++) {
                comp = 0;
                if (a[names[i]] < b[names[i]]) {
                    comp = -1;
                } else if (a[names[i]] > b[names[i]]) {
                    comp = 1;
                }

                if (comp !== 0) {
                    return comp;
                }
            }
        });
    }

    return array;
};


// TODO sort on server
var sortGamesByTime = function (games) {
    "use strict";
    if ($.isArray(games) && games.length > 0) {
        games.sort(function (a, b) {
            if (a.hasOwnProperty("playedAt") && typeof a.playedAt !== 'number') {
                a.playedAt = 0;
            }
            if (b.hasOwnProperty("playedAt") && typeof b.playedAt !== 'number') {
                b.playedAt = 0;
            }

            return a.playedAt - b.playedAt;
        });
    }

    // unaltered if no array
    return games;
};
