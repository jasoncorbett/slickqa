/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcrun.slickij.api.data.dao;

import com.google.code.morphia.dao.DAO;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.*;

/**
 *
 * @author jcorbett
 */
public interface TestrunDAO extends DAO<Testrun, ObjectId>
{
	public Testrun findByTestrunReference(TestrunReference ref);

	public TestRunSummary getSummary(Testrun run);

	public void rescheduleByStatus(ObjectId testrunid, ResultStatus status);

    public void addNewResultStatusToRun(ObjectId testrunid, ResultStatus status);

    public void changeResultStatus(ObjectId testrunid, ResultStatus old, ResultStatus newStatus);

    public void deleteResultStatusFromRun(ObjectId testrunid, ResultStatus status);

    public Testrun updateSummary(Testrun run);

}
