/*
 * ---------------------------------------------------------------------------
 * runtest-view-machinestatus.js
 * ---------------------------------------------------------------------------
 */

Pages.group("runtests").page("machinestatus").addRequiredTemplate("runtest-view-machinestatus-machinestatustable").addRequiredData("hoststatuses", function() {
	return "api/hoststatus";
}).addDisplayMethod(function() {
			hoststatustable = this.templates["runtest-view-machinestatus-machinestatustable"];
			setSlickTitle("Automation Machine Pool Status");
			$.tmpl(hoststatustable, getCurrentProject()).appendTo("#main");
			var tbldata = [];
			var hoststatuses = this.data["hoststatuses"];
			for(i in hoststatuses) {
				hoststatus = hoststatuses[i];
				tbldata[tbldata.length] = [hoststatus.hostname, 
                                                           new Date(hoststatus.lastCheckin),
                                                           safeReference(hoststatus, "currentWork.testcase.name", ""),
                                                           safeReference(hoststatus, "currentWork.testrun.name", ""),
                                                           getDuration(new Date().getTime(), safeReference(hoststatus, "currentWork.recorded", 0))
                                                           ];
			}
			var datatable = $("#hoststatustable").dataTable({aaData: tbldata,
                                                             aoColumns: [{"sTitle": "Host Name", "sWidth": "25%", "sType": "html"},
                                                                         {"sTitle": "Last Check-in", "sWidth": "18.75%"},
                                                                         {"sTitle": "Current Test", "sWidth": "18.75%"},
                                                                         {"sTitle": "Current Run", "sWidth": "18.75%"},
                                                                         {"sTitle": "Run Time", "sWidth": "18.75%"}],
                                                             bJQueryUI: true,
                                                             bAutoWidth: false,
                                                             bDeferRender: true,
                                                             bPaginate: false,
                                                             sDom: "<\"H\"lfr>tS<\"F\"ip>",
                                                             sScrollY: "" + ($(document).height() - (4 * $("#pagetitle").height()) -  (3 * $("#titlebar").height())) + "px"
			});
			datatable.fnSort([[0, "asc"]]);
});

