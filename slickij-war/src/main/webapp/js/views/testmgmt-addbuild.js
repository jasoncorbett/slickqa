var TestManagementAddBuildPage = SlickPage.extend({
    name: "Add Build",
    codename: "addbuild",
    group: "testmgmt",
    navigation: true,

    requiredData: {
        "proj": function() {
            return "api/projects/" + getCurrentProject().id;
        }
    },

    initialize: function() {
        this.on("ready", this.onReady, this);
    },

    onReady: function() {
        this.defaultRelease = this.data.proj.defaultRelease;
        if(this.options.positional && this.options.positional[0]) {
            this.defaultRelease = this.options.positional[0];
        }

        _.each(this.data.proj.releases, function(release) {
            if(release.id == this.defaultRelease) {
                release.defaultRelease = true;
            }
        }, this);
    }
});
