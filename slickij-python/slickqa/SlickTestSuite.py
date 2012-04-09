import sys
import logging
from unittest import TestSuite, util, case
from unittest.suite import _isnotsuite,_DebugResult,_ErrorHolder
from SlickTestResult import SlickTestResult

class SlickTestSuite(TestSuite):
    def __init__(self, tests=(), loggerName='root'):
        super(SlickTestSuite, self).__init__(tests)
        
    def set_logger(self, loggerName):
        self.logger = logging.getLogger("{}.testsuite".format(loggerName))
        
    def run(self, result, debug=False):
        topLevel = False
        if getattr(result, '_testRunEntered', False) is False:
            result._testRunEntered = topLevel = True

        for test in self:
            if result.shouldStop:
                break

            if _isnotsuite(test):
                self._tearDownPreviousClass(test, result)
                self._handleModuleFixture(test, result)
                self._handleClassSetUp(test, result)
                result._previousTestClass = test.__class__

                if (getattr(test.__class__, '_classSetupFailed', False) or
                    getattr(result, '_moduleSetUpFailed', False)):
                    continue

            if not debug:
                test(result)
            else:
                test.debug()

        if topLevel:
            self._tearDownPreviousClass(None, result)
            self._handleModuleTearDown(result)
            result._testRunEntered = False
        return result
    
    def _addClassOrModuleLevelException(self, result, exception, errorName):
        error = _ErrorHolder(errorName)
        if isinstance(result, SlickTestResult):
            # if setup, get future test case
            if 'setUp' in errorName:
                test = self._tests[0]
                if isinstance(exception, case.SkipTest):
                    result.addSkip(test, 'Just for fun')
                else:
                    result.addError(test, sys.exc_info())
                last_result = result.get_last_result()
            # if teardown, get last test case
            else:
                last_result = result.get_last_result()
                if isinstance(exception, case.SkipTest):
                    last_result['status'] = "SKIPPED"
                else:
                    last_result['status'] = "BROKEN_TEST"
                result.logger.error("TearDown failed", exc_info=exception)
                result.update_result(last_result)
            self.logger.end_test(last_result['id'])
        else:
            addSkip = getattr(result, 'addSkip', None)
            if addSkip is not None and isinstance(exception, case.SkipTest):
                addSkip(error, str(exception))
            else:
                result.addError(error, sys.exc_info())
            
    def _tearDownPreviousClass(self, test, result):
        previousClass = getattr(result, '_previousTestClass', None)
        currentClass = test.__class__
        if currentClass == previousClass:
            return
        if getattr(previousClass, '_classSetupFailed', False):
            return
        if getattr(result, '_moduleSetUpFailed', False):
            return
        if getattr(previousClass, "__unittest_skip__", False):
            return

        tearDownClass = getattr(previousClass, 'tearDownClass', None)
        if tearDownClass is not None:
            try:
                tearDownClass()
            except Exception, e:
                if isinstance(result, _DebugResult):
                    raise
                className = util.strclass(previousClass)
                errorName = 'tearDownClass (%s)' % className
                self._addClassOrModuleLevelException(result, e, errorName)
            finally:
                self.logger.end_test(result.get_last_result_id())
                if hasattr(test, 'queued_files') and test.queued_files:
                    updateMe = result.get_last_result()
                    updateMe['files'].extend(test.queued_files)
                    test.clear_queue()
                    result.update_result(updateMe)
