import logging
import sys
from unittest import TestCase, SkipTest
from unittest.case import _ExpectedFailure, _UnexpectedSuccess
from slickLogging import Slicklogger

class SlickTestCase(TestCase):
    """"""
    logger = None
    # for wing ide source assistance and code completion
    isinstance(logger, Slicklogger)
    
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
    
if __name__ == '__main__':
    import SlickTestRunner
    testme = SlickTestRunner.SlickTestRunner()
    testme.slickCon.get_project_by_name("Slickij Developer Project")
    slick_release = testme.slickCon.add_release("1.0.311.311")
    testme.slickCon.set_default_release(slick_release["id"])
    slick_build = testme.slickCon.add_build("113")
    testme.slickCon.set_default_build(slick_build["id"])
    testme.run(SlickTestCase)
