
Pages.group("reports").page("testrundetail")
.addRequiredTemplate("report-view-testrundetail-testruntable")
.addRequiredData("results", function() { 
	// we must examine url options before determining which data to load
	if(this.pageParameters.query.only) {
		return SlickUrlBuilder.result.getResultsByTestrun(this.pageParameters.positional[0], {"status": this.pageParameters.query.only});
	} else if(this.pageParameters.query.includepass) {
		return SlickUrlBuilder.result.getResultsByTestrun(this.pageParameters.positional[0]);
	} else {
		return SlickUrlBuilder.result.getResultsByTestrun(this.pageParameters.positional[0], {"excludestatus": "PASS"});
	}
})
.addRequiredData("testrun", function() {
	return SlickUrlBuilder.testrun.getTestrun(this.pageParameters.positional[0]);
})
.addDataRecievedListener(function(key, value) {
	if(key == "testrun" && value.testplanId) {
		this.addRequiredData("testplan", SlickUrlBuilder.testplan.getTestplan(value.testplanId));
	}
})
.addDisplayMethod(function() {
	setSlickTitle("Report - Detailed Testrun Results");

	if(this.data.testplan) {
		$("#subtitle1").text(this.data.testplan.name);
	} else {
		$("#subtitle1").text(this.data.testrun.name);
	}
	var buildname = "";
	if(this.data.testrun.release) {
		buildname = this.data.testrun.release.name + " ";
	}
	if(this.data.testrun.build) {
		buildname = buildname + "Build " + this.data.testrun.build.name;
	}
	$("#subtitle2").text(buildname);


	var detailtable = this.templates["report-view-testrundetail-testruntable"];
	var results = this.data["results"];
	var tbldata = [];

	$.tmpl(detailtable, {"testrunid": this.pageParameters.positional[0], "includepass": this.pageParameters.query.includepass}).appendTo("#main");
	for(i in results) {
		result = results[i];
		tbldata[tbldata.length] = ["<span id=\"" + result.id + "\" class=\"clickable-individual-result\">" + safeReference(result, "testcase.name", safeReference(result, "testcase.automationId", "Unknown Test Name")) + "</span>", 
								   safeReference(result, "component.name", ""),
								   new Date(result.recorded),
		                           safeReference(result, "testcase.automationId", ""),
								   safeReference(result, "reason", ""),
								   safeReference(result, "hostname", ""),
								   "<img src=\"images/reschedule.png\" id=\"reschedule-" + result.id + "\" class=\"reschedule-result\" alt=\"Reschedule Result\" title=\"Reschedule Result\" />",
								   "<span class=\"result-status-" + result["status"].replace("_","") + "\">" + result["status"].replace("_", " ") + 
								   "<img class=\"result-status-image\" src=\"images/status-" + result["status"] + ".png\" /></span>"];
	}
	var datatable = $("#trdetailtable").dataTable({aaData: tbldata,
	                                               aoColumns: [
	                                                   {"sTitle": "Test Name", "sWidth": "55%", "sType": "html"},
													   {"sTitle": "Component", "sWidth": "10%"},
	                                                   {"sTitle": "Time Reported", "sWidth": "20%"},
	                                                   {"sTitle": "Automation ID", "bVisible": false},
	                                                   {"sTitle": "Reason", "bVisible": false},
	                                                   {"sTitle": "Hostname", "bVisible": false},
													   {"sTitle": "Actions", "sWidth": "5%", "sType": "html", "sClass": "right-justify"},
	                                                   {"sTitle": "Result Status", "sWidth": "10%", "sType": "html", "sClass": "right-justify"}],
	                                               bJQueryUI: true,
	                                               bAutoWidth: false,
	                                               bDeferRender: true,
	                                               bPaginate: false,
	                                               sDom: '<"H"lfrT<"clear">>tS<"F"ip>',
	                                               sScrollY: "" + ($(document).height() - (5 * $("#pagetitle").height()) -  (4 * $("#titlebar").height())) + "px",
	                                               oTableTools: {"sSwfPath": "media/swf/copy_cvs_xls_pdf.swf"}
	});
	datatable.fnSort([[2, "desc"]]);

});

$(function() {
	$(".reschedule-result").live("click", function() {
		var resultid = $(this).attr("id");
		resultid = resultid.replace("reschedule-","");
		$.ajax({url: "api/results/" + resultid + "/reschedule",
		        type: "POST",
		        dataType: "json",
				success: function(data) {
					// TODO: figure out how to change the row
					$.jGrowl("Successfully rescheduled result.");
					$.address.update();
				}
		});
	});
});
