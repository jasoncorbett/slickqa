import logging
import traceback
import slickLogging
from slickApi import SlickAsPy, SlickError
from SlickTestResult import SlickTestResult
from unittest import TextTestRunner
from unittest.signals import registerResult

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
        return self.resultclass(self.project, self.testRunRef, self.slickCon, self.logger_name)
    
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
                    self.slickCon.add_testcase(test.shortDescription(), automated=True)

    def _checkTestPlan(self):
        tp = self.slickCon.get_test_plan(self.testPlan)
        # if not, create it? This assumes too much. Should we make them pass in the whole test plan?
        if not tp:
            self.testPlan = self.slickCon.add_test_plan(self.testPlan, queries=[
                    {"query": {"className": "org.tcrun.slickij.api.data.testqueries.ContainsTags",
                               "tagnames": [self.testPlan]},"name": self.testPlan}])
        else:
            self.testPlan = tp

    def _setTestRunRef(self, testRun):
        self.testRunRef = {"name": testRun["name"], "id": testRun["id"]}

    def setup_test_run(self, test, loggername):
        '''setup the test run so multiple tests can be placed in one test run'''
        self._checkTests(test)
        self.logger_name = loggername

    def run(self, test):
        """Run the given test case or test suite."""
        result = self._makeSlickResult()
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
    
