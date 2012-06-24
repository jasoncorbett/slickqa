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

                this.addRequiredData("summary", "api/results/summary/bybuild/" + this.defaultBuildObj.id);
        }
    },

    onReady: function() {
        this.title = this.name;
        this.statuses = [];
        _.each(this.data.summary.statusListOrdered, function(statusName) {

            // add the line to the summary element
            this.statuses[this.statuses.length] = {
                resultstatus: statusName,
                resulttype: statusName.replace("_", " "),
                statusclass: statusName.replace("_", ""),
                numberoftests: this.data.summary.resultsByStatus[statusName],
                percentageoftotal: "" + (((0.0 + this.data.summary.resultsByStatus[statusName]) / (0.0 + this.data.summary.total)) * 100.0).toFixed(1) + "%"
            };

        }, this);
    }

});