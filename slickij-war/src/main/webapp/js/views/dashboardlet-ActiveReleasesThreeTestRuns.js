var ActiveReleasesThreeTestRuns = SlickPage.extend({

    group: "dashboardlet",
    codename: "activereleasesthreetestruns",
    name: "Active Releases Most Recent Testruns",
    attributes: {
        class: "box width-6"
    },

    initialize: function() {
        this.project = this.options.positional[0];
        this.release = this.options.positional[1];
        this.projectname = this.project.name;
        this.releasename = this.options.positional[1].name
        this.testruns = this.options.positional[2];
        this.on("ready", this.onReady, this);
        this.on("finish", this.onFinish, this);
    },

    onReady: function() {
        // Ready the chart for the first result
        $(this.el).addClass("box width-5");
        this.nodata = true;
        this.title = this.projectname + " " + this.releasename;
        this.extralinks = false;
        this.linkslist = [];
        for (var attr in this.project.attributes) {
            if (this.project.attributes[attr].indexOf("http") > -1) {
                this.linkslist.push({name:attr,value:this.project.attributes[attr]})
                this.extralinks = true;
            }
        }
        if (this.testruns.length > 0) {
            this.nodata = false
            var summary = this.testruns[0].summary
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

            //Ready test runs
            this.theruns = [];
            for(var j = 0; j < 4; j++) {
                if (this.testruns[j]) {
                    var testrun = this.testruns[j];
                    var date = new Date(testrun.dateCreated)
                    testrun.datestring = date.toLocaleDateString() + " " + date.toLocaleTimeString();
                    testrun.name = safeReference(testrun, "testplan.name", testrun.name);
                    _.each(["PASS", "FAIL", "BROKEN_TEST"], function(resulttype) {
                        if(!_.has(testrun.summary.resultsByStatus, resulttype)) {
                            testrun.summary.resultsByStatus[resulttype] = 0;
                        }
                    });
                    this.theruns[j] = testrun;
                }
            }
        }
    },

    onFinish: function() {
        //first result
        if (!this.nodata) {
            var chart = new google.visualization.PieChart(document.getElementById("dashboardlet-" + this.title + "-chart"));
            chart.draw(this.chartdata, {
                is3D: true,
                backgroundColor: $("#main").css('background-color'),
                legendTextStyle: {
                    color: $("#main").css('color')},
                colors: this.statusColors
            });
            var data = this.chartdata;
            google.visualization.events.addListener(chart, "select", function() {
                var name = (data.getValue((chart.getSelection()[0]).row, 0)).replace(" ","_");
                $.address.value("/reports/testrundetail/" + this.testruns[0].id + "?only=" + name);
            });
        }
    }
});
