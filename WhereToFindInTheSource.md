<font size='+5'><b>Map of the source code</b>:</font>


# New VS Old Slick #

---


Slick is in the middle of a small rewrite.  The backend will be exactly the same (maybe a few minor additions as normal), but the UI frontend is moving from Wicket to a pure html/javascript application.  Both are currently co-existing until all functionality has moved from the old (Apache Wicket) framework to the new (jquery, jquery-templates).  All the wicket source should be located in the slickij-web-base project, and shouldn't be touched (or at least new functionality should go into the new stuff).

# Java Server Side Code #

The java server side code was written first and was meant to be mostly complete.  However as new features are needed some additions may need to be made.  Mostly This reference is here so that you can tell what your javascript is calling on the backend, and what it should expect back.

## REST APIs ##

All the rest apis are under the url api, so if slick is deployed on your localhost in the root url (this is the default when using `mvn jetty:run`) all the apis would be under `http://localhost:8080/api`.

Under that url there are several resources (as the REST vernacular would define them as) available.  In code all these resources are defined in interfaces that live in the slickij-data-api project under `src/main/java/org/tcrun/slickij/api/` directory.  You can browse them [here](http://code.google.com/p/slickqa/source/browse/#git%2Fslickij-data-api%2Fsrc%2Fmain%2Fjava%2Forg%2Ftcrun%2Fslickij%2Fapi).

Right before the interface, there is an annotation of **@Path**.  This defines where under the url these endpoints will show up.  So in the [ConfigurationResource](http://code.google.com/p/slickqa/source/browse/slickij-data-api/src/main/java/org/tcrun/slickij/api/ConfigurationResource.java) it would be under `[slickij root]/api/configurations` or `http://localhost:8080/api/configurations`.

Inside the interface methods are defined, but they also have annotations that describe both where they are in the URL and what they accept/return and which HTTP method is used.  Operations that create something most often use the **POST** method, operations that modify **PUT**, operations that delete use **DELETE**, and operations that fetch data use **GET**.

URL parameters are put in the **@Path** inside curly braces, and referred to in the method signature as **@PathParam** with the same name.

## Data Types ##

Data types returned and posted to slick are converted to and from JSON using a toolkit called [Jackson](http://jackson.codehaus.org/).  You shouldn't need to do anything with it, but in case you do, there is the reference.  Jackson uses JavaBean methods for naming and determining what to turn into JSON objects.  That means that if you see a method called `getSomething()` that will turn into `{ something: "" }`.

All the data types are located under the slickij-data-api project in the `src/main/java/org/tcrun/slickij/api/data` browsed from [here](http://code.google.com/p/slickqa/source/browse/#git%2Fslickij-data-api%2Fsrc%2Fmain%2Fjava%2Forg%2Ftcrun%2Fslickij%2Fapi%2Fdata).

# Pages in the New Slick #

The new slick's ui is all defined in html and javascript, and can be found in the slickij-war project under the src/main/webapp directory.

## Registrations ##

Registrations are what is used to define pages, before adding logic to them.  They are under the js/pages directory browsable [here](http://code.google.com/p/slickqa/source/browse/#git%2Fslickij-war%2Fsrc%2Fmain%2Fwebapp%2Fjs%2Fpages).

## Templates ##

Templates define the html snippets that are needed to produce much of the html you see in slick.  They are all stored in the templates directory, and are browsable [here](http://code.google.com/p/slickqa/source/browse/#git%2Fslickij-war%2Fsrc%2Fmain%2Fwebapp%2Ftemplates)

## Views ##

Views are the javascript that makes up the vast majority of what is in a page on slick.  They are stored in js/views and are browsable [here](http://code.google.com/p/slickqa/source/browse/#git%2Fslickij-war%2Fsrc%2Fmain%2Fwebapp%2Fjs%2Fviews).

## CSS ##

All css files under the css/style directory are combined at the server side, so you can choose to add to the main [style.css](http://code.google.com/p/slickqa/source/browse/slickij-war/src/main/webapp/css/style/style.css) or add another file in the same directory].
