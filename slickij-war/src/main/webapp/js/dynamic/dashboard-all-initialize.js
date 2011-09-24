/*
 * ---------------------------------------------------------------------------
 * dashboard-all-initialize.js
 * ---------------------------------------------------------------------------
 */

DashboardsView = function(){
	for(view in DashboardsView.views) {
		this[view] = DashboardsView.views[view];
	}
};

DashboardsView.views = {};

DashboardsView._instance = false;
DashboardsView.instance = function() {
/*	$(".groupselected").removeClass("groupselected");
	$("#actiongroup-dashboards").addClass("groupselected");
	$("#dashboards-pane").show();
*/
	if(!DashboardsView._instance) {
		DashboardsView._instance = new DashboardsView();
	}
	return DashboardsView._instance;
};


