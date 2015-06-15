<font size='+3'><b>Projects Rest API Documentation</b>:</font>


# Data Type Description #

The following is an example of the JSON returned in a project object.

JSON Example:

```
	    {
	        "attributes": {}, 
	        "automationTools": [
	            "tcrunij", 
	            "tcrun", 
	            "Shell Script"
	        ], 
	        "components": [
	            {
	                "code": "dataext", 
	                "description": null, 
	                "id": "4e8244882ad97069b6077ad4", 
	                "name": "Data Extensions"
	            }, 
	            {
	                "code": "web-ui", 
	                "description": null, 
	                "id": "4e8244892ad97069b8077ad4", 
	                "name": "HTML Web UI"
	            }, 
	            {
	                "code": "rest", 
	                "description": null, 
	                "id": "4e8244892ad97069b9077ad4", 
	                "name": "REST APIs"
	            }
	        ], 
	        "configuration": {
	            "configurationData": {}, 
	            "configurationType": "PROJECT", 
	            "filename": null, 
	            "id": "4e8244882ad97069b5077ad4", 
	            "name": "Slickij Developer Project's Project Configuration"
	        }, 
	        "datadrivenProperties": [], 
	        "defaultRelease": "4e8244892ad97069ba077ad4", 
	        "description": "A Project to be used by slickij developers to test features.", 
	        "extensions": [], 
	        "id": "4e8244882ad97069b7077ad4", 
	        "lastUpdated": 1317160073504, 
	        "name": "Slickij Developer Project", 
	        "releases": [
	            {
	                "builds": [
	                    {
	                        "built": 1317160073477, 
	                        "id": "4e8244892ad97069bb077ad4", 
	                        "name": "SNAPSHOT"
	                    }
	                ], 
	                "defaultBuild": "4e8244892ad97069bb077ad4", 
	                "id": "4e8244892ad97069ba077ad4", 
	                "name": "1.0.0", 
	                "target": 1304269945716
	            }
	        ], 
	        "tags": [
	            "basics", 
	            "api", 
	            "negative"
	        ]
	    }
```

# Rest Endpoints #

## /api/projects ##

---


### GET ###
This will return a list of project objects found in Slick.

Example:

```sh

curl -s http://localhost:8080/slickij-war/api/projects | python -m json.tool
```

### POST ###
Adds a new project to Slick

Example:
```sh

curl -s --digest -u <username>:<password> -X POST -H 'Content-Type: application/json' -d '{"name": "My Testing Project", "description": "All manual and automated tests for the My Testing Project", "tags": ["functional", "api", "negative"], "automationTools": ["tcrunij", "curl", "Shell Script"], "components": [{"name": "Data Extensions", "code": "dataext"}]}' "http://localhost:8080/slickij/api/projects"
```

## /api/projects/{id} ##

---


### GET ###
Retrieve a specific project object using it's id field as the key.  This is useful if you have a reference from another object like a release, configuration, or component object.

Example:
```sh

curl -s http://localhost:8080/slickij-war/api/projects/4e8244882ad97069b7077ad4 | python -m json.tool
```

## /api/projects/byname/{name} ##

---


### GET ###
Retrieve a specific project object using the project's name property. Be sure to use proper URL encoding if the project name contains characters like a space.

Example:
```sh

curl -s http://localhost:8080/slickij-war/api/projects/byname/Slickij%20Developer%20Project | python -m json.tool
```

## /api/projects/{id} ##

---


### DELETE ###
Removes a project from Slick

Example:
```sh

curl -s --digest -u <username>:<password> -X DELETE "http://localhost:8080/slickij/api/projects/4e8244882ad97069b7077ad4 | python -m json.tool"
```

## /api/projects/{id}/name ##

---


### PUT ###
Change the name of the Slick project with {id}

Example:
```sh

curls -s | python -m json.tool
```

## /api/projects/{id}/description ##

---


### PUT ###
Change the description of the Slick project with {id}

Example:
```sh

curls -s | python -m json.tool
```

## /api/projects/{id}/attributes ##

---


### GET ###

Example:
```sh

curls -s | python -m json.tool
```

### POST ###

Example:
```sh

curls -s | python -m json.tool
```

## /api/projects/{id}/attributes/{attributeName} ##

---


### DELETE ###

Example:
```sh

curls -s | python -m json.tool
```

## /api/projects/{id}/releases ##

---


### POST ###

Example:
```sh

curls -s | python -m json.tool
```

### GET ###

Example:
```sh

curls -s | python -m json.tool
```

## /api/projects/{projectid}/releases/{releaseid} ##

---


### GET ###

Example:
```sh

curls -s | python -m json.tool
```

## /api/projects/{id}/releases/default ##

---


### GET ###

Example:
```sh

curls -s | python -m json.tool
```

## /api/projects/{projectid}/setdefaultrelease/{releaseid} ##

---


### GET ###

Example:
```sh

curls -s | python -m json.tool
```

## /api/projects/{projectid}/releases/{releaseid} ##

---


### PUT ###

Example:
```sh

curls -s | python -m json.tool
```

### DELETE ###

Example:
```sh

curls -s | python -m json.tool
```

## /api/projects/{projectid}/releases/{releaseid}/builds ##

---


### GET ###

Example:
```sh

curl -s | python -m json.tool
```

### POST ###

Example:
```sh

curl -s -X POST -H 'Content-type: application/json' -d '{"name": "a build name"}' http://localhost:8080/slickij-war/api/projects/4e8244882ad97069b7077ad4/releases/502290e0e4b0e2eca8e21c78/builds | python -m json.tool
```

## /api/projects/{projectid}/releases/{releaseid}/builds/{buildid} ##

---


### GET ###

Example:
```sh

curls -s | python -m json.tool
```

## /api/projects/{projectid}/releases/{releaseid}/builds/default ##

---


### GET ###

Example:
```sh

curls -s | python -m json.tool
```

## /api/projects/{projectid}/releases/{releaseid}/setdefaultbuild/{buildid} ##

---


### GET ###

Example:
```sh

curls -s | python -m json.tool
```

## /api/projects/{projectid}/releases/{releaseid}/builds/{buildid} ##

---


### PUT ###

Example:
```sh

curls -s | python -m json.tool
```

### DELETE ###

Example:
```sh

curls -s | python -m json.tool
```

## /api/projects/{projectid}/components ##

---


### POST ###

Example:
```sh

curls -s | python -m json.tool
```

### GET ###

Example:
```sh

curls -s | python -m json.tool
```

## /api/projects/{projectid}/components/{componentid} ##

---


### GET ###

Example:
```sh

curls -s | python -m json.tool
```

### PUT ###

Example:
```sh

curls -s | python -m json.tool
```

### DELETE ###

Example:
```sh

curls -s | python -m json.tool
```

## /api/projects/{projectid}/tags ##

---


### GET ###

Example:
```sh

curls -s | python -m json.tool
```

### POST ###

Example:
```sh

curls -s | python -m json.tool
```

### DELETE ###

Example:
```sh

curls -s | python -m json.tool
```

## /api/projects/{projectid}/automationTools ##

---


### GET ###

Example:
```sh

curls -s | python -m json.tool
```

### POST ###

Example:
```sh

curls -s | python -m json.tool
```

## /api/projects/{projectid}/automationTools/{automationTool} ##

---


### DELETE ###

Example:
```sh

curls -s | python -m json.tool
```

## /api/projects/{projectid}/datadrivenProperties ##

---


### GET ###

Example:
```sh

curls -s | python -m json.tool
```

### POST ###

Example:
```sh

curls -s | python -m json.tool
```

## /api/projects/{projectid}/datadrivenProperties/{propertyname} ##

---


### DELETE ###

Example:
```sh

curls -s | python -m json.tool
```

## /api/projects/{projectid}/extensions ##

---


### GET ###

Example:
```sh

curls -s | python -m json.tool
```

## /api/projects/{projectid}/extensions/{extensionid} ##

---


### GET ###

Example:
```sh

curls -s | python -m json.tool
```

### POST ###

Example:
```sh

curls -s | python -m json.tool
```

## /api/projects/{projectid}/extensions/{extensionid} ##

---


### PUT ###

Example:
```sh

curls -s | python -m json.tool
```

## /api/projects/{projectid}/extensions/{extensionid} ##

---


### DELETE ###

Example:
```sh

curls -s | python -m json.tool
```