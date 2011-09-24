Routes["/reports/testrunsummary/:testrunid"] = "ReportsView#testrunsummary";
ReportsView.views.testrunsummary = function(options) {
	grabTemplate("report-view-testrunsummary", function(template) {
		grabTemplate("report-view-testrunsummary-dataline", function(dataline) {
			$.tmpl(template, options).appendTo("#main");
			$("#testrunsummary-chart").addClass("slick-event-testrunsummary-load").bind("slick-data-testrunsummary-load", function(evt, summary) {
				setSlickTitle(safeReference(summary, "testplan.name", summary.name) + " Summary");
				if(summary.testplanId) {
					$("#subtitle1").text("Testrun for Plan: " + safeReference(summary, "testplan.name", "?"));
				} else {
					$("#subtitle1").text(summary.name);
				}
				$("#subtitle2").text(safeReference(summary, "release.name", "") + " Build " + safeReference(summary, "build.name", "Unknown"));
				var data = new google.visualization.DataTable();
				data.addColumn('string', 'Result Type');
				data.addColumn('number', 'Number of Results');
				var resultTypeCount = 0;
				if(summary.statusListOrdered) {
					resultTypeCount = summary.statusListOrdered.length;
				}
				data.addRows(resultTypeCount);
				statusColors = [];
				for(var i = 0; i < summary.statusListOrdered.length; ++i) {
					var statusName = summary.statusListOrdered[i];
	
					// get the color from CSS
					var statusColorElement = $('<div class="result-status-' + statusName.replace("_", "") + '" style="display: none" />').appendTo("#main");
					statusColors[statusColors.length] = statusColorElement.css('color');
					statusColorElement.remove();
	
					// add the data to the chart
					data.setValue(i, 0, statusName.replace("_", " "));
					data.setValue(i, 1, summary.resultsByStatus[statusName]);

					// add the line to the summary element
					$.tmpl(dataline, {testrunid: options.testrunid,
					                  resultstatus: statusName,
					                  resulttype: statusName.replace("_", " "),
					                  statusclass: statusName.replace("_", ""),
					                  numberoftests: summary.resultsByStatus[statusName], 
									  percentageoftotal: "" + (((0.0 + summary.resultsByStatus[statusName]) / (0.0 + summary.total)) * 100.0).toFixed(1) + "%"}).appendTo("#summary");
				}
				$('<hr />').appendTo("#summary");
				$.tmpl(dataline, {resulttype: "TOTAL", statusclass: "TOTAL", numberoftests: summary.total, percentageoftotal: ""}).appendTo("#summary");
	
				var chart = new google.visualization.PieChart(document.getElementById("testrunsummary-chart"));
				chart.draw(data, {is3D: true, backgroundColor: $("#main").css('background-color'), legendTextStyle: { color: $("#main").css('color') }, colors: statusColors});
			});
		});
		Slick.testrun.getSummary(options.testrunid);
	});
}
