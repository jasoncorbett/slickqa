__author__ = 'jcorbett'

import unittest
from rest import RestApi


class ProjectTest(unittest.TestCase):
    """
    Tests for adding, deleting, updating specifically projects.
    """

    def setUp(self):
        self.api = RestApi("http://localhost:9090/api")

    def create_project(self):
        return self.api.post({"name": "Another Project"}, "projects")

    def test_add_delete(self):
        """Add and Delete Project"""
        proj = self.create_project()
        self.assertTrue(proj is not None)
        self.assertTrue(proj["id"] is not None)
        self.api.delete("projects", proj["id"])
        deleted = False
        try:
            self.api.get("projects", proj["id"])
        except:
            deleted = True

        self.assertTrue(deleted)

    def test_update_description(self):
        """Add and Update a Project with a description"""
        proj = self.create_project()
        self.assertTrue("description" not in proj or proj["description"] is None)

        newproj = self.api.put("A description", "projects", proj["id"], "description")

        self.assertEqual(proj["id"], newproj["id"])
        self.assertEqual(proj["name"], newproj["name"])
        self.assertEqual(u"A description", newproj["description"])

        self.api.delete("projects", proj["id"])

    def test_update_name(self):
        """Add and Update a Project with a different name"""
        proj = self.create_project()

        newproj = self.api.put("A new name", "projects", proj["id"], "name")

        self.assertEqual(proj["id"], newproj["id"])
        self.assertEqual(u"A new name", newproj["name"])

        self.api.delete("projects", proj["id"])




