function deleteGroup(deleteSubmit) {
    "use strict";

    var groupId = -1, sendOK = false;

    try {
        groupId = parseInt(($(deleteSubmit).parent().parent().attr("id")
                .replace("group-id-", "")), 10);
        sendOK = true;
    } catch (e) {
        alert(e);
    }

    if (sendOK) {

        if (window
                .confirm("Warning: Removing this group\nwill also delete all players\nand games belonging to this group!")) {
            $
                    .ajax({
                        url : "rest/v1/delete-group/" + groupId,
                        type : "DELETE",
                        success : function() {
                            // alert("Group deleted");

                            // remove as selectable option
                            $("#player-select-group").find(
                                    "#group-id-" + groupId).remove();
                            $("#rule-source-group").find(
                                    "#rule-source-group-id-" + groupId)
                                    .remove();
                            $("#rule-destination-group").find(
                                    "#rule-destination-group-id-" + groupId)
                                    .remove();

                            showAllGroups($("#show-all-groups"));
                        },
                        statusCode : {
                            409 : function() {
                                alert("Conflict trying to delete group (violated constraint).");
                            },
                            500 : function() {
                                alert("Server error while trying to delete group.");
                            }
                        }
                    });
        }
    }
}