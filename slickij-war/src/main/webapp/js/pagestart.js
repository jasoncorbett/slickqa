/* loaded after all other javascript */

$(function() {
	grabTemplate("main-navigation-group", function(grouptemplate) {
		grabTemplate("main-navigation-groupactions", function(groupactionstemplate) {
			grabTemplate("main-navigation-action", function(actiontemplate) {
				for(igroup in Pages.groups)
				{
					var group = Pages.groups[igroup];
					if(group.isVisible)
					{
						var groupid = group.name;
						var groupmenu = $("#actiongroup-" + groupid);
						var groupmenu = $("#actiongroup-" + groupid);
						var groupactions = $("#" + groupid + "-pane");
						if(!groupmenu.length) {
							groupmenu = $.tmpl(grouptemplate, {groupid: groupid, group: group.displayName}).appendTo("#mainnavigation");
						}
						if(!groupactions.length) {
							groupactions = $.tmpl(groupactionstemplate, {groupid: groupid, group: group.displayName}).appendTo("#actions");
							for(ipage in group.pages)
							{
								page = group.pages[ipage];
								if(page.isAction)
								{
									var icon = "action-" + group.name + "-" + page.name;
									var actionlink = $.tmpl(actiontemplate, {url: "#/" + group.name + "/" + page.name, icon: icon, displayName: page.displayName}).appendTo(groupactions);
								}
							}
						}
						
					}
				}
				var onPageChange = function() {
					$("#main").html("");
					$("#main-loading").show();
					$(".actions").hide(250);
					$(".groupselected").removeClass("groupselected");
					setSlickTitle(" ");
					Pages.currentPage = null;
					Pages.getCurrentPage(); // this kicks it all off
				}
				$.address.change(onPageChange);

				// Load the projects, needs to be done before execution of the page continues.
				grabTemplate("main-project-dropdown", function(template) {
					$.ajax({
						url: "api/projects",
						type: "GET",
						dataType: "json",
						success: function(data) {
							for(i in data)
							{
								var project = data[i];
								var projElement = $.tmpl(template, project).appendTo("#current-project-select");
								projElement.data("project", project);
							}
							window.getCurrentProject = function() {
								return $("#current-project-select option:selected").data("project");
							}
							$("#current-project-select").change(function(evt) {
								$(".slick-event-current-project-change").trigger("slick-data-current-project-change", getCurrentProject());
							});
							onPageChange();
						}
					});
				});

				/*
				for(i in MainNavigation)
				{
					var registration = MainNavigation[i];

					var groupid = registration.group.toLowerCase();
					groupid = groupid.replace(" ", "");
					registration["groupid"] = groupid;

					var icon = registration.url;
					icon = icon.replace(/^#\//, "");
					icon = icon.replace("/", "-");
					icon = "action-" + icon;
					registration["icon"] = icon;

					var groupmenu = $("#actiongroup-" + registration.groupid);
					var groupactions = $("#" + registration.groupid + "-pane");
					if(!groupmenu.length) {
						groupmenu = $.tmpl(grouptemplate, registration).appendTo("#mainnavigation");
					}
					if(!groupactions.length) {
						groupactions = $.tmpl(groupactionstemplate, registration).appendTo("#actions");
					}
					var actionlink = $.tmpl(actiontemplate, registration).appendTo(groupactions);
				}
				$.routes.dispatcher = function(callback,params,path){
					$("#main").html("");
					$(".actions").hide(250);
					$(".groupselected").removeClass("groupselected");
					setSlickTitle(" ");
					callback(params);
				};
				$.routes(Routes, true);
				*/
			});
		});
	});
});
