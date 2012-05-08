/**
 * Created with IntelliJ IDEA.
 * User: jcorbett
 * Date: 5/8/12
 * Time: 4:13 PM
 */

var ProjectLinksDashboardlet = SlickPage.extend({
    name: "Looking for something?",
    codename: "projectlinks",
    group: "dashboardlet",

    attributes: {
        class: "box width-6"
    },

    requiredData: {
        project: function() {
            return "api/projects/" + getCurrentProject().id;
        }
    }
});
