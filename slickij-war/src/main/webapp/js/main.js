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


// TODO: write unit tests for this critical code!

var SlickUrlParser = function(url) {
    this.url = url;
    this.query = {};
    this.pathNames = [];
    var queryStartIndex = url.indexOf('?');
    var beforeQuery = url;
    if(queryStartIndex >= 0) {
        // no query string

        beforeQuery = url.substring(0, queryStartIndex);

        var afterQuery = "";
        // make sure we don't try to do a substring when the ? is at the end of the url;
        if(queryStartIndex < (url.length - 1)) {
            afterQuery = url.substr(queryStartIndex + 1);
        }

        // use jsUri's Query parser to do the hard part ;-)
        var queryObj = new Query(afterQuery);
        _.each(queryObj.params, function(params) {
            var key = params[0];
            var values = queryObj.getParamValues(key);
            if(values.length > 1) {
                this.query[key] = values;
            } else {
                this.query[key] = queryObj.getParamValue(key);
            }
        }, this);
    }

    var anchorStartIndex = beforeQuery.indexOf('#');
    if(anchorStartIndex >= 0 && beforeQuery.charAt(anchorStartIndex + 1) === '/') {
        // make sure #/ isn't the end of the string
        if(anchorStartIndex < (beforeQuery.length - 2)) {
            var path = beforeQuery.substr(anchorStartIndex + 2);
            this.pathNames = path.split('/');
        }
    }
};

function getPageFromUrl(url, options) {
    if(! options) {
        // in case they don't pass in any options
        options = {positional: [], query: {}};
    }
    var parser = new SlickUrlParser(url);
    options.query = parser.query;

    if(parser.pathNames.length > 0) {
        if(parser.pathNames.length >= 2) {
            var groupname = parser.pathNames[0];
            var pagename = parser.pathNames[1];
            options.positional = parser.pathNames.slice(2);
            if(SlickPage.PageGroups[groupname] && SlickPage.PageGroups[groupname][pagename]) {
                var page = SlickPage.PageGroups[groupname][pagename];
                var instance = null;
                try {
                    instance = new page(options);
                } catch(err) {
                    options.url = url;
                    options.error = "Error In Page Start";
                    options.err = err;
                    instance = new ErrorPage(options);
                }
                return instance;
            } else
            {
                options.url = url;
                options.parser = parser;
                return new NotFoundPage(options);
            }

        } else {
            options.error = "Bad Url: " + url;
            options.parser = parser;
            return new ErrorPage(options);
        }
    } else {
        // there was nothing left of the url, return the *default* page
        return new AboutSlickPage(options);
    }

}

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
          var nHours = (duration/3600).toFixed(0);
          var nTimeInSeconds = (duration%3600).toFixed(0);
          var nMinutes = (nTimeInSeconds/60).toFixed(0);
          var nSeconds = (nTimeInSeconds%60).toFixed(0);
          var retval = "";
          if (nHours != 0) {
              retval = retval + nHours + " hours, "; }
          if (nMinutes != 0) {
              retval = retval + nMinutes + " minutes, "; }
          if (nSeconds != 0) {
              retval = retval + nSeconds + " seconds"; }
          return retval;
     }
}

function getDurationMilliseconds(duration) {
    if (duration == 0) {
        return "";
    } else {
        var nHours = (duration/3600000).toFixed(0);
        var nTimeInSeconds = (duration%3600000).toFixed(0);
        var nMinutes = (nTimeInSeconds/60000).toFixed(0);
        var nSeconds = ((nTimeInSeconds%60000)/1000).toFixed(3);
        var retval = "";
        if (nHours != 0) {
            retval = retval + nHours + " hours, "; }
        if (nMinutes != 0) {
            retval = retval + nMinutes + " minutes, "; }
        if (nSeconds != 0) {
            retval = retval + nSeconds + " seconds"; }
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
            if(! this.options.noSetTitle) {
                setSlickTitle(this.getTitle());
            }
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
                $("[title]").tooltip();
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

var NotFoundPage = SlickPage.extend({
    primaryTemplateName: "not-found.html",
    title: "Unknown Page"
});

var ErrorPage = SlickPage.extend({
    primaryTemplateName: "error.html",
    title: "Oh No!"
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

