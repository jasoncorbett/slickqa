/*
 * ---------------------------------------------------------------------------
 * report-view-bytestrun.js
 * ---------------------------------------------------------------------------
 */
addNavigationMenu({"group": "Reports", "action": "Test Run Results", "url": "#/reports/bytestrun"});
Routes["/reports/bytestrun"] = "ReportsView#bytestrun";

ReportsView.views.bytestrun = function() {
		grabTemplate("report-view-bytestrun-testruntable", function(trtable) {
			setSlickTitle("Report - Testrun Results");
			$.tmpl(trtable, Slick.project.getCurrentProject()).appendTo("#main");
			$("#trtable").addClass("slick-event-testrun-load").bind("slick-data-testrun-load", function(evt, testruns) {
				var tbldata = [];
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
			Slick.testrun.getTestrunsForProject(Slick.project.getCurrentProject().id);
		});
}

