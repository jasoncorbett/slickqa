<font size='+5'><b>Javascript Reference</b></font>


# Introduction #

---


When writing pages for slick, you will want to know about special css classes, page options, page class methods and properties, and handlebars template tags.  These will make you life a lot easier and make your pages better conform to the rest of slick.  It also allows slick's ui to be refined in a more global manner.

# The SlickPage class #

## Options ##

---


Options are provided in the extend property and are applied as properties on the class (not the instance, though they are copied to the instance).  See the SlickPageHOWTO examples for how to apply.

### group ###

**Required**

The name of the group your page belongs to.  The group name is used as the first part of the url after the hash ('#') sign.  So if your group is set to "foo", then the beginning of your url would be index.html#/foo.  The group also has meaning if you use navigation, as it groups a link to your page in the menu heading.

### name ###

**Required**

The name of your page.  The name can have spaces, and this should be considered the "friendly" name of your page.  The title of your page will be set to this name, unless you specify another one via the title property or overriding the getTitle or render methods.

### codename ###

The codename is used as the second part of the url (right after group) and is a url friendly name.  By default it is the name parameter converted to lowercase with spaces replaced by dashes.  You can provide the codename option instead if you want to customize the url.

### navigation ###

navigation is set to false by default, but can be set to true.  When true if your group is in the main menu, then you will recieve a link in the main navigation for your page. The link will use your name option as the title, and the image url will be "images/action-group-codename.png".

### refreshable ###

`refreshable` and `watchable` overlap a bit in functionality.  In fact any page that is watchable is automatically refreshable.  A refreshable page will have a button added to it called 'Reload' that will reload the page in a **nice** manner.  By nice I mean that the normal flow will be followed, except instead of the loading screen, the data and templates are loaded before the current content is removed.

### watchable ###

`refreshable` and `watchable` overlap a bit in functionality.  In fact any page that is watchable is automatically refreshable.  A watchable page adds the option of having a timer available on the page so that a user can do a sort of **auto refresh** on the page.  Again, all you need to do is add this property, and you'll get the widgets.

### title ###

If you have a static title for the page, you can add the title as an option, and setTitle will be called by the default render method.  If your title has any dynamic qualities, override the getTitle method instead.  If you write your own render method, be sure to set the title using setTitle.

### requiredTemplates ###

The requiredTemplates parameter should be a list of templates to be downloaded and compiled before performing the rendering steps.  If you have a precompiled template, use `requiredCompiledTemplates` instead.  You should omit the .html extension, and your template should be in the templates directory.

### requiredCompiledTemplates ###

Just like requiredTemplates, except instead of downloading an html and compiling it, these templates should be .js files.  Note that you should ommit the .js extension.

### requiredData ###

This should be a list of urls, or functions.  If it's a function, then the function should return a url.  This can be useful if you need data based on positional or query parameters.

## Standard Properties ##

---


These are available to your page instance when they are created.  You can reference them by simply prepending `this.`.

### parameters.positional ###

This is every part of the url after your group and codename.  So if the url was `index.html#/foo/bar/a/b/c` then `parameters.positional` would be `[ 'a', 'b', 'c' ]`.  If you require positional parameters, be sure to check for them and handle errors gracefully.

### parameters.query ###

These are any url query parameters provided.  So if the url is `index.html#/foo/bar?id=something` then `parameters.query` would be `{ 'id': 'something' }`.  This shouldn't normally be used for required parameters, but is good for optional ones.

## Methods ##

---


Some of these methods (they will be noted with a **override** tag) can or should be overriden.  Others are there to help you in writing your page.

### message ###

Send a message (non intrusive) to the user.  This can be notification of success or failure.  Note that the error method calls this method for you.

Example:

```

this.message("This is a message from the page you are viewing...");

```

### error ###

Send an error message to the user, and (when it is implemented) try to log the error to slick's datastore for an admin to review.

Example:

```

this.error("This error message should be logged along with the current URL and this page class converted to JSON.");

```

### template ###

If you write you're own render method, or you need to use more than one template, you may find yourself calling the template method.  It takes 3 parameters, the second two being optional.  The options are:

  1. The template name (same as provided to requiredTemplates or requiredCompiledTemplates)
  1. The data object passed to the template, the default being `this`
  1. The element to apply it to.  Default is `this.el` which is the element passed into this object as the top level element of the view.

### render ###

**override**

This method can be overriden, or you can use the default implementation.  The default takes the first requiredTemplate, and applies it to the top level element for this view using the view as the data object passed in.

If you want to override the functionality, but still use it in your own render method you can do this:

```
SlickPage.render.apply(this);
```

### preRender ###

**override**

If you want to do something before the default render method (like prepare data) you can override the preRender method.  The default is a no-op function.

### postRender ###

**override**

If you want to do something after the default render method (like attach event handlers) you can override the postRender method.  The default is a no-op function.

### setSlickTitle ###

You can use this method to set the slick title (both the page title, and the text displayed above the navigation).

### getTitle ###

**override**

You can override this method to produce a dynamic title for the default render method.  If you don't use the default render method, then overriding this method is optional.  By default this method will use the title option if it exists, and if not it will use the name option.

# Global Functions #

---


## pageUrl ##

This function allows you to get the url for a View.  The parameters are this:

  1. The view you want to generate the url for
  1. (Optional) an array of positional parameters
  1. (Optional) an object (key/value map) of query parameters

The return value is the url for the view (starting with the `#` character).

# ComingSoon Page #

---


The coming soon page class is a sub class of the SlickPage class, except it provides no group or name.  If you subclass this page, all you need to provide is the group and name (and possibly navigation) options and you'll get a nice "Coming Soon" page where your page would normally be.

# Template Tags #

---


## button ##

## imgbutton ##

## link ##

## tooltip ##

## block ##

## blocklayout ##

# CSS Classes #

---


## button ##

## imgbutton ##
