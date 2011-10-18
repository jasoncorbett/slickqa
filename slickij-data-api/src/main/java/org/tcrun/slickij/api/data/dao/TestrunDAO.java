/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcrun.slickij.api.data.dao;

import com.google.code.morphia.dao.DAO;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.ResultStatus;
import org.tcrun.slickij.api.data.TestRunSummary;
import org.tcrun.slickij.api.data.Testrun;
import org.tcrun.slickij.api.data.TestrunReference;

/**
 *
 * @author jcorbett
 */
public interface TestrunDAO extends DAO<Testrun, ObjectId>
{
	public Testrun findByTestrunReference(TestrunReference ref);

	public TestRunSummary getSummary(Testrun run);

	public void rescheduleByStatus(ObjectId testrunid, ResultStatus status);
}
