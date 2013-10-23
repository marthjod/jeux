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
        $.ajax({
            url : "rest/v1/delete-group/" + groupId,
            type : "DELETE",
            success : function() {
                alert("Group deleted");
                showAllGroups($("#show-all-groups"));
            },
            error : function() {
                alert("Error trying to delete group");
            }
        });
    }
}