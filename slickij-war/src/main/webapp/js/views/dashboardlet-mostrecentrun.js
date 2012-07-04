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
        this.on("finish", this.onFinish, this);
    },

    onReady: function() {
        this.testrun = this.data.testruns[0];
        $(this.el).addClass("box width-5");
        this.title = "Most Recent Testrun: " + safeReference(this.testrun, "testplan.name", this.testrun.name);

        this.chartdata = new google.visualization.DataTable();
        this.chartdata.addColumn('string', 'Result Type');
        this.chartdata.addColumn('number', 'Number of Results');
        var resultTypeCount = 0;
        if(this.testrun.summary.statusListOrdered) {
            resultTypeCount = this.testrun.summary.statusListOrdered.length;
        }
        this.chartdata.addRows(resultTypeCount);
        this.statusColors = [];
        this.summarylines = [];
        for(var i = 0; i < this.testrun.summary.statusListOrdered.length; ++i) {
            var statusName = this.testrun.summary.statusListOrdered[i];

            // get the color from CSS
            var statusColorElement = $('<div class="result-status-' + statusName.replace("_", "") + '" style="display: none" />').appendTo("#main");
            this.statusColors[this.statusColors.length] = statusColorElement.css('color');
            statusColorElement.remove();

            // add the data to the chart
            this.chartdata.setValue(i, 0, statusName.replace("_", " "));
            this.chartdata.setValue(i, 1, this.testrun.summary.resultsByStatus[statusName]);

            // add the line to the summary element
            this.summarylines[this.summarylines.length] = {
                testrunid: this.options.positional[0],
                resultstatus: statusName,
                resulttype: statusName.replace("_", " "),
                statusclass: statusName.replace("_", ""),
                numberoftests: this.testrun.summary.resultsByStatus[statusName],
                percentageoftotal: "" + (((0.0 + this.testrun.summary.resultsByStatus[statusName]) / (0.0 + this.testrun.summary.total)) * 100.0).toFixed(1) + "%"
            };
        }
        this.summarytotal = {
            resulttype: "TOTAL",
            statusclass: "TOTAL",
            numberoftests: this.testrun.summary.total,
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

        var data = this.chartdata;
        var testrun = this.testrun;

        google.visualization.events.addListener(chart, "select", function() {
            var name = (data.getValue((chart.getSelection()[0]).row, 0)).replace(" ","_");
            $.address.value("/reports/testrundetail/" + testrun.id + "?only=" + name);
        });

    }

});
