function prefill_max_sets(minSetsInput) {
	"use strict";

	var minSets = $(minSetsInput).val();

	if (minSets === "1") {
		$("#max-sets").val("1");
	} else if (minSets === "2") {
		$("#max-sets").val("3");
	}
}

function check_submit_ready(inputElement) {
	"use strict";

	var groupForm = null, minSets = "", maxSets = "", name = "";

	groupForm = $(inputElement).parent();

	minSets = $(groupForm).find("#min-sets").val();
	maxSets = $(groupForm).find("#max-sets").val();
	name = $(groupForm).find("#name").val();

	if (minSets !== "" && maxSets !== "" && name !== "") {
		$("#submit-create-group").removeAttr("disabled");
	}

}