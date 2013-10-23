function createNewGroup(groupSubmit) {
    "use strict";

    var minSets = 0, maxSets = 0, name = "", round = 0, active = false, sendGroup = {}, groupForm = null;

    groupForm = $(groupSubmit).parent();

    minSets = $(groupForm).find("#min-sets").val();
    maxSets = $(groupForm).find("#max-sets").val();
    round = $(groupForm).find("#round").val();
    name = $(groupForm).find("#name").val();
    active = $(groupForm).find("#group-active")[0].checked;

    sendGroup = {
        "minSets" : minSets,
        "maxSets" : maxSets,
        "name" : name,
        "round" : round,
        // newly-created cannot be completed
        "completed" : false,
        "active" : active
    };

    // assumption: all input values present
    // see checkSubmitReady()
    $.ajax({
        url : "create-group",
        type : "PUT",
        data : JSON.stringify(sendGroup),
        contentType : "application/json",
        success : function() {

            $(groupSubmit).attr("value", "Created.");
            // disable submit button, clear values and success message
            $(groupSubmit).attr("disabled", "disabled");
            clearForm(groupForm);
            window.setTimeout(function() {
                // reset text
                $(groupSubmit).attr("value", "Create new group");
            }, 3000);

            // refresh
            showAllGroups($("#show-all-groups"));

        },
        error : function() {
            $(groupSubmit).attr("value", "Failed to create group!");
        }
    });
}

// function createNewPlayersForGroup(groupSubmit) {
// "use strict";
//
// var groupForm = null, i = 0, createdPlayers = [], sendPlayers = [], groupName
// = "";
//
// groupForm = $(groupSubmit).parent();
// groupName = $(groupForm).find("#name").val();
//
// createdPlayers = groupForm.find($(".create-player"));
// if (createdPlayers.length > 0) {
// for (i = 0; i < createdPlayers.length; i += 1) {
// // non-empty player name?
// if ($(createdPlayers[i]).val() !== "") {
// // add player object with name attribute
// // to group data
// sendPlayers.push({
// "name" : $(createdPlayers[i]).val(),
// "groupName" : groupName
// });
// }
//
// // remove parsed input element
// $(createdPlayers[i]).remove();
// }
//
// $.ajax({
// url : "create-players",
// type : "PUT",
// data : JSON.stringify(sendPlayers),
// contentType : "application/json",
// success : function() {
//
// },
// error : function() {
// $("#creation-result").html("Failed to create players");
// }
// });
// }
// }
