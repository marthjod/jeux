function getRESTApiStatus(statusDiv) {
	"use strict";

	$.get("rest/v1/status", function(data) {
		$(statusDiv).html("REST API status " + data);
	});
}