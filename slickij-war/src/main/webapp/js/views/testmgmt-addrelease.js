var TestManagementAddReleasePage = SlickPage.extend({
    name: "Add Release",
    codename: "addrelease",
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
        this.title = "Add Release to Project '" + this.data.proj.name + "'";
        this.releases = [];
        _.each(this.data.proj.releases, function(release) {
            // create the arrays that we need to populate the data table.
            if(this.data.proj.defaultRelease == release.id) {
                this.releases[this.releases.length] = [release.name, new Date(release.target), '<a href="javascript:false;" data-release-id="' + release.id + '" data-release-name="' + release.name + '" id="defaultRelease" class="defaultRelease">default</a>'];
            } else {
                this.releases[this.releases.length] = [release.name, new Date(release.target), '<a href="javascript:false;" data-release-id="' + release.id + '" data-release-name="' + release.name + '" class="notDefaultRelease">make default</a>'];
            }
        }, this);
    },
    
    onFinish: function() {
        // make the box the right width
        $("#add-release-list-box").css("float", "left");
        $("#add-release-list-box").css("margin-left", $("#add-release-box").css("margin-left"));
        $("#add-release-list-box").width($(document).width() - (1.2 * $("#add-release-box").width()));

        // add the datatable
        var releaseList = $("#add-release-list").dataTable({
            aaData: this.releases,
            aoColumns: [
                {"sTitle": "Name", "sWidth": "65%"},
                {"sTitle": "Target", "bVisible": false},
                {"sTitle": "Default", "sWidth": "35%"}],
            bJQueryUI: true,
            bAutoWidth: false,
            bDeferRender: true,
            bPaginate: false,
            sDom: "<\"H\"lfr>tS<\"F\"ip>",
            sScrollY: "" + ($("#add-release-box").height() - ($("#add-release-box-name").height() + $("#add-release-box-eplanation").height()))
        });

        releaseList.fnSort([[1, "desc"]]);

        // Setup action based on the add release form submit button
        $("#add-release-form-submit").on("click", { page: this }, this.onAddRelease);
        $(".notDefaultRelease").on("click", { page: this }, this.onMakeDefault);
    },

    onAddRelease: function(event) {
        // the context object is not the page object!  A Jquery Event handler has the element that caused the event as the context (this).
        var page = event.data.page;
        var release = {name: $("#add-release-form-name").val()};
        $.ajax({
            type: "POST",
            url: "api/projects/" + page.data.proj.id + "/releases",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(release),
            success: function(data) {
                if($("#add-release-form-isdefault").is(":checked")) {
                    $.ajax({
                        type: "GET",
                        url: "api/projects/" + page.data.proj.id + "/setdefaultrelease/" + data.id,
                        dataType: "json",
                        success: function() {
                            page.message("Added release '" + release.name + "' to project '" + page.data.proj.name + "' as default release.");
                            window.onPageChange(); // reload page
                        },
                        error: function() {
                            page.error("Setting release as default failed, but release added successfully.");
                            window.onPageChange(); // reload page
                        }
                    });
                } else {
                    page.message("Added release '" + release.name + "' to project '" + page.data.proj.name + "'.");
                    window.onPageChange(); // reload page
                }
            },
            error: function() {
                page.error("Unable to add release.");
                window.onPageChange(); // reload page
            }
        });
    },

    onMakeDefault: function(event) {
        var page = event.data.page;
        var release = { name: $(this).data("release-name"), id: $(this).data("release-id")};
        $.ajax({
            type: "GET",
            url: "api/projects/" + page.data.proj.id + "/setdefaultrelease/" + release.id,
            dataType: "json",
            success: function() {
                page.message("Release '" + release.name + "' set as the default release.");
                window.onPageChange(); // reload page to show the difference
            },
            error: function() {
                page.error("Unable to make release '" + release.name + "' the default.");
                window.onPageChange(); // just in case it did work, or something changed
            }
        });
    }
});
