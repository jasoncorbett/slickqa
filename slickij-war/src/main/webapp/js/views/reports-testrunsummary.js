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
        "summary": function() {
            return SlickUrlBuilder.testrun.getSummary(this.options.positional[0]);
        }
    },

    initialize: function() {
        this.on("ready", this.onReady, this);
        this.on("finish", this.onFinish, this);
    },

    onReady: function() {

        // this section sets up data needed by the template
        this.subtitle1 = this.data.summary.name;
        if(this.data.summary.testplanId) {
            this.subtitle1 = "Testrun for Plan: " + safeReference(this.data.summary, "testplan.name", "?");
        }
        this.subtitle2 = safeReference(this.data.summary, "release.name", "") + " Build " + safeReference(this.data.summary, "build.name", "Unknown");
        this.subtitle3 = safeReference(this.data.summary, "config.name", "");


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
        // draw the chart
        var chart = new google.visualization.PieChart(document.getElementById("testrunsummary-chart"));
        chart.draw(this.chartdata, {
            is3D: true,
            backgroundColor: $("#main").css('background-color'),
            legendTextStyle: {
                color: $("#main").css('color')},
            colors: this.statusColors
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
        return safeReference(this.data.summary, "testplan.name", this.data.summary.name) + " Summary";
    }
});

