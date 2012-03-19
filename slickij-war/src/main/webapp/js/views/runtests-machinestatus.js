/*
 * ---------------------------------------------------------------------------
 * runtests-machinestatus.js
 * ---------------------------------------------------------------------------
 */
var RunTestsMachineStatusPage = SlickPage.extend({
    name: "Machine Status",
    codename: "machinestatus",
    group: "runtests",
    navigation: true,
    title: "Automation Machine Pool Status",

    requiredData: {
        "hoststatuses": function() {
            return "api/hoststatus";
        }
    },

    initialize: function() {
        this.on("ready", this.onReady, this);
        this.on("finish", this.onFinish, this); 
    },

    onReady: function() {
    },
    
    onFinish: function() {
        var tabledata = [];
        _.each(this.data.hoststatuses, function(hoststatus) {
           var runTime = "";
           var cancelButton = "";
           if (hoststatus.currentWork !== null)
           {
              runTime = getDuration(safeReference(hoststatus, "secondsSinceLastCheckin", 0));   
              cancelButton = "<button id=\"cancel-" + safeReference(hoststatus, "currentWork.id", "notest") + "\" class=\"cancel-result button\" alt=\"Cancel Result\" title=\"Cancel Result\">Cancel</button>";
           }
           tabledata[tabledata.length] = [safeReference(hoststatus, "hostname", ""),
                                          new Date(safeReference(hoststatus, "lastCheckin", "")),
                                          safeReference(hoststatus, "currentWork.testcase.name", ""),
                                          safeReference(hoststatus, "currentWork.testrun.name", ""),
                                          runTime,
                                          cancelButton,
                                      ];
        });
        $("#host-status-table").dataTable({
            aaData: tabledata,
            aoColumns: [
                {"sTitle": "Host Name", "sWidth": "18%", "sType": "html"},
                {"sTitle": "Last Check-in", "sWidth": "17%"},
                {"sTitle": "Current Test", "sWidth": "24%"},
                {"sTitle": "Current Run", "sWidth": "14%"},
                {"sTitle": "Runtime", "sWidth": "17%"},
                {"sTitle": "Actions", "sWidth": "10%"}],
            bJQueryUI: true,
            bAutoWidth: false,
            bDeferRender: true,
            bPaginate: false,
            sDom: "<\"H\"lfr>tS<\"F\"ip>",
            sScrollY: "" + ($(document).height() - (4 * $("#pagetitle").height()) -  (3 * $("#titlebar").height()) - (2 * $("h2.center").height())) + "px"
        });
        
        $(".cancel-result").on("click", function() {
            var resultid = $(this).attr("id");
            resultid = resultid.replace("cancel-","");
            $.ajax({
                type: "POST",
                url: "api/results/" + resultid + "/cancel",
                dataType: "json",
                contentType: "application/json",
                data: "{reason: 'cancelled'}",
                success: function(data) {
                    $.jGrowl("Successfully Canceled result.");
                    onPageChange();
                }
            });
        });
    }
});
