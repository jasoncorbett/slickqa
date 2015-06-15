# Introduction #

Narc is a program that responds to events from slick.  It was written with the first plugin being an email responder, thus the name narc as it "narcs" on slick.

# Details #

Narc is written in python, and has been tested on linux and mac.  It might run on windows, but installation is an exercise left for the unfortunate person who decides to run it on windows.

## Requirements ##

Narc is written for python 3.3.  It will likely have problems with previous versions of python.  If you're running ubuntu version earlier than 12.10, there are several suggestions on installing it here:
http://askubuntu.com/questions/244544/how-to-install-python-3-3

If you're running 12.10, I've got some bad news for you.  Although there are python 3.3 packages, the module path is somewhat broken.  The module path in ubuntu 12.10 python 3.3 packages includes /usr/lib/python3, but there are binary packages installed there that only work on python3.2.  I've gotten it to work, but it does take some configuring.  First you should remove apport.  Apport has a python-wide exception hook, so if you don't remove it, every time you throw an exception, you'll end up throwing 3.  This is a known bug that is marked won't fix: https://bugs.launchpad.net/ubuntu/+source/apport/+bug/1059017

The next thing to do is install pycairo.  Don't install the ubuntu package pycairo.  Instead, download the source, and compile using python3.3.  You may need to temporarily change your default python to python3 (update-alternatives --config python).  Before proceeding you should test pycairo by running the interactive shell and try importing it:
```
>>> from cairo import Surface
```

Narc requires several dependent python modules.  Most of these will be installed for you when you install narc.  However one very important one will not.  **pycairo** is a module which is not auto installed, but must be working.  You can find it here: http://cairographics.org/pycairo/.

In order to install narc you will need pip, and pip needs distribute on python 3.  Follow the instructions on these sites:
http://pypi.python.org/pypi/distribute/
http://www.pip-installer.org/en/latest/installing.html

## Installation ##

After you have pip installed, and pycairo, you can easily install narc!  run:
```
# pip-3.3 install slickqa-narc
```

Then you will want to configure narc, using narcctl:
```
# narcctl --configure
```

**Important**: for the slick url you will want to specify a url that is usable in the email.  Right now links in the email are based on this url.

Narc has a config file in /etc/narc.conf, and it's logs by default go to /var/log/narc.log

If you have previously configured slick to use an AMQP broker (like rabbitmq), then you should be all set!

## Running ##

On ubuntu, you will probably want to use upstart to start narc.  Create a file called /etc/init/narc.conf:
```
exec /usr/local/bin/narc
```

that will allow you to run narc by doing:
```
# start narc
```

## Adding smtp configuration ##

Hopefully soon I'll have a page in slick for configuring the SMTP configuration.  For now, python might be the easiest.  You can also do curl, but since python is already installed by this point, you can use python:

```
>>> import slickqa
>>> slick = slickqa.SlickConnection('http://localhost:8080')
>>> smtpconfig = slickqa.EmailSystemConfiguration()
>>> smtpconfig.smtpHostname = 'smtp.somewhere.com'
>>> smtpconfig.smtpPort = 25
>>> smtpconfig.sender = 'me@notvalid'
>>> smtpconfig.enabled = True
>>> slick.systemconfigurations(slickqa.EmailSystemConfiguration, smtpconfig).create()
```

There are also smtpUsername and smtpPassword fields if you need authentication.  You can also use ssl, by setting the ssl property to True.

After you setup the smtp configuration, if narc is running you need to restart it.  That's easy with narcctl:
```
# narcctl --restart
```

If narc was running, that would restart it.  Check the logs (/var/log/narc.log) and they should indicate that it found the smtp configuration.

## Adding Subscriptions ##

Soon there will be a page to configure email subscriptions.  For now you will have to do it by hand.  I found python makes things easier.  The following example will show adding a global email subscription for the address someone@notvalid, and a project subscription for the address someoneelse@notvalid.

There are several different types of subscriptions:
  * **Global** means that every testrun that finishes on the slick system generates an email
  * **Project** means that every testrun that finishes and is tied to that particular project, they will get an email
  * **Testplan** is the same but for a particular Testplan
  * **Release** is the same but for a particular Release
  * **Configuration** is the same but for a particular Environment

In each of these subscriptions, there is a subscriptionType and a subscriptionValue.  The subscriptionValue should be a string with the id of the object it is tied to.

Finally the example:
```
>>> import slickqa
>>> slick = slickqa.SlickConnection('http://localhost:8080')
>>> sub = slickqa.EmailSubscription()
>>> sub.name = 'someone@notvalid'
>>> sub.enabled = True
>>> sub.subscriptions = []
>>> inf = slickqa.SubscriptionInfo()
>>> inf.subscriptionType = 'Global'
>>> inf.onStart = False
>>> sub.subscriptions.append(inf)
>>> slick.systemconfigurations(slickqa.EmailSubscription, sub).create()
>>> sub.name = 'someoneelse@notvalid'
>>> inf.subscriptionType = 'Project'
>>> proj = slick.projects.findByName('Another Project')
>>> proj.id
'50452a7cda0685ba6a265acd'
>>> inf.subscriptionValue = proj.id
>>> slick.systemconfigurations(slickqa.EmailSubscription, sub).create()
```