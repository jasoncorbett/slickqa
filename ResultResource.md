<font size='+3'><b>Result Rest API Documentation</b>:</font>


# Data Type Description #
JSON Example:

```

	{
	    "attributes": null, 
	    "build": {
	        "buildId": "4fc68103c2e65daeca9221e1", 
	        "name": "SNAPSHOT"
	    }, 
	    "component": {
	        "code": "integration-tests", 
	        "id": "4fd645e6c2e6238224ec9195", 
	        "name": "Integration tests"
	    }, 
	    "config": {
	        "configId": "4fc68103c2e65daeca9221dd", 
	        "filename": "default.ini", 
	        "name": "Default Environment"
	    }, 
	    "configurationOverride": null, 
	    "extensions": null, 
	    "files": [], 
	    "history": null, 
	    "hostname": "corbett-mbpro.local", 
	    "id": "4ffc8f5ac2e6d67fd00f8f14", 
	    "log": [
	        {
	            "entryTime": 1341951834509, 
	            "exceptionClassName": null, 
	            "exceptionMessage": null, 
	            "exceptionStackTrace": null, 
	            "level": "INFO", 
	            "loggerName": "test.org.tcrun.integration.tests.tcapiselenium.TestDoubleClick", 
	            "message": "Configuration key 'browser.persistent' missing, using default 'true'."
	        }, 
	        {
	            "entryTime": 1341951834518, 
	            "exceptionClassName": null, 
	            "exceptionMessage": null, 
	            "exceptionStackTrace": null, 
	            "level": "INFO", 
	            "loggerName": "test.org.tcrun.integration.tests.tcapiselenium.TestDoubleClick", 
	            "message": "Configuration key 'browser' missing, using default 'ff'."
	        }, 
	        {
	            "entryTime": 1341951842199, 
	            "exceptionClassName": null, 
	            "exceptionMessage": null, 
	            "exceptionStackTrace": null, 
	            "level": "INFO", 
	            "loggerName": "test.org.tcrun.integration.tests.tcapiselenium.TestDoubleClick", 
	            "message": "Configuration key 'defaults.timeout' missing, using default '30'."
	        }, 
	        {
	            "entryTime": 1341951842208, 
	            "exceptionClassName": null, 
	            "exceptionMessage": null, 
	            "exceptionStackTrace": null, 
	            "level": "DEBUG", 
	            "loggerName": "test.org.tcrun.selenium.DefaultWebDriverWrapper", 
	            "message": "Going to page 'http://tcrunij.tcrun.googlecode.com/hg/tcrunij-integration/integration-tests/src/main/html/UglyTestPage.html'."
	        }, 
	        {
	            "entryTime": 1341951845961, 
	            "exceptionClassName": null, 
	            "exceptionMessage": null, 
	            "exceptionStackTrace": null, 
	            "level": "DEBUG", 
	            "loggerName": "test.org.tcrun.selenium.DefaultWebDriverWrapper", 
	            "message": "Waiting for page 'org.tcrun.integration.tests.tcapiselenium.pages.UglyTestPage' a max of 30 seconds."
	        }, 
	        {
	            "entryTime": 1341951846082, 
	            "exceptionClassName": null, 
	            "exceptionMessage": null, 
	            "exceptionStackTrace": null, 
	            "level": "INFO", 
	            "loggerName": "test.org.tcrun.selenium.DefaultWebDriverWrapper", 
	            "message": "Found page 'org.tcrun.integration.tests.tcapiselenium.pages.UglyTestPage' after 0 seconds."
	        }, 
	        {
	            "entryTime": 1341951846082, 
	            "exceptionClassName": null, 
	            "exceptionMessage": null, 
	            "exceptionStackTrace": null, 
	            "level": "DEBUG", 
	            "loggerName": "test.org.tcrun.selenium.DefaultWebDriverWrapper", 
	            "message": "Clicking on element with name 'Link to Double click test page.' and found 'In Frame 'menuframe' By.linkText: Double Click Test'."
	        }, 
	        {
	            "entryTime": 1341951846210, 
	            "exceptionClassName": null, 
	            "exceptionMessage": null, 
	            "exceptionStackTrace": null, 
	            "level": "DEBUG", 
	            "loggerName": "test.org.tcrun.selenium.DefaultWebDriverWrapper", 
	            "message": "Waiting for page 'org.tcrun.integration.tests.tcapiselenium.pages.DoubleClickTestPage' a max of 30 seconds."
	        }, 
	        {
	            "entryTime": 1341951846211, 
	            "exceptionClassName": null, 
	            "exceptionMessage": null, 
	            "exceptionStackTrace": null, 
	            "level": "DEBUG", 
	            "loggerName": "test.org.tcrun.selenium.DefaultWebDriverWrapper", 
	            "message": "Checking for existence of element 'Link which will cause our test to pass if we double click it.'."
	        }, 
	        {
	            "entryTime": 1341951847299, 
	            "exceptionClassName": null, 
	            "exceptionMessage": null, 
	            "exceptionStackTrace": null, 
	            "level": "DEBUG", 
	            "loggerName": "test.org.tcrun.selenium.DefaultWebDriverWrapper", 
	            "message": "Checking for existence of element 'Link which will cause our test to pass if we double click it.'."
	        }, 
	        {
	            "entryTime": 1341951847335, 
	            "exceptionClassName": null, 
	            "exceptionMessage": null, 
	            "exceptionStackTrace": null, 
	            "level": "INFO", 
	            "loggerName": "test.org.tcrun.selenium.DefaultWebDriverWrapper", 
	            "message": "Found page 'org.tcrun.integration.tests.tcapiselenium.pages.DoubleClickTestPage' after 1 seconds."
	        }, 
	        {
	            "entryTime": 1341951847336, 
	            "exceptionClassName": null, 
	            "exceptionMessage": null, 
	            "exceptionStackTrace": null, 
	            "level": "INFO", 
	            "loggerName": "test.org.tcrun.integration.tests.tcapiselenium.TestDoubleClick", 
	            "message": "Step 1 Description: Get the value of the result element."
	        }, 
	        {
	            "entryTime": 1341951847336, 
	            "exceptionClassName": null, 
	            "exceptionMessage": null, 
	            "exceptionStackTrace": null, 
	            "level": "INFO", 
	            "loggerName": "test.org.tcrun.integration.tests.tcapiselenium.TestDoubleClick", 
	            "message": "Step 1 Expected Result: Text of the element is NOT TESTED"
	        }, 
	        {
	            "entryTime": 1341951847337, 
	            "exceptionClassName": null, 
	            "exceptionMessage": null, 
	            "exceptionStackTrace": null, 
	            "level": "DEBUG", 
	            "loggerName": "test.org.tcrun.selenium.DefaultWebDriverWrapper", 
	            "message": "Getting text from element with name 'Result of the click test' and found 'In Frame 'contentframe' By.id: result'."
	        }, 
	        {
	            "entryTime": 1341951847408, 
	            "exceptionClassName": null, 
	            "exceptionMessage": null, 
	            "exceptionStackTrace": null, 
	            "level": "INFO", 
	            "loggerName": "test.org.tcrun.integration.tests.tcapiselenium.TestDoubleClick", 
	            "message": "Successful Check: actual value is 'NOT TESTED', expected value is \"NOT TESTED\""
	        }, 
	        {
	            "entryTime": 1341951847408, 
	            "exceptionClassName": null, 
	            "exceptionMessage": null, 
	            "exceptionStackTrace": null, 
	            "level": "INFO", 
	            "loggerName": "test.org.tcrun.integration.tests.tcapiselenium.TestDoubleClick", 
	            "message": "Step 2 Description: Click the test link once (not a double click)."
	        }, 
	        {
	            "entryTime": 1341951847408, 
	            "exceptionClassName": null, 
	            "exceptionMessage": null, 
	            "exceptionStackTrace": null, 
	            "level": "INFO", 
	            "loggerName": "test.org.tcrun.integration.tests.tcapiselenium.TestDoubleClick", 
	            "message": "Step 2 Expected Result: The text of the result element should be FAIL."
	        }, 
	        {
	            "entryTime": 1341951847408, 
	            "exceptionClassName": null, 
	            "exceptionMessage": null, 
	            "exceptionStackTrace": null, 
	            "level": "DEBUG", 
	            "loggerName": "test.org.tcrun.selenium.DefaultWebDriverWrapper", 
	            "message": "Clicking on element with name 'Link which will cause our test to pass if we double click it.' and found 'In Frame 'contentframe' By.id: dblclick-link'."
	        }, 
	        {
	            "entryTime": 1341951848025, 
	            "exceptionClassName": null, 
	            "exceptionMessage": null, 
	            "exceptionStackTrace": null, 
	            "level": "DEBUG", 
	            "loggerName": "test.org.tcrun.selenium.DefaultWebDriverWrapper", 
	            "message": "Getting text from element with name 'Result of the click test' and found 'In Frame 'contentframe' By.id: result'."
	        }, 
	        {
	            "entryTime": 1341951848086, 
	            "exceptionClassName": null, 
	            "exceptionMessage": null, 
	            "exceptionStackTrace": null, 
	            "level": "INFO", 
	            "loggerName": "test.org.tcrun.integration.tests.tcapiselenium.TestDoubleClick", 
	            "message": "Successful Check: actual value is 'FAIL', expected value is \"FAIL\""
	        }, 
	        {
	            "entryTime": 1341951848086, 
	            "exceptionClassName": null, 
	            "exceptionMessage": null, 
	            "exceptionStackTrace": null, 
	            "level": "INFO", 
	            "loggerName": "test.org.tcrun.integration.tests.tcapiselenium.TestDoubleClick", 
	            "message": "Step 3 Description: Double click the test link."
	        }, 
	        {
	            "entryTime": 1341951848087, 
	            "exceptionClassName": null, 
	            "exceptionMessage": null, 
	            "exceptionStackTrace": null, 
	            "level": "INFO", 
	            "loggerName": "test.org.tcrun.integration.tests.tcapiselenium.TestDoubleClick", 
	            "message": "Step 3 Expected Result: The text of the result element should change to PASS."
	        }, 
	        {
	            "entryTime": 1341951848125, 
	            "exceptionClassName": null, 
	            "exceptionMessage": null, 
	            "exceptionStackTrace": null, 
	            "level": "DEBUG", 
	            "loggerName": "test.org.tcrun.selenium.DefaultWebDriverWrapper", 
	            "message": "Double clicking element 'Link which will cause our test to pass if we double click it.' located by 'In Frame 'contentframe' By.id: dblclick-link'."
	        }, 
	        {
	            "entryTime": 1341951848685, 
	            "exceptionClassName": null, 
	            "exceptionMessage": null, 
	            "exceptionStackTrace": null, 
	            "level": "DEBUG", 
	            "loggerName": "test.org.tcrun.selenium.DefaultWebDriverWrapper", 
	            "message": "Getting text from element with name 'Result of the click test' and found 'In Frame 'contentframe' By.id: result'."
	        }, 
	        {
	            "entryTime": 1341951848757, 
	            "exceptionClassName": null, 
	            "exceptionMessage": null, 
	            "exceptionStackTrace": null, 
	            "level": "INFO", 
	            "loggerName": "test.org.tcrun.integration.tests.tcapiselenium.TestDoubleClick", 
	            "message": "Successful Check: actual value is 'PASS', expected value is \"PASS\""
	        }, 
	        {
	            "entryTime": 1341951848758, 
	            "exceptionClassName": null, 
	            "exceptionMessage": null, 
	            "exceptionStackTrace": null, 
	            "level": "INFO", 
	            "loggerName": "test.org.tcrun.integration.tests.tcapiselenium.TestDoubleClick", 
	            "message": "Configuration key 'browser.persistent' missing, using default 'true'."
	        }
	    ], 
	    "project": {
	        "id": "4fc68103c2e65daeca9221dc", 
	        "name": "Slickij Developer Project"
	    }, 
	    "reason": "Test returned PASS from doTest method.", 
	    "recorded": 1341951848772, 
	    "release": {
	        "name": "1.0.0", 
	        "releaseId": "4fc68103c2e65daeca9221e0"
	    }, 
	    "runlength": 14296, 
	    "runstatus": "FINISHED", 
	    "status": "PASS", 
	    "testcase": {
	        "automationId": "org.tcrun.integration.tests.tcapiselenium.TestDoubleClick", 
	        "automationKey": "cf5f4aa5-ce5b-4c41-92d1-779a812f55c1", 
	        "automationTool": "tcrunij", 
	        "name": "Test Double Click", 
	        "testcaseId": "4fd645e6c2e6238224ec9196"
	    }, 
	    "testrun": {
	        "name": "TCRunIJ Run of all available tests", 
	        "testrunId": "4ffc8f5ac2e6d67fd00f8f13"
	}

}

```

**`attributes`** is a key -> value map of strings that can be set to whatever you wish.  It does not affect Slick's runtime or UI in any way (though they could be displayed).

**`reason`** is a string which can display first and formost the reason for the result.  If the result is FAIL or BROKEN\_TEST it is appropriate to put the exception and/or the stack trace in this field.

**`runlength`** is a time recorded in milliseconds.

**`files`** is a list of stored files already created in slick.  See [/api/files](StoredFileResource.md) documentation for more information.

# Rest Endpoints #

## /api/results ##

---


### GET ###

Get a list of results that match the query parameters provided.  Available parameters are:

  * **testrunid**: Get all the results that have the provided testrun id set as their testrun.testrunId property.
  * **status**: Only get the results with the status set as the provided status.
  * **excludestatus**: Get all the matching results that do not have their status as the one provided.
  * **runstatus**: Only get results that have the specified runstatus (available runstatus is TO\_BE\_RUN, RUNNING, and FINISHED)
  * **allfields**: Without allfields set to true, the **`log`**, **`release`**, **`build`**, **`project`**, **`testrun`**, and **`config`** properties won't be returned.

Example:

```sh

curl -s http://localhost:8080/api/results?testrunid=4ffc8f5ac2e6d67fd00f8f14
```

### POST ###

Create a new result with the provided fields in JSON.  You must specify the following properties:

  * **`status`**: status must be set to one of: **`PASS`**, **`FAIL`**, **`NOT_TESTED`**, **`NO_RESULT`**, **`BROKEN_TEST`**, **`SKIPPED`**, **`CANCELLED`**
  * **`testcase`**: testcase must have at least one property set that will enable it to be found, all other properties will be filled in after slick finds the test.  One of these properties must be set to reference an already created testcase: **`testcaseId`**, **`name`**, **`automationId`**, **`automationKey`**, **`automationTool`**

The following properties will be set for you if you do not provide them:

  * **`project`**: If not provided the project will be set to the one provided by the testcase referenced.
  * **`component`**: If not provided the project will be set to the one provided by the testcase referenced.
  * **`release`**: If the project is not null (either provided and found, or testcase had it set and it was found), the default release from the project is used.
  * **`build`**: If the project is not null (either provided and found, or testcase had it set and it was found), the default build from the project is used.
  * **`component`**: If not provided the project will be set to the one provided by the testcase referenced.
  * **`testrun`**: If you do not provide a testrun, one will be created new for this result.  The config, release, build, and project properties are copied over to the testrun if created new.
  * **`recorded`**: The current date and time on the server is used if not provided.
  * **`runstatus`**: If your result status is set to **`NO_RESULT`** then the runstatus is set to **`RUNNING`**, otherwise the runstatus is set to **`FINISHED`**

If you provide a **`hostname`** field, then a hoststatus object is created or updated for that host.  If your runstatus is set to **`RUNNING`** the **`currentWork`** of the hoststatus is set to this result, otherwise the **`currentWork`** property is set to null.  Also the lastCheckin is set to the current time.

The result with all updated properties is returned.

Example:

```sh

curl -s -X POST -H 'Content-Type: application/json' -d @result.json http://localhost:8080/api/results
```

## /api/results/summary/bybuild/{buildid} ##

---


### GET ###

Get a summary (a count of results by status) for every test run against a particular build.  Please note that it might be quicker to get a list of testruns by build, and add up the summary information stored in the testrun.

Example:

```sh

curl -s http://localhost:8080/api/results/summary/bybuild/4fc68103c2e65daeca9221e1
```

## /api/results/nextToBeRun ##

---


### POST ###

This method provides a way for an agent to be created that get's tests to run one by one.  It is the current way that scheduling of tests works in slick.

First the POST data must include:
  * **``**:
  * **``**:
  * **``**:
  * **``**:

Example:

```sh

curl -s
```

## /api/results/{resultid} ##

---


### GET ###

Example:

```sh

curl -s
```

### DELETE ###

Example:

```sh

curl -s
```

### PUT ###

Example:

```sh

curl -s
```

## /api/results/{resultid}/cancel ##

---


### POST ###

Example:

```sh

curl -s
```

## /api/results/{resultid}/reschedule ##

---


### POST ###

Example:

```sh

curl -s
```

## /api/results/{resultid}/log ##

---


### POST ###

Example:

```sh

curl -s
```