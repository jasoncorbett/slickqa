from slickApi import *
from unittest import TestResult
from datetime import datetime, timedelta

FIN = "FINISHED"

class SlickTestResult(TestResult):
    """Adds reporting through slick"""

    def __init__(self, project, testRunRef, slick, stream=None, descriptions=None, verbosity=None):
        """Constructor"""
        super(SlickTestResult, self).__init__(stream, descriptions, verbosity)
        self.slick = slick
        self.project = project
        self.testRunRef = testRunRef
        
        isinstance(slick, SlickAsPy)
        
    def getTestCaseName(self, test):
        return test.shortDescription()

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
        self.slick.add_result(self.testRunRef, self._getTest(self.getTestCaseName(test)), 
            datetime.now().isoformat(), "PASS", FIN, runLength=taken, hostname=self._getHostname(test))

    def addError(self, test, err):
        super(SlickTestResult, self).addError(test, err)
        taken = self._getTestTimeTaken(self.testStartTime)
        self.slick.add_result(self.testRunRef, self._getTest(self.getTestCaseName(test)), 
            datetime.now().isoformat(), "BROKEN_TEST", FIN, runLength=taken, hostname=self._getHostname(test))

    def addFailure(self, test, err):
        super(SlickTestResult, self).addFailure(test, err)
        taken = self._getTestTimeTaken(self.testStartTime)
        self.slick.add_result(self.testRunRef, self._getTest(self.getTestCaseName(test)),
            datetime.now().isoformat(), "FAIL", FIN, runLength=taken, hostname=self._getHostname(test))

    def addSkip(self, test, reason):
        super(SlickTestResult, self).addSkip(test, reason)
        taken = self._getTestTimeTaken(self.testStartTime)
        self.slick.add_result(self.testRunRef, self._getTest(self.getTestCaseName(test)), 
            datetime.now().isoformat(), "SKIPPED", FIN, runLength=taken, hostname=self._getHostname(test))

        # TODO: What should we set this result to in slick?
    def addExpectedFailure(self, test, err):
        super(SlickTestResult, self).addExpectedFailure(test, err)
        taken = self._getTestTimeTaken(self.testStartTime)
        self.slick.add_result(self.testRunRef, self._getTest(self.getTestCaseName(test)), 
            datetime.now().isoformat(), "BROKEN_TEST", FIN, runLength=taken, hostname=self._getHostname(test))

        # TODO: What should we set this result to in slick?
    def addUnexpectedSuccess(self, test):
        super(SlickTestResult, self).addUnexpectedSuccess(test)
        taken = self._getTestTimeTaken(self.testStartTime)
        self.slick.add_result(self.testRunRef, self._getTest(self.getTestCaseName(test)), 
            datetime.now().isoformat(), "FAIL", FIN, runLength=taken, hostname=self._getHostname(test))
        
    def _getTestTimeTaken(self, start):
        stop = datetime.now()
        timetaken = stop - start
        return timetaken.seconds
    
    def _getTest(self, testcaseName):
        return self.slick.get_testcases_by_name(testcaseName).pop()
    
    def _getHostname(self, test):
        '''Override to provide hostname info'''
        if hasattr(test, "computer"):
            if hasattr(test.computer, "hostname"):
                return test.computer.hostname
        else:
            return None
        

    