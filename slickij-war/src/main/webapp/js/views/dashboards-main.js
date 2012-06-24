/**
 * Created by IntelliJ IDEA.
 * User: jcorbett
 * Date: 2/7/12
 * Time: 2:49 PM
 */

var DashboardMainView = SlickPage.extend({
    group: "dashboards",
    codename: "main",
    name: "Main Dashboard",
    navigation: true,

    initialize: function() {
        this.on("ready", this.onready, this);
        this.on("finish", this.onFinish, this);
    },

    onready: function() {
        this.data.project = getCurrentProject();
        this.dashboardlets = [
            new MostRecentTestRunSummaryDashboardlet({positional: [], query: {}, noSetTitle: true}),
            new ThreeMostRecentTestrunsDashboardlet({positional: [], query: {}, noSetTitle: true}),
            new BuildStatusDashboardlet({positional: [], query: {}, noSetTitle: true})
        ];
    },

    onFinish: function() {
        _.each(this.dashboardlets, function(value) {
            $("#dashboardlets-container").append(value.el);
            value.pageStart();
        }, this);

    }
});
