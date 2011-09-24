/* loaded after all other javascript */

$(function() {
	grabTemplate("main-navigation-group", function(grouptemplate) {
		grabTemplate("main-navigation-groupactions", function(groupactionstemplate) {
			grabTemplate("main-navigation-action", function(actiontemplate) {
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
			});
		});
	});
});
