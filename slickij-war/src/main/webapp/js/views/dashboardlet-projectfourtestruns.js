var ProjectFourTestRuns = SlickPage.extend({

    group: "dashboardlet",
    codename: "projectfourtestruns",
    name: "All Projects Four Most Recent Testruns",
    attributes: {
        class: "box width-6"
    },

    requiredData: {
        testruns: function() {
            return "api/testruns?projectid=" + this.options.positional[0].id + "&limit=4";
        }
    },

    initialize: function() {
        this.project = this.options.positional[0];
        this.projectname = this.project.name;
        this.on("ready", this.onReady, this);
        this.on("finish", this.onFinish, this);
    },

    onReady: function() {
        // Ready the chart for the first result
        $(this.el).addClass("box width-5");
        this.nodata = true;
        this.title = this.projectname;
        this.extralinks = false;
        if (this.project.attributes) {}
        if (this.data.testruns.length > 0) {
            this.nodata = false
            var summary = this.data.testruns[0].summary
            this.chartdata = new google.visualization.DataTable();
            this.chartdata.addColumn('string', 'Result Type');
            this.chartdata.addColumn('number', 'Number of Results');
            var resultTypeCount = 0;
            if(summary.statusListOrdered) {
                resultTypeCount = summary.statusListOrdered.length;
            }
            this.chartdata.addRows(resultTypeCount);
            this.statusColors = [];
            this.summarylines = [];
            for(var i = 0; i < summary.statusListOrdered.length; ++i) {
                var statusName = summary.statusListOrdered[i];

                // get the color from CSS
                var statusColorElement = $('<div class="result-status-' + statusName.replace("_", "") + '" style="display: none" />').appendTo("#main");
                this.statusColors[this.statusColors.length] = statusColorElement.css('color');
                statusColorElement.remove();

                // add the data to the chart
                this.chartdata.setValue(i, 0, statusName.replace("_", " "));
                this.chartdata.setValue(i, 1, summary.resultsByStatus[statusName]);

                // add the line to the summary element
                this.summarylines[this.summarylines.length] = {
                    testrunid: summary.id,
                    resultstatus: statusName,
                    resulttype: statusName.replace("_", " "),
                    statusclass: statusName.replace("_", ""),
                    numberoftests: summary.resultsByStatus[statusName],
                    percentageoftotal: "" + (((0.0 + summary.resultsByStatus[statusName]) / (0.0 + summary.total)) * 100.0).toFixed(1) + "%"
                };
            }
            this.summarytotal = {
                resulttype: "TOTAL",
                statusclass: "TOTAL",
                numberoftests: summary.total,
                percentageoftotal: ""
            };

            //Ready the other three test runs
            this.theruns = [];
            for(var i = 0; i < 4; i++) {
                if (this.data.testruns[i]) {
                    var summary = this.data.testruns[i];
                    summary.datestring = (new Date(summary.dateCreated)).toLocaleDateString();
                    summary.name = safeReference(summary, "testplan.name", summary.name);
                    _.each(["PASS", "FAIL", "BROKEN_TEST"], function(resulttype) {
                        if(!_.has(summary.summary.resultsByStatus, resulttype)) {
                            summary.summary.resultsByStatus[resulttype] = 0;
                        }
                    });
                    this.theruns[i - 1] = summary;
                }
            }
        }
    },

    onFinish: function() {
        //first result
        if (!this.nodata) {
            var chart = new google.visualization.PieChart(document.getElementById("dashboardlet-" + this.projectname + "-chart"));
            chart.draw(this.chartdata, {
                is3D: true,
                backgroundColor: $("#main").css('background-color'),
                legendTextStyle: {
                    color: $("#main").css('color')},
                colors: this.statusColors
            });
            var data = this.chartdata;
            var testrun = this.data.testruns[0];
            google.visualization.events.addListener(chart, "select", function() {
                var name = (data.getValue((chart.getSelection()[0]).row, 0)).replace(" ","_");
                $.address.value("/reports/testrundetail/" + testrun.id + "?only=" + name);
            });
        }
    }
});
