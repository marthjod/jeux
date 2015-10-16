var getApiStatus = function (statusDiv, prefix) {
    "use strict";

    var url = "rest/audience/status";

    if (prefix && typeof prefix === 'string') {
        url = prefix + '/' + url;
    }
    $.ajax(url).done(function (ret) {
        $(statusDiv).html("API status: " + ret);
    });
};

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

