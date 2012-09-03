__author__ = 'jcorbett'

import unittest
import types
import time
import random
from rest import RestApi, get_default_release, get_default_build, AttributeDict
random.seed()

class TestrunGroupTests(unittest.TestCase):
    """
    Tests for adding, deleting, updating specifically projects.
    """

    def setUp(self):
        self.api = RestApi("http://localhost:9090/api")
        self.project = self.api.projects.get(name="Slickij Developer Project")[0]
        self.testruns = self.api.testruns.get(projectid=self.project.id)

    @classmethod
    def setUpClass(cls):
        """Make sure that we have testruns with results in slick."""
        api = RestApi("http://localhost:9090/api")
        proj = api.projects.byname["Slickij Developer Project"].get()
        release = get_default_release(proj)
        build = get_default_build(release)
        env = api.configurations.get(name="Default Environment")
        if isinstance(env, types.ListType):
            env = env[0]
        base_testcase = {"project": {"id": proj.id,
                                     "name": proj.name},
                         "component": {"id": proj.components[0].id,
                                       "name": proj.components[0].name,
                                       "code": proj.components[0].code},
                         "automated": True,
                         "automationTool": "python",
                         "steps": [{"name": "Step 1",
                                    "expectedResult": "Step 1 completed successfully"},
                                   {"name": "Step 2",
                                    "expectedResult": "Step 2 completed successfully"},
                                   {"name": "Step 3",
                                    "expectedResult": "Step 3 completed successfully"}]}
        testcases = []
        for i in range(10):
            testcase = AttributeDict(base_testcase)
            testcase["name"] = "Example Testcase #" + str(i + 1)
            testcases.append(api.testcases.post(testcase))

        base_testrun = {"release": {"releaseId": release.id,
                                    "name": release.name},
                        "build": {"buildId": build.id,
                                  "name": build.name},
                        "config": {"configId": env.id,
                                   "name": env.name},
                        "project": {"id": proj.id,
                                    "name": proj.name}}

        testruns = []
        for i in range(6):
            testrun = dict(base_testrun)
            testrun["name"] = "Testrun " + str(i + 1)
            testrun = api.testruns.post(testrun)
            for j in range(random.randint(20, 40)):
                result = AttributeDict(base_testrun)
                result["testrun"] = {"name": testrun["name"],
                                     "testrunId": testrun["id"]}
                test = random.choice(testcases)
                result["testcase"] = {"testcaseId": test["id"],
                                      "name": test["name"],
                                      "automationTool": "python"}
                result["hostname"] = "localhost"
                result["runstatus"] = "FINISHED"
                result["status"] = random.choice(["PASS", "PASS", "PASS", "PASS", "FAIL", "FAIL", "BROKEN_TEST"])
                result["reason"] = "Randomly chose a result of " + result.status
                api.results.post(result)
            testrun = api.testruns[testrun.id].get()
            testruns.append(testrun)

        cls.testruns = testruns

    def test_add_testrun_group(self):
        """Add testrun group test"""
        group = self.api.testrungroups.post({"name": "A test testrun group"})
        self.assertIn('id', group)

    def test_add_testruns_to_group(self):
        self.assertGreater(len(TestrunGroupTests.testruns), 0)
        group = self.api.testrungroups.get(createdafter=(int(time.time()) - (60 * 60 * 24)))[0]
        total_tests = group.groupSummary.total
        self.assertIn('id', group)
        for testrun in TestrunGroupTests.testruns:
            self.api.testrungroups[group.id].addtestrun[testrun.id].post(None)

        group = self.api.testrungroups[group.id].get()
        self.assertEqual(len(group.testruns), len(TestrunGroupTests.testruns))
        self.assertNotEqual(group.groupSummary.total, total_tests)

    def test_group_summary_accurate(self):
        totals = {}
        group = self.api.testrungroups.get(createdafter=(int(time.time()) - (60 * 60 * 24)))[0]
        self.assertIn('id', group)
        for testrun in group.testruns:
            for resultstatus in testrun.summary.resultsByStatus.keys():
                if resultstatus in totals:
                    totals[resultstatus] += testrun.summary.resultsByStatus[resultstatus]
                else:
                    totals[resultstatus] = testrun.summary.resultsByStatus[resultstatus]

        for resultstatus in group.groupSummary.resultsByStatus.keys():
            if group.groupSummary.resultsByStatus[resultstatus] != 0:
                self.assertEquals(group.groupSummary.resultsByStatus[resultstatus], totals[resultstatus])

    def test_remove_testrun_from_group(self):
        group = self.api.testrungroups.get(createdafter=(int(time.time()) - (60 * 60 * 24)))[0]
        self.assertIn('id', group)
        total_tests = group.groupSummary.total
        num_of_testruns = len(group.testruns)
        self.assertGreaterEqual(num_of_testruns, 1)
        self.api.testrungroups[group.id].removetestrun[group.testruns[0].id].delete()
        group = self.api.testrungroups.get(createdafter=(int(time.time()) - (60 * 60 * 24)))[0]
        self.assertLess(len(group.testruns), num_of_testruns)
        self.assertNotEqual(group.groupSummary.total, total_tests)



