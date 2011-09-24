package org.tcrun.slickij.webgroup.reports;

import org.apache.wicket.model.LoadableDetachableModel;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.TestRunSummary;
import org.tcrun.slickij.api.data.dao.TestrunDAO;

/**
 *
 * @author jcorbett
 */
public class LoadableDetatchableTestrunSummaryModel extends LoadableDetachableModel<TestRunSummary>
{
	private ObjectId testrunId;
	private TestrunDAO trDAO;

	public LoadableDetatchableTestrunSummaryModel(TestrunDAO trDAO, TestRunSummary summary, ObjectId testrunId)
	{
		super(summary);
		this.testrunId = testrunId;
		this.trDAO = trDAO;
	}

	@Override
	protected TestRunSummary load()
	{
		return trDAO.getSummary(trDAO.get(testrunId));
	}
}
