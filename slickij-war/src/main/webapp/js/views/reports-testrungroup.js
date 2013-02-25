/**
 * Created by IntelliJ IDEA.
 * User: jcorbett
 * Date: 8/3/12
 * Time: 9:40 AM
 */

var ReportsTestrunGroupPage = SlickPage.extend({
    group: "reports",
    codename: "testrungroup",
    name: "Test Run Groups",
    navigation: true,

    initialize: function() {
        if (this.options.positional.length > 0) {
            this.requiredData = {
                testrungroup: function() {
                    return "api/testrungroups/" + this.options.positional[0]
                }
            }
            this.on("ready", this.onReportReady, this);
            this.on("finish", this.onReportFinish, this);
            this.primaryTemplateName = "reports-testrungroup-report.html";
        } else {
            // if we don't have a testrun group id in the url, go into select mode
            this.requiredData = {
                testrungroups: function() {
                    return "api/testrungroups?createdafter=" + moment().subtract('months', 3).valueOf();
                }
            }
            this.on("ready", this.onSelectGroupReady, this);
            this.on("finish", this.onSelectGroupFinish, this);
            this.primaryTemplateName = "reports-testrungroup-select.html";
        }
    },

    onSelectGroupReady: function() {
        this.title = "Test Run Group";
        var tbldata = [];
        _.each(this.data.testrungroups, function(testrungroup) {
            var resultBar = "";
            _.each(testrungroup.groupSummary.statusListOrdered, function(statusName) {
                resultBar = resultBar + "<div class=\"result-bar-inner status-background-" + statusName.replace("_", "") + "\" style=\"width: " + (testrungroup.groupSummary.resultsByStatus[statusName] / testrungroup.groupSummary.total).toFixed(2) + "in;\"></div>";
            });


            tbldata[tbldata.length] = [
                "<a href=\"#/reports/testrungroup/" + testrungroup.id + "\">" + testrungroup.name + "</a>",
                "<div class=\"result-bar center-block\">" + resultBar + "</div>",
                safeReference(testrungroup, "groupSummary.total", 0),
                safeReference(testrungroup, "testruns.length", 0),
                "<a href=\"#/reports/testrungroup-edit/" + testrungroup.id + "\" class=\"button modal-link\">Edit</a>" +
                "<a href=\"javascript:;\" class=\"button testrungroup-delete-button\" data-id=\"" + testrungroup.id + "\">Delete</a>",
                new Date(testrungroup.created)];
        });
        this.testrungroupData = tbldata;
    },

    onSelectGroupFinish: function() {
        var datatable = $("#reports-testrungroup-table").dataTable({
            aaData: this.testrungroupData,
            aoColumns: [
                {"sTitle": "Name", "sWidth": "40%", "sType": "html"},
                {"sTitle": "Results", "sWidth": "10%", "sType": "html", "sClass": "center"},
                {"sTitle": "Total", "sWidth": "5%", "sClass": "center"},
                {"sTitle": "Testruns", "sWidth": "8%", "sClass": "center"},
                {"sTitle": "Actions", "sWidth": "20%", "sClass": "center"},
                {"sTitle": "Date Created", "sWidth": "17%", "sClass": "center"}],
            bJQueryUI: true,
            bAutoWidth: false,
            bDeferRender: true,
            bPaginate: false,
            sDom: "<\"H\"lfr>tS<\"F\"ip>",
            sScrollY: "" + ($("#content").height() - (4 * $("#bytestrun-release-select").height()) - (6 * $("#footer").height()) - $("#content-bottom-pad").height()) + "px"
        });
        datatable.fnSort([[5, "desc"]]);

        $("#reports-testrungroup-table").on("click", ".testrungroup-delete-button", {page: this}, this.onDeleteTestrunGroup);

    },

    onReportReady: function() {
        this.title = this.data.testrungroup.name + " Testrun Group";

        // create the table data
        this.tabledata = [];
        var statuswidth =  50.0 / (this.data.testrungroup.groupSummary.statusListOrdered.length + 1.0);
        this.tablecolumns = [{sTitle: "Name", sWidth: "40%", sType: "html"},
                             {sTitle: "Build", sWidth: "10%"},
                             {sTitle: "Total Tests", sWidth: "" + statuswidth + "%"}];

        // create pie and bar chart data
        this.piechartdata = new google.visualization.DataTable();
        this.piechartdata.addColumn('string', 'Result Type');
        this.piechartdata.addColumn('number', 'Number of Results');
        this.piechartdata.addRows(this.data.testrungroup.groupSummary.statusListOrdered.length);
        this.statusColors = [];
        this.summarylines = [];
        this.barchartdata = new google.visualization.DataTable();
        this.barchartdata.addColumn('string', 'Testrun Name');

        // go through each status
        _.each(this.data.testrungroup.groupSummary.statusListOrdered, function(statusName, index) {
            // get the color from CSS
            var statusColorElement = $('<div class="result-status-' + statusName.replace("_", "") + '" style="display: none" />').appendTo("#main");
            this.statusColors[this.statusColors.length] = statusColorElement.css('color');
            statusColorElement.remove();

            // add the data to the chart
            this.piechartdata.setValue(index, 0, statusName.replace("_", " "));
            this.piechartdata.setValue(index, 1, this.data.testrungroup.groupSummary.resultsByStatus[statusName]);
            this.barchartdata.addColumn('number', statusName);

            // add to the table columns
            this.tablecolumns[this.tablecolumns.length] = {sTitle: statusName.replace("_", " "), sWidth: statuswidth};
        }, this);

        this.barchartdata.addRows(this.data.testrungroup.testruns.length);
        _.each(this.data.testrungroup.testruns, function(testrun, i) {
            this.barchartdata.setValue(i, 0, testrun.name);

            var testruncolumns = ["<a href=\"#/reports/testrunsummary/" + testrun.id + "\">" + testrun.name + "</a>",
                                  safeReference(testrun, "release.name", "") + " Build " + safeReference(testrun, "build.name", ""),
                                  testrun.summary.total];
            _.each(this.data.testrungroup.groupSummary.statusListOrdered, function(statusName, j) {
                var value = 0;
                if(testrun.summary.resultsByStatus[statusName]) {
                    value = testrun.summary.resultsByStatus[statusName];
                }
                this.barchartdata.setValue(i, j + 1, value);
                testruncolumns[testruncolumns.length] = value;
            }, this);

            this.tabledata[this.tabledata.length] = testruncolumns;

        }, this);

    },

    onReportFinish: function() {
        var piechart = new google.visualization.PieChart(document.getElementById("testrungroup-piechart"));
        piechart.draw(this.piechartdata, {
            is3D: true,
            backgroundColor: $("#main").css('background-color'),
            legendTextStyle: {
                color: $("#main").css('color')},
            colors: this.statusColors
        });

        var barchart = new google.visualization.ColumnChart(document.getElementById("testrungroup-barchart"));
        barchart.draw(this.barchartdata, {
            backgroundColor: $("#main").css('background-color'),
            legendTextStyle: {
                color: $("#main").css('color')},
            colors: this.statusColors,
            hAxis: { textStyle: { color: $("#main").css('color')}},
            vAxis: { baselineColor: $("#main").css('color'), textStyle: { color: $("#main").css('color')}}
        });

        var datatable = $("#testruns-table").dataTable({
            aaData: this.tabledata,
            aoColumns: this.tablecolumns,
            bJQueryUI: true,
            bAutoWidth: false,
            bDeferRender: true,
            bPaginate: false,
            sDom: "<\"H\"lfr>tS<\"F\"ip>",
            sScrollY: "" + ($("#content").height() - $("#testrungroup-piechart-container").height() - (6 * $("#footer").height()) - $("#content-bottom-pad").height()) + "px"
        });

    },

    onDeleteTestrunGroup: function(event) {
        event.preventDefault();
        var page = event.data.page;
        var id = $(event.target).data("id");

        $.ajax({
            url: "api/testrungroups/" + id,
            type: "DELETE",
            success: function() {
                window.onPageChange();
            },
            error: function() {
                page.error("Unable to delete testrun group with id " + id);
            }
        });

    }
});
