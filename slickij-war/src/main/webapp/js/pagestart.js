/* loaded after all other javascript */

$(function() {
    // Build the main menu
                var pagenav = [];
                _.each(SlickPage.StandardNavigationGroups, function(group) {
                    if(SlickPage.PageGroups[group.codename]) {
                        var groupobj = {group: group.codename, pages: []};
                        _.each(SlickPage.PageGroups[group.codename], function(pageview, pagename) {
                            if(pageview.prototype['navigation']) {
                                var pageobj = {group: group.codename, name: pagename, displayname: pageview.prototype.name, url: urlOfPage(pageview)};
                                groupobj.pages[groupobj.pages.length] = pageobj;
                            }
                        });
                        pagenav[pagenav.length] = groupobj;
                    }
                });
                $('#pagenav').html(Handlebars.templates['slicknavigation.html']({groups: SlickPage.StandardNavigationGroups, pages: pagenav}));

				window.onPageChange = function() {
                    $(".ui-dialog-content").dialog("close");
					$("#main").html("");
					$("#main-loading").show();
					$(".actions").hide(250);
					$(".groupselected").removeClass("groupselected");
					setSlickTitle(" ");
                    window.CurrentPage = getPageFromUrl('#' + $.address.value());
                    window.CurrentPage.on("render", function() {
                        $("#main").append(window.CurrentPage.el);
                        $("#main-loading").hide();
                    });
                    window.CurrentPage.pageStart();
				};

				// Load the projects, needs to be done before execution of the page continues.
				$.ajax({
					url: "api/projects",
					type: "GET",
					dataType: "json",
					success: function(data) {
                        var default_project = $.cookie('slick_default_project');
                        _.each(data, function(project) {
                            if(default_project && default_project == project.id)
                            {
                                project.isdefault = true;
                            }
                            $('#current-project-select').append(Handlebars.templates['main-project-dropdown.html'](project));
                            $('#project-select-' + project.id).data('project', project);
                        });
						window.getCurrentProject = function() {
							return $("#current-project-select option:selected").data("project");
						};
                        $.address.change(window.onPageChange);
                        window.onPageChange();

                        $("#current-project-select").on("change", function() {
                            $.cookie('slick_default_project', window.getCurrentProject().id);
                            window.onPageChange();
                        })
					}
				});

                $("#main").on("click", "a.modal-link", function(event) {
                    event.preventDefault();

                    // open the linked to page in a dialog instead of in the main window.
                    var urlOfDialog = $(this).attr("href");
                    var page = getPageFromUrl(urlOfDialog, {noSetTitle: true});
                    page.on("render", function() {
                        $("#main").append(this.el);
                        $(this.el).dialog({
                            modal: true,
                            title: "<a href=\"" + urlOfDialog + "\">" + this.getTitle() + "</a>",
                            draggable: false,
                            show: "slide",
                            width: Math.round($(window).width() * .90),
                            height: Math.round($(window).height() * .90)
                        });
                    }, page);
                    page.pageStart();
                    $(page.el).on("dialogclose", function() {
                        $(page.el).html(""); // wipe it out
                    });
                });
});
