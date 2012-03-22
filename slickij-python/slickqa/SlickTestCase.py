import logging
import sys
from unittest import TestCase, SkipTest
from unittest.case import _ExpectedFailure, _UnexpectedSuccess
from slickApi import SlickAsPy
from slickLogging import Slicklogger

class SlickTestCase(TestCase):
    """"""
    logger = None
    queued_files = []
    # for wing ide source assistance and code completion
    isinstance(logger, Slicklogger)
    
    def add_file(self, file_name, mime_type, data, chunksize=None, uploaddate=None, md5=None, length=None):
        self.queued_files.append((file_name, mime_type, data, chunksize, uploaddate, md5, length))
    
    @classmethod
    def add_class_file(cls, file_name, mime_type, data, chunksize=None, uploaddate=None, md5=None, length=None):
        cls.queued_files.append((file_name, mime_type, data, chunksize, uploaddate, md5, length))
        
    def clear_queue(self):
        self.queued_files = []
    
    def run(self, result=None):
            orig_result = result
            if result is None:
                result = self.defaultTestResult()
                startTestRun = getattr(result, 'startTestRun', None)
                if startTestRun is not None:
                    startTestRun()
    
            self._resultForDoCleanups = result
            result.startTest(self)
    
            testMethod = getattr(self, self._testMethodName)
            if (getattr(self.__class__, "__unittest_skip__", False) or
                getattr(testMethod, "__unittest_skip__", False)):
                # If the class or method was skipped.
                try:
                    skip_why = (getattr(self.__class__, '__unittest_skip_why__', '')
                                or getattr(testMethod, '__unittest_skip_why__', ''))
                    self._addSkip(result, skip_why)
                finally:
                    result.stopTest(self)
                return
            try:
                success = False
                try:
                    self.setUp()
                except SkipTest as e:
                    self._addSkip(result, str(e))
                except Exception:
                    result.addError(self, sys.exc_info())
                try:
                    testMethod()
                except self.failureException:
                    result.addFailure(self, sys.exc_info())
                except _ExpectedFailure as e:
                    addExpectedFailure = getattr(result, 'addExpectedFailure', None)
                    if addExpectedFailure is not None:
                        addExpectedFailure(self, e.exc_info)
                    else:
                        warnings.warn("TestResult has no addExpectedFailure method, reporting as passes",
                                      RuntimeWarning)
                        result.addSuccess(self)
                except _UnexpectedSuccess:
                    addUnexpectedSuccess = getattr(result, 'addUnexpectedSuccess', None)
                    if addUnexpectedSuccess is not None:
                        addUnexpectedSuccess(self)
                    else:
                        warnings.warn("TestResult has no addUnexpectedSuccess method, reporting as failures",
                                      RuntimeWarning)
                        result.addFailure(self, sys.exc_info())
                except SkipTest as e:
                    self._addSkip(result, str(e))
                except Exception:
                    result.addError(self, sys.exc_info())
                else:
                    success = True
        
                try:
                    self.tearDown()
                except Exception:
                    result.addError(self, sys.exc_info())
                    success = False
    
                cleanUpSuccess = self.doCleanups()
                success = success and cleanUpSuccess
                if success:
                    result.addSuccess(self)
            finally:
                result.stopTest(self)
                self.logger.end_test(result._results[len(result._results)-1]["id"])
                if orig_result is None:
                    stopTestRun = getattr(result, 'stopTestRun', None)
                    if stopTestRun is not None:
                        stopTestRun()
