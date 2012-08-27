var TestManagementAddReleasePage = SlickPage.extend({
    name: "Manage Release",
    codename: "managerelease",
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
        var project = this.data.proj;
        this.title = "Add Release to Project '" + project.name + "'";
        var MAX_DATE = new Date();
        MAX_DATE.setTime(8640000000000000);
        this.releases = [];
        //this.releases = [["", '<a href="javascript:;" class="short-button" id="add-release-form-submit">Add New Release</a>', MAX_DATE, ""]];
        this.inactiveReleases = [];
        _.each(project.releases, function(release) {
            // create the arrays that we need to populate the data table.
            if(project.defaultRelease == release.id) {
                this.releases.push([release.id, release.name, new Date(release.target), '<a href="javascript:;" data-release-id="' + release.id + '" data-release-name="' + release.name + '" id="defaultRelease" class="defaultRelease">default</a>']);
            } else {
                this.releases.push([release.id, release.name, new Date(release.target), '<a href="javascript:;" data-release-id="' + release.id + '" data-release-name="' + release.name + '" class="notDefaultRelease">make default</a>']);
            }
        }, this);
        _.each(project.inactiveReleases, function(inactive) {
            this.inactiveReleases.push([inactive.id, inactive.name, new Date(inactive.target), '<a href="javascript:;" data-release-id="' + inactive.id + '" data-release-name="' + inactive.name + '" class="notDefaultRelease">make default</a>']);
        }, this);
    },
    
    onFinish: function() {
        // add the datatable
        var releaseList = $("#active-release-list").dataTable({
            aaData: this.releases,
            aoColumns: [
                {"sTitle": "id", "bVisible": false},
                {"sTitle": "Name", "sWidth": "65%"},
                {"sTitle": "Created", "bVisible": false},
                {"sTitle": "Default", "sWidth": "35%"}],
            bJQueryUI: true,
            bAutoWidth: false,
            bDeferRender: true,
            bPaginate: false,
            sDom: '<"H"lf<"add-release"><"edit-release">r>tS<"F"ip>',
            sScrollY: "" + $("#content").height() - (7 * $("#footer").height()) - $("#content-bottom-pad").height() + "px"
        });
        $("div.add-release").html('<a href="javascript:;" class="short-button" id="add-release">Add New Release</a>');
        $("div.edit-release").html('<a href="javascript:;" class="short-button" id="edit-release">Edit Selected Release</a>')

        var inactiveList = $("#inactive-release-list").dataTable({
            aaData: this.inactiveReleases,
            aoColumns: [
                {"sTitle": "id", "bVisible": false},
                {"sTitle": "Name", "sWidth": "65%"},
                {"sTitle": "Created","bVisible": false},
                {"sTitle": "Default", "sWidth": "35%"}],
            bJQueryUI: true,
            bAutoWidth: false,
            bDeferRender: true,
            bPaginate: false,
            sDom: "<\"H\"lfr>tS<\"F\"ip>",
            sScrollY: "" + $("#content").height() - (7 * $("#footer").height()) - $("#content-bottom-pad").height() + "px"
        });

        releaseList.fnSort([[2, "desc"]]);

        // Setup action based on the add release form submit button
        $("#add-release").on("click", {page: this}, this.onAddRelease);
        $(".notDefaultRelease").on("click", {page: this}, this.onMakeDefault);
        //add actions based on clicking the activate or deactivate buttons
        $("#deactivate-release").on("click", {page: this, activeTable: releaseList, inactiveTable: inactiveList}, this.onDeactivateRelease);
        $("#activate-release").on("click", {page: this, activeTable: releaseList, inactiveTable: inactiveList}, this.onActivateRelease);
        //Make table rows selectable
        $("table").on("click", 'tr', {page: this, table: releaseList}, this.onRowSelected);
        //trying to edit
        $("#edit-release").on("click", {page: this, table: releaseList}, this.onTableCellEdit);
    },

    onTableCellEdit: function(event) {
        $("tr.row-selected td:eq(0)").trigger("edit");
    },

    onRowSelected: function(event) {
        if ( $(this).hasClass('row-selected') ) {
            $(this).removeClass('row-selected');
        }
        else {
            $('tr.row-selected').removeClass('row-selected');
            $(this).addClass('row-selected');
            $('tr.row-selected td:eq(0)').editable(function(value, settings) {
                var releaseId = event.data.table._('tr.row-selected')[0][0];
                $.ajax({
                    type: "PUT",
                    url: "api/projects/" + event.data.page.data.proj.id + "/releases/" + releaseId,
                    dataType: "json",
                    contentType: "application/json",
                    data: '{"name": "' + value + '"}'
                });
                return value;
                },{
                    event: "edit",
                    callback: function( value, settings) {
                        table.fnUpdate(value);
                    }
            })
        }
    },

    onDeactivateRelease: function(event) {
        var page = event.data.page;
        var selectedRow = $("#active-release-list tr.row-selected")[0];
        var rowData = event.data.activeTable.fnGetData(selectedRow);
        if (page.data.proj.defaultRelease == rowData[0]) {
            page.message(rowData[1] + " is the default release. Please make another release the default before deactivating this release.");
        }
        else {
            $.getJSON("api/projects/" + page.data.proj.id + "/releases/" + rowData[0] + "/deactivate",
                function() {
                    page.message("Deactivated release '" + rowData[1] + "'.");
                    event.data.inactiveTable.fnAddData(rowData);
                    event.data.activeTable.fnDeleteRow(selectedRow);
            });
        }
    },

    onActivateRelease: function(event) {
        var page = event.data.page;
        var selectedRow = $("#inactive-release-list tr.row-selected")[0];
        var rowData = event.data.inactiveTable.fnGetData(selectedRow);
        $.getJSON("api/projects/" + page.data.proj.id + "/inactivereleases/" + rowData[0] + "/activate",
            function() {
                page.message("Activated release '" + rowData[1] + "'.");
                event.data.activeTable.fnAddData(rowData);
                event.data.inactiveTable.fnDeleteRow(selectedRow);
            });
    },

    onAddRelease: function(event) {
        // the context object is not the page object!  A Jquery Event handler has the element that caused the event as the context (this).
        var page = event.data.page;
        var release = {name: 'Release name'};
        $.ajax({
            type: "POST",
            url: "api/projects/" + page.data.proj.id + "/releases",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(release),
            success: function(data) {
                page.message("Added release '" + release.name + "' to project '" + page.data.proj.name + "'.");
                // Adding a blank release. This will make it so we always get a valid new release
                $("#active-release-list").dataTable().fnAddData([data.id, data.name, new Date(), '<a href="javascript:;" data-release-id="' + data.id + '" data-release-name="' + data.name + '" class="notDefaultRelease">make default</a>']);
            },
            error: function() {
                page.error("Unable to add release.");
            }
        });
    },

    onMakeDefault: function(event) {
        var page = event.data.page;
        var release = {name: $(this).data("release-name"), id: $(this).data("release-id")};
        $.ajax({
            type: "GET",
            url: "api/projects/" + page.data.proj.id + "/setdefaultrelease/" + release.id,
            dataType: "json",
            success: function() {
                page.message("Release '" + release.name + "' set as the default release.");
                window.onPageChange();
            },
            error: function() {
                page.error("Unable to make release '" + release.name + "' the default.");
            }
        });
    }
});
