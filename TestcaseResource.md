<font size='+3'><b>Testcase Rest API Documentation</b>:</font>


# Data Type Description #
JSON Example:

```
	{
		"name": "A simple testcase",
		"purpose": "To document the properties of a testcase",
		"requirements": "The ability to read",
		"steps": [
			{
				"name": "The first step",
				"expectedResult": "To be done correctly"
			}
			{
				"name": "The second step",
				"expectedResult": "Should be done now"
			}
		],
		"author": "Jason Corbett",
		"attributes": {
			"these": "are mostly ignored",
			"by": "slick",
			"they": "can store whatever you want"
		},
		"automated": true,
		"automationPriority": 0,
		"automationTool": "TestNG",
		"automationConfiguration": "This area to be used by an automation client, slick doesn't use it",
		"automationId": "org.tcrun.slick.tests.SimpleTest#method",
		"automationKey": "02836e4c-cc96-4330-92bd-99ae224f46d9",
		"tags": ["simple", "footag"],
		"project": {
			"name": "Slickij Developer Project",
			"id": "4e8244882ad97069b7077ad4"
		},
		"component": {
			"name": "REST APIs",
			"id": "4e8244892ad97069b9077ad4",
			"code": "rest"
		}
	}
```

The only field required is `name`, but it's best to have project, and component to help with organization.  The only automation field slick uses is `automationTool`, however that can be any string.  The other automation properties can be used by the automation tool to help identify the testcase, or configure it.  Tags can also help you locate specific tests, or build dynamic testplans.  Tags are sometimes called groups by other tools.

# Rest Endpoints #

## /api/testcases ##

---


### GET ###

Get a list of matching tests.  The available query parameters are:
  * projectid
  * componentid
  * automationKey
  * automationId
  * automationTool
  * tag
  * automated (any value other than `true` means false)
  * author
  * namecontains
  * name (exact match)

Example:

```sh

curl -s http://localhost:8080/api/testcases?namecontains=foo
```

### POST ###

Create a new testcase.  The return value will be the saved testcase, the primary difference being that the return value will have an id.

Example:

In this example the example JSON from above was saved to a file called `testcase.json`.

```sh

curl -s -X POST -H 'Content-Type: application/json' -d @testcase.json http://localhost:8080/api/testcases
```

## /api/testcases/{testcaseid} ##

---


### GET ###

Get a specific testcase by it's id.

Example:

```sh

curl -s http://localhost:8080/api/testcases/4fd64623c2e6238224ec91a4
```

### PUT ###

Change one or more properties of a testcase.  You do not need to include every property of the testcase, only those properties you wish to change.

Example:

This example would change the tags property of the testcase to the tags `foo` and `bar`.

```sh

curl -s -X PUT -H 'Content-Type: application/json' -d '{"tags": ["foo", "bar"]}' http://localhost:8080/api/testcases/4fd64623c2e6238224ec91a4
```

### DELETE ###

Delete a testcase, or more specifically mark it as deleted.  Tests aren't actually deleted, they just have a property called deleted, that is set to true.  All the queries for tests (at least the ones that query for a list of tests) will filter out deleted tests.
Example:

```sh

curl -s -X DELETE http://localhost:8080/api/testcases/4fd64623c2e6238224ec91a4
```

## /api/testcases/query ##

---


### POST ###

TestcaseQuery objects are part of a testplan, this call is available so you can test a query before adding it to a testplan.  For finding testcases usually the `GET` on `api/testcases` with query parameters should suffice.  For more information, see [/api/testplans rest endpoint](TestplanResource.md) for more information.

This endpoint will return a list of testcases matching the query posted.

Example:

```sh

curl -s -X POST -H 'ContentType: application/json' -d @query.json http://localhost:8080/api/testcases/query
```
