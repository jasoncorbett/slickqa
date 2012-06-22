/**
 * Created with IntelliJ IDEA.
 * User: jcorbett
 * Date: 6/21/12
 * Time: 3:26 PM
 */

var ThreeMostRecentTestrunsDashboardlet = SlickPage.extend({

    group: "dashboardlet",
    codename: "threemostrecentruns",
    name: "Three Most Recent Testruns",
    attributes: {
        class: "box width-6"
    },

    requiredData: {
        testruns: function() {
            // The first testrun is ignored because it's in the mostrecentrun dashboardlet
            return "api/testruns?projectid=" + getCurrentProject().id + "&limit=4";
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
            _.each(data.slice(1), function(testrun, index) {
                this.addRequiredData("summary" + (index + 1), SlickUrlBuilder.testrun.getSummary(testrun.id));
            }, this);
        }
    },

    onReady: function() {
        $(this.el).addClass("box width-5");
        this.theruns = [];
        for(var i = 1; i < 4; i++) {
            var key = "summary" + i;
            if(_.has(this.data, key)) {
                var summary = this.data[key];
                summary.name = safeReference(summary, "testplan.name", summary.name);
                _.each(["PASS", "FAIL", "BROKEN_TEST"], function(resulttype) {
                    if(!_.has(summary.resultsByStatus, resulttype)) {
                        summary.resultsByStatus[resulttype] = 0;
                    }
                });
                this.theruns[i - 1] = summary;
            } else {
                break;
            }
        }
        this.title = "Three most recent Testruns";
    }
});

