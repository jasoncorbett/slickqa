import logging
from slickApi import SlickAsPy
from SlickTestRunner import SlickTestRunner
from SlickTestCase import SlickTestCase
from slickLogging import SlickFormatter,SlickHandler
from SlickTestSuite import SlickTestSuite
from questLogger import newLogger,start_logging

class SlickTest(SlickTestCase):
    """Sample test case"""
    
    @classmethod
    def setUpClass(cls):
        cls.logger.info("in setupclass")
        #cls.mac.install()
    
    def test_01(self):
        """test1-:bug"""
        self.logger.warn("in test 1")
        self.logger.warn("in test 1")
        self.logger.warn("in test 1")
        self.logger.warn("in test 1")
        self.logger.info("I'm betting this passes")
    
    def test_02(self):
        """test2"""
        self.logger.error("in test 2")
        self.logger.warn("in test 2")
        self.logger.info("in test 2")
        self.logger.debug("in test 2")
        self.logger.info("in test 2")
        self.logger.warn("in test 2")
        self.logger.warn("in test 2")
        self.assertThisIsGoingToBeAnError(WhatDayathink)
    
    def test_03(self):
        """test3"""
        self.logger.info("in test 3")
        self.logger.info("in test 3")
        self.logger.info("in test 3")
        self.logger.debug("in test 3")
        self.logger.debug("in test 3")
        self.logger.warn("in test 3")
        self.logger.warn("in test 3")
        self.assertTrue(False, "this is what a failure looks like")
        
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
        self.logger.warn("I'm going to skip this test")
        self.skipTest("Skipping cause I said so!")
        
    def test_05(self):
        """test5"""
        self.logger.warn("I have a bad feeling about this")
    
    @classmethod
    def tearDownClass(cls):
        cls.logger.warn("in teardownclass, finally")
        cls.mac.install()
    
if __name__ == '__main__':
    import unittest
    import sys
    slickcon = SlickAsPy("http://localhost:8080/api")
    handler = logging.StreamHandler(sys.stdout)
    unittest.loader.TestLoader.suiteClass = SlickTestSuite
    tests = unittest.TestLoader().loadTestsFromTestCase(SlickTest)    
    logger = start_logging('test1', slickcon, otherhandlers=handler)
    testme = SlickTestRunner("1.0.311", "6", slickLocation=slickcon)
    SlickTest.logger = logger
    tests.set_logger(logger.name)
    testme.setup_test_run(tests, 'test1')
    result = testme.run(tests)
    
    print result
    