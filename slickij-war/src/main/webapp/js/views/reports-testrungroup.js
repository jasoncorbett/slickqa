/**
 * Created by IntelliJ IDEA.
 * User: jcorbett
 * Date: 8/3/12
 * Time: 9:40 AM
 */

var ReportsResultViewPage = SlickPage.extend({
    group: "reports",
    codename: "testrungroup",
    name: "Test Run Group",
    navigation: true,

    requiredData: {
        testrungroups: function() {
            return "api/testrungroups?createdafter=" + moment().subtract('months', 3).valueOf();
        }
    },

    initialize: function() {
        this.on("ready", this.onReady, this);
        this.on("finish", this.onFinish, this);
    },

    onReady: function() {
        this.title = "Test Run Group";
        var tbldata = [];
        _.each(this.data.testrungroups, function(testrungroup) {
            var resultBar = "";
            _.each(testrungroup.groupSummary.statusListOrdered, function(statusName) {
                resultBar = resultBar + "<div class=\"result-bar-inner status-background-" + statusName.replace("_", "") + "\" style=\"width: " + (testrungroup.groupSummary.resultsByStatus[statusName] / testrungroup.groupSummary.total).toFixed(2) + "in;\"></div>";
            });


            tbldata[tbldata.length] = [
                "<a href=\"#/reports/testrunsummary/" + testrungroup.id + "\">" + testrungroup.name + "</a>",
                "<div class=\"result-bar center-block\">" + resultBar + "</div>",
                safeReference(testrungroup, "groupSummary.total", 0),
                safeReference(testrungroup, "testruns.length", 0),
                "<a href=\"javascript:;\" class=\"button testrungroup-edit-button\" data-id=\"" + testrungroup.id + "\" > Edit </a>" +
                "<a href=\"javascript:;\" class=\"button testrungroup-delete-button\" data-id=\"" + testrungroup.id + "\"> Delete </a>",
                new Date(testrungroup.created)];
        });
        this.testrungroupData = tbldata;
    },

    onFinish: function() {
        var datatable = $("#reports-testrungroup-table").dataTable({
            aaData: this.testrungroupData,
            aoColumns: [
                {"sTitle": "Name", "sWidth": "40%", "sType": "html"},
                {"sTitle": "Results", "sWidth": "10%", "sType": "html", "sClass": "center"},
                {"sTitle": "Total", "sWidth": "5%", "sClass": "center"},
                {"sTitle": "Total Testruns", "sWidth": "5%", "sClass": "center"},
                {"sTitle": "Actions", "sWidth": "20%", "sClass": "center"},
                {"sTitle": "Date Created", "sWidth": "20%", "sClass": "center"}],
            bJQueryUI: true,
            bAutoWidth: false,
            bDeferRender: true,
            bPaginate: false,
            sDom: "<\"H\"lfr>tS<\"F\"ip>",
            sScrollY: "" + ($("#content").height() - (4 * $("#bytestrun-release-select").height()) - (5 * $("#footer").height()) - $("#content-bottom-pad").height()) + "px"
        });
        datatable.fnSort([[5, "desc"]]);


    }
});
