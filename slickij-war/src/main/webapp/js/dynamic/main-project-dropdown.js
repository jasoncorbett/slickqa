/* 
 * Handling of loading the dropdown at the top of the page that selects the current project.
 */

$(function() {
	var projSelect = $("#current-project-select");
	var addProjectToList = function(template, project) {
				var projElement = $.tmpl(template, project).appendTo("#current-project-select");
				projElement.data("project", project);
				projElement.bind("slick-data-project-update-" + project.id, function(e, data) {
					projElement.html(data.name);
					projElement.data("project", data);
				});
				projElement.bind("slick-data-project-delete-" + project.id, function(e) {
					projElement.remove();
				});
	}
	projSelect.addClass("slick-event-project-load")
	          .addClass("slick-event-project-add")
	          .bind("slick-data-project-load", function(e, data) {
		grabTemplate("main-project-dropdown", function(template) {
			for(i in data)
			{
				var project = data[i];
				addProjectToList(template, project);
			}
		});
	}).bind("slick-data-project-add", function(e, data) {
		grabTemplate("main-project-dropdown", function(template) {
			addProjectToList(template, data);
		});
	});
	Slick.project.getAll();
	Slick.project.getCurrentProject = function() {
		return $("#current-project-select option:selected").data("project");
	}
	$("#current-project-select").change(function(evt) {
		$(".slick-event-current-project-change").trigger("slick-data-current-project-change", Slick.project.getCurrentProject());
	});
});

