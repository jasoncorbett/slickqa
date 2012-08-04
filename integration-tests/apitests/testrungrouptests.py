__author__ = 'jcorbett'

import unittest
from rest import RestApi


class ProjectTest(unittest.TestCase):
    """
    Tests for adding, deleting, updating specifically projects.
    """

    def setUp(self):
        self.api = RestApi("http://localhost:9090/api")

    def test_add_testrun_group(self):
        """Add testrun group test"""


