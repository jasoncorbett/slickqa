var TestManagementTestcasePage = SlickPage.extend({
    name: "Files For Failed Test Cases",
    codename: "viewfiles",
    group: "testmgmt",
    navigation: false,

    requiredData: {
        "results": function() {
            return "api/results/" + this.options.positional[0];
        }
    },

    initialize: function() {
        this.on("ready", this.onReady, this);
        this.on("finish", this.onFinish, this);
    },

    onReady: function() {
        this.title = "File List For Testcase: '" + this.data.results.testcase.name + "'";
    },

    onFinish: function() {
        var tabledata = [];
        _.each(this.data.results.files, function(files) {
            tabledata[tabledata.length] = [safeReference(files, "filename", ""),
                                           safeReference(files, "mimetype", ""),
                                           safeReference(files, "uploadDate", "")];
        });

        $("#file-detail-table").dataTable({
            aaData: tabledata,
            aoColumns: [
                {"sTitle": "Name", "sWidth": "60%"},
                {"sTitle": "Mimetype", "sWidth": "40%"},
                {"sTitle": "Date", "sWidth": "40%"}],
            bJQueryUI: true,
            bSort: true,
            bAutoWidth: false,
            bDeferRender: true,
            bPaginate: false,
            sDom: "<\"H\"lfr>tS<\"F\"ip>",
            sScrollY: "" + ($(document).height() - (4 * $("#pagetitle").height()) -  (3 * $("#titlebar").height()) ) + "px"
        });
    }

});