import logging
from slickApi import *
from unittest import TestResult
from datetime import datetime, timedelta

FIN = "FINISHED"

class SlickTestResult(TestResult):
    """Adds reporting through slick"""

    def __init__(self, project, testRunRef, slick, loggername='root'):
        """Constructor"""
        super(SlickTestResult, self).__init__()
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
            
    def add_files(self, test):
        if hasattr(test, "queued_files") and test.queued_files:
            return self._add_files(test.queued_files)            
            
    def _add_files(self, files):
        return [self.slick.add_stored_file(*i) for i in files]
    
    def _add_result(self, test, parent_function, result_name, arg=None):
        if arg:
            parent_function(test, arg)
        else:
            parent_function(test)
        taken = self._getTestTimeTaken(self.testStartTime)
        test_name = self.getTestCaseName(test)
        files = self.add_files(test)
        result = self.slick.add_result(
            self.testRunRef, self._getTest(test_name), datetime.now().isoformat(), result_name, FIN, 
            fileList=files, runLength=taken, hostname=self._getHostname(test))
        self._results.append(result)
        return test_name, taken

    def addSuccess(self, test):
        super_fun = super(SlickTestResult, self).addSuccess
        test_name, taken = self._add_result(test, super_fun, "PASS")
        self.logger.info("{} passed in {}".format(test_name, taken))

    def addError(self, test, err):
        super_fun = super(SlickTestResult, self).addError
        test_name, taken = self._add_result(test, super_fun, "BROKEN_TEST", err)
        self.logger.error("{} is broken".format(test_name), exc_info=err)

    def addFailure(self, test, err):
        super_fun = super(SlickTestResult, self).addFailure
        test_name, taken = self._add_result(test, super_fun, "FAIL", err) 
        self.logger.error("{} Failed in {}".format(test_name, taken), exc_info=err)
        
    def addSkip(self, test, reason):
        super_fun = super(SlickTestResult, self).addSkip
        test_name, taken = self._add_result(test, super_fun, "SKIPPED", reason)
        self.logger.warn("{} skipped in {} because {}".format(test_name, taken, reason))
        

        # TODO: What should we set this result to in slick?
    def addExpectedFailure(self, test, err):
        super_fun = super(SlickTestResult, self).addExpectedFailure
        test_name, taken = self._add_result(test, super_fun, "BROKEN_TEST", err)
        self.logger.info("{} expected failed in {}.".format(test_name, taken), exc_info=err)
        

        # TODO: What should we set this result to in slick?
    def addUnexpectedSuccess(self, test):
        super_fun = super(SlickTestResult, self).addUnexpectedSuccess
        test_name, taken = self._add_result(test, super_fun, "BROKEN_TEST")
        self.logger.info("{} unexpectedly passed in {}".format(test_name, taken))
        
        
    def _getTestTimeTaken(self, start):
        stop = datetime.now()
        timetaken = stop - start
        return str(timetaken)
    
    def _getTest(self, testcaseName):
        testname = self.slick.get_testcases_by_name(testcaseName)
        if len(testname) > 0:
            return testname.pop()
        else:
            print "{} was not found in slick".format(testcaseName)
            return testcaseName
    
    def _getHostname(self, test):
        """Override to provide hostname info"""
        if hasattr(test, "computer"):
            if hasattr(test.computer, "hostname"):
                return test.computer.hostname
        else:
            return "None"
        