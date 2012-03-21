import logging
from slickApi import *
from unittest import TestResult
from datetime import datetime, timedelta

FIN = "FINISHED"

class SlickTestResult(TestResult):
    """Adds reporting through slick"""

    def __init__(self, project, testRunRef, slick, stream=None, descriptions=None, verbosity=None, loggername='root'):
        """Constructor"""
        super(SlickTestResult, self).__init__(stream, descriptions, verbosity)
        self.slick = slick
        self.project = project
        self.testRunRef = testRunRef
        self._results = []
        self.logger = logging.getLogger('{}.result'.format(loggername))
        
        isinstance(slick, SlickAsPy)
        
    def hasResults(self):
        return len(self._results) > 0
        
    def getTestCaseName(self, test):
        return test.shortDescription()
    
    def startTestRun(self):
        self.testStartTime = datetime.now()

    def startTest(self, test):
        super(SlickTestResult, self).startTest(test)
        testname = self.getTestCaseName(test)
        # TODO: add a parser that will create a test case from docstring?
        try:
            slickTest = self.slick.get_testcases_by_name(testname)
            if not slickTest:
                self.addSkip(test, "test case {} not found".format(testname))
        except SlickError as se:
            self.addSkip(test, str(se))
        finally:
            self.testStartTime = datetime.now()

    def addSuccess(self, test):
        super(SlickTestResult, self).addSuccess(test)
        taken = self._getTestTimeTaken(self.testStartTime)
        test_name = self.getTestCaseName(test)
        result = self.slick.add_result(self.testRunRef, self._getTest(test_name), datetime.now().isoformat(),
                                       "PASS", FIN, runLength=taken, hostname=self._getHostname(test))
        self.logger.info("Test {} passed in {}".format(test_name, taken))
        self._results.append(result)

    def addError(self, test, err):
        super(SlickTestResult, self).addError(test, err)
        taken = self._getTestTimeTaken(self.testStartTime)
        test_name = self.getTestCaseName(test)
        result = self.slick.add_result(self.testRunRef, self._getTest(test_name), datetime.now().isoformat(),
                                       "BROKEN_TEST", FIN, runLength=taken, hostname=self._getHostname(test))
        self.logger.error("Test {} is broken".format(test_name), exc_info=err)
        self._results.append(result)

    def addFailure(self, test, err):
        super(SlickTestResult, self).addFailure(test, err)
        taken = self._getTestTimeTaken(self.testStartTime)
        test_name = self.getTestCaseName(test)
        result = self.slick.add_result(self.testRunRef, self._getTest(test_name), datetime.now().isoformat(),
                                       "FAIL", FIN, runLength=taken, hostname=self._getHostname(test))
        self.logger.error("Test {} Failed in {}".format(test_name, taken), exc_info=err)
        self._results.append(result)

    def addSkip(self, test, reason):
        super(SlickTestResult, self).addSkip(test, reason)
        taken = self._getTestTimeTaken(self.testStartTime)
        test_name = self.getTestCaseName(test)
        result = self.slick.add_result(self.testRunRef, self._getTest(test_name), datetime.now().isoformat(),
                                       "SKIPPED", FIN, runLength=taken, hostname=self._getHostname(test))
        self.logger.warn("Test {} skipped in {} because {}".format(test_name, taken, reason))
        self._results.append(result)

        # TODO: What should we set this result to in slick?
    def addExpectedFailure(self, test, err):
        super(SlickTestResult, self).addExpectedFailure(test, err)
        taken = self._getTestTimeTaken(self.testStartTime)
        test_name = self.getTestCaseName(test)
        result = self.slick.add_result(self.testRunRef, self._getTest(test_name), datetime.now().isoformat(),
                                       "BROKEN_TEST", FIN, runLength=taken, hostname=self._getHostname(test))
        self.logger.info("Test {} expected failed in {}.".format(test_name, taken), exc_info=err)
        self._results.append(result)

        # TODO: What should we set this result to in slick?
    def addUnexpectedSuccess(self, test):
        super(SlickTestResult, self).addUnexpectedSuccess(test)
        taken = self._getTestTimeTaken(self.testStartTime)
        test_name = self.getTestCaseName(test)
        result = self.slick.add_result(self.testRunRef, self._getTest(test_name), datetime.now().isoformat(),
                                       "BROKEN_TEST", FIN, runLength=taken, hostname=self._getHostname(test))
        self.logger.info("Test {} unexpectedly passed in {}".format(test_name, taken))
        self._results.append(result)
        
    def _getTestTimeTaken(self, start):
        stop = datetime.now()
        timetaken = stop - start
        return str(timetaken)
    
    def _getTest(self, testcaseName):
        return self.slick.get_testcases_by_name(testcaseName).pop()
    
    def _getHostname(self, test):
        """Override to provide hostname info"""
        if hasattr(test, "computer"):
            if hasattr(test.computer, "hostname"):
                return test.computer.hostname
        else:
            return "None"
        

    