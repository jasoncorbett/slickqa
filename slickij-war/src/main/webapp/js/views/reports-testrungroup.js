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
        result: function() {
            return "api/testrungroups";
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
            _.each(testrungroup.summary.statusListOrdered, function(statusName) {
                resultBar = resultBar + "<div class=\"result-bar-inner status-background-" + statusName.replace("_", "") + "\" style=\"width: " + (testrungroup.summary.resultsByStatus[statusName] / testrungroup.summary.total).toFixed(2) + "in;\"></div>";
            });


            tbldata[tbldata.length] = [
                "<a href=\"#/reports/testrunsummary/" + testrungroup.id + "\">" + testrungroup.name + "</a>",
                "<div class=\"result-bar center-block\">" + resultBar + "</div>",
                safeReference(testrungroup, "groupSummary.total", 0),
                safeReference(testrungroup, "testruns.length", 0),
                "<a href=\"javascript:;\" class=\"button testrungroup-edit-button\" data-id=\"" + testrungroup.id + "\" > Edit </a>",
                "<a href=\"javascript:;\" class=\"button testrungroup-delete-button\" data-id=\"" + testrungroup.id + "\"> Delete </a>",
                new Date(testrungroup.dateCreated)];
        });
        this.testrunData = tbldata;
    },

    onFinish: function() {

    }
});
