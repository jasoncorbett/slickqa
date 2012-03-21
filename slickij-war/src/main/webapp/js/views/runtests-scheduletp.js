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
            }
        };
        this.on("ready", this.onReady, this);
        this.on("finish", this.onFinish, this);
        this.on("dataRecieved", this.onDataRecieved, this);
    },
    
    // the purpose here is to get the builds for the specified release.  Default is the default release.
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

            // get the builds for the release selected (defaultRelease of the project is the default)
            //http://localhost:8080/api/projects/4f627d8eaf93d0097e2c3334/releases/4f627d8eaf93d009812c3334/builds
            this.addRequiredData("builds", "api/projects/" + data.id + "/releases/" + this.defaultReleaseId + "/builds");

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
      
    },

    onFinish: function() {
        // trigger onReleaseChange whenever the release dropdown changes
        $("#schedule-testplan-release-build-select").val(this.defaultReleaseId);
        
        $("#schedule-testplan-release-build-select").on("change", {page: this}, this.onReleaseChange);
        
        $("#schedule-testplan-form-submit").on("click", {page: this}, this.onScheduleTestPlan);
    },
    
    onReleaseChange: function(event) {
        var page = event.data.page;
        var releaseid = $("#schedule-testplan-release-build-select option:selected").val();
        $.address.value("/runtests/scheduletp?releaseid=" + releaseid);
    },

    onScheduleTestPlan: function(event) {
        $.jGrowl("Scheduling test plan now");

    }
});
