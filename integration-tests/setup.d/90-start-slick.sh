#!/bin/bash

# determine correct directory
SLICK_SOURCE=$(pwd)


if [ -n "$WORKSPACE" ];
then
	SLICK_SOURCE="$WORKSPACE"
else
	SLICK_SOURCE=$(cd $(pwd)/$(dirname $0)/../..; pwd -P)
fi

cd $SLICK_SOURCE/slickij-war; 
nohup mvn -Djetty.port=9090 jetty:run >$SLICK_SOURCE/integration-tests/slick.log 2>&1 &
cd $SLICK_SOURCE/integration-tests
echo $! >slick.pid

while ! curl -s http://localhost:9090/index.html |grep -q html 
do
	sleep 1
done

