var TestManagementViewTestcasePage = SlickPage.extend({
    name: "View Test Case",
    codename: "viewtestcase",
    group: "testmgmt",
    navigation: false,

    requiredData: {
        "testcase": function() {
                return "api/testcases/" + this.options.positional[0];
        }
    },

    initialize: function() {
        this.on("ready", this.onReady, this);
        this.on("finish", this.onFinish, this);
    },

    onReady: function() {
        this.title = "View Testcase Details: '" + this.data.testcase.name + "'";
    },

    onFinish: function() {
        var tabledata = [];
        _.each(this.data.testcase.steps, function(steps) {
           tabledata[tabledata.length] = [safeReference(steps, "name", ""),
                                          safeReference(steps, "expectedResult", "")];
        });

        $("#testcase-detail-table").dataTable({
            aaData: tabledata,
            aoColumns: [
                {"sTitle": "Name", "sWidth": "40%"},
                {"sTitle": "Expected", "sWidth": "60%"}],
            bJQueryUI: true,
            bSort: false,
            bAutoWidth: false,
            bDeferRender: true,
            bPaginate: false,
            sDom: "<\"H\"lfr>tS<\"F\"ip>",
            sScrollY: "" + ($(document).height() - (4 * $("#pagetitle").height()) -  (3 * $("#titlebar").height()) ) + "px"
        });
    }

});


