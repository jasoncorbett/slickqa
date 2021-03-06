#!/bin/bash

SLICKURL="http://localhost:8080"
MONGO="mongo localhost/slickij"

POST="curl -s -X POST"

# Create Project ---------------------------------------------------------------------------------
PROJECT=`$POST -H 'Content-Type: application/json' -d '{"name": "Slickij Developer Project", "description": "A Project to be used by slickij developers to test features.", "tags": ["basics", "api", "negative"], "automationTools": ["tcrunij", "tcrun", "Shell Script"], "components": [{"name": "Data Extensions", "code": "dataext"}]}' "${SLICKURL}/api/projects" |python -m json.tool |grep '^    .id' |perl -pi -e 's/.*"([0-9a-f]+)".*/$1/'`
echo "Project ID: $PROJECT"
# End Create Project ---------------------------------------------------------------------------------


# Get Project Configuration Id ---------------------------------------------------------------------------------
CONFIGID=`curl -s "${SLICKURL}/api/projects/$PROJECT" |python -m json.tool |grep '^        .id' |perl -pi -e 's/.*"([0-9a-f]+)".*/$1/'`
echo "Configuration ID: $CONFIGID"
# End Get Project Configuration Id ---------------------------------------------------------------------------------


# Create Default Environment ---------------------------------------------------------------------------------
$POST -H 'Content-Type: application/json' -d '{"name": "Default Environment", "filename": "default.ini", "configurationType": "ENVIRONMENT", "configurationData": {"foo": "bar"}}' "${SLICKURL}/api/configurations" >/dev/null
# End Create Default Environment ---------------------------------------------------------------------------------


# Get Component ID ---------------------------------------------------------------------------------
DATAEXTCOMP=`curl -s "${SLICKURL}/api/projects/$PROJECT" |python -m json.tool |grep '^            .id' |perl -pi -e 's/.*"([0-9a-f]+)".*/$1/'`
echo "DataExtension Component ID: $DATAEXTCOMP"
# End Get Component ID ---------------------------------------------------------------------------------


# Get Component ID ---------------------------------------------------------------------------------
WEBCOMP=`$POST -H 'Content-Type: application/json' -d '{"name": "HTML Web UI", "code": "web-ui"}' "${SLICKURL}/api/projects/$PROJECT/components" |python -m json.tool |grep '^    .id' |head -1 |perl -pi -e 's/.*"([0-9a-f]+)".*/$1/'`
echo "WEB Component ID: $WEBCOMP"
# End Get Component ID ---------------------------------------------------------------------------------


# Get Component ID ---------------------------------------------------------------------------------
RESTCOMP=`$POST -H 'Content-Type: application/json' -d '{"name": "REST APIs", "code": "rest"}' "${SLICKURL}/api/projects/$PROJECT/components" |python -m json.tool |grep '^    .id' |head -1 |perl -pi -e 's/.*"([0-9a-f]+)".*/$1/'`
echo "REST Component ID: $RESTCOMP"
# End Get Component ID ---------------------------------------------------------------------------------


# Add Release ---------------------------------------------------------------------------------
RELEASE=`$POST -H 'Content-Type: application/json' -d '{"name": "1.0.0", "target": 1304269945716}' "${SLICKURL}/api/projects/${PROJECT}/releases" |python -m json.tool |grep '^    .id' |perl -pi -e 's/.*"([0-9a-f]+)".*/$1/'`
curl -s "${SLICKURL}/api/projects/${PROJECT}/setdefaultrelease/${RELEASE}" >/dev/null
echo "Release 1.0.0 ID: $RELEASE"
# End Add Release ---------------------------------------------------------------------------------


# Add Build ---------------------------------------------------------------------------------
BUILD=`$POST -H 'Content-Type: application/json' -d '{"name": "SNAPSHOT"}' "${SLICKURL}/api/projects/${PROJECT}/releases/${RELEASE}/builds" |python -m json.tool |grep '^    .id' |perl -pi -e 's/.*"([0-9a-f]+)".*/$1/'`
curl -s "${SLICKURL}/api/projects/${PROJECT}/releases/${RELEASE}/setdefaultbuild/${BUILD}" >/dev/null
echo "Build SNAPSHOT of 1.0.0 Release: $BUILD"
# End Add Build ---------------------------------------------------------------------------------


# Create Tests ---------------------------------------------------------------------------------
CREATEPROJTC=`$POST -H "Content-Type: application/json" -d "{\"name\": \"Create Project Using REST Api\", \"purpose\": \"To test the ability of the REST Api to create, save, and retrieve a new project in slick v2\", \"requirements\": \"A running instance of slick, prefferably without any projects, or at least not the one you are going to create.\", \"steps\": [{\"name\": \"Call slick api with authentication, and JSON post.\", \"expectedResult\": \"A Project instance is returned with a generated id.\"}], \"author\": \"jcorbett\", \"automated\": true, \"automationTool\": \"Shell Script\", \"tags\": [\"basics\", \"api\"], \"project\": {\"id\": \"$PROJECT\"}, \"component\": {\"id\": \"$RESTCOMP\"} }" "${SLICKURL}/api/testcases" |python -m json.tool |grep '^    .id' |perl -pi -e 's/.*"([0-9a-f]+)".*/$1/'`
CREATERELEASETC=`$POST -H "Content-Type: application/json" -d "{\"name\": \"Create a Release on a Project Using REST Api\", \"purpose\": \"To test the ability of the REST Api to create, save, and retrieve a new release on a project in slick v2\", \"requirements\": \"A running instance of slick, and a project without a release by the same name.\", \"steps\": [{\"name\": \"Call slick api with authentication, and JSON post, create release 1.0.0.\", \"expectedResult\": \"A Release instance is returned with a generated id.\"}], \"author\": \"jcorbett\", \"automated\": true, \"automationTool\": \"Shell Script\", \"tags\": [\"basics\", \"api\"], \"project\": {\"id\": \"$PROJECT\"}, \"component\": {\"id\": \"$RESTCOMP\"} }" "${SLICKURL}/api/testcases" |python -m json.tool |grep '^    .id' |perl -pi -e 's/.*"([0-9a-f]+)".*/$1/'`
CREATEBUILDTC=`$POST -H "Content-Type: application/json" -d "{\"name\": \"Create a Build on a Release Using REST Api\", \"purpose\": \"To test the ability of the REST Api to create, save, and retrieve a new build on a project in slick v2\", \"requirements\": \"A running instance of slick, and a project with the 1.0.0 release and no SNAPSHOT build.\", \"steps\": [{\"name\": \"Call slick api with authentication, and JSON post, create build SNAPSHOT.\", \"expectedResult\": \"A Build instance is returned with a generated id.\"}], \"author\": \"jcorbett\", \"automated\": true, \"automationTool\": \"Shell Script\", \"tags\": [\"basics\", \"api\"], \"project\": {\"id\": \"$PROJECT\"}, \"component\": {\"id\": \"$RESTCOMP\"} }" "${SLICKURL}/api/testcases" |python -m json.tool |grep '^    .id' |perl -pi -e 's/.*"([0-9a-f]+)".*/$1/'`
CREATECOMPTC=`$POST -H "Content-Type: application/json" -d "{\"name\": \"Create a Component in a Build Using REST Api\", \"purpose\": \"To test the ability of the REST Api to create, save, and retrieve a new component in a project in slick v2\", \"requirements\": \"A running instance of slick, and a project previously saved.\", \"steps\": [{\"name\": \"Call slick api with authentication, and JSON post, create component with the name REST APIs.\", \"expectedResult\": \"A Component instance is returned with a generated id.\"}], \"author\": \"jcorbett\", \"automated\": true, \"automationTool\": \"Shell Script\", \"tags\": [\"basics\", \"api\"], \"project\": {\"id\": \"$PROJECT\"}, \"component\": {\"id\": \"$RESTCOMP\"} }" "${SLICKURL}/api/testcases" |python -m json.tool |grep '^    .id' |perl -pi -e 's/.*"([0-9a-f]+)".*/$1/'`
CREATETESTTC=`$POST -H "Content-Type: application/json" -d "{\"name\": \"Create a Testcase Using REST Api\", \"purpose\": \"To test the ability of the REST Api to create, save, and retrieve a new testcase in slick v2\", \"requirements\": \"A running instance of slick, a project previously saved, and a component.\", \"steps\": [{\"name\": \"Call slick api with authentication, and JSON post, creating a testcase.\", \"expectedResult\": \"A Testcase instance is returned with a generated id, and component and project references verified.\"}], \"author\": \"jcorbett\", \"automated\": true, \"automationTool\": \"Shell Script\", \"tags\": [\"basics\", \"api\"], \"project\": {\"id\": \"$PROJECT\"}, \"component\": {\"id\": \"$RESTCOMP\"} }" "${SLICKURL}/api/testcases" |python -m json.tool |grep '^    .id' |perl -pi -e 's/.*"([0-9a-f]+)".*/$1/'`
CREATEPLANTC=`$POST -H "Content-Type: application/json" -d "{\"name\": \"Create a test plan in slick v2 using REST Api\", \"purpose\": \"To test the ability of the REST Api to create, save, and retrieve a new testplan in slick v2, and query the appropriate test cases as part of that test plan\", \"requirements\": \"A running instance of slick, and several test cases previously saved.\", \"steps\": [{\"name\": \"Call slick api with authentication, and JSON post, creating a test plan with at least 2 queries.\", \"expectedResult\": \"A Testplan instance is returned with a generated id.\"}, {\"name\": \"Fetch testcases from that plan using the id generated and the slick api.\", \"expectedResult\": \"A list of tests in the proper order is returned.\"}], \"author\": \"jcorbett\", \"automated\": true, \"automationTool\": \"Shell Script\", \"tags\": [\"basics\", \"api\"], \"project\": {\"id\": \"$PROJECT\"}, \"component\": {\"id\": \"$RESTCOMP\"} }" "${SLICKURL}/api/testcases" |python -m json.tool |grep '^    .id' |perl -pi -e 's/.*"([0-9a-f]+)".*/$1/'`
RUNPLANTC=`$POST -H "Content-Type: application/json" -d "{\"name\": \"Run a previously created test plan in slick v2 using REST Api\", \"purpose\": \"To test the ability of the REST Api to run a testplan in slick v2\", \"requirements\": \"A running instance of slick, and a previously saved testplan.\", \"steps\": [{\"name\": \"Call slick api to run a test plan with at several testcases.\", \"expectedResult\": \"A Testrun instance is returned with a generated id.\"}, {\"name\": \"Fetch results that need to be run from that testrun using the id generated and the slick api.\", \"expectedResult\": \"A list of results in the proper order is returned with NO_RESULT statuses.\"}], \"author\": \"jcorbett\", \"automated\": true, \"automationTool\": \"Shell Script\", \"tags\": [\"basics\", \"api\"], \"project\": {\"id\": \"$PROJECT\"}, \"component\": {\"id\": \"$RESTCOMP\"} }" "${SLICKURL}/api/testcases" |python -m json.tool |grep '^    .id' |perl -pi -e 's/.*"([0-9a-f]+)".*/$1/'`
# End Create Tests ---------------------------------------------------------------------------------


# Create Testplan ---------------------------------------------------------------------------------
TESTPLAN=`$POST -H "Content-Type: application/json" -d "{\"name\": \"Basic REST Api Checks\", \"createdBy\": \"jcorbett\", \"isprivate\": false, \"project\": {\"id\": \"$PROJECT\"}, \"queries\": [{\"name\": \"Create Project First\", \"query\": { \"className\": \"org.tcrun.slickij.api.data.testqueries.IsTestcase\", \"ref\": {\"testcaseId\": \"$CREATEPROJTC\"}}}, {\"name\": \"All Other Basic Tests\", \"query\": {\"className\": \"org.tcrun.slickij.api.data.testqueries.RequireAll\", \"criteria\": [{\"className\": \"org.tcrun.slickij.api.data.testqueries.IsNotTestcase\", \"testcaseId\": \"$CREATEPROJTC\"}, {\"className\": \"org.tcrun.slickij.api.data.testqueries.ContainsTags\", \"tagnames\": [\"basics\"]}]}}]}" "${SLICKURL}/api/testplans" |python -m json.tool |grep '^    .id' |perl -pi -e 's/.*"([0-9a-f]+)".*/$1/'`
echo "Basic Testplan ID: $TESTPLAN"
# End Create Testplan ---------------------------------------------------------------------------------


# Count Tests In Testplan ---------------------------------------------------------------------------------
NUMOFTCINPLAN=`curl -s "${SLICKURL}/api/testplans/${TESTPLAN}/testcases" | python -m json.tool | grep -c '^    }'`
echo "Number of tests in plan (should be 7): $NUMOFTCINPLAN"
# End Count Tests In Testplan ---------------------------------------------------------------------------------

# Run Tests In Testplan ---------------------------------------------------------------------------------
TESTRUN=`$POST -H "Content-Type: application/json" -d "{\"release\": {\"releaseId\": \"$RELEASE\"}, \"build\": {\"buildId\": \"$BUILD\"}}" "${SLICKURL}/api/testplans/${TESTPLAN}/run" |python -m json.tool |grep '^    .id' |perl -pi -e 's/.*"([0-9a-f]+)".*/$1/'`
echo "Testrun ID from the previous testplan: $TESTRUN"
# End Run Tests In Testplan ---------------------------------------------------------------------------------


