import logging
from slickApi import *
from unittest import TestResult
from datetime import datetime, timedelta
from dateutil.tz import *
import slick_utilities

FIN = "FINISHED"

class SlickTestResult(TestResult):
    """Adds reporting through slick"""

    def __init__(self, project, testRunRef, slick, loggername='root', not_tested_results=[]):
        """Constructor"""
        super(SlickTestResult, self).__init__()
        self.slick = slick
        self.project = project
        self.testRunRef = testRunRef
        self._results = not_tested_results
        self.logger = logging.getLogger('{}.result'.format(loggername))
        self.completed_tests = []
        
        isinstance(slick, SlickAsPy)
        
    def hasResults(self):
        return len(self.completed_tests) > 0
    
    def get_result_by_test_name(self, test_name):
        for result in self._results:
            testcase = result.get("testcase")
            if testcase:
                testcase_name = testcase.get("name")
                if testcase_name == test_name:
                    return result
        
    def getTestCaseName(self, test):
        return test.shortDescription()
    
    def startTest(self, test):
        super(SlickTestResult, self).startTest(test)
        testname = self.getTestCaseName(test)
        # TODO: add a parser that will create a test case from docstring?
        try:
            slickTest = self.slick.get_testcases_with_name(testname)
            if not slickTest:
                self.addSkip(test, "test case {} not found".format(testname))
        except SlickError as se:
            self.addSkip(test, str(se))
        finally:
            self.testStartTime = datetime.now(tzlocal())
            
    def stopTest(self, test):
        super(SlickTestResult, self).stopTest(test)
        files = self.add_files(test)
        if test._classSetupFailed:
            name = test.shortDescription()
            for r in self._results:
                if r["testcase"]["name"] == name:
                    last_result = r
        else:   
            last_result = self.get_last_result()
            
        if files:
            if isinstance(last_result.get('files', None), list):
                last_result['files'].extend(files)
            else:
                last_result['files'] = files
        
        if last_result:
            self.slick.update_result(last_result['id'], last_result)
            self.completed_tests.append(last_result)
        
        test.clear_queue() 
        
    def get_last_result(self):
        """Will remove the result from the list so make sure to add it back if you want to keep it"""
        #TODO: would pop_last_result be a better name?
        if self.completed_tests:
            return self.completed_tests.pop()
        
    def get_last_result_id(self):
        if self.completed_tests != []:
            return self.completed_tests[-1]['id']
            
    def update_result(self, result):
        #TODO: should this add the result back to the list of results as well?
        self.slick.update_result(result['id'], result)
            
    def add_files(self, test):
        if hasattr(test, "queued_files") and test.queued_files:
            return self._add_files(test.queued_files)            
            
    def _add_files(self, files):
        return [self.slick.add_stored_file(*i) for i in files]
    
    def _add_result(self, test, parent_function, result_name, arg=None):
        # TODO: Optimize this with an ordered dictionary
        if arg:
            parent_function(test, arg)
        else:
            parent_function(test)
            
        # We need to set the value of skipped tests to something other than zero, otherwise we get a crazy value like 365 hours.
        if result_name in ["SKIPPED"]:
            taken = 1
        else:
            taken = self._getTestTimeTaken(self.testStartTime)
            
        test_name = self.getTestCaseName(test)
        files = self.add_files(test)
        
        # Get the test case result from the test case name 
        test_result = self.get_result_by_test_name(test_name)
        
        if test_result:
            # Update current result with latest test information. A slick result can be seen in result.java
            test_result["status"] = result_name
            test_result["runstatus"] = "FINISHED"
            test_result["runlength"] = taken
            test_result["recorded"] = self.testStartTime.strftime('%a, %m %b %Y %H:%M:%S %Z')
            test_result["files"] = files
            test_result["hostname"] = self._getHostname(test)
            
            self.update_result(test_result)
            self.completed_tests.append(test_result)
        
        # Old way
        #result = self.slick.add_result(
            #self.testRunRef, self._getTest(test_name), slick_utilities.get_date(), 
            #result_name, FIN, fileList=files, runLength=taken, hostname=self._getHostname(test))
        #self._results.append(result)
        
        test.clear_queue()
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
        stop = datetime.now(tzlocal())
        timetaken = stop - start
        
        # Convert the seconds in to milliseconds 
        millisec = timetaken.total_seconds() * 1000
        
        return int(millisec)
    
    def _getTest(self, testcaseName):
        testname = self.slick.get_testcases_with_name(testcaseName)
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
        
