/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
	$("#mainnavigation .actiongroup a").live("click", function () {
		var showid="#" + $(this).attr("id") + "-pane";
		if($("#actiongroup-" + $(this).attr("id")).hasClass("groupselected")) {
			$("#actiongroup-" + $(this).attr("id")).removeClass("groupselected");
			$(showid).hide(250);
		} else {
			$(".groupselected").removeClass("groupselected");
			$(".actions").hide();
			$("#actiongroup-" + $(this).attr("id")).addClass("groupselected");
			$(showid).show(250);
		}
		return false;
	});
});

function grabTemplate(templatename, callback) {
	var template = $.template(templatename);
	if(template.length) {
		callback(template);
		return;
	}
	$.ajax({
		url: "templates/" + templatename + ".html",
		dataType: "html",
		success: function(data) {
			callback($.template(templatename, data));
		}
	});
}

function safeReference(obj, ref, def) {
	var context = obj;
	parts = ref.split(".");
	for(i in parts) {
		part = parts[i];
		if(context[part]) {
			context = context[part];
		} else {
			return def;
		}
	}
	return context;
}

var MainNavigation = [];
function addNavigationMenu(registration) {
	MainNavigation[MainNavigation.length] = registration;
}

function setSlickTitle(title) {
	$("head title").text("Slick: " + title);
	$("#pagetitle").text(title);
}

var Routes = {"/": "DashboardsView#main"};

var Slick = {
	cache: {},
	signalEventsForData: function(type, data, processDeleted) {

		if(arguments.length == 2) {
			processDeleted = false;
		}

		if(Slick.cache[type]) {
			var cached = Slick.cache[type];
			if(_.isArray(data)) {
				var updated = [];
				var added = [];
				var deleted = [];
				_.each(data, function(newobj, nindex, nlist) {
					found = false;
					_.each(cached, function(oldobj, objid, olist) {
						if(oldobj.id == newobj.id) {
							found = true;
							if(!_.isEqual(oldobj, newobj)) {
								updated[updated.length] = newobj;
							}
						}
					});
					if(!found){
						added[added.length] = newobj;
					}
				});

				_.each(updated, function(update, index, list) {
					$(".slick-event-" + type + "-update-" + update.id).trigger("slick-data-" + type + "-update-" + update.id, update);
					Slick.cache[type][update.id] = update;
				});

				_.each(added, function(addedobj, index, list) {
					$(".slick-event-" + type + "-add").trigger("slick-data-" + type + "-add", addedobj);
					Slick.cache[type][addedobj.id] = addedobj;
				});

				if (processDeleted) {
					_.each(cached, function(oldobj, objid, olist) {
						var found = false;
						for(newindex in data)
						{
							var newobj = data[newindex];
							if(newobj.id == oldobj.id)
							{
								found = true;
								break;
							}
						}
						if(!found)
						{
							deleted[deleted.length] = oldobj;
						}
					});

					_.each(deleted, function(delobj, index, list) {
						$(".slick-event-" + type + "-delete-" + delobj.id).trigger("slick-data-" + type + "-delete-" + delobj.id, delobj);
						delete Slick.cache[type][delobj.id];
					});
					
				}
			} else {
				if(_.indexOf(_.keys(cached), data.id) > 0)
				{
					var cachedobj = cached[data.id];
					if(!_.isEqual(cachedobj, data)) {
						$(".slick-event-" + type + "-update-" + cachedobj.id).trigger("slick-data-" + type + "-update-" + cachedobj.id, data);
						Slick.cache[type][data.id] = data;
					}
				} else {
					$(".slick-event-" + type + "-add").trigger("slick-data-" + type + "-add", data);
					Slick.cache[type][data.id] = data;
				}
			}
		} else {
			$(".slick-event-" + type + "-load").trigger("slick-data-" + type + "-load", [data]);
			Slick.cache[type] = {};
			if(_.isArray(data)) {
				_.each(data, function(obj, index, list) {
					Slick.cache[type][obj.id] = obj;
				});
			} else
			{
				Slick.cache[type][data.id] = data;
			}
		}
	},
	project: {
		getAll: function() {
			$.ajax({
				url: "api/projects",
				type: "GET",
				dataType: "json",
				success: function(projects) {
					Slick.signalEventsForData("project", projects, true);
				}
			});
		}
	},
	result: {
		getResultsForTestRun: function(testrunid, options) {
			delete Slick.cache.result;
			var url = "api/results?testrunid=" + testrunid;
			if(options)
			{
				_.each(options, function(value, key, obj) {
					url = url + "&" + key + "=" + value;
				});
			}
			$.ajax({
				url: url,
				type: "GET",
				dataType: "json",
				success: function(results) {
					Slick.signalEventsForData("result", results, true);
				}
			});
		}
	},
	testplan: {
		getTestplan: function(testplanid) {
			delete Slick.cache.testplan;
			var url = "api/testplans/" + testplanid;

			$.ajax({
				url: url,
				type: "GET",
				dataType: "json",
				success: function(results) {
					Slick.signalEventsForData("testplan", results, true);
				}
			});
		}
	},
	testrun: {
		getTestrun: function(testrunid) {
			delete Slick.cache.testrun;
			var url = "api/testruns/" + testrunid;

			$.ajax({
				url: url,
				type: "GET",
				dataType: "json",
				success: function(results) {
					Slick.signalEventsForData("testrun", results, true);
				}
			});
		},
		getTestrunsForProject: function(projectid, options) {
			delete Slick.cache.testrun;
			var url = "api/testruns?projectid=" + projectid;
			if(options)
			{
				_.each(options, function(value, key, obj) {
					url = url + "&" + key + "=" + value;
				});
			}
			$.ajax({
				url: url,
				type: "GET",
				dataType: "json",
				success: function(results) {
					Slick.signalEventsForData("testrun", results, true);
				}
			});
		},
		getSummary: function(testrunid) {
			delete Slick.cache.testrunsummary;
			var url = "api/testruns/" + testrunid + "/summary";
			$.ajax({
				url: url,
				type: "GET",
				dataType: "json",
				success: function(results) {
					Slick.signalEventsForData("testrunsummary", results, true);
				}
			});
		}
	},
	result: {
		getResultsByTestrun: function(testrunid, options) {
			delete Slick.cache.result;
			var url = "api/results?testrunid=" + testrunid;
			if(options) {
				_.each(options, function(value, key, obj) {
					url = url + "&" + key + "=" + value;
				});
			}
			$.ajax({
				url: url,
				type: "GET",
				dataType: "json",
				success: function(results) {
					Slick.signalEventsForData("result", results, true);
				}
			});
		}
	}
}

