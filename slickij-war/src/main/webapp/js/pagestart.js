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
					$("#main").html("");
					$("#main-loading").show();
					$(".actions").hide(250);
					$(".groupselected").removeClass("groupselected");
					setSlickTitle(" ");
                    var parts = $.address.pathNames();
                    if(parts.length == 0)
                    {
                        parts = ["dashboards", "main"];
                    }
                    if(parts.length >= 2)
                    {
                        if(SlickPage.PageGroups[parts[0]])
                        {
                            var page = SlickPage.PageGroups[parts[0]][parts[1]];
                            window.CurrentPage = new page({positional: parts.slice(2), query: queryParametersToObject()});
                            window.CurrentPage.on("render", function() {
                                $("#main").append(window.CurrentPage.el);
                                $("#main-loading").hide();
                            });
                            window.CurrentPage.pageStart();
                        } else
                        {
                            //TODO: Not Found Page!
                        }
                    } else
                    {
                        //TODO: Not Found Page!
                    }
				};

				// Load the projects, needs to be done before execution of the page continues.
				$.ajax({
					url: "api/projects",
					type: "GET",
					dataType: "json",
					success: function(data) {
                        _.each(data, function(project){
                            $('#current-project-select').append(Handlebars.templates['main-project-dropdown.html'](project));
                            $('#project-select-' + project.id).data('project', project);
                        });
						window.getCurrentProject = function() {
							return $("#current-project-select option:selected").data("project");
						};
                        $.address.change(window.onPageChange);
                        window.onPageChange();
					}
				});
});
