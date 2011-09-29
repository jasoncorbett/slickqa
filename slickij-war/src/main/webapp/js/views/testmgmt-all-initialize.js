/*
 * ---------------------------------------------------------------------------
 * testmgmt-all-initialize.js
 * ---------------------------------------------------------------------------
 */

TestMgmtView = function(){
	for(view in TestMgmtView.views) {
		this[view] = TestMgmtView.views[view];
	}
};

TestMgmtView.views = {};

TestMgmtView._instance = false;
TestMgmtView.instance = function() {
/*	$(".groupselected").removeClass("groupselected");
	$("#actiongroup-testmgmt").addClass("groupselected");
	$("#testmgmt-pane").show();
*/
	if(!TestMgmtView._instance) {
		TestMgmtView._instance = new TestMgmtView();
	}
	return TestMgmtView._instance;
};

addNavigationMenu({"group": "Manage Tests", "action": "View Current Project", "url": "#/testmgmt/viewproject"});
Routes["/testmgmt/viewproject"] = comingSoon;
addNavigationMenu({"group": "Manage Tests", "action": "Add Release", "url": "#/testmgmt/addrelease"});
Routes["/testmgmt/addrelease"] = comingSoon;
addNavigationMenu({"group": "Manage Tests", "action": "Add Build", "url": "#/testmgmt/addbuild"});
Routes["/testmgmt/addbuild"] = comingSoon;
addNavigationMenu({"group": "Manage Tests", "action": "Add Component", "url": "#/testmgmt/addcomponent"});
Routes["/testmgmt/addcomponent"] = comingSoon;
addNavigationMenu({"group": "Manage Tests", "action": "Add Test Case", "url": "#/testmgmt/addtest"});
Routes["/testmgmt/addtest"] = comingSoon;
addNavigationMenu({"group": "Manage Tests", "action": "Find Tests", "url": "#/testmgmt/findtests"});
Routes["/testmgmt/findtests"] = comingSoon;

