/**
 * Created by IntelliJ IDEA.
 * User: jcorbett
 * Date: 3/5/12
 * Time: 10:51 AM
 */

var ReportsTestrunSummaryPage = SlickPage.extend({
    name: "Testrun Summary",
    codename: "testrunsummary",
    group: "reports",

    requiredData: {
        "testrun": function() {
            return SlickUrlBuilder.testrun.getTestrun(this.options.positional[0]);
        }
    },

    initialize: function() {
        this.on("ready", this.onReady, this);
        this.on("finish", this.onFinish, this);
    },

    onReady: function() {

        // this section sets up data needed by the template
        this.subtitle1 = (new Date(this.data.testrun.dateCreated)).toLocaleDateString();
        this.subtitle2 = safeReference(this.data.testrun, "project.name", "");
        if (this.data.testrun.config) {
            this.subtitle3 = safeReference(this.data.testrun, "config.name", "");
        }
        this.subtitle4 = safeReference(this.data.testrun, "release.name", "") + " Build " + safeReference(this.data.testrun, "build.name", "Unknown");
        if (this.data.testrun.runtimeOptions) {
            this.subtitle5 = "Runtime Options: " + safeReference(this.data.testrun, "runtimeOptions.name", "");
        }
		this.timeCreated = (new Date(this.data.testrun.dateCreated)).toLocaleTimeString();
		this.chartdata = new google.visualization.DataTable();
        this.chartdata.addColumn('string', 'Result Type');
        this.chartdata.addColumn('number', 'Number of Results');
        var resultTypeCount = 0;
        if(this.data.testrun.summary.statusListOrdered) {
            resultTypeCount = this.data.testrun.summary.statusListOrdered.length;
        }
        this.chartdata.addRows(resultTypeCount);
        this.statusColors = [];
        this.summarylines = [];
        for(var i = 0; i < this.data.testrun.summary.statusListOrdered.length; ++i) {
            var statusName = this.data.testrun.summary.statusListOrdered[i];

            // get the color from CSS
            var statusColorElement = $('<div class="result-status-' + statusName.replace("_", "") + '" style="display: none" />').appendTo("#main");
            this.statusColors[this.statusColors.length] = statusColorElement.css('color');
            statusColorElement.remove();

            // add the data to the chart
            this.chartdata.setValue(i, 0, statusName.replace("_", " "));
            this.chartdata.setValue(i, 1, this.data.testrun.summary.resultsByStatus[statusName]);

            // add the line to the summary element
            this.summarylines[this.summarylines.length] = {
                testrunid: this.options.positional[0],
                resultstatus: statusName,
                resulttype: statusName.replace("_", " "),
                statusclass: statusName.replace("_", ""),
                numberoftests: this.data.testrun.summary.resultsByStatus[statusName],
                percentageoftotal: "" + (((0.0 + this.data.testrun.summary.resultsByStatus[statusName]) / (0.0 + this.data.testrun.summary.total)) * 100.0).toFixed(1) + "%"
            };
        }
        this.summarytotal = {
            resulttype: "TOTAL",
            statusclass: "TOTAL",
            numberoftests: this.data.testrun.summary.total,
            percentageoftotal: ""
        };
    },

    onFinish: function() {
        // draw the chart
        var chart = new google.visualization.PieChart(document.getElementById("testrunsummary-chart"));
        chart.draw(this.chartdata, {
            is3D: true,
            backgroundColor: $("#main").css('background-color'),
            legendTextStyle: {
                color: $("#main").css('color')},
            colors: this.statusColors
        });

        var data = this.chartdata;
        var testrun = this.data.testrun;

        google.visualization.events.addListener(chart, "select", function() {
            var name = (data.getValue((chart.getSelection()[0]).row, 0)).replace(" ","_");
            $.address.value("/reports/testrundetail/" + testrun.id + "?only=" + name);
        });

        $(".testrun-reschedule-bulk").on("click", function(){
            $.ajax({
                url: "api/testruns/" + $(this).attr("id"),
                type: "POST",
                dataType: "json",
                success: function(data) {
                    window.onPageChange();
                    $.jGrowl("Successfully rescheduled tests.");
                }
            });
        });
    },

    getTitle: function() {
        return safeReference(this.data.testrun, "testplan.name", this.data.testrun.name) + " Summary";
    }
});

