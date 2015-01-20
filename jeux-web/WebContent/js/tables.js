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