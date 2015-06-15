<font size='+3'><b>Host Status Rest API Documentation</b>:</font>


# Data Type Description #

The following is a listing of the properties contained in host status.  Properties marked as required mean that those properties are required when creating a new instance:

  * :
  * **(required)**:

JSON Example:

```
	{
	}
```

# Rest Endpoints #

## /api/hoststatus ##

---


### GET ###

Example:

```sh

curl -s http://slick.host.name/slickij/api/hoststatus |python -m json.tool
```

## /api/hoststatus/{hostname} ##

---


### GET ###

Example:

```sh

curl -s http://slick.host.name/slickij/api/hoststatus/linws1480 | python -m json.tool
```