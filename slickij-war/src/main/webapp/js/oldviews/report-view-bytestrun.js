/*
 * ---------------------------------------------------------------------------
 * report-view-bytestrun.js
 * ---------------------------------------------------------------------------
 */
Pages.group("reports").page("bytestrun").addRequiredTemplate("report-view-bytestrun-testruntable").addRequiredData("testruns", function() {
	return "api/testruns?projectid=" + getCurrentProject().id;
}).addDisplayMethod(function() {
			trtable = this.templates["report-view-bytestrun-testruntable"];
			setSlickTitle("Report - Testrun Results");
			$.tmpl(trtable, getCurrentProject()).appendTo("#main");
			var tbldata = [];
			var testruns = this.data["testruns"];
			for(i in testruns) {
				testrun = testruns[i];
				tbldata[tbldata.length] = ["<a href=\"#/reports/testrunsummary/" + testrun.id + "\">" + testrun.name + "</a>", 
				                           safeReference(testrun, "config.name", "Unknown Environment"), 
										   safeReference(testrun, "release.name", "Unknown Release") + " Build " + safeReference(testrun, "build.name", "Unknown"), 
										   new Date(testrun.dateCreated)];
			}
			$("#data-loading").hide();
			var datatable = $("#trtable").dataTable({aaData: tbldata,
			                                         aoColumns: [
													   {"sTitle": "Name", "sWidth": "40%", "sType": "html"},
													   {"sTitle": "Environment", "sWidth": "20%"},
													   {"sTitle": "Build", "sWidth": "20%"},
													   {"sTitle": "Date Created", "sWidth": "20%"}],
													 bJQueryUI: true,
									                 bAutoWidth: false,
													 bDeferRender: true,
													 bPaginate: false,
													 sDom: "<\"H\"lfr>tS<\"F\"ip>",
													 sScrollY: "" + ($(document).height() - (4 * $("#pagetitle").height()) -  (3 * $("#titlebar").height())) + "px"
			});
			datatable.fnSort([[3, "desc"]]);
});

