/*
 * ---------------------------------------------------------------------------
 * testmgmt-view-project.js
 * ---------------------------------------------------------------------------
 */

var TestManagementViewProjectPage = SlickPage.extend({
    name: "View Current Project",
    codename: "viewproject",
    group: "testmgmt",
    navigation: true,

    requiredData: {
        "proj": function() {
            return "api/projects/" + getCurrentProject().id;
        }
    },

    initialize: function() {
        this.on("ready", this.onReady, this);
        this.on("finish", this.onFinish, this);
    },

    onReady: function() {
        this.title = "Detail for Project '" + this.data.proj.name + "'";
        this.addBuildUrl = urlOfPage(TestManagementAddBuildPage);
        this.addReleaseUrl = urlOfPage(TestManagementAddReleasePage);
        this.data.proj.lastUpdatedString = new Date(this.data.proj.lastUpdated).toLocaleString();
    },

    onFinish: function() {
        var tabledata = [];
        _.each(this.data.proj.components, function(component) {
           tabledata[tabledata.length] = [safeReference(component, "code", ""),
                                          safeReference(component, "name", ""),
                                          safeReference(component,"description", "")];
        });
        $("#project-component-table").dataTable({
            aaData: tabledata,
            aoColumns: [
                {"sTitle": "Code Name", "sWidth": "15%"},
                {"sTitle": "Name", "sWidth": "15%"},
                {"sTitle": "Description", "sWidth": "70%"}],
            bJQueryUI: true,
            bAutoWidth: false,
            bDeferRender: true,
            bPaginate: false,
            sDom: "<\"H\"lfr>tS<\"F\"ip>",
            sScrollY: "" + ($("#content").height() - (2 * $("#header").height()) - (2 * ($("#project-description-box").height())) - $("#content-bottom-pad").height()) + "px"
        });
    }

});


