package org.tcrun.slickij.data.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.tcrun.slickij.api.data.*;
import org.tcrun.slickij.api.data.dao.ResultDAO;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.dao.BasicDAO;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import com.google.code.morphia.query.UpdateResults;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;

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

    @Override
    public ResultGroupSummary getSummary(ResultQuery query) {
        ResultGroupSummary summary = new ResultGroupSummary();
        summary.setResultQuery(query);

        DBCollection resultsCol = ds.getCollection(Result.class);
        BasicDBObject key = new BasicDBObject();
        key.put("status", true);
        BasicDBObject condition = new BasicDBObject();
        query.addToDBObject(condition);
        BasicDBObject initial = new BasicDBObject();
        initial.put("count", 0);
        initial.put("totaltime", 0);
        String reduce = "function(obj, prev) { prev.count++; prev.totaltime += obj.runlength; }";

        DBObject resultsObject = resultsCol.group(key, condition, initial, reduce);
        List<DBObject> results = (List<DBObject>)resultsObject;
        summary.setTotalTime(0);
        for(DBObject statuscount : results)
        {
            summary.getResultsByStatus().put((String)statuscount.get("status"), new Long( String.format("%.0f", (Double)statuscount.get("count"))));
            summary.setTotalTime(summary.getTotalTime() + ((Double)statuscount.get("totaltime")).intValue());
        }

        return summary;
    }

    @Override
    public List<ResultReference> getHistory(Result result)
    {
        List<ResultReference> retval = new ArrayList<ResultReference>(10);
        if(result.getConfig() != null && result.getRelease() != null)
        {
            Query<Result> query = createQuery();
            query.criteria("testcase.testcaseId").equal(result.getTestcase().getActualId());
            query.criteria("config.configId").equal(result.getConfig().getConfigObjectId());
            query.criteria("release.releaseId").equal(result.getRelease().getReleaseObjectId());
            query.criteria("recorded").lessThan(result.getRecorded());
            query.order("-recorded");
            query.retrievedFields(true, "id", "status", "recorded", "build"); // only need these fields as we are creating a reference
            for(Result hist : query)
            {
                if(retval.size() < 10 && result.getObjectId() != hist.getObjectId())
                    retval.add(hist.createReference());
            }
        }

        if(retval.size() < 10 && result.getConfig() != null && result.getRelease() != null)
        {
            Query<Result> query = createQuery();
            query.criteria("testcase.testcaseId").equal(result.getTestcase().getActualId());
            query.criteria("config.configId").equal(result.getConfig().getConfigObjectId());
            query.criteria("release.releaseId").notEqual(result.getRelease().getReleaseObjectId());
            query.criteria("recorded").lessThan(result.getRecorded());
            query.order("-recorded");
            query.retrievedFields(true, "id", "status", "recorded", "build"); // only need these fields as we are creating a reference
            for(Result hist : query)
            {
                if(retval.size() < 10 && result.getObjectId() != hist.getObjectId() && !retval.contains(hist.createReference()))
                    retval.add(hist.createReference());
            }
        }

        if(retval.size() < 10 && result.getRelease() != null && result.getConfig() != null)
        {
            Query<Result> query = createQuery();
            query.criteria("testcase.testcaseId").equal(result.getTestcase().getActualId());
            query.criteria("config.configId").notEqual(result.getConfig().getConfigObjectId());
            query.criteria("release.releaseId").equal(result.getRelease().getReleaseObjectId());
            query.criteria("recorded").lessThan(result.getRecorded());
            query.order("-recorded");
            query.retrievedFields(true, "id", "status", "recorded", "build"); // only need these fields as we are creating a reference
            for(Result hist : query)
            {
                if(retval.size() < 10 && result.getObjectId() != hist.getObjectId() && !retval.contains(hist.createReference()))
                    retval.add(hist.createReference());
            }
        }

        if(retval.size() < 10)
        {
            Query<Result> query = createQuery();
            query.criteria("testcase.testcaseId").equal(result.getTestcase().getActualId());
            query.criteria("recorded").lessThan(result.getRecorded());
            query.order("-recorded");
            query.retrievedFields(true, "id", "status", "recorded", "build"); // only need these fields as we are creating a reference
            for(Result hist : query)
            {
                if(retval.size() < 10 && result.getObjectId() != hist.getObjectId() && !retval.contains(hist.createReference()))
                    retval.add(hist.createReference());
            }
        }
        return retval;
    }
}
