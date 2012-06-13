from slickqa.SlickTestCase import SlickTestCase
from slickqa.SlickTestSuite import SlickTestSuite

# This sample test case shows the basic usage of slick
# combined with the unittest framework.
# You can run this file and it will run the tests.

class SlickTest(SlickTestCase):
    """Sample test case
    Author: Jared Jorgensen
    """
    
    @classmethod
    def setUpClass(cls, logger):
        logger.debug("in setupclass")
    
    def test_01(self):
        """test1
        Author: Jared Jorgensen
        """
        self.logger.warn("in test 1")
    
    def test_02(self):
        """test2"""
        self.logger.error("in test 2")
    
    def test_03(self):
        """test3"""
        self.logger.info("in test 3")
        
    def test_04(self):
        """test4"""
        self.logger.info("in test 4")
    
    @classmethod
    def tearDownClass(cls, logger):
        logger.debug("in teardownclass")
    
if __name__ == '__main__':
    import SlickTestRunner
    import unittest
    testme = SlickTestRunner.SlickTestRunner()
    testme.slickCon.get_project_by_name("Slickij Developer Project")
    slick_release = testme.slickCon.add_release("1.0.311.311")
    testme.slickCon.set_default_release(slick_release["id"])
    slick_build = testme.slickCon.add_build("113")
    testme.slickCon.set_default_build(slick_build["id"])
    unittest.loader.TestLoader.suiteClass = SlickTestSuite
    tests = unittest.TestLoader().loadTestsFromTestCase(SlickTest)
    testme.run(tests)

    