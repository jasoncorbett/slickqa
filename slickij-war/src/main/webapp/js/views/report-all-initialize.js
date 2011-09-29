/*
 * ---------------------------------------------------------------------------
 * report-all-initialize.js
 * ---------------------------------------------------------------------------
 */

ReportsView = function(){
	for(view in ReportsView.views) {
		this[view] = ReportsView.views[view];
	}
};

ReportsView.views = {};

ReportsView._instance = false;
ReportsView.instance = function() {
/*	$(".groupselected").removeClass("groupselected");
	$("#actiongroup-reports").addClass("groupselected");
	$("#reports-pane").show();
*/
	if(!ReportsView._instance) {
		ReportsView._instance = new ReportsView();
	}
	return ReportsView._instance;
};

addNavigationMenu({"group": "Reports", "action": "Compare Test Runs", "url": "#/reports/comparetestrun"});
Routes["/reports/comparetestrun"] = comingSoon;
