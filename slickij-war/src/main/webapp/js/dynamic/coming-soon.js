
function comingSoon(options) {
	var relurl = $.routes("get");
/*	if(relurl.match(/testmgmt/)) {
		$(".groupselected").removeClass("groupselected");
		$("#actiongroup-testmanagement").addClass("groupselected");
		$("#testmanagement-pane").show();
	} else if(relurl.match(/runtests/)) {
		$(".groupselected").removeClass("groupselected");
		$("#actiongroup-runtests").addClass("groupselected");
		$("#runtests-pane").show();
	} else if(relurl.match(/reports/)) {
		$(".groupselected").removeClass("groupselected");
		$("#actiongroup-reports").addClass("groupselected");
		$("#reports-pane").show();
	}
*/
	displayeasteregg = {"display": "none"};
	if(options.easteregg) {
		displayeasteregg.display="inline-block";
	}
	grabTemplate("coming-soon", function(template) {
		setSlickTitle("Coming Soon To Slick Near You");
		$.tmpl(template, displayeasteregg).appendTo("#main");
	});
		
}

