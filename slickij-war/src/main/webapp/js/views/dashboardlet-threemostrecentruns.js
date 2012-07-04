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
    },

    onReady: function() {
        $(this.el).addClass("box width-5");
        this.theruns = [];
        for(var i = 1; i < 4; i++) {
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
        this.title = "Three most recent Testruns";
    }
});

