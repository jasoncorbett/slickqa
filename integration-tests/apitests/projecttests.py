__author__ = 'jcorbett'

import unittest
import slumber
import json


class ProjectTest(unittest.TestCase):
    """
    Tests for adding, deleting, updating specifically projects.
    """

    def setUp(self):
        self.api = slumber.API("http://localhost:9090/api")

    def test_add_delete(self):
        """Add and Delete Project"""
        proj = json.loads(self.api.projects.post({"name": "Another Project"}))
        self.assertTrue(proj is not None)
        self.assertTrue(proj["id"] is not None)
        self.api.projects(proj["id"]).delete()
        deleted = False
        try:
            self.api.projects(proj["id"]).get()
        except:
            deleted = True

        self.assertTrue(deleted)