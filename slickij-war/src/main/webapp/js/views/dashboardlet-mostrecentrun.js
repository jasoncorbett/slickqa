/**
 * This "dashboardlet" (an embeddable page that isn't meant to live on it's own) will show a graph and numbers of the
 * most recent test run.
 * User: jcorbett
 * Date: 4/30/12
 * Time: 3:36 PM
 */

var MostRecentTestRunSummaryDashboardlet = SlickPage.extend({

    group: "dashboardlet",
    codename: "mostrecenttestrunsummary",
    name: "Most Recent Testrun Summary",
    attributes: {
        class: "box width-6"
    },

    requiredData: {
        testruns: function() {
            return "api/testruns?projectid=" + getCurrentProject().id + "&limit=1";
        }
    },

    initialize: function() {
        this.on("ready", this.onReady, this);
        this.on("dataRecieved", this.onDataRecieved, this);
        this.on("finish", this.onFinish, this);
    },

    onDataRecieved: function(event) {
        var key = event[0];
        var data = event[1];
        if (key == "testruns") {
            this.addRequiredData("summary", SlickUrlBuilder.testrun.getSummary(data[0].id));
        }
    },

    onReady: function() {
        $(this.el).addClass("box width-5");
        this.title = this.data.summary.name;
        if(this.data.summary.testplanId) {
            this.title = "Testrun for Plan: " + safeReference(this.data.summary, "testplan.name", "?");
        }

        this.chartdata = new google.visualization.DataTable();
        this.chartdata.addColumn('string', 'Result Type');
        this.chartdata.addColumn('number', 'Number of Results');
        var resultTypeCount = 0;
        if(this.data.summary.statusListOrdered) {
            resultTypeCount = this.data.summary.statusListOrdered.length;
        }
        this.chartdata.addRows(resultTypeCount);
        this.statusColors = [];
        this.summarylines = [];
        for(var i = 0; i < this.data.summary.statusListOrdered.length; ++i) {
            var statusName = this.data.summary.statusListOrdered[i];

            // get the color from CSS
            var statusColorElement = $('<div class="result-status-' + statusName.replace("_", "") + '" style="display: none" />').appendTo("#main");
            this.statusColors[this.statusColors.length] = statusColorElement.css('color');
            statusColorElement.remove();

            // add the data to the chart
            this.chartdata.setValue(i, 0, statusName.replace("_", " "));
            this.chartdata.setValue(i, 1, this.data.summary.resultsByStatus[statusName]);

            // add the line to the summary element
            this.summarylines[this.summarylines.length] = {
                testrunid: this.options.positional[0],
                resultstatus: statusName,
                resulttype: statusName.replace("_", " "),
                statusclass: statusName.replace("_", ""),
                numberoftests: this.data.summary.resultsByStatus[statusName],
                percentageoftotal: "" + (((0.0 + this.data.summary.resultsByStatus[statusName]) / (0.0 + this.data.summary.total)) * 100.0).toFixed(1) + "%"
            };
        }
        this.summarytotal = {
            resulttype: "TOTAL",
            statusclass: "TOTAL",
            numberoftests: this.data.summary.total,
            percentageoftotal: ""
        };

    },

    onFinish: function() {
        var chart = new google.visualization.PieChart(document.getElementById("dashboardlet-testrunsummary-chart"));
        chart.draw(this.chartdata, {
            is3D: true,
            backgroundColor: $("#main").css('background-color'),
            legendTextStyle: {
                color: $("#main").css('color')},
            colors: this.statusColors
        });


    }

});
