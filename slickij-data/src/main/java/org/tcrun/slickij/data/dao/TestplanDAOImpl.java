package org.tcrun.slickij.data.dao;

import org.tcrun.slickij.api.data.dao.TestplanDAO;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.dao.BasicDAO;
import com.google.code.morphia.query.Query;
import com.google.inject.Inject;
import java.util.List;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.Testplan;

/**
 *
 * @author jcorbett
 */
public class TestplanDAOImpl extends BasicDAO<Testplan, ObjectId> implements TestplanDAO
{
	@Inject
	public TestplanDAOImpl(Datastore ds)
	{
		super(Testplan.class, ds);
	}

	@Override
	public List<Testplan> getAllTestplansForProject(ObjectId projectid)
	{
		Query<Testplan> query = getQueryOfTestplansForProject(projectid);
		return query.asList();
	}

	@Override
	public List<Testplan> getAllTestplansForUser(ObjectId projectid, String username)
	{
		Query<Testplan> query = getQueryOfTestplansForUser(projectid, username);
		return query.asList();
	}

	@Override
	public Query<Testplan> getQueryOfTestplansForProject(ObjectId projectid)
	{
		Query<Testplan> query = createQuery();
		query.and(query.criteria("project.id").equal(projectid), query.criteria("isprivate").equal(Boolean.FALSE));
		return query;
	}

	@Override
	public Query<Testplan> getQueryOfTestplansForUser(ObjectId projectid, String username)
	{
		Query<Testplan> query = createQuery();
		query.and(query.criteria("project.id").equal(projectid), query.or(query.criteria("isprivate").equal(Boolean.FALSE),
		                                                                  query.criteria("createdBy").equal(username),
																		  query.criteria("sharedWith").equal(username)));
		return query;
	}
}
