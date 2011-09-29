/*
 * ---------------------------------------------------------------------------
 * dashboard-view-main.js
 * ---------------------------------------------------------------------------
 */
addNavigationMenu({"group": "Dashboards", "action": "Main Dashboard", "url": "#/dashboards/main"});
Routes["/dashboards/main"] = "DashboardsView#main";

DashboardsView.views.main = function() {
	grabTemplate("dashboard-view-main", function(template) {
		setSlickTitle("Main Dashboard");
		$.tmpl(template, Slick.project.getCurrentProject()).appendTo("#main");
		$("#dashboard-view-main-project-name").addClass("slick-event-current-project-change").bind("slick-data-current-project-change", function(evt, project) {
			$(this).text(project.name);
		});
	});
}


