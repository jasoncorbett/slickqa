var DashboardAllProjectsView = SlickPage.extend({
    group: "dashboards",
    codename: "allprojects",
    name: "All Projects",
    navigation: true,

    requiredData: {
        projects: function() {
            return "api/projects";
        }
    },

    initialize: function() {
        this.on("ready", this.onready, this);
        this.on("finish", this.onFinish, this);
    },

    onready: function() {
        this.title = "All Projects";
        this.dashboardlets = [];
        _.each(this.data.projects, function(value) {
            this.dashboardlets.push(new ProjectFourTestRuns({positional: [value], query: {}, noSetTitle: true}))
        }, this);
    },

    onFinish: function() {
        _.each(this.dashboardlets, function(value) {
            $("#dashboardlets-container").append(value.el);
            value.pageStart();
        }, this);
    }
});