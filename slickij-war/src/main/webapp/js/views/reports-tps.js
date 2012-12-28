/**
 * Created with IntelliJ IDEA.
 * User: jcorbett
 * Date: 7/5/12
 * Time: 11:11 PM
 */

var TPSReportPage = SlickPage.extend({
    name: "Test Plan Summary",
    codename: "tps",
    group: "reports",
    navigation: true,


    initialize: function() {
        if(!_.has(this.options.query, "testplanid")) {
            this.primaryTemplateName = "reports-tps-choosetp.html";
            this.requiredData = {
                "testplans": "api/testplans?projectid=" + getCurrentProject().id,
                "environments": "api/configurations?configurationType=ENVIRONMENT"
            };
            this.on("finish", this.onChooseFinish, this);
        } else {
            this.primaryTemplateName = "reports-tps-report.html";
            var testrunUrl = "api/testruns?testplanid=" + this.options.query.testplanid + "&limit=10";
            if(_.has(this.options.query, "environmentid")) {
                testrunUrl = testrunUrl + "&configid=" + this.options.query.environmentid;
            }
            this.requiredData = {
                "theruns": testrunUrl
            };

            this.primaryTemplateName = "reports-tps-report.html";
            this.on("ready", this.onReportReady, this);
            this.on("finish", this.onReportFinish, this);
        }
    },

    onChooseFinish: function() {
        var page=this;
        $("#tps-report-generate-button").on("click", function() {
            var url = "reports/tps?testplanid=" + $("#tps-report-testplan-select :selected").val();
            var environmentid = $("#tps-report-environment-select :selected").val();

            if (environmentid != "none")
            {
                url = url + "&environmentid=" + environmentid;
            }
            $.address.value(url);
        });
    },

    onReportReady: function() {
        this.title = "Testplan Summary for " + this.data.theruns[0].testplan.name;
        this.data.theruns = this.data.theruns.reverse();
        this.dataArray = [["Date", "TOTAL", "PASSED", "FAILED", "BROKEN", "NOT TESTED", "SKIPPED"]];
        this.dataTableArray = [];
        this.statusColors = [$("#main").css('color')];
        _.each(this.data.theruns, function(testrun) {
            _.each(["PASS", "FAIL", "BROKEN_TEST", "NOT_TESTED", "SKIPPED"], function(result) {
                if(!testrun.summary.resultsByStatus[result]) {
                    testrun.summary.resultsByStatus[result] = 0;
                }
                var statusColorElement = $('<div class="result-status-' + result.replace("_", "") + '" style="display: none" />').appendTo("#main");
                this.statusColors[this.statusColors.length] = statusColorElement.css('color');
                statusColorElement.remove();
            }, this);

            this.dataArray.push([
                (new Date(testrun.dateCreated)).toDateString(),
                testrun.summary.total,
                testrun.summary.resultsByStatus.PASS,
                testrun.summary.resultsByStatus.FAIL,
                testrun.summary.resultsByStatus.BROKEN_TEST,
                testrun.summary.resultsByStatus.NOT_TESTED,
                testrun.summary.resultsByStatus.SKIPPED
            ]);
            this.dataTableArray.push([
                (new Date(testrun.dateCreated)).toDateString(),
                "<a href='#/reports/testrunsummary/" + testrun.id + "'>" + (new Date(testrun.dateCreated)).toDateString() + "</a>",
                testrun.summary.total,
                testrun.summary.resultsByStatus.PASS,
                testrun.summary.resultsByStatus.FAIL,
                testrun.summary.resultsByStatus.BROKEN_TEST,
                testrun.summary.resultsByStatus.NOT_TESTED,
                testrun.summary.resultsByStatus.SKIPPED
            ]);

        }, this);

    },

    onReportFinish: function() {
        var chart = new google.visualization.LineChart(document.getElementById("tps-report-line-chart"));
        chart.draw(google.visualization.arrayToDataTable(this.dataArray), {
            backgroundColor: $("#main").css('background-color'),
            legendTextStyle: {
                color: $("#main").css('color')
            },
            colors: this.statusColors,
            vAxis: {
                baselineColor: $("#main").css('color'),
                textStyle: {
                    color: $("#main").css('color')
                }
            },
            hAxis: {
                textStyle: {
                    color: $("#main").css('color')
                }
            }
        });

        var datatable = $("#tps-report-data-table").dataTable({
            aaData: this.dataTableArray,
            aoColumns: [
                {"sTitle": "Real Date", "bVisible": false},
                {"sTitle": "Date", "sWidth": "40%", "sType": "html", "sClass": "text-left"},
                {"sTitle": "TOTAL", "sWidth": "10%", "sClass": "center"},
                {"sTitle": "PASSED", "sWidth": "10%", "sClass": "center"},
                {"sTitle": "FAILED", "sWidth": "10%", "sClass": "center"},
                {"sTitle": "BROKEN", "sWidth": "10%", "sClass": "center"},
                {"sTitle": "NOT TESTED", "sWidth": "10%", "sClass": "center"},
                {"sTitle": "SKIPPED", "sWidth": "10%", "sClass": "center"}
            ],
            bJQueryUI: true,
            bAutoWidth: false,
            bDeferRender: true,
            bPaginate: false,
            sDom: '<"H"lfrT<"clear">>tS<"F"ip>',
            sScrollY: "2in",
            oTableTools: {"sSwfPath": "http://www.datatables.net/release-datatables/extras/TableTools/media/swf/copy_csv_xls_pdf.swf"}
        });

    }

});
