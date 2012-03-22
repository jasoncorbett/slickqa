var RunTestsScheduleTestPlanPage = SlickPage.extend({
    name: "Schedule Test Plan",
    codename: "scheduletp",
    group: "runtests",
    navigation: false,

    initialize: function() {
        this.requiredData = {
            proj: function() {
                return "api/projects/" + getCurrentProject().id;
            },
            config: function() {
                return "api/configurations";
            },
            tp: function() {
                return "api/testplans/" + this.options.positional[0];
            },
            testcount: function() {
                return "api/testplans/" + this.options.positional[0] + "/testcount";
            }
        };
        this.on("ready", this.onReady, this);
        this.on("finish", this.onFinish, this);
    },
    
    onReady: function() {
        _.each(this.data.proj.releases, function(release) {
            if(this.data.proj.defaultRelease == release.id) {
                release.isDefaultRelease = true;
            }
        }, this);
      
    },

    onFinish: function() {
        this.onReleaseChange({ data: {page: this}});

        $("#schedule-testplan-release-select").on("change", {page: this}, this.onReleaseChange);
        
        $("#schedule-testplan-form-submit").on("click", {page: this}, this.onScheduleTestPlan);
    },
    
    onReleaseChange: function(event) {
        var page = event.data.page;
        var releaseid = $("#schedule-testplan-release-select option:selected").val();
        var releaseObj = null;

        // find the release object in the project from the id of what was selected.
        _.each(page.data.proj.releases, function(release) {
            if(releaseid == release.id) {
                releaseObj = release;
            }
        });

        // wipe out any existing builds
        $("#schedule-testplan-release-build-select").html("");

        // add the builds from the selected release into the build select drop down.
        _.each(releaseObj.builds, function(build) {
            if(releaseObj.defaultBuild == build.id) {
                $("#schedule-testplan-release-build-select").append("<option value=\"" + build.id + "\" data-name=\"" + build.name + "\" selected>" + build.name + "</option>");
            } else {
                $("#schedule-testplan-release-build-select").append("<option value=\"" + build.id + "\" data-name=\"" + build.name + "\">" + build.name + "</option>");
            }
        })
    },

    onScheduleTestPlan: function(event) {
        var runparameters = {
            config: {
                name: $("#schedule-testplan-environment-select").data("environmentname"),
                configId: $("#schedule-testplan-environment-select").val()
            },
            release: {
                name: $("#schedule-testplan-release-select :selected").data("name"),
                releaseId: $("#schedule-testplan-release-select :selected").val()
            },
            build: {
                name: $("#schedule-testplan-release-build-select :selected").data("name"),
                buildId: $("#schedule-testplan-release-build-select :selected").val()
            }
        };
        if($("#schedule-testplan-config").val()) {
            runparameters.overrides = [
                {
                    isRequirement: false,
                    key: $("#schedule-testplan-config").val(),
                    value: $("#schedule-testplan-config-value").val()
                }
            ]
        }

        $.ajax({
            url: "api/testplans/" + event.data.page.options.positional[0] + "/run",
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(runparameters),
            success: function(data) {
                $.address.value("reports/testrunsummary/" + data.id);
            },
            error: function() {
                //TODO: Actually use the page class error method so that it could potentially be logged.
                $.jGrowl("Unable to schedule test plan.");
            }

        });

    }
});
