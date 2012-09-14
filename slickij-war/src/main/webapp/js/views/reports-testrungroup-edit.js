/**
 * Created with IntelliJ IDEA.
 * User: jcorbett
 * Date: 9/4/12
 * Time: 9:47 AM
 */

var ReportsTestrunGroupEditPage = SlickPage.extend({
    group: "reports",
    codename: "testrungroup-edit",
    name: "Edit Test Run Group",


    initialize: function() {
        this.on("ready", this.onReady, this);
        this.on("finish", this.onFinish, this);
        this.on("dataRecieved", this.onDataRecieved, this);
        this.on("reload", this.onReload, this);

        this.projectid = getCurrentProject().id;
        this.noProject = false;
        if (this.options.query['projectid']) {
            this.projectid = this.options.query['projectid'];
            if (this.projectid == "null") {
                this.noProject = true;
            }
        }

        this.requiredData = {
            testrungroup: "api/testrungroups/" + this.options.positional[0],
            projects: "api/projects"
        };
    },

    onReload: function() {
        delete this.projectid;
        delete this.project;
        delete this.releases;
        delete this.release;
        delete this.noProject;
        delete this.noRelease;
    },

    onDataRecieved: function(event) {
        var key = event[0];
        var data = event[1];

        if(key == 'projects') {
            _.each(this.data.projects, function(project) {
                if(project.id == this.projectid) {
                    this.project = project;
                    project.selectedProject = true;
                    this.releases = project.releases;
                    var releaseid = project.defaultRelease;
                    this.noRelease = false;
                    if (this.options.query['releaseid']) {
                        releaseid = this.options.query.releaseid;
                        if (releaseid == 'null') {
                            this.noRelease = true;
                        }
                    }
                    _.each(this.releases, function(release) {
                        if(releaseid == release.id) {
                            release.selectedRelease = true;
                            this.release = release;
                        }
                    }, this);
                }
            }, this);
            var url = "api/testruns";
            var added_questionmark = false;
            if (this.project) {
                if (!added_questionmark) {
                    url = url + "?";
                    added_questionmark = true;
                }
                url = url + "projectid=" + this.project.id;
            }

            if (this.release) {
                if(added_questionmark) {
                    url = url + "&";
                } else {
                    url = url + "?";
                }
                url = url + "releaseid=" + this.release.id;
            }

            this.addRequiredData("testruns", url);

        }

    },

    onReady: function() {
        this.title = "Edit Testrun Group: " + this.data.testrungroup.name;

        // create data for included testruns
        var tbldata = [];
        var includedids = [];
        _.each(this.data.testrungroup.testruns, function(testrun) {
            includedids[includedids.length] = testrun.id;
            var resultBar = "";
            _.each(testrun.summary.statusListOrdered, function(statusName) {
                resultBar = resultBar + "<div class=\"result-bar-inner status-background-" + statusName.replace("_", "") + "\" style=\"width: " + (testrun.summary.resultsByStatus[statusName] / testrun.summary.total).toFixed(2) + "in;\"></div>";
            });

            tbldata[tbldata.length] = [
                "<a href=\"#/reports/testrunsummary/" + testrun.id + "\">" + testrun.name + "</a>",
                "<div class=\"result-bar center-block\">" + resultBar + "</div>",
                safeReference(testrun, "summary.total", "?"),
                "<a href=\"javascript:;\" class=\"button remove-testrun\" data-testrunid=\"" + testrun.id + "\">Remove</a>",
                safeReference(testrun, "config.name", "Unknown Environment"),
                safeReference(testrun, "release.name", "Unknown Release") + " Build " + safeReference(testrun, "build.name", "Unknown"),
                new Date(testrun.dateCreated)
            ];
        }, this);
        this.includedRuns = tbldata;

        // create data for potential testruns
        tbldata = [];
        _.each(this.data.testruns, function(testrun) {
            if(!_.include(includedids, testrun.id)) {
                var resultBar = "";
                _.each(testrun.summary.statusListOrdered, function(statusName) {
                    resultBar = resultBar + "<div class=\"result-bar-inner status-background-" + statusName.replace("_", "") + "\" style=\"width: " + (testrun.summary.resultsByStatus[statusName] / testrun.summary.total).toFixed(2) + "in;\"></div>";
                });

                tbldata[tbldata.length] = [
                    "<a href=\"#/reports/testrunsummary/" + testrun.id + "\">" + testrun.name + "</a>",
                    "<div class=\"result-bar center-block\">" + resultBar + "</div>",
                    safeReference(testrun, "summary.total", "?"),
                    "<a href=\"javascript:;\" class=\"button add-testrun\" data-testrunid=\"" + testrun.id + "\">Add</a>",
                    safeReference(testrun, "config.name", "Unknown Environment"),
                    safeReference(testrun, "release.name", "Unknown Release") + " Build " + safeReference(testrun, "build.name", "Unknown"),
                    new Date(testrun.dateCreated)
                ];
            }
        });
        this.potentialRuns = tbldata;

    },

    onFinish: function() {
        $("#testrungroup-edit-page").height("100%");
        var includedTable = $("#testrungroup-edit-included").dataTable({
            aaData: this.includedRuns,
            aoColumns: [
                {"sTitle": "Name", "sWidth": "40%", "sType": "html"},
                {"sTitle": "Results", "sWidth": "10%", "sType": "html", "sClass": "center"},
                {"sTitle": "Total", "sWidth": "5%", "sClass": "center"},
                {"sTitle": "Actions", "sWidth": "5%", "sClass": "center"},
                {"sTitle": "Environment", "sWidth": "10%", "sClass": "center"},
                {"sTitle": "Build", "sWidth": "15%", "sClass": "center"},
                {"sTitle": "Date Created", "sWidth": "15%", "sClass": "center"}],
            bJQueryUI: true,
            bAutoWidth: false,
            bDeferRender: true,
            bPaginate: false,
            sDom: "<\"H\"lfr>tS<\"F\"ip>",
            sScrollY: "" + Math.floor(($("#testrungroup-edit-page").height() - ($("#testrungroup-edit-selects").height())) *.2) + "px"
        });

        var potentialTable = $("#testrungroup-edit-potential").dataTable({
            aaData: this.potentialRuns,
            aoColumns: [
                {"sTitle": "Name", "sWidth": "40%", "sType": "html"},
                {"sTitle": "Results", "sWidth": "10%", "sType": "html", "sClass": "center"},
                {"sTitle": "Total", "sWidth": "5%", "sClass": "center"},
                {"sTitle": "Actions", "sWidth": "5%", "sClass": "center"},
                {"sTitle": "Environment", "sWidth": "10%", "sClass": "center"},
                {"sTitle": "Build", "sWidth": "15%", "sClass": "center"},
                {"sTitle": "Date Created", "sWidth": "15%", "sClass": "center"}],
            bJQueryUI: true,
            bAutoWidth: false,
            bDeferRender: true,
            bPaginate: false,
            sDom: "<\"H\"lfr>tS<\"F\"ip>",
            sScrollY: "" + Math.floor(($("#testrungroup-edit-page").height() - ($("#testrungroup-edit-selects").height())) *.4) + "px"
        });

        $("#testrungroup-edit-project-select").on("change", {page: this}, this.onProjectSelectChange);
        $("#testrungroup-edit-release-select").on("change", {page: this}, this.onReleaseSelectChange);
        $("#testrungroup-edit-included").on("click", ".remove-testrun", {page: this}, this.onRemoveTestrun);
        $("#testrungroup-edit-potential").on("click", ".add-testrun", {page: this}, this.onAddTestrun);
    },

    onProjectSelectChange: function(event) {
        var proj_id = $("#testrungroup-edit-project-select :selected").val();
        var page = event.data.page;
        page.options.query.projectid = proj_id;
        delete page.options.query.releaseid;
        page.reload();
    },

    onReleaseSelectChange: function(event) {
        var proj_id = $("#testrungroup-edit-project-select :selected").val();
        var release_id = $("#testrungroup-edit-release-select :selected").val();
        var page = event.data.page;
        page.options.query.projectid = proj_id;
        page.options.query.releaseid = release_id;
        page.reload();
    },

    onAddTestrun: function(event) {
        var page = event.data.page;
        var testrunid = $(event.target).data("testrunid");
        var testrungroupid = page.options.positional[0];

        $.ajax({
            type: "POST",
            url: "api/testrungroups/" + testrungroupid + "/addtestrun/" + testrunid,
            success: function() {
                page.reload();
            },
            error: function() {
                page.error("Unable to add testrun with id '" + testrunid + "' to testrungroup with id '" + testrungroupid + "'.");
            }
        });

    },

    onRemoveTestrun: function(event) {
        var page = event.data.page;
        var testrunid = $(event.target).data("testrunid");
        var testrungroupid = page.options.positional[0];

        $.ajax({
            type: "DELETE",
            url: "api/testrungroups/" + testrungroupid + "/removetestrun/" + testrunid,
            success: function() {
                page.reload();
            },
            error: function() {
                page.error("Unable to remove testrun with id '" + testrunid + "' from testrungroup with id '" + testrungroupid + "'.");
            }
        });

    }
});
