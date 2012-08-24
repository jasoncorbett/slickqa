var DashboardActiveProjectsView = SlickPage.extend({
    group: "dashboards",
    codename: "activeprojects",
    name: "Active Projects",
    navigation: true,

    requiredData: {
        projects: function() {
            return "api/projects";
        }
    },

    initialize: function() {
        this.dashboardInfo = new Array();
        this.on("dataRecieved", this.onDataRecieved, this);
        this.on("ready", this.onready, this);
        this.on("finish", this.onFinish, this);
    },

    onDataRecieved: function(event) {
        var allReleases = [];
        var key = event[0];
        var data = event[1];
        if (key == "projects") {
            _.each(data, function(value) {
                _.each(value.releases, function(activeRelease) {
                    this.dashboardInfo.push([activeRelease.id, value, activeRelease]);
                    this.addRequiredData(activeRelease.id, "api/testruns?projectid=" + value.id + "&releaseid=" + activeRelease.id + "&limit=3");
                }, this);
            }, this);
        }
    },

    onready: function() {
        this.title = "Active Projects";
        this.dashboardlets = [];
        var sortable = [];
        _.each(this.dashboardInfo, function(info, id, list) {
            info.push(this.data[info[0]]);
            sortable.push(info);
        }, this);
        //now we can sort by number of test runs (limited by 3)
        sortable.sort(function(a, b){
            var nameA = a[3].length;
            var nameB = b[3].length;
            if (nameA < nameB)
                return 1;
            if (nameA > nameB)
                return -1;
            return 0;
        });
        //add the dashboardlets
        _.each(sortable, function(ar) {
            this.dashboardlets.push(new ActiveReleasesThreeTestRuns({positional: [ar[1], ar[2], ar[3]], query: {}, noSetTitle: true}))
        }, this);
    },

    onFinish: function() {
        _.each(this.dashboardlets, function(value) {
            $("#dashboardlets-container").append(value.el);
            value.pageStart();
        }, this);
    }
});