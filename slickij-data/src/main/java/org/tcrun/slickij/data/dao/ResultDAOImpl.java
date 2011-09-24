package org.tcrun.slickij.data.dao;

import org.tcrun.slickij.api.data.dao.ResultDAO;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.dao.BasicDAO;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import com.google.code.morphia.query.UpdateResults;
import com.google.inject.Inject;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.Result;
import org.tcrun.slickij.api.data.ResultStatus;
import org.tcrun.slickij.api.data.RunStatus;
import org.tcrun.slickij.api.data.TestRunParameter;
import org.tcrun.slickij.api.data.Testrun;

/**
 *
 * @author jcorbett
 */
public class ResultDAOImpl extends BasicDAO<Result, ObjectId> implements ResultDAO
{
	@Inject
	public ResultDAOImpl(Datastore ds)
	{
		super(Result.class, ds);
	}

	@Override
	public List<Result> findResultsByTestrun(Testrun testrun)
	{
		return createQuery().field("testrun.testrunId").equal(testrun.getObjectId()).order("recorded").asList();
	}

	@Override
	public Result getNextToBeRun(TestRunParameter params)
	{
		// first check for running tests with that hostname
		Query<Result> queryForRunningTests = createQuery();
		queryForRunningTests.criteria("runstatus").equal(RunStatus.RUNNING);
		queryForRunningTests.criteria("hostname").equal(params.getHostname());
		if(queryForRunningTests.countAll() > 0)
		{
			return queryForRunningTests.asList().get(0);
		}


		Query<Result> query = createQuery();

		query.criteria("runstatus").equal(RunStatus.TO_BE_RUN);
		if(params.getAutomationTool() != null)
			query.criteria("testcase.automationTool").equal(params.getAutomationTool());
		List<Result> listOfPossible = query.order("-recorded").asList();
		for(Result possible : listOfPossible)
		{
			if(!possible.isSatisfiedBy(params))
				continue;
			Query<Result> updateQuery = createQuery();
			updateQuery.criteria("id").equal(possible.getObjectId());
			updateQuery.criteria("runstatus").equal(RunStatus.TO_BE_RUN);
			UpdateOperations<Result> updateOps = ds.createUpdateOperations(Result.class);
			updateOps.set("runstatus", RunStatus.RUNNING);
			updateOps.set("hostname", params.getHostname());
			updateOps.set("recorded", new Date());
			UpdateResults<Result> results = ds.update(updateQuery, updateOps);
			if(results.getUpdatedCount() > 0)
			{
				// we need to return the updated version
				return get(possible.getObjectId());
			}
		}

		return null;
	}
}
