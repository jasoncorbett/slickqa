<font size='+5'><b>HOWTO Write a Page for Slick</b>:</font>


# Introduction #

---


This HOWTO is written for the purpose of helping people come up to speed on how to add to Slick.  It is not a full description of
JQuery or how everything in Slick works.  It is intended as a starting point to help get some one used to the way pages are
written for Slick, and gives an example of such.

## Technologies ##

If you do not understand how [jQuery](http://jquery.com) works, or how [AJAX](http://en.wikipedia.org/wiki/Ajax_(programming)) works, please read up.  This tutorial
will not be an [introduction into jQuery](http://docs.jquery.com/Tutorials), and will not explain all jQuery Usage.  If you are new to Javascript or jQuery Nettuts.com has a really good [introductory course to jQuery](http://tutsplus.com/course/30-days-to-learn-jquery/).

In addition, if you are familiar with JQuery, and want to know about some of the technologies in use in Slick Page Development, Take a look at:

  * Though you may not use it directly, a lot of slick javascript apis are based on [Backbone.js](http://documentcloud.github.com/backbone/), so reading up may help.
  * [Handlebars](http://handlebarsjs.com) is the template engine provided.
  * [REST](http://en.wikipedia.org/wiki/Representational_state_transfer) using [JSON](http://en.wikipedia.org/wiki/JSON) encoding are used to get data from Slick's datastore.
  * [Datatables](http://datatables.net/) is an excellent data table extension to JQuery, and is used throughout Slick.

# Examples #

## Example Page: ComingSoon ##

This page shows how to integrate into the menu structure in slick.  It would add itself the the Dashboards link, and add an entry called Foo.  Because it extends the ComingSoonPage instead of SlickPage directly, all the display elements are taken from the ComingSoonPage.  This is a super easy way for you to test your "add a new page" skills.

```

// ---- slickij-war/src/main/webapp/js/views/foo.js ----

var FooPage = ComingSoonPage.extend({

	group: "dashboards",
	name: "Foo",
	navigation: true

});

```

## Example Page: Project View ##

I won't try to show everything we want in a project view page, but a short example might help show off the templating engine.  Notice there is no render method, just definitions.  The default render method takes the first template, applies it using the data fetched as the context.

The codename is used for the URL, as the name parameter has spaces in it.  If you don't provide a codename, your name will be converted to lower case, and spaces replaced with dashes.

```

// --- slickij-war/src/main/webapp/js/views/testmgmt-viewproject.js ---

var TestManagementViewProjectPage = SlickPage.extend({
    name: "View Current Project",
    codename: "viewproject",
    group: "testmgmt",
    navigation: true,

    requiredData: {
        "proj": function() {
            return "api/projects/" + getCurrentProject().id;
        }
    },

    initialize: function() {
        this.on("ready", this.onReady, this);
        this.on("finish", this.onFinish, this);
    },

    onReady: function() {
        this.title = "Detail for Project '" + this.data.proj.name + "'";
        this.addBuildUrl = urlOfPage(TestManagementAddBuildPage);
        this.addReleaseUrl = urlOfPage(TestManagementAddReleasePage);
        this.data.proj.lastUpdatedString = new Date(this.data.proj.lastUpdated).toLocaleString();
    },

    onFinish: function() {
        var tabledata = [];
        _.each(this.data.proj.components, function(component) {
           tabledata[tabledata.length] = [safeReference(component, "code", ""),
                                          safeReference(component, "name", ""),
                                          safeReference(component,"description", "")];
        });
        $("#project-component-table").dataTable({
            aaData: tabledata,
            aoColumns: [
                {"sTitle": "Code Name", "sWidth": "15%"},
                {"sTitle": "Name", "sWidth": "15%"},
                {"sTitle": "Description", "sWidth": "70%"}],
            bJQueryUI: true,
            bAutoWidth: false,
            bDeferRender: true,
            bPaginate: false,
            sDom: "<\"H\"lfr>tS<\"F\"ip>",
            sScrollY: "" + ($(document).height() - (4 * $("#pagetitle").height()) -  (3 * $("#titlebar").height()) - (3 * ($("#project-description-box").height()))) + "px"
        });
    }

});

```

And the template:

```

<!-- slickij-war/src/main/webapp/templates/testmgmt-viewproject.html -->
{{#with data.proj}}
<div id="project-description-box" class="box width-4 projectclass" >
	<div class="boxname">Description</div>
	<div class="boxcontent" id="project-description">{{description}}</div>
</div>

<div id="project-default-build-box" class="box width-4 projectclass" >
	<div class="boxname">Default Build</div>
	<div class="boxcontent" id="project-default-build">{{defaultBuildName}}</div>
</div>

<div id="project-last-updated-box" class="box width-4 projectclass" >
	<div class="boxname">Date Last Updated</div>
	<div class="boxcontent" id="project-last-updated">{{lastUpdatedString}}</div>
</div>

<div class="clear"></div>

<div class="boxname">Components</div>
<table class="table-full-width" id="project-component-table"></table>
{{/with}}

<div align="center" id="project-links">
    <a href="{{addBuildUrl}}" class="button">Add Build</a>
    <a href="{{addReleaseUrl}}" class="button">Add Release</a>
</div>

```

What's that **modal-link** class you ask?  When you use that css class slick will automatically load the page in a modal dialog.

## Example Page: Testrun Summary ##

This is the `#/reports/testrun-summary` page.  It requires an id of the testrun to provide a summary of.  It has a chart, a list of stats, and some buttons that perform actions.  This is an "advanced" example showing some of the more wonderful parts of the view framework.

First we take a look at [report-view-testrun-summary.js](http://code.google.com/p/slickqa/source/browse/slickij-war/src/main/webapp/js/views/report-view-testrunsummary.js):

```

```