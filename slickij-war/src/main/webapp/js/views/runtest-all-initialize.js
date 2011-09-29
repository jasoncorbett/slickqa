/*
 * ---------------------------------------------------------------------------
 * runtest-all-initialize.js
 * ---------------------------------------------------------------------------
 */

RunTestsView = function(){
	for(view in RunTestsView.views) {
		this[view] = RunTestsView.views[view];
	}
};

RunTestsView.views = {};

RunTestsView._instance = false;
RunTestsView.instance = function() {
/*	$(".groupselected").removeClass("groupselected");
	$("#actiongroup-runtests").addClass("groupselected");
	$("#runtests-pane").show();
*/
	if(!RunTestsView._instance) {
		RunTestsView._instance = new RunTestsView();
	}
	return RunTestsView._instance;
};

addNavigationMenu({"group": "Run Tests", "action": "Add Test Plan", "url": "#/runtests/addtp"});
Routes["/runtests/addtp"] = comingSoon;
addNavigationMenu({"group": "Run Tests", "action": "Find Test Plans", "url": "#/runtests/findtp"});
Routes["/runtests/findtp"] = comingSoon;
addNavigationMenu({"group": "Run Tests", "action": "Machine Status", "url": "#/runtests/machinestatus"});
Routes["/runtests/machinestatus"] = comingSoon;
addNavigationMenu({"group": "Run Tests", "action": "Enter Manual Results", "url": "#/runtests/manualresults"});
Routes["/runtests/manualresults"] = comingSoon;


