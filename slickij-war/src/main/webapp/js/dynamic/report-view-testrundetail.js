
Routes["/reports/testrundetail/:testrunid"] = "ReportsView#testrundetail";
ReportsView.views.testrundetail = function(options) {
		grabTemplate("report-view-testrundetail-testruntable", function(detailtable) {
			setSlickTitle("Report - Detailed Testrun Results");
			$.tmpl(detailtable, options).appendTo("#main");
			$("#trdetailtable").addClass("slick-event-result-load").bind("slick-data-result-load", function(evt, results) {
				var tbldata = [];
				for(i in results) {
					result = results[i];
					tbldata[tbldata.length] = ["<span id=\"" + result.id + "\" class=\"clickable-individual-result\">" + safeReference(result, "testcase.name", safeReference(result, "testcase.automationId", "Unknown Test Name")) + "</span>", 
											   safeReference(result, "component.name", ""),
											   new Date(result.recorded),
					                           safeReference(result, "testcase.automationId", ""),
											   safeReference(result, "reason", ""),
											   safeReference(result, "hostname", ""),
											   "<span class=\"result-status-" + result["status"].replace("_","") + "\">" + result["status"].replace("_", " ") + 
											   "<img class=\"result-status-image\" src=\"images/status-" + result["status"] + ".png\" /></span>"];
				}
				$("#data-loading").hide();
				var datatable = $("#trdetailtable").dataTable({aaData: tbldata,
				                                               aoColumns: [
				                                                   {"sTitle": "Test Name", "sWidth": "55%", "sType": "html"},
																   {"sTitle": "Component", "sWidth": "10%"},
				                                                   {"sTitle": "Time Reported", "sWidth": "25%"},
				                                                   {"sTitle": "Automation ID", "bVisible": false},
				                                                   {"sTitle": "Reason", "bVisible": false},
				                                                   {"sTitle": "Hostname", "bVisible": false},
				                                                   {"sTitle": "Result Status", "sWidth": "10%", "sType": "html", "sClass": "right-justify"}],
				                                               bJQueryUI: true,
				                                               bAutoWidth: false,
				                                               bDeferRender: true,
				                                               bPaginate: false,
				                                               sDom: '<"H"lfrT<"clear">>tS<"F"ip>',
				                                               sScrollY: "" + ($(document).height() - (5 * $("#pagetitle").height()) -  (4 * $("#titlebar").height())) + "px",
				                                               oTableTools: { "sSwfPath": "media/swf/copy_cvs_xls_pdf.swf" }
				});
				datatable.fnSort([[2, "desc"]]);
			});

			$("#subtitle1").addClass("slick-event-testrun-load").bind("slick-data-testrun-load", function(evt, testrun) {
				if(testrun.testplanId) {
					$(this).addClass("slick-event-testplan-load").bind("slick-data-testplan-load", function(evt, testplan) {
						$(this).text("Testrun for Plan: " + testplan.name);
					});
					Slick.testplan.getTestplan(testrun.testplanId);
				} else {
					$(this).text(testrun.name);
				}
				var buildname = "";
				if(testrun.release) {
					buildname = testrun.release.name + " ";
				}
				if(testrun.build) {
					buildname = buildname + "Build " + testrun.build.name;
				}
				$("#subtitle2").text(buildname);
			});
			Slick.testrun.getTestrun(options.testrunid);
			if(options.only) {
				Slick.result.getResultsByTestrun(options.testrunid, {"status": options.only});
			} else if(options.includepass) {
				Slick.result.getResultsByTestrun(options.testrunid);
			} else { 
				Slick.result.getResultsByTestrun(options.testrunid, {"excludestatus": "PASS"});
			}
		});
}


