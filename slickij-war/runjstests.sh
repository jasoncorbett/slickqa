#!/bin/bash

which node >/dev/null
NODE_EXISTS=$?

if [ "$NODE_EXISTS" != "0" ];
then
	echo "You must install nodejs and npm in order to run javascript tests."
	exit 1
fi

which node >/dev/null
NPM_EXISTS=$?

if [ "$NPM_EXISTS" != "0" ];
then
	echo "You must install nodejs and npm in order to run javascript tests."
	exit 1
fi

for nodepkg in nodeunit should backbone;
do
	if [ ! -d "node_modules/$nodepkg" ];
	then
		npm install "$nodepkg";
	fi
done

NODEUNITPARAMS="--reporter nested"
if [ "$1" = "junit" ];
then
	NODEUNITPARAMS="--reporter junit --output target/nodeunit"
fi

node_modules/.bin/nodeunit ${NODEUNITPARAMS} src/test/js
