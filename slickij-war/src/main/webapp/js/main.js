/* 
 * main.js
 * Author: Jason Corbett <Jason_Corbett@intuit.com>
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


// Write a replacement for routesjs (using jquery address):
//   Use Pages variable
//   #/group/pagename[/added/to/positional]?query=parameters
//   write a onhashchange function (cleanup)
//   div#main and div#mainloading, main doesn't get displayed till all display functions finished

/**
 * Turn the query parameters into an object mapping.
 * 
 * @return {Object} All the Query String parameters of the location turned into an object name->value mapping.
 */
function queryParametersToObject() {
	var retval = new Object();
	paramNames = $.address.parameterNames();
	for(i in paramNames)
	{
		var param = paramNames[i];
		retval[param] = $.address.parameter(param);
	}
	return retval;
}

/**
 * A Page Definition is a sort of registration and template for a PageInstance.  It contains all the
 * references to functions needed to fetch data and display the page.  PageDefinitions don't actually do
 * the displaying, instead a PageInstance is created by using the newInstance method.
 * 
 * @param {String} name The name of the page.  This parameter is used to identify the page, and it is used in the url after the group name to indicate which page should be loaded.
 * @param {String} displayName The displayable name of the page.  Only used if the isAction is set to true, and the containing group's isVisible is set to true.  Then it will be used as part of the navigation.
 * @param {Boolean} isAction If true, and the containing group's isVisible is set to true then this page should be listed in the navigation.
 */
function PageDefinition(name, displayName, isAction) {
	this.name = name;
	this.displayName = displayName;
	this.isAction = isAction;
	this.requiredData = {};
	this.onDataRecieved = [];
	this.displayMethods = [];
	this.requiredTemplates = [];
	this.plugins = {};
}

/**
 * Create a new PageInstance object from this PageDefinition.  You won't need to call this directly, the
 * getCurrentPage() method of Pages should call this when initializing a new page.
 * 
 * @param {Array} positionalParams The parts of the url after the group and page name, but before any query parameters.
 * @param {Object} queryParams The query parameter string turned into name->value mapping.
 * @return {PageInstance} A new page instance based on this PageDefinition.
 */
PageDefinition.prototype.newInstance = function(positionalParams, queryParams) {
	return new PageInstance(this, positionalParams, queryParams);
}

/**
 * Add a required template to the list of required templates.
 * 
 * @param {String} name The name of the template to be added to the list of prerequisites.
 * @return {PageDefinition} The current page definition object for further chaining of methods.
 */
PageDefinition.prototype.addRequiredTemplate = function(name) {
	this.requiredTemplates.push(name);
	return this;
}

/**
 * Add a display method to the list of methods to be called when this page is loaded.  All
 * required data and required templates are processed first.
 *  
 * @param {Function} func The function to be called when the page loads.  No parameters passed, but this is set to the {PageInstance}.
 * @return {PageDefinition} The current page definition object for further chaining of methods.
 */
PageDefinition.prototype.addDisplayMethod = function(func) {
	this.displayMethods.push(func);
	return this;
}

/**
 * Add required data to the set of data to be fetched before displaying the page.  The name will be used
 * to determine where in the data property of {PageInstance} to insert the required data.  The url can
 * either be a url, or a function which returns a url.  This may be useful if you need to get parameters
 * (such as an id) from the url parameters in order to generate the URL.  If it is a function, this will
 * be set to the {PageInstance} so that the positional and query parameters are available.
 * 
 * @param {String} name The name under the PageInstance to file the resulting data.
 * @param {String} url (Or {Function}) The url, or the function to generate the url from which to get the required data. 
 * @return {PageDefinition} The current page definition object for further chaining of methods.
 */
PageDefinition.prototype.addRequiredData = function(name, url) {
	this.requiredData[name] = url;
	return this;
}

/**
 * Add a plugin to the list of available plugins.  The name should be the plugin 'type', and func
 * should be whatever function or object the plugin requires.  This is page specific and the framework
 * has no idea of what plugins are, or what they do, it just provides storage for them.
 * 
 * @param {String} name The name of the plugin type.
 * @param {Function} func The function / object / constructor that the plugin type requires.
 * @return {PageDefinition} The current page definition object for further chaining of methods.
 */
PageDefinition.prototype.addPlugin = function(name, func) {
	this.plugins[name] = func;
	return this;
}

/**
 * Add a listener for any data recieved.  The function should accept 2 parameters, a key, and a value object.  The key
 * is the name under the data object the data will be set to, the object is whatever the api call returned.
 * 
 * @param {Function} func The function to be called whener any data is recieved.
 * @return {PageDefinition} The current page definition object for further chaining of methods.
 */
PageDefinition.prototype.addDataRecievedListener = function(func) {
	this.onDataRecieved.push(func);
	return this;
}

/**
 * Turn a page into a "coming soon" page.
 * 
 * @return {PageDefinition} The current page definition after modifications.
 */
PageDefinition.prototype.comingSoon = function() {
	this.addRequiredTemplate("coming-soon");
	this.addDisplayMethod(function() {
		setSlickTitle("Coming Soon To Slick Near You");
		displayeasteregg = {"display": "none"};
		if(this.pageParameters.query.easteregg) {
			displayeasteregg.display="inline-block";
		}
		$.tmpl(this.templates["coming-soon"], displayeasteregg).appendTo("#main");
	});
	return this;
}

/**
 * A Page Instance is the core controller of the current page.  Each time navigation changes, the instance is
 * discarded.  This is to ensure that the view is completely dynamic and not using stale data or references.
 * 
 * @param {PageDefinition} definition The Page Definition to base this instance on.  Much of the data is copied from the definition.
 * @param {Array} positionalParams The parts of the url after the group and page name, but before any query parameters.
 * @param {Object} queryParams The query parameter string turned into name->value mapping.
 */
function PageInstance(definition, positionalParams, queryParams) {
	this.pageParameters = {
		positional: positionalParams.slice(0),
		query: _.clone(queryParams)
	}
	this.name = definition.name;
	this.displayName = definition.displayName;
	this.isAction = definition.isAction;
	this.requiredData = _.clone(definition.requiredData);
	this.onDataRecieved = definition.onDataRecieved.slice(0);
	this.displayMethods = definition.displayMethods.slice(0);
	this.requiredTemplates = definition.requiredTemplates.slice(0);
	this.plugins = _.clone(definition.plugins);

	this.templates = {};
	this.data = {};

}

PageInstance.prototype.start = function() {
	for(i in this.requiredTemplates)
	{
		var templateName = this.requiredTemplates[i];
		grabTemplate(templateName, function(template, name) {
			Pages.getCurrentPage().addTemplate(name, template);
		});
	}

	for(keyName in this.requiredData)
	{
		var reqDataObj = this.requiredData[keyName];
		var url = reqDataObj;
		if(_.isFunction(reqDataObj))
		{
			url = reqDataObj.call(this, keyName);
		}
		$.ajax({
			url: url,
			type: "GET",
			dataType: "json",
			keyName: "" + keyName,
			success: function(data) {
				Pages.getCurrentPage().addData(this.keyName, data);
			}
		});
	}
}

/**
 * Add to the list of required data, and fetch it.  Only use this from one of the onDataRecieved methods.
 * 
 * @param {String} keyName The name inside of the data object to store the result.
 * @param {String} url The url to fetch, use SlickUrlBuilder to create it.
 */
PageInstance.prototype.addRequiredData = function(keyName, url) {
	this.requiredData[keyName] = url;
	$.ajax({
		url: url,
		type: "GET",
		dataType: "json",
		success: function(data) {
			Pages.getCurrentPage().addData(keyName, data);
		}
	});
}

/**
 * Add a template to the templates, calls displayIfReady afterwards.
 * 
 * @param {String} templateName The name of the template retrieved.
 * @param {Element} The template retrieved.
 */
PageInstance.prototype.addTemplate = function(templateName, template) {
	this.templates[templateName] = template;
	this.displayIfReady();
}

/**
 * Add data to the data variable, calling displayIfReady afterwards.
 * 
 * @param {String} dataKey The name of the key inside the data variable in which to place the data.
 * @param {Object} data The data to save.
 */
PageInstance.prototype.addData = function(dataKey, data) {
	this.data[dataKey] = data;
	for(i in this.onDataRecieved)
	{
		var datafunc = this.onDataRecieved[i];
		if(_.isFunction(datafunc))
		{
			datafunc.apply(this, [dataKey, data]);
		}
	}
	this.displayIfReady();
}

/**
 * Start calling display methods if all required templates and required data has been retrieved.
 */
PageInstance.prototype.displayIfReady = function() {
	if((_.size(this.templates) == this.requiredTemplates.length) &&
	   (_.size(this.data) == _.size(this.requiredData)))
	{
		for(i in this.displayMethods)
		{
			var displayMethod = this.displayMethods[i];
			displayMethod.call(this, []);
		}
		$("#main-loading").hide(250);
		$("[title]").tooltip({'events': {'def': "mouseover,mouseout click"}});
	}
}

/**
 * A logical group of pages.  You should call Pages.addGroup to add a group.  A group of pages is used
 * both in the url for identifying pages, and in navigation as a title of a group of actions.
 * 
 * @param {String} name The name of the group to use in the urls and to identify the group.  Must be unique.
 * @param {String} displayName The displayable name of the group.  Only used if isVisible is true.
 * @param {Boolean} isVisible If true the group will be available in the navigation on the page.
 */
function GroupOfPages(name, displayName, isVisible) {
	this.name = name;
	this.displayName = displayName;
	this.isVisible = isVisible;
	this.pages = {};
}

/**
 * Register / Create a new PageDefinition.
 * 
 * @param {String} name The internal code name of the page, also used in the url to identify which page should be loaded.
 * @param {String} displayName The pretty or displayable name of the page.  If isAction is true and isVisible is true (on the group), then the display name will be used in the navigation.
 * @param {Boolean} isAction If true this page will be available in the navigation.  This only matters if the group property isVisible is set to true.
 * @return {PageDefinition} The page definition object created.
 */
GroupOfPages.prototype.registerPage = function(name, displayName, isAction) {
	this.pages[name] = new PageDefinition(name, displayName, isAction);
	return this.pages[name];
}

/**
 * Retrieve a PageDefinition from the group, if it exists, return null otherwise.
 * 
 * @param {String} name The internal code name of the page, also used in the url to identify which page should be loaded.
 * @return {PageDefinition} The page definition object found, null otherwise.
 */
GroupOfPages.prototype.getPage = function(name) {
	var retval = null;
	if(this.pages[name])
	{
		retval = this.pages[name];
	}
	return retval;
}

/**
 * Retrieve a PageDefinition from the group. If it doesn't exist, a new one will be created with
 * diplayName set to name, and isAction set to false.
 * 
 * @param {String} name The internal code name of the page, also used in the url to identify which page should be loaded.
 * 
 * @return {PageDefinition} The page identified by the name passed in.
 */
GroupOfPages.prototype.page = function(name) {
	var retval = this.pages[name];
	if(!(retval))
	{
		retval = new PageDefinition(name, name, false);
		this.pages[name] = retval;
	}
	return retval;
}

/**
 * The Pages instance is the root of the page dispatching system for Slick.  It allows you to add groups, 
 * and get the current page class.
 */
var Pages = {
	/**
	 * A mapping of group names to the GroupOfPages instances.
	 */
	groups: {},
	/**
	 * The current page.  Always use getCurrentPage() function to get the page.
	 */
	currentPage: null,
	/**
	 * A special page that is not in a group.  This is the equivelant of a 404 page.
	 */
	NotFoundPage: new PageDefinition("notfound", "Page Not Found", false),
	/**
	 * Add a group (group of pages) to the available list of groups.  This affects both URLs and
	 * navigation.
	 * 
	 * @param {String} name The name of the group to be identified internally, and used as the first part of the URL.
	 * @param {String} displayName The name to be displayed in the nagivation if the visibility is set to true.
	 * @param {Boolean} isVisible If true it indicates this group should be visible in the navigation.
	 * 
	 * @return {GroupOfPages} the group created.
	 */
	addGroup: function(name, displayName, isVisible) {
		this.groups[name] = new GroupOfPages(name, displayName, isVisible);
		return this.groups[name];
	},
	/**
	 * Get a group by name.  If it is not found, it will be created, although it won't be visible
	 * or have a displayable group title (for navigation).
	 * 
	 * @param {String} name The name of the group to be identified internally, and used as the first part of the URL.
	 * @return {GroupOfPages} the group found or created.
	 */
	group: function(name) {
		if(this.groups[name])
		{
			return this.groups[name];
		} else
		{
			return this.addGroup(name, name, false);
		}
	},
	/**
	 * Get a page by it's group name and page name.  If the page is not found, null is returned.
	 * 
	 * @param {String} groupName The name of the group by which to find the page.
	 * @param {String} pageName The name of the page to find.  It must have previously been defined.
	 * @return {PageDefinition} the page identified by the group and page name supplied (if found), null otherwise.
	 */
	getPage: function(groupName, pageName) {
		if(this.groups[groupName])
		{
			return this.groups[groupName].getPage(pageName);
		}
		return null;
	},
	/**
	 * Get the current page, initializing it if necessary.
	 * 
	 * @return {PageInstance} The current page identified by the current location.
	 */
	getCurrentPage: function() {
		if(this.currentPage === null)
		{
			parts = $.address.pathNames();
			if(parts.length >= 2)
			{
				var page = this.getPage(parts[0], parts[1]);
				if(page === null)
				{
					this.currentPage = this.NotFoundPage.newInstance(parts, queryParametersToObject());
				} else
				{
					this.currentPage = page.newInstance(parts.slice(2), queryParametersToObject());
				}
			} else if(parts.length == 0)
			{
				this.currentPage = this.getPage("dashboards", "main").newInstance([], queryParametersToObject());
			} else
			{
				this.currentPage = this.NotFoundPage.newInstance(parts, queryParametersToObject());
			}
			this.currentPage.start();
		}
		return this.currentPage;
	}
}

/**
 * Get a template identified by it's name, and pass it to the callback function supplied.  The template
 * is only downloaded if it has not already been downloaded.
 * 
 * @param {String} templatename The name of the template.  It should be located at templates/[templatename].html.
 * @param {Function} callback The callback function to pass the template to when it has been downloaded.
 */
function grabTemplate(templatename, callback) {
	var template = $.template(templatename);
	if(template.length) {
		callback(template, templatename);
		return;
	}
	$.ajax({
		url: "templates/" + templatename + ".html",
		dataType: "html",
		success: function(data) {
			callback($.template(templatename, data), templatename);
		}
	});
}

/**
 * Get the value within an object, separating on the '.' character for nested objects.  A default
 * can be supplied to be returned if the value cannot be located.
 * 
 * @param {Object} obj The object to search for the value in.
 * @param {String} ref The name of the value to retrieve, separate nested objects by the '.' character.
 * @param {Object} def The default to return if the value or one of it's parents are not defined in the object.
 * @return {Object} the value if found or the default supplied.
 */
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

function getDuration(start, end) {
    if (end == 0) {
        return "";
    } else { 
        var duration = start - end;
        var seconds=((duration/1000)%60).toFixed(0);;
        var minutes=((duration/(1000*60))%60).toFixed(0);
        var hours=((duration/(1000*60*60))%24).toFixed(0);
        duration = "";
        if (hours != 0) {
            duration = duration + hours + " hours, "; }
        if (minutes!= 0) {
            duration = duration + minutes + " minutes, "; }
        if (seconds != 0) {
            duration = duration + seconds + " seconds"; }
        return duration;
    }
}

/**
 * Set the title of the page, and the title element in the page.
 * 
 * @param {String} title The title to set.
 */
function setSlickTitle(title) {
	$("head title").text("Slick: " + title);
	$("#pagetitle").text(title);
}

var SlickUrlBuilder = {
	/* Not needed anymore, will remove once everything is working.
	  
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
	*/
	project: {
		getAll: function() {
				return "api/projects";
		}
	},
	result: {
		getResultsForTestRun: function(testrunid, options) {
			var url = "api/results?testrunid=" + testrunid;
			if(options)
			{
				_.each(options, function(value, key, obj) {
					url = url + "&" + key + "=" + value;
				});
			}
			return url;
		}
	},
	testplan: {
		getTestplan: function(testplanid) {
			var url = "api/testplans/" + testplanid;
			return url;
		}
	},
	testrun: {
		getTestrun: function(testrunid) {
			var url = "api/testruns/" + testrunid;
			return url;
		},
		getTestrunsForProject: function(projectid, options) {
			var url = "api/testruns?projectid=" + projectid;
			if(options)
			{
				_.each(options, function(value, key, obj) {
					url = url + "&" + key + "=" + value;
				});
			}
			return url;
		},
		getSummary: function(testrunid) {
			var url = "api/testruns/" + testrunid + "/summary";
			return url;
		}
	},
	result: {
		getResultsByTestrun: function(testrunid, options) {
			var url = "api/results?testrunid=" + testrunid;
			if(options) {
				_.each(options, function(value, key, obj) {
					url = url + "&" + key + "=" + value;
				});
			}
			return url;
		}
	}
}

