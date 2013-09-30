function create_new_group(groupSubmit) {
	"use strict";

	var minSets = 0, maxSets = 0, name = "", round = 0, sendData = {}, groupForm = null;

	groupForm = $(groupSubmit).parent();

	minSets = $(groupForm).find("#min-sets").val();
	maxSets = $(groupForm).find("#max-sets").val();
	round = $(groupForm).find("#round").val();
	name = $(groupForm).find("#name").val();

	sendData = {
		"minSets" : minSets,
		"maxSets" : maxSets,
		"name" : name,
		"round" : round
	};

	// assumption: all input values present
	// see check_submit_ready()
	$.ajax({
		url : "create-group",
		type : "PUT",
		data : JSON.stringify(sendData),
		contentType : "application/json",
		success : function() {
			$("#creation-result").html("Created.");
			// disable submit button, clear values and success message
			$(groupForm).find("#submit-create-group").attr("disabled",
					"disabled");
			$(groupForm).find("#min-sets").val("");
			$(groupForm).find("#max-sets").val("");
			$(groupForm).find("#name").val("");
			$(groupForm).find("#round").val("");

			window.setTimeout(function() {
				$("#creation-result").html("");
			}, 5000);
		},
		error : function () {
			$("#creation-result").html("Failed to create group");
		}
	});
}