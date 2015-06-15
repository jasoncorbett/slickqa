<font size='+3'><b>Testplan Rest API Documentation</b>:</font>


# Data Type Description #
JSON Example:

```
	{
		"createdBy": "Jason Corbett", 
		"isprivate": false, 
		"name": "Example Testplan", 
		"project": {
			"id": "4dacb02834bf1d1a920cf28b"
			"name": "Foo Project"
		}, 
		"queries": [
			{
				"name": "Tests that have an automationId starting with org.tcrun.example", 
				"query": {
					"className": "org.tcrun.slickij.api.data.testqueries.FieldStartsWith", 
					"fieldName": "automationId", 
					"fieldValue": "org.tcrun.example"
				}
			},
			{
				"name": "Tests marked with tag regression", 
				"query": {
					"className": "org.tcrun.slickij.api.data.testqueries.ContainsTags", 
					"tagnames": [
						"regression"
					]
				}
			}
		]
	}
```

A testplan is made to be created by individual users and shared.  Currently there is no way for users to log on to slick individaully, making the `createdBy` and `isprivate` less important.  **`isprivate`** should always be false until a multi-user system in slick is created.

A testplan is made up of multiple test queries.  All queries results are joined together.  If duplicates are in the various queries, they will be kept in the testplan.  No special exclusion is performed.

You can create testplans with an empty set of queries if you are using them only for history tracking and not for scheduling.

Below is a list of the available queries, and the parameters they need.

## Test Queries ##

All the test queries have a package of **`org.tcrun.slickij.api.data.testqueries`**.  The query needs to have a name, and a query section, and the query object needs to have a className (with the `org.tcrun.slickij.api.data.testqueries` package and a class name from below).  The individual properties of each type of query are listed with the classes below.

  * **BelongsToComponent**: Query for all tests that have the specified component set.
    * **componentId**: The component id of the inteded component.
  * **BelongsToProject**: Query for all tests that belong to a particular project.
    * **projectId**: The id of the project that you want to find tests for.
  * **ContainsTags**: Find all tests that contain each of the specified tags.
    * **tagnames**: A list of tag names (strings) that each matching test should contain.
  * **DoesNotContainTags**: Find all tests that do not contain the specified tags.
    * **tagnames**: A list of tag names (strings) that each test should NOT have.
  * **FieldContains**: Query for all tests that have a matching attribute name and value.
    * **attributeName**: a string containing the name of the attribute
  * **attributeValue**: a string containing the intended value of the attribute
    * **IsAutomated**: Query for all tests that have the automated flag set to **`true`**.
    * **IsNotAutomated**: Query for all tests that have the automated flag set to **`false`**.
    * **IsNotTestcase**: Find all tests that are not the test provided in the query.
      * **testcaseId**: The id of the test.
    * **IsTestcase**: Query for the test that is the test provided in the query.
      * **ref**: A testcase reference of the intended test, you can specify any or all of the following values.  The values are in order of how they will be used.  The first value that is non-null will be used for the query.
      * **testcaseId**: the id of the testcase
      * **automationKey**: the value of the automationKey property
      * **automationId**: the value of the automationId property
      * **name**: the name of the testcase (exact)
    * **RequireAll**: A way of doing an **`AND`** of several queiries.
      * **criteria**: A list of queries (not **named** test queries).
    * **RequireAny**: A way of doing an **`OR`** of several queiries.
      * **criteria**: A list of queries (not **named** test queries).

Here is an example of a RequireAll and RequireAny:

```
	{
		"createdBy": "Jason Corbett", 
		"isprivate": false, 
		"name": "Example Testplan", 
		"project": {
			"id": "4dacb02834bf1d1a920cf28b"
			"name": "Foo Project"
		}, 
		"queries": [
			{
				"name": "Example of RequireAny",
				"query": {
					"className": "org.tcrun.slickij.api.data.testqueries.RequireAny", 
					"criteria": [
						{
							"name": "Tests that have an automationId starting with org.tcrun.example", 
							"query": {
								"className": "org.tcrun.slickij.api.data.testqueries.FieldStartsWith", 
								"fieldName": "automationId", 
								"fieldValue": "org.tcrun.example"
							}
						},
						{
							"name": "Tests marked with tag regression", 
							"query": {
								"className": "org.tcrun.slickij.api.data.testqueries.ContainsTags", 
								"tagnames": [
									"regression"
								]
							}
						}
					]
				}
			},
			{
				"name": "Example of RequireAll",
				"query": {
					"className": "org.tcrun.slickij.api.data.testqueries.RequireAll", 
					"criteria": [
						{
							"name": "Tests that have an automationId starting with org.tcrun.example", 
							"query": {
								"className": "org.tcrun.slickij.api.data.testqueries.FieldStartsWith", 
								"fieldName": "automationId", 
								"fieldValue": "org.tcrun.example"
							}
						},
						{
							"name": "Tests marked with tag regression", 
							"query": {
								"className": "org.tcrun.slickij.api.data.testqueries.ContainsTags", 
								"tagnames": [
									"regression"
								]
							}
						}
					]
				}
			},

		]
	}
```

# Rest Endpoints #

## /api/testplans ##

---


### GET ###

Get a list of testplans available for a particular project.  You must provide a query parameter for projectid.

Query Parameter Options:
  * **projectid**(**`required`**): the id of the project to which the testplans should belong.
  * **createdby**: (optional) if provided, only the testplans with a matching **`createdBy`** property will be returned.

Example:

```sh

curl -s http://localhost:8080/api/testplans?projectid=4dacb02834bf1d1a920cf28b
```

### POST ###

Create a new testplan.  You must supply a name, and projectid.  If the testplan is marked private, the createdBy property cannot be null.

Example:

This example assumes that the JSON for the testplan is contained in a file called testplan.json in the current working directory.

```sh

curl -s -X POST -H 'Content-Type: application/json' -d @testplan.json http://localhost:8080/api/testplans
```

## /api/testplans/{testplanid} ##

---


### GET ###

Retrieve a specific testplan by it's id property.

Example:

```sh

curl -s http://localhost:8080/api/testplans/4ff785f0c2e622bff42e2345
```

### PUT ###

Modify an existing testplan.  Only the properties you wish to modify are required to be provided in the JSON.

Example:

```sh

curl -s -X PUT -H 'Content-Type: application/json' -d '{"name": "I am changing the name property"}' http://localhost:8080/api/testplans/4ff785f0c2e622bff42e2345
```

### DELETE ###

Delete a testplan.  Unlike testcases, testplans are deleted fully when this method is called.  References to testplans may exist in testruns after testplans are removed.

Example:

```sh

curl -s -X DELETE http://localhost:8080/api/testplans/4ff785f0c2e622bff42e2345
```

## /api/testplans/{testplanid}/run ##

---


### GET ###

Schedule or **run** a testplan.  This will create a result for all matching testcases in a testplan, set the status to `NO_RESULT`, and add them to a testrun linked to the testplan.  The testrun is returned.  The release and build for the testrun will be the default release and build for the project.

Example:

```sh

curl -s http://localhost:8080/api/testplans/4ff785f0c2e622bff42e2345/run
```

### POST ###

Schedule or **run** a testplan.  This will create a result for all matching testcases in a testplan, set the status to `NO_RESULT`, and add them to a testrun linked to the testplan.  The testrun is returned.  The release, build, and runtime options for the testrun can be provided by the JSON data in the POST.  The format is listed below.

Example data for scheduling a testplan:

```
	{
		"config": {
			"configId": "9a3785f0c2e452bff42a2349",
			"name": "Default Environment",
			"filename": "default.ini"
		},
		"runtimeOptions": {
			"configId": "2e93c8f0c2eb826ac7f11799",
			"name": "Firefox Browser",
		},
		"release": {
			"name": "1.0.0", 
			"releaseId": "4fc68103c2e65daeca9221e0"
		},
		"build": {
			"buildId": "4fc68103c2e65daeca9221e1", 
			"name": "SNAPSHOT"
		},
		"overrides": [
			{
				"key": "foo",
				"value": "bar",
				"requirement": false
			}
		]
	}
```

To understand the run parameters you must understand what configuration data means to a result.  In this case configuration data from both a configuration object (map of key->value parameters) and from the result itself can be provided to the automation running the test.  Most of these parameters are for customizing the configuration that goes into the result, and therefore to the automation running the test linked to the result.

Explanation of the run parameters:
  * **config**: a reference to the base configuration to provide to the result.  This is provided in the result as a reference and is normally a configuration object with type `ENVIRONMENT`.
  * **runtimeOptions**: This is another configuration object which can store "overrides" or runtime options in addition to, or overriding the base configuration.
  * **release**: A reference to the release the testrun and results should link to.
  * **build**: A reference to the build the testrun and results should link to.
  * **overrides**: A list of key value parameters that can override or add to the base configuration available to the result.

Example:

In this example the running options for the testplan is contained in a file called runtestplan.json:

```sh

curl -s -X POST -H 'Content-Type: application/json' -d @runtestplan.json http://localhost:8080/api/testplans/4ff785f0c2e622bff42e2345/run
```

## /api/testplans/{testplanid}/testcases ##

---


### GET ###

Get a list of testcases that are part of this testplan (meaning that the queries in the testplan match these testcases). Returned is a list of testcase objects.

Example:

```sh

curl -s http://localhost:8080/api/testplans/4ff785f0c2e622bff42e2345/testcases
```

## /api/testplans/{testplanid}/testcount ##

---


### GET ###

Get a count of testcases in the testplan.  Instead of returning all the testcases, then counting them, you can just call this method and it will return a count of tests that match the queries in the testplan.

The return value is an integer.

Example:

```sh

curl -s http://localhost:8080/api/testplans/4ff785f0c2e622bff42e2345/testcount
```