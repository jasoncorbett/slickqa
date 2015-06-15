<font size='+3'><b>Top Level Data Types</b>:</font>


Because we're using a [document oriented database](http://www.mongodb.org/display/DOCS/Schema+Design) ([mongodb](http://www.mongodb.org)),
we only have a few "top level" objects.  This section describes the properties of those objects.

![http://wiki.slickqa.googlecode.com/git/images/SlickDataModel.png](http://wiki.slickqa.googlecode.com/git/images/SlickDataModel.png)

# Project #

A project is the basic organizational structure for slick v2.  All results and test cases are related to a project and / or one of it's
components.  Below is a list of a Projects properties.

  * **id**: an ObjectId (on the server side, and a String when serialized to JSON) assigned when saving the Project
  * **name**: A String containing the name of the project, this should be a "displayable" name
  * **description**: A String containing a plain text description of the project.
  * **configuration**: A Configuration object (described later).  To modify this object, get it's id and modify it via Configuration APIs.
  * **defaultRelease**: The id of the default release object
  * **releases**: A list of embedded Release Objects (described below)
    * **id**: an ObjectId (on the server side, and a String when serialized to JSON) assigned when saving the Release
    * **name**: A String containing the name of the release
    * **target**: A Date (serialized to an int containing the number of milliseconds since epoch) of when the release is targeted to release
    * **defaultBuild**: The id of the build that is the default for this release.
    * **builds**: A list of embedded Build Objects (described below)
      * **id**: an ObjectId (on the server side, and a String when serialized to JSON) assigned when saving the Release
      * **name**: A String containing the name of the build
      * **built**: A Date (serialized to an int containing the number of milliseconds since epoch) of when the build was built
  * **components**: A list of Component Objects (described below).  Components can be used to help categorize tests.
    * **id**: an ObjectId (on the server side, and a String when serialized to JSON) assigned when saving the Component
    * **name**: A String containing the name of the component
    * **description**: A String containing the description of the component
    * **code**: A String containing a shortened more restrictive name of the component.  This will have more rules for verification.  Basically it should be able to be used as a Java identifier (class name, package name, variable name, etc).
  * **lastUpdate**: A Date (serialized to an int containing the number of milliseconds since epoch) of when the Project was last updated.  This is automatically set when the object is saved.
  * **tags**: A List of Strings containing all the tags that are valid for this project.  These are used for labeling and grouping test cases (and potentially other items).  It is possible for this list to be out of sync with the tags applied to tests.  Anytime an object is saved with tags on it, it should be checked that the tags are in this list.
  * **attribues**: A map of key-value pairs that contain arbitrary attributes about the project.
  * **automationTools**: A list of Strings containing the names of the tools used to run automation for this project.  This is used to provide a selection list for the Testcase property **automationTool**.
  * **datadriven**: A list of DataDrivenProperty objects (described below)
    * **name**: A String "key" of a data driven config
    * **requirement**: A boolean indicating if the config is a "requirement" indicating that a machine wanting to run the data driven test would need to be able to run tests of this type
    * **values**: A list of standard available properties for the key given.
  * **extensions**: A List of objects containing any type of data, but must contain the properties listed below.
    * **className**: A String containing the extension class name (Java class name).
    * **name**: Some user and or programmer friendly name of the type of data this extension contains.
    * **summary**: A String (probably dynamically generated) containing a summary of the data.  This should be like Java's toString method on classes.

# Testcase #

A [test case](http://en.wikipedia.org/wiki/Test_case) is an object describing a proceedure that verifies a single condition that helps
verify the quality of a project or product.  Test cases are the central type slick is intended to manage.  Below is a list of a Testcase
Object's properties:

  * **id**: an ObjectId (on the server side, and a String when serialized to JSON) assigned when saving the Testcase
  * **name**: A String containing the name of the test case
  * **purpose**: A String containing a general description, or purpose of the test case.
  * **requirements**: A String describing the requirements for following the steps in the test case.
  * **steps**: A list of TestcaseStep objects (properties described below) that document the steps involved in running the test
    * **name**: A String containing the name or general instruction of the step.
    * **expectedResult**: A String containing the expected result of following the step.
  * **author**: A String containing the name of the person who added the test
  * **attribues**: A map of key-value pairs that contain arbitrary attributes about the test case.
  * **automated**: A boolean flag indicating that the test is automated if the flag is set to true
  * **automationPriority**: An integer between 1 and 100 indicating the priority of automating the test case (only has meaning if automated is false).  Default is 50. 1 is the highest priority (priority 1).
  * **automationTool**: A String containing the name of the tool to be used.  This could be informational, or it could be used programmatically by an agent.
  * **automationConfiguration**: A String to send to the automation tool that helps describe how to get or otherwise setup the tool to run the proper tests.
  * **automationId**: A String containing a unique identifier and or some other information describing to the automation how to find the test to run.  This could be anything, it is completely dependent on the automation toolkit used.
  * **automationKey**: Another String that can contain some unique identifier for the automation.  The difference with this key is that it will be indexed, meaning it could be used to help identify the test in a non-slick specific manner.  An example of this is using a GUID for each test.  Then even if the test is refactored, renamed, it's "id" changes, etc you can still find the test case in slick that identifies the test.  Completely optional use.
  * **stabilityRating**: An integer from 1 to 100 that is a rating of how likely the test is to pass.  1 is the "lowest" rating indicating the test is unlikely to pass, 100 would indicate the test will most likely pass.  The exact definition of how this number is calculated will likely change over time to make it more accurate.  If the test "always passes" then the number would be likely be at 100.
  * **tags**: A list of Strings that contain "tags" for an arbitrary organizational structure.  If a tag in this list is not in the Project's tag list, it should be added.
  * **project**: A ProjectReference object (described below) pointing to the project this test case is for.
    * **name**: A String containing the name of the project.
    * **id**:  an ObjectId (on the server side, and a String when serialized to JSON) that is a reference to the Id field of the project.  If it is null at save time a query will be performed to find it based on the name.
  * **component**: A ComponentReference object (described below) pointing to the component in the project this test belongs to.  Any field not known at save time should be found when the save happens.
    * **id**: an ObjectId (on the server side, and a String when serialized to JSON) that is a reference to the componentId from a component on the project.
    * **name**: A String containing the name field fromt he component.
    * **code**: A String field containing the code from the component.
  * **datadriven**: A list of data driven properties that this test should be repeated for.
    * **name**: A String "key" of a data driven config
    * **requirement**: A boolean indicating if the config is a "requirement" indicating that a machine wanting to run the data driven test would need to be able to run tests of this type
    * **values**: A list of specific values for this test.  If the list is empty or omitted then the list from the Project will be used.
    * **excluded**: A list of values to specifically exclude.  For instance if browser was the name, and the standard list (contained in the Project) was ["ff", "ie", "chrome"], then the test writer could put "ie" in the exclude list to keep this test from repeating with the ie configuration property for browser.
  * **deleted**: A boolean indicating if the test case has been deleted.  Deleted tests are never actually deleted, just marked as deleted.
  * **extensions**: A List of objects containing any type of data, but must contain the properties listed below.
    * **className**: A String containing the extension class name (Java class name).
    * **name**: Some user and or programmer friendly name of the type of data this extension contains.
    * **summary**: A String (probably dynamically generated) containing a summary of the data.  This should be like Java's toString method on classes.

# Configuration #

Configuration objects hold configuration data.  They could be for a user, or they could be for a set of tests, really it could be for anything.

  * **id**: an ObjectId (on the server side, and a String when serialized to JSON) assigned when saving the configuration
  * **configurationData**: A Map of key=>value configuration strings
  * **name**: A friendly name for this configuration.
  * **type**: A String detailing the "type" of configuration data.  It could be "USER" for user configuration data, "PLUGIN" for plugin configuration data, "ENVIRONMENT" for environment configuration data.
  * **filename**: an optional name of a file this configuration came from (if it came from one)

# Testplan #

Test plans can be of 2 different types: fixed or dynamic.  Fixed test plans are literally a list of tests.  Dynamic contain a list of "queries" to help in finding the tests
to include at "run time", meaning the time when the test plan is run.

  * **id**:
  * **name**: A String containing the name for the test plan.
  * **createdBy**: A String containing the name of the person who created the test plan (username / login name).
  * **private**: A boolean indicating that this test plan is only to be used by the person who created it.
  * **testcases**: a list of testcase reference objects (described below, but same as on result object).
    * **id**: If the test case id is known, it will go here.  This is a reference to the Testcase object's Id property.  Upon saving of the result, if the test case can be found, then this property will be set.
    * **name**: A String containing the name of the test case.
    * **automationId**: A String field that should match the automationId field of the test case.  It is not necessary to populate this field and the automationKey field.  The automationKey field always takes precedence.
    * **automationKey**: The key that should match the automationKey property in the Testcase object.
  * **filters**: A list of test filter objects (for dynamic test plans)
    * ????

# Testrun #

  * **id**: an ObjectId (on the server side, and a String when serialized to JSON) assigned when saving the test run
  * **testplanId**: A reference to the test plan id, if this test run was created from a test plan.
  * **name**: A String containing the name of the Test run (if a name is assigned).
  * **config**: A ConfigurationReference object (described below) pointing to the configuration used for this test run.  When the reference is saved, a lookup will be performed if not all the configuration reference information is known.  After this if the configId is still not present (is null), then the configuration has not previously been saved.
    * **configId**: the id from the configuration set
    * **name**: the name of the configuration set
    * **filename**: the filename for the configuration set

# Result #

Recording results is paramount to slick.  The first version of slick mostly just recorded results.  It is the opinion of the author of this that recording a result, even if you don't have all the information about how the result should be categorized is very important.  You can add many of the types of data within slick just by adding results with previously unknown values.  Below is the description of a Result's properties:

  * **id**: an ObjectId (on the server side, and a String when serialized to JSON) assigned when saving the result
  * **testrun**: A TestrunReference object pointint to the test run for this result.  If not all the values are known when saved they are looked up from the values that do exist.  If the testrunId property is still null after save, then a testrun has not been created for this result yet.
    * **testrunId**: A String containing the id of the testrun (if known)
    * **name**: The name of the test run
  * **configurationOverride**: A list of ConfigurationOverride Objects (described below) for this specific result.
    * **name**: A String containing the "key" or name of the configuration to override
    * **value**: A String contianing the override value
    * **requirement**: A Boolean indicating that this override configuration is a requirement for the machine on which the test is to be run.
  * **config**: A ConfigurationReference object (described below) pointing to the configuration used for this test run.  When the reference is saved, a lookup will be performed if not all the configuration reference information is known.  After this if the configId is still not present (is null), then the configuration has not previously been saved.
    * **configId**: the id from the configuration set
    * **name**: the name of the configuration set
    * **filename**: the filename for the configuration set
  * **testcase**: A TestcaseReference Object (described below) that points to the test case that this result is for.  Any fields unknown on save should be queried for an populated.
    * **id**: If the test case id is known, it will go here.  This is a reference to the Testcase object's Id property.  Upon saving of the result, if the test case can be found, then this property will be set.
    * **name**: A String containing the name of the test case.
    * **automationId**: A String field that should match the automationId field of the test case.  It is not necessary to populate this field and the automationKey field.  The automationKey field always takes precedence.
    * **automationKey**: The key that should match the automationKey property in the Testcase object.
  * **recorded**: A Date (serialized to an int containing the number of milliseconds since epoch) of when the result was recorded, this is automatically populated on save.  If the result is updated in any way this timestamp is updated.
  * **status**: A String indicating the status of the result.  This should be one of (PASS, FAIL, BROKEN\_TEST, NOT\_TESTED, SKIPPED, NO\_RESULT).
  * **reason**: A simple String containing a one-line description of why the status was given.  Not required.
  * **attribues**: A map of key-value pairs that contain arbitrary attributes about the result.
  * **files**: A list of file objects (described below) that are associated with the result.
    * **id**: an ObjectId (on the server side, and a String when serialized to JSON) assigned when saving the file object
    * **name**: a String containing the name of the file (including extension)
    * **mimetype**: A String containing the MIME type.  If none is provided a guess is made based on the name.
  * **project**: A ProjectReference object (described below) pointing to the project this test case is for.
    * **name**: A String containing the name of the project.
    * **id**:  an ObjectId (on the server side, and a String when serialized to JSON) that is a reference to the Id field of the project.  If it is null at save time a query will be performed to find it based on the name.
  * **component**: A ComponentReference object (described below) pointing to the component in the project this test belongs to.  Any field not known at save time should be found when the save happens.
    * **id**: an ObjectId (on the server side, and a String when serialized to JSON) that is a reference to the componentId from a component on the project.
    * **name**: A String containing the name field from the component.
    * **code**: A String field containing the code from the component.
  * **release**: A ReleaseReference object (described below) that points to a release that this result is for. If the release reference is null, then the default release information will be used.
    * **id**: an ObjectId (on the server side, and a String when serialized to JSON) that is a reference to the release id from a release on the project.
    * **name**: A String containing the name of the release.
  * **build**: A BuildReference object (described below) that points to a build (that is part of this release).  If this is null the default build will be used.
    * **id**: an ObjectId (on the server side, and a String when serialized to JSON) that is a reference to the build id from a build that is part of a release on the project.
    * **name**: A String containing the build name.
  * **runlength**: An integer containg the amount of time in seconds it took to run the test.
  * **extensions**: A List of objects containing any type of data, but must contain the properties listed below.
    * **className**: A String containing the extension class name (Java class name).
    * **name**: Some user and or programmer friendly name of the type of data this extension contains.
    * **summary**: A String (probably dynamically generated) containing a summary of the data.  This should be like Java's toString method on classes.

Note: the result of creating a new result should be an object that both contains the result that was created, and an indicator of if a new test, component, release, or build was created as a result.