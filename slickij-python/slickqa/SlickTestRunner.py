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
                try:
                    slicktest = self.slickCon.get_testcases_with_name(test.shortDescription())
                except SlickError as se:
                    print traceback.format_exc()
                if not slicktest:
                    # if not, add them?
                    
                    # Parse the data and add it to add test. This will populate the testcase
                    #self._parseTestCaseInfo(test)
                    
                    slicktest = self.slickCon.add_testcase(test.shortDescription(), automated=True)
                    self.testsFromSlick.append(slicktest)
                    
                else:
                    # If we find multiple tests with the same name, we will run the last one it finds. Tests need a unique name 
                    if isinstance(slicktest, list):
                        slicktest = slicktest.pop()
                        
                    self.testsFromSlick.append(slicktest)
                    
    def _parseTestCaseInfo(self, test):
        foundValues = {}
        testInfo = test.__doc__
        testInfoLines = testInfo.splitlines()
        
        for line in testInfoLines:
            # Expecting the values to be formatted like 'Author: Jared' Each value on its own line except for test steps
            if "Author:" in line:
                foundValues["Author"] = line.replace("Author:", "").strip()
            elif "Purpose:" in line:
                foundValues["Purpose"] = line.replace("Purpose:", "").strip()
                
        

    def _checkTestPlan(self):
        try:
            tp = self.slickCon.get_test_plan(self.testPlan)
        except SlickError:
            # if not, create it? This assumes too much. Should we make them pass in the whole test plan?
            tp = None
            if not tp:
                self.testPlan = self.slickCon.add_test_plan(self.testPlan, queries=[
                        {"query": {"className": "org.tcrun.slickij.api.data.testqueries.ContainsTags",
                                   "tagnames": [self.testPlan]},"name": self.testPlan}])
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
            self.not_tested_result_list.append(self.slickCon.add_result(self.testRunRef, test, get_date(), "NOT_TESTED", "TO_BE_RUN"))
        
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
    
