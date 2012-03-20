import logging
from SlickTestRunner import SlickTestRunner
from SlickTestCase import SlickTestCase
from slickLogging import SlickFormatter,Slicklogger,SlickHandler,start_logging
from SlickTestSuite import SlickTestSuite

class SlickTest(SlickTestCase):
    """Sample test case"""
    
    @classmethod
    def setUpClass(cls):
        cls.logger.info("in setupclass")
    
    def test_01(self):
        """test1"""
        self.logger.warn("in test 1")
        self.logger.warn("in test 1")
        self.logger.warn("in test 1")
        self.logger.warn("in test 1")
    
    def test_02(self):
        """test2"""
        self.logger.error("in test 2")
        self.logger.warn("in test 2")
        self.logger.info("in test 2")
        self.logger.debug("in test 2")
        self.logger.info("in test 2")
        self.logger.warn("in test 2")
        self.logger.warn("in test 2")
    
    def test_03(self):
        """test3"""
        self.logger.info("in test 3")
        self.logger.info("in test 3")
        self.logger.info("in test 3")
        self.logger.debug("in test 3")
        self.logger.debug("in test 3")
        self.logger.warn("in test 3")
        self.logger.warn("in test 3")
        
        
    def test_04(self):
        """test4"""
        self.logger.info("in test 4")
        self.logger.info("in test 4")
        self.logger.info("in test 4")
        self.logger.warn("in test 4")
        self.logger.warn("in test 4")
        self.logger.error("in test 4")
        self.logger.error("in test 4")
        self.logger.debug("in test 4")
        self.logger.trace("in test 4")
        self.logger.info("in test 4")
        self.logger.error("in test 4")
    
    @classmethod
    def tearDownClass(cls):
        cls.logger.warn("in teardownclass, finally")
    
if __name__ == '__main__':
    import SlickTestRunner
    import unittest
    import sys
    handler = logging.StreamHandler(sys.stdout)
    unittest.loader.TestLoader.suiteClass = SlickTestSuite
    tests = unittest.TestLoader().loadTestsFromTestCase(SlickTest)    
    logger = start_logging('test1', slickurl="http://localhost:8080/api", otherhandlers=handler)
    testme = SlickTestRunner.SlickTestRunner(slickLocation="http://localhost:8080/api")
    testme.slickCon.get_project_by_name("Slickij Developer Project")
    slick_release = testme.slickCon.get_release_by_name("1.0.311.315")
    if not slick_release:
        slick_release = testme.slickCon.add_release("1.0.311.315")
    testme.slickCon.set_default_release(slick_release["id"])
    slick_build = testme.slickCon.get_build_by_name("5")
    if not slick_build:
        slick_build = testme.slickCon.add_build("5")
    testme.slickCon.set_default_build(slick_build["id"])
    testme.setup_test_run(tests)
    SlickTest.logger = logger
    tests.set_logger(logger.name)
    result = testme.run(tests)
    
    print result
    