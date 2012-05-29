#/bin/bash

# determine correct directory
SLICK_SOURCE=$(pwd)


if [ -n "$WORKSPACE" ];
then
	SLICK_SOURCE="$WORKSPACE"
else
	SLICK_SOURCE=$(cd $(pwd)/$(dirname $0)/../..; pwd -P)
fi

echo "Shutting down slick [" `cat $SLICK_SOURCE/integration-tests/slick.pid` "]"
kill -TERM `cat $SLICK_SOURCE/integration-tests/slick.pid`

while ps -p `cat $SLICK_SOURCE/integration-tests/slick.pid` >/dev/null
do
	sleep 1
done

rm $SLICK_SOURCE/integration-tests/slick.pid
