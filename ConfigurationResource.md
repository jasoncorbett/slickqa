<font size='+3'><b>Configuration Rest API Documentation</b>:</font>


# Data Type Description #

The configuration data type is used as a generic place to store different types of key=value data.  The data could be for programmatic configuration of slick itself, user preferences, or for data given to automation to configure test cases.  The `type` property indicates what the configuration is for.  It is a plain string, but several standard "types" are already in use: "PROJECT", "ENVIRONMENT", "USER".

The following is a listing of the properties contained in configuration.  Properties marked as required mean that those properties are required when creating a new instance:

  * **id**: an ObjectId (on the server side, and a String when serialized to JSON) assigned when saving the configuration
  * **name (required)**: A friendly name for this configuration (String).
  * **type**: A String detailing the "type" of configuration data.  It could be "USER" for user configuration data, "PLUGIN" for plugin configuration data, "ENVIRONMENT" for environment configuration data.
  * **filename**: an optional name of a file this configuration came from (if it came from one)
  * **configurationData**: A Map of key=>value configuration strings

JSON Example:

```
	{
		"name": "Environment: QA",
		"type": "ENVIRONMENT",
		"filename": "qa.ini",
		"configurationData": 
		{
			"URL.base": "http://code.google.com/p/slickqa",
			"URL.source": "http://code.google.com/p/slickqa/source/checkout"
		}
	}
```

# Rest Endpoints #

## /api/configurations ##

---


### GET ###

This will return a list of configuration objects found in slick.  If no query parameters are included in the URL then all configuration objects will be returned.  You can specify the values for the following properties using query parameters:

  * **name**: A string containing part of or all of the name of the configuration.
  * **configurationType**: A type such as "PROJECT", "ENVIRONMENT", "USER".
  * **filename**: A string containing the value of the filename field.

Example:

```sh

curl -s http://localhost:8080/slickij-war/api/configurations?filename=qa.ini |python -m json.tool
```

### POST ###

This will create a new configuration object using the json data provided in the post.
As with all JSON requests make sure you set the Content-Type header to application/json.
**DO NOT** provide the id field, one will be generated and returned if the creation was successful.

Example:

```sh

curl -s -X POST -H 'Content-Type: application/json' -d '{ "name": "Environment: Stage", "type": "ENVIRONMENT", "filename": "stage.properties", "configurationData": {"foo": "bar"} }' http://localhost:8080/slickij-war/api/configurations |python -m json.tool
```

## /api/configurations/{configurationId} ##

---


### GET ###

Retrieve a specific configuration using it's id field as the key.  This is useful if you have a reference from another object (like a [Testplan](TestplanResource.md) or a [Testrun](TestrunResource.md)).

Example:

```sh

curl -s http://localhost:8080/slickij-war/api/configurations/4e80f546c3da70694cd5d6e8 |python -m json.tool
```

### PUT ###

This allows you to make changes to the configuration object.  You can't change the id field, but any other field you specify will be replaced with the value provided in the PUT request.
You only need to put the properties which you wish to replace.
As with all JSON requests make sure you set the Content-Type header to application/json.

Example:

```sh

curl -s -X PUT -H 'Content-Type: application/json' -d '{ "filename": "stage.ini" }' http://localhost:8080/slickij-war/api/configurations/4e80f546c3da70694cd5d6e8 |python -m json.tool
```

### DELETE ###

This will allow you to delete a configuration resource.  THIS IS PERMANANT!  Unlike testcases, configurations are deleted, not just marked for deletion.

Example:

```sh

curl -s -X DELETE http://localhost:8080/slickij-war/api/configurations/4e80f546c3da70694cd5d6e8
```

## /api/configurations/{configurationId}/configurationData ##

---


### POST ###

This endpoint allows you to ADD / OVERWRITE the configuration data of a configuration object.  If all you want to do is add a few keys to the configuration, this is how to easily do it.
Do not use this to remove a key (set it to null), instead use the DELETE method below.
As with all JSON requests make sure you set the Content-Type header to application/json.

Example:

```sh

curl -s -X POST -H 'Content-Type: application/json' -d '{ "hello": "world", "this": "that" }' http://localhost:8080/slickij-war/api/configurations/4e80f546c3da70694cd5d6e8/configurationData |python -m json.tool
```

## /api/configurations/{configurationId}/configurationData/{key} ##

---


### DELETE ###

Delete a specific key from the configuration data of a configuration object.  The configuration object (minus that key) is returned.

Example:

```sh

curl -s -X DELETE http://localhost:8080/slickij-war/api/configurations/4e80f546c3da70694cd5d6e8/configurationData/hello |python -m json.tool
```