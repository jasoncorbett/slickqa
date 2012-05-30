__author__ = 'jcorbett'

import unittest
from rest import RestApi


class ProjectTest(unittest.TestCase):
    """
    Tests for adding, deleting, updating specifically projects.
    """

    def setUp(self):
        self.api = RestApi("http://localhost:9090/api")

    def create_project(self, **kwargs):
        if "name" not in kwargs:
            kwargs["name"] = "Another Project"
        return self.api.post(kwargs, "projects")

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

    def test_add_automation_tool(self):
        """Add an automation tool to the project"""
        proj = self.create_project()

        self.assertTrue(proj["automationTools"] is None or "python" not in proj["automationTools"])
        self.api.post(["python",], "projects", proj["id"], "automationTools")
        newproj = self.api.get("projects", proj["id"])
        self.assertTrue("python" in newproj["automationTools"])

        self.api.delete("projects", proj["id"])

    def test_get_automation_tools(self):
        """Getting just the automation tools from a project"""
        automationTools = ["python","tcrunij"]
        proj = self.create_project()
        emptyAutomationTools = self.api.get("projects", proj["id"], "automationTools")
        self.assertEquals([], emptyAutomationTools)
        self.api.post(automationTools, "projects", proj["id"], "automationTools")
        projAutomationTools = self.api.get("projects", proj["id"], "automationTools")
        self.assertEquals(automationTools, projAutomationTools)
        self.api.delete("projects", proj["id"])

    def test_delete_automation_tools(self):
        """Deleting an automation tool from the list."""
        proj = self.create_project(automationTools=["python", "tcrunij"])
        self.assertEquals(["python", "tcrunij"], proj["automationTools"])
        self.api.delete("projects", proj["id"], "automationTools", "tcrunij")
        proj = self.api.get("projects", proj["id"])
        self.assertEquals(["python"], proj["automationTools"])





