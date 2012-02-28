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

    // Initialize is called
    initialize: function() {
        this.on("ready", this.onready, this);
    },

    onready: function() {
        this.data.project = getCurrentProject();
    }
});
