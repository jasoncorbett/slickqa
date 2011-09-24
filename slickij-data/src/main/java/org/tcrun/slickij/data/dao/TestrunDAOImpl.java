package org.tcrun.slickij.data.dao;

import org.tcrun.slickij.api.data.TestRunSummary;
import org.tcrun.slickij.api.data.dao.TestrunDAO;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.dao.BasicDAO;
import com.google.code.morphia.query.Query;
import com.google.inject.Inject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.util.List;
import org.bson.BSONObject;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.Testrun;
import org.tcrun.slickij.api.data.Result;
import org.tcrun.slickij.api.data.Testplan;
import org.tcrun.slickij.api.data.TestplanReference;
import org.tcrun.slickij.api.data.TestrunReference;

/**
 *
 * @author jcorbett
 */
public class TestrunDAOImpl extends BasicDAO<Testrun, ObjectId> implements TestrunDAO
{
	@Inject
	public TestrunDAOImpl(Datastore ds)
	{
		super(Testrun.class, ds);
	}

	@Override
	public Testrun findByTestrunReference(TestrunReference ref)
	{
		Testrun result = null;
		if(ref.getTestrunObjectId() != null)
		{
			Query<Testrun> query = createQuery();
			result = query.field("id").equal(ref.getTestrunObjectId()).get();
		}
		if(result == null && ref.getName() != null)
		{
			Query<Testrun> query = createQuery();
			result = query.field("name").equal(ref.getName()).get();
		}

		if(result != null && ref.getTestrunObjectId() == null)
			ref.setTestrunId(result.getObjectId());
		if(result != null && ref.getName() == null)
			ref.setName(result.getName());

		return result;
	}

	@Override
	public TestRunSummary getSummary(Testrun run)
	{
		// to understand what is going on here read:
		//  - the group command: http://www.mongodb.org/display/DOCS/Aggregation
		//  - this comment: http://www.mongodb.org/display/DOCS/Java+Tutorial#comment-134207880

		TestplanDAOImpl tpdao = new TestplanDAOImpl(ds);
		TestplanReference tpref = new TestplanReference();
		if(run.getTestplanObjectId() != null)
		{
			Testplan plan = tpdao.get(run.getTestplanObjectId());
			tpref = new TestplanReference(plan);
		}

		TestRunSummary summary = new TestRunSummary(run, tpref);
		DBCollection resultsCol = ds.getCollection(Result.class);
		BasicDBObject key = new BasicDBObject();
		key.put("status", true);
		BasicDBObject condition = new BasicDBObject();
		condition.put("testrun.testrunId", run.getObjectId());
		BasicDBObject initial = new BasicDBObject();
		initial.put("count", 0);
		String reduce = "function(obj, prev) { prev.count++;}";

		DBObject resultsObject = resultsCol.group(key, condition, initial, reduce);
		List<DBObject> results = (List<DBObject>)resultsObject;
		for(DBObject statuscount : results)
		{
			summary.getResultsByStatus().put((String)statuscount.get("status"), new Long( String.format("%.0f", (Double)statuscount.get("count"))));
		}

		return summary;
	}
}
