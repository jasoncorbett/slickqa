/**
 * Created by IntelliJ IDEA.
 * User: jcorbett
 * Date: 3/8/12
 * Time: 2:33 PM
 */

var ReportsTestRunDetailPage = SlickPage.extend({
    name: "Testrun Result Details",
    codename: "testrundetail",
    group: "reports",

    initialize: function() {
        this.requiredData = {
            testrun: function() {
                return SlickUrlBuilder.testrun.getTestrun(this.options.positional[0]);
            },
            results: function() {
                // we must examine url options before determining which data to load
                if(this.options.query.only) {
                    return SlickUrlBuilder.result.getResultsByTestrun(this.options.positional[0], {"status": this.options.query.only});
                } else if(this.options.query.includepass) {
                    return SlickUrlBuilder.result.getResultsByTestrun(this.options.positional[0]);
                } else {
                    return SlickUrlBuilder.result.getResultsByTestrun(this.options.positional[0], {"excludestatus": "PASS"});
                }
            }
        };
        this.on("dataRecieved", this.onDataRecieved, this);
        this.on("ready", this.onReady, this);
        this.on("finish", this.onFinish, this);
    },

    onDataRecieved: function(event) {
        var key = event[0];
        var value = event[1];
        if(key == "testrun" && value.testplanId) {
            this.addRequiredData("testplan", SlickUrlBuilder.testplan.getTestplan(value.testplanId));
        }

    },

    onReady: function() {
        this.includepass = (this.options.query && this.options.query.includepass);

        this.tbldata = [];
        _.each(this.data.results, function(result) {
            this.tbldata[this.tbldata.length] = [
                "<img src=\"images/reschedule.png\" id=\"reschedule-" + result.id + "\" class=\"reschedule-result\" alt=\"Reschedule Result\" title=\"Reschedule Result\" />",
                "<a id=\"" + result.id + "\" href=\"#/reports/result/" + result.id + "\" class=\"modal-link\">" + safeReference(result, "testcase.name", safeReference(result, "testcase.automationId", "Unknown Test Name")) + "</span>",
                safeReference(result, "component.name", ""),
                new Date(result.recorded),
                getDurationMilliseconds(result.runlength),
                safeReference(result, "testcase.automationId", ""),
                safeReference(result, "reason", ""),
                safeReference(result, "hostname", ""),
                "<span class=\"result-status-" + result["status"].replace("_","") + "\">" + result["status"].replace("_", " ") + "<img class=\"result-status-image\" src=\"images/status-" + result["status"] + ".png\" /></span>"
            ];
        }, this);
    },

    onFinish: function() {
        var datatable = $("#trdetailtable").dataTable({
            aaData: this.tbldata,
            aoColumns: [
                {"sTitle": "Actions", "sWidth": "5%", "sType": "html", "sClass": "center"},
                {"sTitle": "Test Name", "sWidth": "55%", "sType": "html"},
                {"sTitle": "Component", "sWidth": "10%", "sClass": "center"},
                {"sTitle": "Time Reported", "sWidth": "10%", "bVisible": false},
                {"sTitle": "Test Duration", "sWidth": "10%", "sClass": "center"},
                {"sTitle": "Automation ID", "bVisible": false},
                {"sTitle": "Reason", "bVisible": false},
                {"sTitle": "Hostname", "sWidth": "10%", "sClass": "center"},
                {"sTitle": "Result Status", "sWidth": "10%", "sType": "html", "sClass": "center"}],
            bJQueryUI: true,
            bAutoWidth: false,
            bDeferRender: true,
            bPaginate: false,
            sDom: '<"H"lfrT<"clear">>tS<"F"ip>',
            sScrollY: "" + ($(document).height() - (5 * $("#pagetitle").height()) -  (4 * $("#titlebar").height())) + "px",
            oTableTools: {"sSwfPath": "media/swf/copy_cvs_xls_pdf.swf"}
        });
        datatable.fnSort([[3, "asc"]]);


        $(".reschedule-result").on("click", function() {
            $(".tooltip").remove();
            var resultid = $(this).attr("id");
            resultid = resultid.replace("reschedule-","");
            $.ajax({url: "api/results/" + resultid + "/reschedule",
                type: "POST",
                dataType: "json",
                success: function(data) {
                    // TODO: figure out how to change the row
                    $.jGrowl("Successfully rescheduled result.");
                    onPageChange();
                }
            });
        });

    }
});
