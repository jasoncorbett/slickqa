/**
 * Created with IntelliJ IDEA.
 * User: jcorbett
 * Date: 6/23/12
 * Time: 9:18 PM
 */

var BuildStatusDashboardlet = SlickPage.extend({
    group: "dashboardlet",
    codename: "buildstatus",
    name: "Current Build Status",
    attributes: {
        class: "box width-6"
    },

    requiredData: {
        project: function() {
            // The first testrun is ignored because it's in the mostrecentrun dashboardlet
            return "api/projects/" + getCurrentProject().id;
        }
    },

    initialize: function() {
        this.on("ready", this.onReady, this);
        this.on("dataRecieved", this.onDataRecieved, this);
        //this.on("finish", this.onFinish, this);
    },

    onDataRecieved: function(event) {
        var key = event[0];
        var data = event[1];
        if (key == "project") {
            this.defaultRelease = data.defaultRelease;
            if(this.options.positional && this.options.positional[0]) {
                this.defaultRelease = this.options.positional[0];
            }

            _.each(data.releases, function(release) {
                if(release.id == this.defaultRelease) {
                    release.defaultRelease = true;
                    this.defaultReleaseObj = release;
                }
            }, this);

            // prepare the builds array needed for the datatable
            _.each(this.defaultReleaseObj.builds, function(build) {
                if(this.defaultReleaseObj.defaultBuild == build.id) {
                    this.defaultBuildObj = build
                }
            }, this);

            this.addRequiredData("testruns", "api/testruns?buildid=" + this.defaultBuildObj.id);
        }
    },

    onReady: function() {
        this.title = this.name;
        var statusNames = [];
        var statuses = {};
        this.total = 0;
        _.each(this.data.testruns, function(testrun) {
            _.each(testrun.summary.statusListOrdered, function(statusName) {

                if(_.contains(statusNames, statusName)) {
                    statuses[statusName].numberoftests = statuses[statusName].numberoftests + testrun.summary.resultsByStatus[statusName];
                } else {
                    statusNames[statusNames.length] = statusName;
                    statuses[statusName] = {
                        resultstatus: statusName,
                        resulttype: statusName.replace("_", " "),
                        statusclass: statusName.replace("_", ""),
                        numberoftests: testrun.summary.resultsByStatus[statusName]
                    };
                }
                this.total = this.total + testrun.summary.resultsByStatus[statusName];
            }, this);
        }, this);

        // calculate the % of total
        this.statuses = [];
        _.each(statusNames, function(statusName) {
            statuses[statusName].percentageoftotal = "" + ((statuses[statusName].numberoftests / this.total) * 100.0).toFixed(1) + "%";
            this.statuses[this.statuses.length] = statuses[statusName];
        }, this);
    }

});