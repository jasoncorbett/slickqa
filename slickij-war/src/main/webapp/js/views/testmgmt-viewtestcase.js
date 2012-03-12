var TestManagementTestcasePage = SlickPage.extend({
    name: "View Test Case",
    codename: "viewtestcase",
    group: "testmgmt",
    navigation: true,

    requiredData: {
        "testcase": function() {
                //return "api/testcases/" + this.options.positional[0];
                return "api/testcases/" + "4f59261b423eeea0e000ab1c";
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
        console.log(tabledata);

        $("#testcase-detail-table").dataTable({
            aaData: tabledata,
            aoColumns: [
                {"sTitle": "Name", "sWidth": "40%"},
                {"sTitle": "Expected", "sWidth": "60%"}],
            bJQueryUI: true,
            bAutoWidth: false,
            bDeferRender: true,
            bPaginate: false,
            sDom: "<\"H\"lfr>tS<\"F\"ip>",
            sScrollY: "" + ($(document).height() - (4 * $("#pagetitle").height()) -  (3 * $("#titlebar").height()) ) + "px"
        });
    }

});


