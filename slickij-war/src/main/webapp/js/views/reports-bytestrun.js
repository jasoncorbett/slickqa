/**
 * Created by IntelliJ IDEA.
 * User: jcorbett
 * Date: 3/4/12
 * Time: 8:58 PM
 */

var ReportsByTestrunPage = SlickPage.extend({
    name: "Testrun Results",
    codename: "bytestrun",
    group: "reports",
    navigation: true,

    initialize: function() {
        this.requiredData = {
            proj: function() {
                return "api/projects/" + getCurrentProject().id;
            }
        };
        this.on("dataRecieved", this.onDataRecieved, this);
        this.on("ready", this.onReady, this);
        this.on("finish", this.onFinish, this);
    },

    // the purpose here is to get the testruns for the specified release.  Default is the default release.
    onDataRecieved: function(event) {
        var key = event[0];
        var data = event[1];
        if(key == "proj") {
            this.defaultReleaseId = data.defaultRelease;
            this.noRelease = false;
            if(this.options.query && this.options.query["releaseid"]) {
                this.defaultReleaseId = this.options.query["releaseid"];
            }

            this.noRelease = this.defaultReleaseId == "null";

            // get the releases for the release selected (defaultRelease of the project is the default)
            this.addRequiredData("testruns", "api/testruns?projectid=" + data.id + "&releaseid=" + this.defaultReleaseId);

            // we could do this step here, or in the onReady
            _.each(data.releases, function(release) {
               if(release.id == this.defaultReleaseId) {
                   release.defaultRelease = true;
                   this.defaultReleaseObj = release;
               }
            }, this);
        }
    },

    onReady: function() {
        setSlickTitle("Report - Testrun Results");
        var tbldata = [];
        _.each(this.data.testruns, function(testrun) {
            var resultBar = "";
            _.each(testrun.summary.statusListOrdered, function(statusName) {
                resultBar = resultBar + "<div class=\"result-bar-inner status-background-" + statusName.replace("_", "") + "\" style=\"width: " + (testrun.summary.resultsByStatus[statusName] / testrun.summary.total).toFixed(2) + "in;\"></div>";
            });


            tbldata[tbldata.length] = [
                "<a href=\"#/reports/testrunsummary/" + testrun.id + "\">" + testrun.name + "</a>",
                "<div class=\"result-bar center-block\">" + resultBar + "</div>",
                safeReference(testrun, "summary.total", "?"),
                safeReference(testrun, "config.name", "Unknown Environment"),
                safeReference(testrun, "release.name", "Unknown Release") + " Build " + safeReference(testrun, "build.name", "Unknown"),
                new Date(testrun.dateCreated)];
        });
        this.testrunData = tbldata;
    },

    onFinish: function() {
        var datatable = $("#trtable").dataTable({
            aaData: this.testrunData,
            aoColumns: [
                {"sTitle": "Name", "sWidth": "40%", "sType": "html"},
                {"sTitle": "Results", "sWidth": "10%", "sType": "html", "sClass": "center"},
                {"sTitle": "Total", "sWidth": "5%", "sClass": "center"},
                {"sTitle": "Environment", "sWidth": "12%", "sClass": "center"},
                {"sTitle": "Build", "sWidth": "15%", "sClass": "center"},
                {"sTitle": "Date Created", "sWidth": "18%", "sClass": "center"}],
            bJQueryUI: true,
            bAutoWidth: false,
            bDeferRender: true,
            bPaginate: false,
            sDom: "<\"H\"lfr>tS<\"F\"ip>",
            sScrollY: "" + ($("#content").height() - (4 * $("#bytestrun-release-select").height()) - (5 * $("#footer").height()) - $("#content-bottom-pad").height()) + "px"
        });
        datatable.fnSort([[5, "desc"]]);

        // trigger onReleaseChange whenever the release dropdown changes
        $("#bytestrun-release-select").on("change", { page: this }, this.onReleaseChange);
    },

    onReleaseChange: function(event) {
        var page = event.data.page;
        var releaseid = $("#bytestrun-release-select option:selected").val();
        $.address.value("reports/bytestrun?releaseid=" + releaseid);
    }


});
