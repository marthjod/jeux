var buildGamesTables = function (gamesData, status, editable) {
    "use strict";

    var tables = [],
            table = null,
            row = null,
            updateButton = null,
            player1Out = null,
            player2Out = null;

    if ($.isArray(gamesData) && gamesData.length > 0) {
        $.each(gamesData, function (id, game) {
            if ($.isArray(game.sets) && game.sets.length > 0) {
                table = $("<table>")
                        .attr("class", "table table-bordered table-condensed");
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
                table.append(row);
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
                    table.append(row);
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
                    table.append($("<tr>").append(updateButton));
                }
                tables.push(table);
            } else {
                tables.push($("<h4>").html("No games available"));
            }
        });

    } else {
        tables.push($("<h4>").html("No games available"));
    }

    return tables;
};

var buildRankingsTable = function (rankingsData) {
    "use strict";

    var table = null,
            row = null;

    if ($.isArray(rankingsData) && rankingsData.length > 0) {

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

        return table;
    } else {
        return $("<h4>").html("No rankings available");
    }
};