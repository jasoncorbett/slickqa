# What you will need #

You will need to install several important technologies on your system if you have not already:

  * [MongoDB](http://mongodb.org)
  * [Java SDK](http://www.oracle.com/technetwork/java/javase/downloads/java-se-jdk-7-download-432154.html)
  * [Apache Maven](http://maven.apache.org) (Use version 3)
  * [Git SCM](http://git-scm.com) (distributed version control system)

After you have that, you should be ready.  Below is instructions to get those installed on Ubuntu/Kubuntu.

# Ubuntu Packages #

Luckily on Ubuntu there are packages for just about everything we need:

```
	sudo apt-get install openjdk-6-jdk git-all curl mongodb-server mongodb-clients maven
```
Note: openjdk-7-jdk has been successfully used in development.

## Maven 3 ##
  1. If you are using a version of Ubuntu before 12.04, you can get version 3 of maven from [apache](http://maven.apache.org/download.html).
  1. You could also try this ppa:
```
	sudo add-apt-repository ppa:natecarlson/maven3
	sudo apt-get update
 	sudo apt-get install maven3
```

## The Source ##

Time to grab the source:

```
	mkdir ~/dev
	cd ~/dev
	git clone https://code.google.com/p/slickqa slick
```

That should get the latest source from google code.

## Building Slick ##

Try this out:

```
	cd ~/dev/slick
	mvn install
```

Now, if maven installation went well, that should work!  You'll see a lot of stuff downloading and running and compiling.  After it's all done:

```
	cd slickij-war
	mvn jetty:run
```

This should start a web server with slick running.  Keep that terminal open and go to another one:

```
	cd ~/dev/slick/utilities
	./adddevdata.sh
```

This is it.  If that worked, and printed out a lot of messages with id's at the end, then slick is up and running on http://localhost:8080/slickij-war/index.html