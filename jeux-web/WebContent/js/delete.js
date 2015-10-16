var deleteGroup = function (groupId, prefix) {
    "use strict";

    var url = "rest/admin/group/id/" + groupId;

    if (prefix && typeof prefix === 'string') {
        url = prefix + '/' + url;
    }

//    if (!window.confirm("Really delete group?")) {
//        return false;
//    }

    $.ajax({
        url: url,
        type: "DELETE",
        success: function () {
            document.location.reload();
        },
        // errors
        statusCode: {
            409: function () {
                alert("Conflict trying to delete group (violated constraint).");
            },
            403: function () {
                alert("Operation not permitted (unauthenticated request).");
            },
            500: function () {
                alert("Server error while trying to delete group.");
            }
        }
    });
};
var deletePlayer = function (playerId, prefix) {
    "use strict";

    var url = "rest/admin/player/id/" + playerId;

    if (prefix && typeof prefix === 'string') {
        url = prefix + '/' + url;
    }

    $.ajax({
        url: url,
        type: "DELETE",
        success: function () {
            document.location.reload();
        },
        statusCode: {
            403: function () {
                alert("Operation not permitted (unauthenticated request).");
            },
            500: function () {
                alert("Server error while trying to delete player.");
            }
        }
    });
};
var deleteRule = function (ruleId, prefix) {
    "use strict";

    var url = "rest/admin/roundswitchrule/id/" + ruleId;

    if (prefix && typeof prefix === 'string') {
        url = prefix + '/' + url;
    }

    $.ajax({
        url: url,
        type: "DELETE",
        success: function () {
            document.location.reload();
        },
        statusCode: {
            403: function () {
                alert("Operation not permitted (unauthenticated request).");
            },
            500: function () {
                alert("Server error while trying to delete rule.");
            }
        }
    });
};