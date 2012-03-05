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
        this.on("finish", this.onFinish, this);
    },

    onReady: function() {
        this.defaultRelease = this.data.proj.defaultRelease;
        if(this.options.positional && this.options.positional[0]) {
            this.defaultRelease = this.options.positional[0];
        }

        _.each(this.data.proj.releases, function(release) {
            if(release.id == this.defaultRelease) {
                release.defaultRelease = true;
                this.defaultReleaseObj = release;
            }
        }, this);

        // prepare the builds array needed for the datatable
        this.builds = [];
        _.each(this.defaultReleaseObj.builds, function(build) {
            if(this.defaultReleaseObj.defaultBuild == build.id) {
                this.builds[this.builds.length] = [build.name, new Date(build.built), '<a href="javascript:;" data-build-id="' + build.id + '" data-build-name="' + build.name + '" id="defaultBuild" class="defaultBuild">default</a>'];
            } else {
                this.builds[this.builds.length] = [build.name, new Date(build.built), '<a href="javascript:;" data-build-id="' + build.id + '" data-build-name="' + build.name + '" class="notDefaultBuild">make default</a>'];
            }
        }, this);

    },

    onFinish: function() {
        // make the box the right width
        $("#add-build-list-box").css("float", "left");
        $("#add-build-list-box").css("margin-left", $("#add-build-box").css("margin-left"));
        $("#add-build-list-box").width($(document).width() - (1.2 * $("#add-build-box").width()));

        // add the datatable
        var releaseList = $("#add-build-list").dataTable({
            aaData: this.builds,
            aoColumns: [
                {"sTitle": "Name", "sWidth": "65%"},
                {"sTitle": "Built", "bVisible": false},
                {"sTitle": "Default", "sWidth": "35%"}],
            bJQueryUI: true,
            bAutoWidth: false,
            bDeferRender: true,
            bPaginate: false,
            sDom: "<\"H\"lfr>tS<\"F\"ip>",
            sScrollY: "" + ($("#add-build-box").height() - ($("#add-build-box-name").height() + $("#add-build-box-explanation").height()))
        });

        // sort the table by the "built" date by default
        releaseList.fnSort([[1, "desc"]]);

        // Setup action based on the add build form submit button
        $("#add-build-form-submit").on("click", { page: this }, this.onAddBuild);

        // trigger onMokeDefault when one of the "make default" links get's clicked
        $(".notDefaultBuild").on("click", { page: this }, this.onMakeDefault);

        // trigger onReleaseChange whenever the release dropdown changes
        $("#add-build-release-select").on("change", { page: this }, this.onReleaseChange);
    },

    onAddBuild: function(event) {
        // the context object is not the page object!  A Jquery Event handler has the element that caused the event as the context (this).
        var page = event.data.page;
        var build = {name: $("#add-build-form-name").val()};
        $.ajax({
            type: "POST",
            url: "api/projects/" + page.data.proj.id + "/releases/" + page.defaultRelease + "/builds",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(build),
            success: function(data) {
                if($("#add-build-form-isdefault").is(":checked")) {
                    $.ajax({
                        type: "GET",
                        url: "api/projects/" + page.data.proj.id + "/releases/" + page.defaultRelease + "/setdefaultbuild/" + data.id,
                        dataType: "json",
                        success: function() {
                            page.message("Added build '" + build.name + "' to project '" + page.data.proj.name + "' and release '" + page.defaultReleaseObj.name + "' as the default build.");
                            window.onPageChange(); // reload page
                        },
                        error: function() {
                            page.error("Setting build as default failed, but build added successfully.");
                            window.onPageChange(); // reload page
                        }
                    });
                } else {
                    page.message("Added build '" + build.name + "' to project '" + page.data.proj.name + "' and release '" + page.defaultReleaseObj.name + "'.");
                    window.onPageChange(); // reload page
                }
            },
            error: function() {
                page.error("Unable to add build.");
                window.onPageChange(); // reload page
            }
        });

    },

    onMakeDefault: function(event) {
        var page = event.data.page;
        var build = { name: $(this).data("build-name"), id: $(this).data("build-id")};
        $.ajax({
            type: "GET",
            url: "api/projects/" + page.data.proj.id + "/releases/" + page.defaultRelease + "/setdefaultbuild/" + build.id,
            dataType: "json",
            success: function() {
                page.message("Build '" + build.name + "' set as the default build for release '" + page.defaultReleaseObj.name + "'.");
                window.onPageChange(); // reload page to show the difference
            },
            error: function() {
                page.error("Unable to make build '" + build.name + "' the default.");
                window.onPageChange(); // just in case it did work, or something changed
            }
        });
    },

    onReleaseChange: function(event) {
        var page = event.data.page;
        var releaseid = $("#add-build-release-select option:selected").val();
        $.address.value("testmgmt/addbuild/" + releaseid);
    }
});
