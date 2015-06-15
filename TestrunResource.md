<font size='+3'><b>Testrun Rest API Documentation</b>:</font>


# Data Type Description #
JSON Example:

```
	{
	    "build": {
	        "buildId": "4fc68103c2e65daeca9221e1", 
	        "name": "SNAPSHOT"
	    }, 
	    "config": {
	        "configId": "4fc68103c2e65daeca9221dd", 
	        "filename": null, 
	        "name": "Default Environment"
	    }, 
	    "dateCreated": 1343698420804, 
	    "extensions": [], 
	    "id": "501735f4c2e6c62a698025f7", 
	    "name": "Testrun for testplan All TCRunIJ Tests", 
	    "project": {
	        "id": "4fc68103c2e65daeca9221dc", 
	        "name": "Slickij Developer Project"
	    }, 
	    "release": {
	        "name": "1.0.0", 
	        "releaseId": "4fc68103c2e65daeca9221e0"
	    }, 
	    "runtimeOptions": null, 
	    "summary": {
	        "resultsByStatus": {
	            "NO_RESULT": 9
	        }, 
	        "statusListOrdered": [
	            "NO_RESULT"
	        ], 
	        "total": 9, 
	        "totalTime": 0
	    }, 
		"testplanId": "4ff785f0c2e622bff42e2345",
	    "testplan": {
			...
		}
	}
```

The testplan property is a read only property (you do not provide it when creating or updating a testrun).  It is simply the testplan linked to by the testplanId.

The summary is also readonly, and is updated as you provide results linking to the testrun.

# Rest Endpoints #

## /api/testruns ##

---


### GET ###

Fetch a list of matching testruns.  The available query parameters are listed below.

  * **projectid**: only return testruns belonging to a project with the specified id
  * **releaseid**: only return testruns reported against a release with the specified id
  * **buildid**: only return testruns reported against a build with the specified id
  * **createdAfter**: only return testruns created after a specified date.  The date or time can be specified in several formats.  See [this](http://docs.oracle.com/javase/6/docs/api/java/text/DateFormat.html#parse(java.lang.String,%20java.text.ParsePosition)) for more information.
  * **configid**: only return testruns that are set with the configuration with the specified id
  * **testplanid**: only return testruns that have the specified testplanId
  * **configName**: only return testruns that are set with the configuration with the specified name
  * **projectName**: only return testruns belonging to a project with the specified name
  * **releaseName**: only return testruns reported against a release with the specified name
  * **buildName**: only return testruns reported against a build with the specified name
  * **name**: only return testruns with the name matching the one specified
  * **limit**: limit the number of testruns returned

Example:

```sh

curl -s 'http://localhost:8080/api/testruns?releaseName=1.0.0&limit=10'
```

### POST ###

Create a new testrun.  You need to specify name, but project, release and build are recommended as they help in having the testrun show up in reports.  A testplanid can link several testruns together.

Example:

```sh

curl -s -X POST -H 'Content-Type: application/json' -d @testrun.json http://localhost:8080/api/testruns
```

## /api/testruns/{testrunid} ##

---


### GET ###

Retrieve a specific testrun.

Example:

```sh

curl -s http://localhost:8080/api/testruns/501735f4c2e6c62a698025f7
```

### PUT ###

Change specific properties of a testrun.  You cannot change the readonly properties.  You only have to specify the properties you wish to change.

Example:

```sh

curl -s -X PUT -H 'Content-Type: application/json' -d '{"name": "I am changing the name of the testrun"}' http://localhost:8080/api/testruns/501735f4c2e6c62a698025f7
```

### DELETE ###

Delete a testrun.  Note that this **does not** automatically delete the results associated with the testrun.

Example:

```sh

curl -s -X DELETE http://localhost:8080/api/testruns/501735f4c2e6c62a698025f7
```

## /api/testruns/{testrunid}/reschedule-{status} ##

---


### POST ###

Reschedule all results with the given status.  Rescheduling means changing the run-status to `TO_BE_RUN`, changing the status to `NO_RESULT`, and removing any logs and files associated with the result.

No post data is required, the testrun is returned (with the summary updated).

Example:

```sh

curl -s -X POST http://localhost:8080/api/testruns/501735f4c2e6c62a698025f7/reschedule-FAIL
```