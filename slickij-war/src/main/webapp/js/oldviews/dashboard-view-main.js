/*
 * ---------------------------------------------------------------------------
 * dashboard-view-main.js
 * ---------------------------------------------------------------------------
 */
Pages.group("dashboards").page("main").addRequiredTemplate("dashboard-view-main")
                                      .addRequiredData("projects", "api/projects")
									  .addDisplayMethod(function() {
	setSlickTitle("Main Dashboard");
	$.tmpl(this.templates["dashboard-view-main"], getCurrentProject()).appendTo("#main");
	$("#dashboard-view-main-project-name").addClass("slick-event-current-project-change")
	                                      .bind("slick-data-current-project-change", function(evt, project) {
		$(this).text(project.name);
	});
});


