#!/bin/bash

# determine correct directory
SLICK_SOURCE=$(pwd)

if [ -n "$WORKSPACE" ];
then
	SLICK_SOURCE="$WORKSPACE"
else
	SLICK_SOURCE=$(cd $(pwd)/$(dirname $0)/..; pwd -P)
fi

INT_ROOT="$SLICK_SOURCE/integration-tests"

rm -f $INT_ROOT/*.log $INT_ROOT/*.pid 2>/dev/null
rm -f TEST*.xml 2>/dev/null

#------- Run Setup -------------------------------------------------------
cd $INT_ROOT/setup.d

for SETUP_SCRIPT in *.sh;
do
	echo -n -e "Executing setup script: $SETUP_SCRIPT............"
	echo "------- Output From: $SETUP_SCRIPT -------------------------------------------" >>$INT_ROOT/setup.log
	./${SETUP_SCRIPT} >>$INT_ROOT/setup.log 2>&1
	echo "Done."
done

#------- Run Javascript Tests -------------------------------------------------------

echo "------------------  Running Javascript Tests -------------------------------"
cd ../../slickij-war
./runjstests.sh http://localhost:9090/test.html
cd -

#------- Run Api Tests -------------------------------------------------------

echo "------------------  Running API Tests -------------------------------"
nosetests --with-nosexunit --core-target=$INT_ROOT $INT_ROOT/apitests/*.py


#------- Run Cleanup -------------------------------------------------------
cd $INT_ROOT/cleanup.d

for CLEANUP_SCRIPT in *.sh;
do
	echo -n -e "Executing cleanup script: $CLEANUP_SCRIPT............"
	echo "------- Output From: $CLEANUP_SCRIPT -------------------------------------------" >>$INT_ROOT/cleanup.log
	./${CLEANUP_SCRIPT} >>$INT_ROOT/cleanup.log 2>&1
	echo "Done."
done


