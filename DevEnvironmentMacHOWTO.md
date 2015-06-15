# What you will need #

You will need to install several important technologies on your system if you have not already:

  * [MongoDB](http://mongodb.org)
  * [Java SDK](http://www.oracle.com/technetwork/java/javase/downloads/java-se-jdk-7-download-432154.html)
  * [Apache Maven](http://maven.apache.org) (Be sure to get **Maven 3**)
  * [Git SCM](http://git-scm.com) (distributed version control system)

After you have that, you should be ready.  Below are instructions to get those installed on Mac OS X.

## Mac ##

For simplicity I'm putting everything related to slick into ~/Development/slick.  You can adapt the path names to wherever you want things put.

### Pre-Setup Setup ###

As a friend once said we're about to commence to start to begin to initialize.

This HOWTO will use the command line a lot, so open Terminal (found in /Applications/Utilities/).

Type the following in the command line:

```
	mkdir -p ~/Development/slick
```

This will create one or more directories under your home directory.  If you don't have it already it'll create the Development directory, and then the slick directory under that.

### MongoDB ###

The easiest way to install MongoDB on mac is to download it from http://www.mongodb.org/downloads.  Assuming you have a 64-bit cpu (you probably do), choose the 64 bit version.  For now choose the 1.8.x release as we haven't done extensive testing on 2.0 yet.

After downloading, use tar on the commandline to extract it (replace the file name with the one you downloaded).  If OS X auto extracted some of it for you, and your left with a .tar file instead of a .tgz, take the 'z' parameter off the below command:
```
	tar xzf mongodb-osx-x86_64-1.8.3.tgz
```

This should create a directory of the same name as the file, but without the .tgz extension.  You can move the directory to wherever you want, but I suggest "~/Development/slick/mongodb" for simplicity:

```
	mv mongodb-osx-x86_64-1.8.3 ~/Development/slick/mongodb
```

From now on if you've put mongodb in a different place you'll have to replace ~/Development/slick/mongodb with your directory.

Also let's create a directory to store the database files in:

```
	mkdir ~/Development/slick/db
```

### Java SDK ###

If you're on Snow Leopard (OS X 10.6) your all set.  If you are on Lion, go to the terminal and type java, and follow the prompts to install.

### Maven ###

Snow Leopard comes with maven (an older version) in /usr/share/maven.  You may or may not need all this, but these command lines should help you accomplish what you need.  First go and download the latest maven from http://maven.apache.org.

Extract it (much the way we did with mongodb).  Then run the following commands, substituting apache-maven-3.0.3 with the directory that was created after extracting the maven you downloaded:

```
	sudo rm -rf /usr/share/maven /usr/bin/mvn
	sudo mv apache-maven-3.0.3 /usr/share/maven
	sudo chown -R root:wheel /usr/share/maven
	sudo ln -s /usr/share/maven/bin/mvn /usr/bin/mvn
```

We'll test the installation later.

### The Source ###

If you don't already have git (try typing git on the command line and see if it says command not found), go download it for your system from http://git-scm.com.  After doing that (and installing it) do:

```
	cd ~/Development/slick
	git clone https://code.google.com/p/slickqa source
```

That should get the latest source from google code.

### Running Mongodb ###

Let's try running mongodb:

```
	cd ~/Development/slick/mongodb/bin
	./mongod --dbpath ~/Development/slick/db
```

You should see mongodb start up, and then pause.  Leave this terminal window open (allowing mongodb to run) and open a new window for the rest of the tutorial.

### Building Slick ###

Try this out:

```
	cd ~/Development/slick/source
	mvn install
```

Now, if maven installation went well, that should work!  You'll see a lot of stuff downloading and running and compiling.  After it's all done:

```
	cd slickij-war
	mvn jetty:run
```

This should start a web server with slick running.  Keep that terminal open and go to another one:

```
	cd ~/Development/slick/source/utilities
	./adddevdata.sh
```

This is it.  If that worked, and printed out a lot of messages with id's at the end, then slick is up and running on http://localhost:8080/index.html