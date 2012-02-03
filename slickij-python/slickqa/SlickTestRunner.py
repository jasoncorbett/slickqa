import time
import slickLogging
from slickApi import SlickAsPy, SlickError
from SlickTestResult import SlickTestResult
from unittest import TextTestRunner
from unittest.signals import registerResult
from unittest import TestSuite

class SlickTestRunner(TextTestRunner):
    """A test runner class that logs results to the Slickqa results database.

    It uses the python rest api to communicate.
    """
    def __init__(self, projectName="Slickij Developer Project", testplanName="The plan", 
                 slickLocation="http://localhost:8080/api", stream=None, descriptions=True,
                 verbosity=1, failfast=False, buffer=False, resultclass=SlickTestResult):
        super(SlickTestRunner, self).__init__(stream, descriptions, verbosity, failfast, buffer, resultclass)
        self.testPlan = testplanName
        self.slickCon = SlickAsPy(slickLocation)
        self.project = self.slickCon.get_project_by_name(projectName)
        self.logger = slickLogging.start_logging(testplanName, self.slickCon)

    def _makeSlickResult(self):
        return self.resultclass(self.project, self.testRunRef, self.slickCon, self.stream, self.descriptions, self.verbosity)
    
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
                    slicktest = self.slickCon.get_testcases_by_name(test.shortDescription())
                except SlickError:
                    pass
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

    def run(self, test):
        "Run the given test case or test suite."
        # check to see if all tests exist
        self._checkTests(test)
        # check to see if the test plan exists
        self._checkTestPlan()
        # create a test run
        self._setTestRunRef(self.slickCon.add_test_run(self.testPlan["name"], self.testPlan["id"]))

        result = self._makeSlickResult()
        registerResult(result)
        result.failfast = self.failfast
        startTestRun = getattr(result, 'startTestRun', None)
        if startTestRun is not None:
            startTestRun()
        try:
            test(result, self.logger)
        finally:
            stopTestRun = getattr(result, 'stopTestRun', None)
            if stopTestRun is not None:
                stopTestRun()

        return result
    