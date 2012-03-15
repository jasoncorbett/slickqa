var ReportsViewFilesPage = SlickPage.extend({
    name: "Files For Failed Test Cases",
    codename: "viewfiles",
    group: "reports",
    navigation: false,
    
    initialize: function() {
        if(! this.options.result) {
            this.requiredData = {
                "results": function() {
                    return "api/results/" + this.options.positional[0];
                }
            };
        }
        this.on("ready", this.onReady, this);
        //this.on("finish", this.onFinish, this);
    },

    onReady: function() {
        if(this.options.result) {
            this.result = this.options.result;
        } else {
            this.result = this.data.results;
        }
        this.title = "File List For Testcase: '" + this.result.testcase.name + "'";
    },

/*    onFinish: function() {
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
    } */

});
