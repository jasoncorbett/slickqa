/*
 * ---------------------------------------------------------------------------
 * testmgmt-view-project.js
 * ---------------------------------------------------------------------------
 */

Pages.group("testmgmt").page("viewproject").addRequiredTemplate("managetests-view-project").addRequiredData("proj", function() {
	return "api/projects/" + getCurrentProject().id;
}).addDisplayMethod(function() {
	var dat = this.data["proj"];
	setSlickTitle("View Project detail: " + safeReference(dat, "name", dat.name));
	$.tmpl(this.templates["managetests-view-project"]).appendTo("#main");
	
	//description box
	$("#project-desc").append(safeReference(dat, "description", dat.description));
	
	//default release box
	$("#project-default-rel").append(safeReference(dat, "defaultRelease", "Default release not set for this project"));
	
	//last updated box
	var lastUpdated = new Date(dat.lastUpdated);
	$("#project-last-updated").append(lastUpdated.toLocaleString());
	
	//display components
	//LIST of components in the project$("#project-para").text
	var comp = dat.components;
	for(i in comp)
	{
		$("#project-components").append("<span class=\"label\">Name: <\span>");
		$("#project-components").append("<span>"+ safeReference(comp[i], "name", "Not Available") + "<\span>");
		$("#project-components").append("<div class=\"clear\"> <\div>");
		
		$("#project-components").append("<span class=\"label\">Description: <\span>");
		$("#project-components").append("<span>"+ safeReference(comp[i], "description", "Not Available") + "<\span>");
		$("#project-components").append("<div class=\"clear\"> <\div>");
		
		$("#project-components").append("<span class=\"label\">Code: <\span>");
		$("#project-components").append("<span>"+ safeReference(comp[i], "code", "Not Available") + "<\span>");
		$("#project-components").append("<div class=\"clear\"> <\div>");
		
		$("#project-components").append("<hr/>");
	}
});

