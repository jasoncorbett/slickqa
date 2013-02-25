#!/bin/bash

which phantomjs >/dev/null
PHANTOM_EXISTS=$?

if [ "$PHANTOM_EXISTS" != "0" ];
then
	echo "You must install phantomjs from http://phantomjs.org in order to run javascript tests on the command line."
	echo "If you just want to run the tests, open a browser to your slick instance and go to test.html"
	exit 1
fi

if [ "$1" = "" ];
then
	phantomjs jstestrunner.js http://localhost:8080/test.html
else
	phantomjs jstestrunner.js "$1"
fi

