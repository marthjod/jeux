function getRESTApiStatus(statusDiv) {
    "use strict";

    $.get("rest/audience/status", function(data) {
        $(statusDiv).html("REST API status " + data);
    });
}

function showAllGroups(showAllGroupsDiv, playerGroupSelect, ruleSrcGroupSelect,
        ruleDestGroupSelect) {
    "use strict";

    var i = 0, table = null, row = null, deletionCell = null, playerGroupSelectOK = false, ruleGroupSelectsOK = false;

    $
            .get("rest/audience/groups",
                    function(data) {

                        if (playerGroupSelect !== undefined
                                && playerGroupSelect !== null) {
                            playerGroupSelectOK = true;
                            $(playerGroupSelect).find("option").remove();
                            $("<option>").attr("id", "no-group-selected").text(
                                    "No group selected").appendTo(
                                    $(playerGroupSelect));
                        }

                        if (ruleSrcGroupSelect !== undefined
                                && ruleSrcGroupSelect !== null
                                && ruleDestGroupSelect !== undefined
                                && ruleDestGroupSelect !== null) {
                            ruleGroupSelectsOK = true;
                            $(ruleSrcGroupSelect).find("option").remove();
                            $(ruleDestGroupSelect).find("option").remove();
                            $("<option>")
                                    .attr("id", "no-source-group-selected")
                                    .text("No source group selected").appendTo(
                                            $(ruleSrcGroupSelect));
                            $("<option>").attr("id",
                                    "no-destination-group-selected").text(
                                    "No destination group selected").appendTo(
                                    $(ruleDestGroupSelect));
                        }

                        // showAllGroupsDiv
                        $(showAllGroupsDiv).empty();
                        $("<span>").attr("class", "title").html(
                                "Existing groups").appendTo(showAllGroupsDiv);
                        $("<br>").appendTo(showAllGroupsDiv);

                        if (typeof data !== undefined && data !== null
                                && typeof data === 'object'
                                && data.hasOwnProperty("length")
                                && data.length !== 0) {

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
                                row = $("<tr>").attr("id",
                                        "group-id-" + data[i].id);
                                $("<td>").attr("class", "group-name").html(
                                        data[i].name).appendTo(row);
                                $("<td>").attr("class", "group-id").html(
                                        data[i].id).appendTo(row);
                                $("<td>").attr("class", "group-round-id").html(
                                        data[i].roundId).appendTo(row);
                                $("<td>").attr("class", "group-minsets").html(
                                        data[i].minSets).appendTo(row);
                                $("<td>").attr("class", "group-maxsets").html(
                                        data[i].maxSets).appendTo(row);
                                $("<td>").attr("class", "group-active").html(
                                        data[i].active.toString())
                                        .appendTo(row);
                                $("<td>").attr("class", "group-completed")
                                        .html(data[i].completed.toString())
                                        .appendTo(row);
                                deletionCell = $("<td>");
                                $("<input>").attr("type", "submit").attr(
                                        "value", "Delete group").attr("class",
                                        "delete-button").appendTo(deletionCell)
                                        .attr("onclick", "deleteGroup(this);");
                                deletionCell.appendTo(row);
                                row.appendTo(table);

                                // populate playerGroupSelect as well
                                // so that <option
                                // id="GROUP_ID">GROUP_NAME</option>
                                // are appended

                                if (playerGroupSelectOK) {
                                    $("<option>").attr("id",
                                            "group-id-" + data[i].id).text(
                                            data[i].name).appendTo(
                                            $(playerGroupSelect));
                                }

                                if (ruleGroupSelectsOK) {
                                    $("<option>").attr(
                                            "id",
                                            "rule-source-group-id-"
                                                    + data[i].id).text(
                                            data[i].name).appendTo(
                                            $(ruleSrcGroupSelect));
                                    $("<option>").attr(
                                            "id",
                                            "rule-destination-group-id-"
                                                    + data[i].id).text(
                                            data[i].name).appendTo(
                                            $(ruleDestGroupSelect));
                                }
                            }

                            table.appendTo(showAllGroupsDiv);

                        } else {
                            $(showAllGroupsDiv).html(
                                    "No or no valid group data available.");
                        }

                    });
}