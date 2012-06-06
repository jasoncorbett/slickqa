import json
import zlib
import urllib
import urlparse
import httplib2
import traceback
from dateutil.tz import *
from datetime import datetime

json_content = {'Content-Type': 'application/json'}
STREAM_CONTENT = {'Content-Type': 'application/octet-stream'}
GET = 'GET'
POST = 'POST'
PUT = 'PUT'
DELETE = 'DELETE'

class SlickAsPy(object):
    # TODO: is there a better default url?
    def __init__(self, baseurl="http://localhost:8080/slickij-war/api", username="tcrunij", password="f00b@r"):
        self.http_connection = httplib2.Http()
        self.http_connection.add_credentials(username, password)
        self.baseurl = baseurl
        self.current_project = ''
        if self.baseurl.endswith('/'):
            self.baseurl = baseurl[0:-1]
        self.last_command = None

    def _fix_uri(self, uri, charset='utf-8'):
        if isinstance(uri, unicode):
            uri = uri.encode(charset, 'ignore')
        scheme, netloc, path, query, fragment = urlparse.urlsplit(uri)
        path = urllib.quote(path, '/%')
        query = urllib.quote(query, ':&=')
        return urlparse.urlunsplit((scheme, netloc, path, query, fragment))

    def _get_url(self, *args, **kwargs):
        if len(kwargs) > 0:
            uri = '/'.join([self.baseurl,] + list(args)) + "?" + kwargs
        else:
            uri = '/'.join([self.baseurl,] + list(args))
        url = self._fix_uri(uri)
        return url

    def _safe_return(self, response, content):
        if response['status'] == "200":
            return json.loads(content)
        else:
            raise SlickError("Response: {}\nContent: {}".format(response, content))

    def _safe_get(self, *args, **kwargs):
        return self._safe_request(GET, None, None, *args, **kwargs)

    def _safe_post(self, post_data, *args, **kwargs):
        return self._safe_request(POST, post_data, None, *args, **kwargs)

    def _safe_post_octet_stream(self, post_data, *args, **kwargs):
        return self._safe_request(POST, post_data, True, *args, **kwargs)

    def _safe_delete(self, *args, **kwargs):
        return self._safe_request(DELETE, None, None, *args, **kwargs)

    def _safe_put(self, update_data, *args, **kwargs):
        return self._safe_request(PUT, update_data, None, *args, **kwargs)
    
    def _safe_request(self, method, data=None, stream=None, *args, **kwargs):
        url = self._get_url(*args, **kwargs)
        if data and not stream:
            data = json.dumps(data)
        content_type = json_content
        if stream:
            content_type = STREAM_CONTENT
        good_content = None
        # sometimes we get bad content back from the server
        # this is a hack to fix that
        for i in range(5):
            if GET in method:
                response, content = self.http_connection.request(url)
            elif POST in method:
                response, content = self.http_connection.request(url, POST, data, content_type)
            elif PUT in method:
                response, content = self.http_connection.request(url, PUT, data, content_type)
            elif DELETE in method:
                response, content = self.http_connection.request(url, DELETE)
            try:
                good_content = self._safe_return(response, content)
            except SlickError as se:
                raise
            except:
                print 'Here is the content recieved from the server: {}'.format(content)
            if good_content:
                return good_content
        raise SlickError("Tried connecting 5 times to {} {} \nData: {}\nResponse: {}\nContent: {}".format(url, method, data, response, content))
                

    def _get_current_project_ref(self):
        return {'name':self.current_project["name"] ,'id': self.current_project["id"]}

    def add_project(self, name, description, tag_list, automation_tool_list, component_list):
        project = {"name": name, "description": description, "tags": tag_list, "automationTools": automation_tool_list, "components":component_list}
        result = self._safe_post(project, "projects")
        self.current_project = result
        return result

    def get_project_by_name(self, name):
        name = urllib.quote(name)
        project = self._safe_get("projects", "byname", name)
        self.current_project = project
        return project

    def get_project_by_id(self, projectId):
        project = self._safe_get("projects", projectId)
        return project

    def delete_project(self, projectId):
        return self._safe_delete("projects", projectId)

    def change_project_name(self, projectId, newName):
        return self._safe_put("projects", projectId, "name", newName)

    def change_project_description(self, projectId, newDescription):
        return self._safe_put("projects", projectId, "description", newDescription)

    def get_attributes(self):
        return self._safe_get("projects", self.current_project["id"], "attributes")

    def add_attributes(self, attributeDict):
        return self._safe_post(attributeDict, "projects", self.current_project["id"], "attributes")

    def delete_attribute(self, attributeName):
        return self._safe_delete("projects", self.current_project["id"], "attributes", attributeName)

    def get_components(self, projectId=None):
        if not projectId:
            return self.current_project['components']
        return self._safe_get("projects", projectId, "components")

    def get_releases(self):
        return self._safe_get("projects", self.current_project["id"], "releases")

    def get_release_by_name(self, release_name):
        releases = self.get_releases()
        for release in releases:
            if release.has_key('name') and release['name'] == release_name:
                self.current_release = release
                return release

    def get_release_by_id(self, release_id):
        return self._safe_get("projects", self.current_project["id"], "releases", release_id)

    def add_release(self, release):
        release = {'name': release}
        result = self._safe_post(release, "projects", self.current_project["id"], "releases")   
        self.current_release = result
        return result

    def get_default_release(self):
        return self._safe_get("projects", self.current_project["id"], "releases", "default")

    def set_default_release(self, releaseId):
        result = self._safe_get("projects", self.current_project["id"], "setdefaultrelease", releaseId)
        self.current_release = result
        return result

    def delete_release(self, releaseId):
        return self._safe_delete("projects", self.current_project["id"], "releases", releaseId)

    def update_release(self, releaseId, release):
        return self._safe_put("projects", self.current_project["id"], "releases", self.current_release["id"], release)

    def _get_current_release_ref(self):
        return {'name':self.current_release["name"] ,'releaseId': self.current_release["id"]}

    def add_build(self, build, release=None):
        build = {'name': build}
        result = self._safe_post(build, "projects", self.current_project["id"], "releases", self.current_release['id'], "builds")
        self.current_build = result
        return result

    def get_builds(self):
        return self._safe_get("projects", self.current_project["id"], "releases", self.current_release['id'], "builds")

    def get_build(self, buildId):
        build = self._safe_get("projects", self.current_project["id"], "releases", self.current_release['id'], "builds", buildId)
        self.current_build = build
        return build

    def get_build_by_name(self, buildName):
        builds = self.get_builds()
        for build in builds:
            if build["name"] == buildName:
                return build
        return None

    def get_default_build(self):
        build = self._safe_get("projects", self.current_project["id"], "releases", self.current_release['id'], "builds", "default")
        self.current_build = build
        return build

    def _get_current_build_ref(self):
        return {'name':self.current_build["name"] ,'buildId': self.current_build["id"]}

    def set_default_build(self, buildId):
        build = self._safe_get("projects", self.current_project["id"], "releases", self.current_release['id'], "setdefaultbuild", buildId)
        self.current_build = build
        return build

    def delete_build(self, buildId):
        return self._safe_delete("projects", self.current_project["id"], "releases", self.current_release['id'], "builds", buildId)

    def update_build(self, buildId, build):
        return self._safe_put("projects", self.current_project["id"], "releases", self.current_release['id'], "builds", buildId, build)

    def add_component(self, name, code=""):
        component = {"name": name, "code": code}
        return self._safe_post(component, "projects", self.current_project["id"], "components")

    def get_project_components(self):
        return self._safe_get("projects", self.current_project["id"], "components")

    def get_component(self, componentId):
        return self._safe_get("projects", self.current_project["id"], "components", componentId)

    def update_component(self, componentId, component):
        return self._safe_put("projects", self.current_project["id"], "components", componentId, component)

    def delete_component(self, componentId):
        return self._safe_delete("projects", self.current_project["id"], "components", componentId)

    def get_project_tags(self):
        return self._safe_get("projects", self.current_project["id"], "tags")

    def project_has_tag(self, tagName):
        tags = self.get_project_tags()
        for tag in tags:
            if tag == tagName:
                return True
        return False

    def add_tags(self, tags):
        if not isinstance(tags, list):
            tags = [tags]
        return self._safe_post(tags, "projects", self.current_project["id"], "tags")

    def delete_tag(self, tagName):
        return self._safe_delete("projects", self.current_project["id"], "tags", tagName)

    def get_automation_tools(self):
        return self._safe_get("projects", self.current_project["id"], "automationTools")

    def project_has_automation_tool(self, toolName):
        tools = self.get_automation_tools()
        for tool in tools:
            if tool == toolName:
                return True
        return False

    def add_automation_tools(self, tools):
        if not isinstance(tools, list):
            tools = [tools]
        return self._safe_post(tools, "projects", self.current_project["id"], "automationTools")

    def delete_automation_tool(self, tool):
        return self._safe_delete("projects", self.current_project["id"], "automationTools", tool)

    def get_data_driven_properties(self):
        return self._safe_get("projects", self.current_project["id"], "datadrivenProperties")

    def add_data_driven_properties(self, name, requirement=False, standardValues=[]):
        ddprop = {"name": name, "requirement": requirement, "standardValues": standardValues}
        return self._safe_post(ddprop, "projects", self.current_project["id"], "datadrivenProperties")

    def delete_data_driven_properties(self, propertyName):
        return self._safe_delete("projects", self.current_project["id"], "datadrivenProperties", propertyName)

    def get_extensions(self):
        return self._safe_get("projects", self.current_project["id"], "extensions")

    def get_extension_by_id(self, extensionId):
        return self._safe_get("projects", self.current_project["id"], "extensions", extensionId)

    def add_extension(self, extension):
        return self._safe_post(extension, "projects", self.current_project["id"], "extensions")

    def update_extension(self, extensionId, extension):
        return self._safe_put("projects", self.current_project["id"], "extensions", extensionId, extension)

    def delete_extension(self, extensionId):
        return self._safe_delete("projects", self.current_project["id"], "extensions", extensionId)

    def add_testcase(self, name, project=None, purpose=None, requirements=None, steps=None, author='API', attributes=None, automated=None, 
                     automationPriority=None, automationTool=None, automationConfiguration=None, automationId=None, automationKey=None, 
                     stabilityRating=None, tags=None, component=None, dataDriven=None):
        if project == None:
            project = self._get_current_project_ref()
        testcase = {'name': name, 'purpose': purpose, 'requirements': requirements, 'steps': steps, 'author': author, 'attributes': attributes,
                    'automated': automated, 'automationPriority': automationPriority, 'automationTool': automationTool, 
                    'automationConfiguration': automationConfiguration, 'automationId': automationId, 'automationKey': automationKey, 
                    'stabilityRating': stabilityRating, 'tags': tags, 'project': project, 'component': component, 'dataDriven': dataDriven}
        return self._safe_post(testcase, "testcases")

    def delete_testcase(self, testcaseId):
        return self._safe_delete("testcases", testcaseId)

    def get_matching_testcase(self, testcaseQuery):
        return self._safe_post(testcaseQuery, "testcases", "query")

    def get_testcases_containing_name(self, testcaseName):
        return self.get_testcases("namecontains", testcaseName)

    def get_testcases_with_name(self, testcaseName):
        return self.get_testcases("name", testcaseName)

    def get_testcases_by_tag(self, tag):
        return self.get_testcases("tag", tag)

    def get_testcases_by_author(self, author):
        return self.get_testcases("author", author)

    def get_testcases_by_projectId(self, projectId):
        return self.get_testcases("projectid", projectId)

    def get_testcases_by_componentId(self, componentId):
        return self.get_testcases("componentid", componentId)

    def get_testcases_by_automationId(self, automationId):
        return self.get_testcases("automationId", automationId)

    def get_testcases_by_automationKey(self, automationKey):
        return self.get_testcases("automationKey", automationKey)

    def get_testcases_by_automationTool(self, automationTool):
        return self.get_testcases("automationTool", automationTool)

    def get_testcases_by_automated(self, automated):
        return self.get_testcases("automated", automated)

    def get_testcases(self, searchmethod, searchcriteria):
        return self._safe_get("testcases?{}={}".format(searchmethod, searchcriteria))

    def add_test_run(self, name, testPlanId, configurationRef=None, projectRef=None, dateCreated=None, releaseRef=None, 
                     buildRef=None, extensions=None):
        if not projectRef:
            projectRef = self._get_current_project_ref()
        if not releaseRef:
            releaseRef = self._get_current_release_ref()
        if not buildRef:
            buildRef = self._get_current_build_ref()
        testRun = {"name": name, "testplanId": testPlanId, "config": configurationRef, "project": projectRef, "dateCreated": dateCreated, 
                   "release": releaseRef, "build": buildRef, "extensions": extensions}
        return self._safe_post(testRun, "testruns")

    def update_test_run(self, testRunId, testRun):
        return self._safe_put("testruns", testRunId, testRun)

    def delete_test_run(self, testRunId):
        return self._safe_delete("testruns", testRunId)

    def get_test_run(self, testrunId):
        return self._safe_get("testruns", testrunId)

    def add_test_plan(self, planName, project=None, createdBy='API', private=False, sharedWith=None, queries=[], extentions=None):
        if not project:
            project = self._get_current_project_ref()
        if not isinstance(queries, list):
            queries = [queries]
        testplan = {'name': planName, 'createdBy': createdBy, 'project': project, 'sharedWith': sharedWith, 'isprivate': private, 
                    'queries': queries, 'extensions': extentions}
        return self._safe_post(testplan, "testplans")

    def update_test_plan(self, testplan):
        return self._safe_put('testplans', testplan[id], testplan)

    def run_test_plan(self, testplanId, parameters=None):
        return self._safe_get("{}/{}/run".format("testplans", testplanId))

    def get_test_plans(self, projectId=None):
        if not projectId:
            projectId = self.current_project["id"]
        return self._safe_get("testplans?projectid={}".format(projectId))

    def get_test_plan(self, testplan_name):
        testplans = self.get_test_plans()
        for testplan in testplans:
            if testplan['name'] == testplan_name:
                return testplan
        return None

    def get_test_plan_by_id(self, testplanId):
        return self._safe_get("testplans", testplanId)

    def get_test_cases_from_test_plan(self, testPlanId):
        return self._safe_get("testplans", testPlanId, "testcases")

    def _get_test_run_ref(self, testrun):
        return {"name": testrun['name'], "testrunId": testrun['id']}

    def _get_test_case_ref(self, testcase):
        return {"name": testcase['name'], "testcaseId": testcase['id']}

    def add_result(self, testrunRef, testcase, date, resultStatus, runStatus=None, reason=None, attributeDict=None, configRef=None, 
                   configOverride=None, fileList=None, log=None, projectRef=None, componentRef=None, releaseRef=None, buildRef=None, 
                   runLength=None, extensions=None, history=None, hostname=None):
        if not projectRef:
            projectRef = self._get_current_project_ref()
        if not releaseRef:
            releaseRef = self._get_current_release_ref()
        if not buildRef:
            buildRef = self._get_current_build_ref()
        if not isinstance(fileList, list) and fileList:
            fileList = [fileList]
        result = {"testrun": self._get_test_run_ref(testrunRef), "config": configRef, "configurationOverride": configOverride, 
                  "testcase": self._get_test_case_ref(testcase), "recorded": date, "status": resultStatus, "project": projectRef, 
                  "release": releaseRef, "build": buildRef, "log": log, "hostname": hostname, "files": fileList}
        return self._safe_post(result, "results")

    def update_result(self, result_id, updated_result):
        return self._safe_put(updated_result, "results", result_id)

    def add_log_entry(self, message, resultId, time=None, level=None, loggername=None, exceptionName=None, exceptionMessage=None, exceptionTraceback=None):
        if not time:
            time = datetime.now(tzlocal()).strftime('%a, %m %b %Y %H:%M:%S %Z')
        logEntry = [{"entryTime": time, "level": level, "loggerName": loggername, "message": message, "exceptionClassName": exceptionName, 
                     "exceptionMessage": exceptionMessage, "exceptionStackTrace":exceptionTraceback}]
        return self._safe_post(logEntry, "results", resultId, "log")

    def add_log_entries(self, entries, resultId):
        return self._safe_post(entries, "results", resultId, "log")

    def create_stored_file(self, filename, mimetype, chunksize=None, uploaddate=None, md5=None, length=None):
        stored_file = {'filename': filename, 'chunkSize': chunksize, 'uploadDate': uploaddate, 
                       'mimetype': mimetype, 'md5': md5, 'length': length}
        return self._safe_post(stored_file, "files")

    def set_file_content(self, file_id, data):
        return self._safe_post_octet_stream(data, "files", file_id, "content")

    def add_stored_file(self, filename, mimetype, data, chunksize=None, uploaddate=None, md5=None, length=None):
        if not chunksize:
            chunksize = len(data)
        if not length:
            length = len(data)
        file_info = self.create_stored_file(filename, mimetype, chunksize, uploaddate, md5, length)
        self.set_file_content(file_info['id'], data)
        return file_info

class SlickError(Exception):
    pass

PASS = "PASS"
FAIL = "FAIL"

def main():    
    '''using this to test for now. Test suites to come later...'''
    #try:
        #from pymongo import Connection
        #from pymongo.errors import ConnectionFailure
        #db = Connection()
        #db.drop_database("slickij")
    #except ImportError:
        #print "you need to install pymongo"
        #exit(1)
    #except ConnectionFailure:
        #print "There was trouble connecting to the mongo db. Make sure it is installed and running"
        #exit(1)

    applePy = SlickAsPy("http://localhost:8080/api")
    #slick_project = applePy.add_project("Slickij Developer Project", "A Project to be used by slickij developers to test features.",
                                        #["basics", "api", "affirmative"], ["tcrunij", "tcrun", "Shell Script", "python unittest"], 
                                        #[{"name": "Data Extensions", "code": "dataext"}])
    #applePy.add_component("HTML Web UI", "web-ui")
    #applePy.add_component("REST APIs", "rest")
    slick_project = applePy.get_project_by_name("Slickij Developer Project")
    slick_release = applePy.get_release_by_name("1.0.0.311")
    if not slick_release:
        slick_release = applePy.add_release("1.0.0.311")
    applePy.set_default_release(slick_release["id"])
    slick_build = applePy.get_build_by_name("311")
    if not slick_build:
        slick_build = applePy.add_build("311")
    applePy.set_default_build(slick_build["id"])
    print slick_project['id']
    try:
        tcList = []
        for i in range(10):
            tcList.append(applePy.add_testcase("Fred{}".format(i), applePy._get_current_project_ref(), "To do something awesome!", 
                                               "Apples, pie crust, and sugar", [{"name": "step name{}".format(i), "expectedResult": "this is what should happen"}],
                                               "me", tags=["tasty", "hairy", "basics"]))
        tp = applePy.add_test_plan("The plan", queries=[{"query": {"className": "org.tcrun.slickij.api.data.testqueries.ContainsTags", 
                                                                   "tagnames": ["basics"]},"name": "All basics"}])
        print tp["id"]
        searchMe = applePy.get_testcases_with_name("Fred1")
        #searchMe = applePy.get_test_case("Fred3")
        print json.dumps(searchMe, indent=2)
        me = applePy.get_testcases_by_author("me")
        print json.dumps(me, indent=2)
        testplans = applePy.get_test_plans()
        #print "\nThe current test plans are:\n"
        #print json.dumps(testplans, indent=2)
        testrun = applePy.add_test_run(
            "testrun{}".format(datetime.now(tzlocal()).strftime('%a, %m %b %Y %H:%M:%S %Z')), tp["id"])
        #print "\nThe test run is this:"
        #print json.dumps(testrun, indent=2)
        p = PASS
        logEntry = [{"entryTime": datetime.now(tzlocal()).strftime('%a, %m %b %Y %H:%M:%S %Z'), "level": "DEBUG", "loggerName": "slickMe.base", 
                     "message": "now for something completely different"}, 
                    {"message": '<a href="http://hal9000.vintela.com/memory_logs/4.0.3.78/2012-01-19T21:49:55.226623/solaris-10.autoqas_info.html">log</a>',
                     "entryTime": datetime.now(tzlocal()).strftime('%a, %m %b %Y %H:%M:%S %Z'), "level": "DEBUG", "loggerName": "slickMe.base"}]
        for tc in tcList:
            result = applePy.add_result(testrun, tc, datetime.now(tzlocal()).strftime('%a, %m %b %Y %H:%M:%S %Z'), p, "FINISHED", "just because", 
                                        log=logEntry)
            #print "Here is the result to be added:"
            #print json.dumps(result, indent=2)

            if p == PASS:
                p = FAIL
            else:
                p = PASS

    except SlickError as slickE:
        print slickE


if __name__ == "__main__":
    main()
