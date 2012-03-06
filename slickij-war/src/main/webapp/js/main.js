/* 
 * main.js
 * Author: Jason Corbett <Jason_Corbett@intuit.com>
 */



$(document).ready(function () {
	$("#mainnavigation a").live("click", function () {
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
	var parts = ref.split(".");
	for(var i in parts) {
		var part = parts[i];
		if(context[part]) {
			context = context[part];
		} else {
			return def;
		}
	}
	return context;
}

function getDuration(duration) {
    if (duration == 0) {
        return "";
    } else { 
        var seconds=((duration/1000)%60).toFixed(0);
        var minutes=((duration/(1000*60))%60).toFixed(0);
        var hours=((duration/(1000*60*60))%24).toFixed(0);
        var retval = "";
        if (hours != 0) {
            retval = retval + hours + " hours, "; }
        if (minutes!= 0) {
            retval = retval + minutes + " minutes, "; }
        if (seconds != 0) {
            retval = retval + seconds + " seconds"; }
        return retval;
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
	project: {
		getAll: function() {
				return "api/projects";
		}
	},
	testplan: {
		getTestplan: function(testplanid) {
			return "api/testplans/" + testplanid;
		}
	},
	testrun: {
		getTestrun: function(testrunid) {
			return "api/testruns/" + testrunid;
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
			return "api/testruns/" + testrunid + "/summary";
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
};

var SlickPage = Backbone.View.extend({
        refreshable: false,
        watchable: false,
        navigation: false,

        message: function(text) {
            $.jGrowl(text);
        },

        error: function(text) {
            $.jGrowl("ERROR: " + text);
            //TODO: log to console everything, including stack trace.
        },

        template: function(name, context, element) {
            if(! context)
            {
                context = this;
            }
            if(! element)
            {
                element = this.$el;
            }
            if(Handlebars.templates[name])
            {
                $(element).html(Handlebars.templates[name](context));
            } else
            {
                this.error("No such template '" + name + "'!");
            }
        },

        render: function() {
            if(this['group'] && this['codename'] && Handlebars.templates[this.group + '-' + this.codename + ".html"]) {
                this.template(this.group + '-' + this.codename + ".html");
            } else if(this['group'] && this['name'] && Handlebars.templates[this.group + '-' + this.name + ".html"]) {
                this.template(this.group + '-' + this.name + ".html");
            } else if(this['primaryTemplateName']) {
                this.template(this.primaryTemplateName);
            }
            setSlickTitle(this.getTitle());
        },

        getTitle: function() {
            if(this['title'])
            {
                return this.title;
            } else
            {
                return this.name;
            }
        },

        // ---- Internal methods ----
        pageStart: function() {
            this.data = {};
            for(var keyName in this.requiredData) {
                var reqDataObj = this.requiredData[keyName];
                var url = reqDataObj;
                if(_.isFunction(reqDataObj))
                {
                    url = reqDataObj.call(this, keyName);
                }
                var page = this;
                $.ajax({
                    url: url,
                    type: "GET",
                    dataType: "json",
                    page: page,
                    keyName: "" + keyName,
                    success: function(data) {
                        this.page.addData(this.keyName, data);
                    }
                });
            }
            this.displayIfReady(); // in case there is no data to fetch!
        },

        addRequiredData: function(keyName, url) {
            this.requiredData[keyName] = url;
            var page = this;
            $.ajax({
                url: url,
                type: "GET",
                dataType: "json",
                page: page,
                success: function(data) {
                    this.page.addData(keyName, data);
                }
            });
        },

        addData: function(dataKey, data) {
            this.data[dataKey] = data;
            this.trigger("dataRecieved", [dataKey, data]);
            this.displayIfReady();
        },

        displayIfReady: function() {
            if(_.size(this.data) >= _.size(this.requiredData))
            {
                this.trigger("ready");
                this.render();
                this.trigger("render");
                this.trigger("finish");
            }
        }
},
{
    PageGroups: {},
    StandardNavigationGroups: [
        {displayname: 'Dashboards', codename: 'dashboards'},
        {displayname: 'Test Management', codename: 'testmgmt'},
        {displayname: 'Run Tests', codename: 'runtests'},
        {displayname: 'Reports', codename: 'reports'}
    ]
});

SlickPage.extend = function(protoProps, classProps) {
    var child = Backbone.View.extend.apply(this, [protoProps, classProps]);
    if(! SlickPage.PageGroups[protoProps.group]) {
        SlickPage.PageGroups[protoProps.group] = {};
    }
    if(protoProps.group && protoProps.codename)
    {
        // if the page defines a group and a name property, then "register" the page for use.
        SlickPage.PageGroups[protoProps.group][protoProps.codename] = child;
    } else if(protoProps.group && protoProps.name)
    {
        // if the page defines a group and a name property, then "register" the page for use.
        SlickPage.PageGroups[protoProps.group][protoProps.name] = child;
    }
    return child;
};

var ComingSoonPage = SlickPage.extend({
     primaryTemplateName: "coming-soon.html",
     title: "This Page Coming Soon"
});

function urlOfPage(page, positional, query) {
    var url = "#/";
    if(page.prototype.group) {
        url = url + page.prototype.group + "/";
    }
    if(page.prototype.codename) {
        url = url + page.prototype.codename;
    } else if(page.prototype.name) {
        url = url + page.prototype.name;
    }

    if(positional) {
        _.each(positional, function(argument) {
           //TODO: urlescape
           url = url + "/" + argument;
        });
    }

    if(query) {
        url = url + "?";
        var first = true;
        _.each(query, function(value, key) {
            if(first) {
                first = false;
            } else
            {
                url = url + "&";
            }
            //TODO: urlescape
            url = url + key + "=" + value;
        });
    }

    return url;
}

Handlebars.registerHelper('pageUrl', function(page) {
    if(page.prototype['group']) {
        if(page.prototype['codename']) {
            return Handlebars.SafeString("#/" + page.prototype['group'] + "/" + page.prototype['codename']);
        } else if(page.prototype['name']) {
            return Handlebars.SafeString("#/" + page.prototype['group'] + "/" + page.prototype['name']);
        }
    }
});

