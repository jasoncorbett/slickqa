import logging
import traceback
import re
import slickLogging
from slickApi import SlickAsPy, SlickError
from SlickTestResult import SlickTestResult
from unittest import TextTestRunner
from unittest.signals import registerResult
from slick_utilities import get_date

class SlickTestRunner(TextTestRunner):
    """
    A test runner class that logs results to the Slickqa results database.
    It uses the python rest api to communicate.
    """
    # TODO: Maybe we should add logging here as well?
    def __init__(self, release, build, projectName="Slickij Developer Project", testplanName="The plan",
                 slickLocation="http://localhost:8080/api", apiuser='tcrunij', apipassword='f00b@r', stream=None, 
                 descriptions=True, verbosity=1, failfast=False, buffer=False, 
                 resultclass=SlickTestResult, loggername='root'):
        super(SlickTestRunner, self).__init__(stream, descriptions, verbosity, failfast, buffer, resultclass)
        self.logger_name = loggername
        self.testPlan = testplanName
        if isinstance(slickLocation, SlickAsPy):
            self.slickCon = slickLocation
        else:
            self.slickCon = SlickAsPy(slickLocation, apiuser, apipassword)
        self.project = self.slickCon.get_project_by_name(projectName)
        self.release = self.slickCon.get_release_by_name(release)
        if not self.release:
            self.release = self.slickCon.add_release(release)
        self.slickCon.set_default_release(self.release['id'])
        self.build = self.slickCon.get_build_by_name(build)
        if not self.build:
            self.build = self.slickCon.add_build(build)
        self.slickCon.set_default_build(self.build['id'])
        self._checkTestPlan()
        self._setTestRunRef(self.slickCon.add_test_run(self.testPlan["name"], self.testPlan["id"]))
        
    def _makeSlickResult(self):
        result = self.resultclass
        return result(self.project, self.testRunRef, self.slickCon, self.logger_name, self.not_tested_result_list)

    def _printTests(self, tests):
        for test in tests._tests:
            print test
            
    def _get_tests(self, tests):
        if hasattr(tests, "_tests"):
            if hasattr(tests._tests, "_tests"):
                return self._get_tests(tests._tests)
            else:
                return tests._tests

    def _checkTests(self, tests):
        self.testsFromSlick = []
        #all_tests = self._get_tests(tests)
        #for testsuite in all_tests:
        if hasattr(tests, "_tests"):
            for test in tests._tests:
                slicktest = None
                # Get all tests with this name and then delete them first 
                try:
                    allTestsWithName = self.slickCon.get_testcases_with_name(test.shortDescription())
                except SlickError:
                    allTestsWithName = []
                                    
                if isinstance(allTestsWithName, list):
                    if len(allTestsWithName) >=1:
                        for foundTest in allTestsWithName:
                            self.slickCon.delete_testcase(foundTest["id"])
            
                values = self._parseTestCaseInfo(test)
                slicktest = self.slickCon.add_testcase(test.shortDescription(), author=values["Author"], purpose=values["Purpose"],
                                                       tags=values["Tags"], requirements=values["Requirements"], 
                                                       steps=values["Steps"], automated=True, component=values["Component"])
                self.testsFromSlick.append(slicktest)
                
    def _checkTestsOld(self, tests):
        self.testsFromSlick = []
        #all_tests = self._get_tests(tests)
        #for testsuite in all_tests:
        if hasattr(tests, "_tests"):
            for test in tests._tests:
                slicktest = None
                try:
                    slicktest = self.slickCon.get_testcases_with_name(test.shortDescription())
                except SlickError as se:
                    print traceback.format_exc()
                #if not slicktest:
                    ## if not, add them?
                    
                    ## Parse the data and add it to add test. This will populate the testcase
                    #values = self._parseTestCaseInfo(test)
                    ##slicktest = self.slickCon.add_testcase(test.shortDescription(), automated=True)
                    ##self.testsFromSlick.append(slicktest)
                    
                #else:
                    ## If we find multiple tests with the same name, we will run the last one it finds. Tests need a unique name 
                    #if isinstance(slicktest, list):
                        #slicktest = slicktest.pop()
                        
                # I am going to add the test everytime because the steps could have changed. 
                # Delete old test first, Need to get test names from the doc string
                testName = self._getTestCaseName(test._testMethodDoc)
                
                # Get all tests with this name and then delete them first 
                try:
                    allTestsWithName = self.slickCon.get_testcases_with_name(testName)
                    for foundTest in allTestsWithName:
                        self.slickCon.delete_testcase(foundTest["id"])
                except SlickError:
                    print "I found no test cases" 
                    pass
            
                values = self._parseTestCaseInfo(test)
                slicktest = self.slickCon.add_testcase(test.shortDescription(), author=values["Author"], purpose=values["Purpose"],
                                                       tags=values["Tags"], requirements=values["Requirements"], 
                                                       steps=values["Steps"], component=values["Component"], automated=True)
                self.testsFromSlick.append(slicktest)
                    
    def _parseTestCaseInfo(self, test):
        foundValues = {"Author":"API", "Purpose":None, "Attributes":None, 
                       "Tags":[], "Requirements":None, "Component":None,
                       "Steps": [{'name': "Testcase", 'expectedResult': "Coming Soon"}]}

        # Set Author if it is found in the main doc string
        testInfo = test.__doc__
        if testInfo:
            testInfoLines = testInfo.splitlines()
            for line in testInfoLines:
                if "Author:" in line:
                    foundValues["Author"] = line.replace("Author:", "").strip()
            
        # Now look at individual test cases for other info
        testInfo = test._testMethodDoc
        testInfoLines = testInfo.splitlines()
        for line in testInfoLines:
            # Expecting the values to be formatted like 'Author: Jared' Each value on its own line except for test steps
            if "Author:" in line:
                foundValues["Author"] = line.replace("Author:", "").strip()
            
            elif "Purpose:" in line:
                foundValues["Purpose"] = line.replace("Purpose:", "").strip()
            
            elif "Attributes" in line:
                foundValues["Attributes"] = line.replace("Attributes:", "").strip()
            
            # Tags need to be a list of strings. Value needs to be separated with a ","
            elif "Tags" in line:
                foundValues["Tags"] = line.replace("Tags:", "").strip()
                foundValues["Tags"] = foundValues["Tags"].split(",")
            
            elif "Requirements" in line:
                foundValues["Requirements"] = line.replace("Requirements:", "").strip()
            
            elif "Component" in line:
                # We need to see if component exists. If not add it? Yeah sure I will add it 
                foundValues["Component"] = line.replace("Component:", "").strip()
                try:
                    components = self.slickCon.get_components()
                except SlickError:
                    components = []
                    
                componentNames = []
                for component in components:                    
                    componentNames.append(component["name"])
                if foundValues["Component"] not in componentNames:
                    self.slickCon.add_component(foundValues["Component"])
                
                foundValues["Component"] = {"name": foundValues["Component"]}
            
            # Steps needs to be a list of step object. [{'name': 'testName', 'expectedResult': 'outcome'}]
            elif "Steps" in line:
                foundValues["Steps"] = []
                for step in testInfoLines:
                    testStep = re.search('([^\r\n]+\s+[^\r\n]+);\s+([^\r\n]+)', step)
                    if testStep:
                        foundValues["Steps"].append({"name": testStep.group(1), "expectedResult": testStep.group(2)})
                        
        return foundValues
                
    def _checkTestPlan(self):
        try:
            tp = self.slickCon.get_test_plan(self.testPlan)
        except SlickError:
            # if not, create it? This assumes too much. Should we make them pass in the whole test plan?
            self.testPlan = self.slickCon.add_test_plan(self.testPlan, queries=[
                    {"query": {"className": "org.tcrun.slickij.api.data.testqueries.ContainsTags",
                               "tagnames": [self.testPlan]},"name": self.testPlan}])
        if not tp:
            self.testPlan = self.slickCon.add_test_plan(self.testPlan, queries=[
                {"query": {"className": "org.tcrun.slickij.api.data.testqueries.ContainsTags",
                           "tagnames": [self.testPlan]},"name": self.testPlan}])
        else:
            self.testPlan = tp

    def _setTestRunRef(self, testRun):
        self.testRunRef = {"name": testRun["name"], "id": testRun["id"]}

    def setup_test_run(self, testSuite, loggername):
        '''setup the test run so multiple tests can be placed in one test run'''
        self._checkTests(testSuite)
        self.logger_name = loggername
        
        # Set all tests to not tested, so every test has a result and the final result reflects all tests
        # We need to keep all the results in a list. (Complete result object)
        # 1. Creat a result in slick for each test that we will run
        self.not_tested_result_list = []
        for test in self.testsFromSlick:
            self.not_tested_result_list.append(self.slickCon.add_result(self.testRunRef, test, get_date(), 
                                                                        "NOT_TESTED", "TO_BE_RUN", 
                                                                        componentRef=test["component"], hostname=loggername))
        
        # 2. Pass the result to the corrisponding test case 
        
        # 3. Change result to update instead of add (This will be done in the result class)
        
    def run(self, test):
        """Run the given test case or test suite."""
        
        # This is initializing the given result class. This is how we will treat the result
        result = self._makeSlickResult()
        
        # These are set to keep compatibility with the unit test framework
        registerResult(result)
        result.failfast = self.failfast
        
        startTestRun = getattr(result, 'startTestRun', None)
        if startTestRun is not None:
            startTestRun()
        try:
            test(result)
        finally:
            stopTestRun = getattr(result, 'stopTestRun', None)
            if stopTestRun is not None:
                stopTestRun()

        return result
    
