package org.tcrun.slickij.api.data.dao;

import com.google.code.morphia.dao.DAO;
import com.google.code.morphia.query.Query;
import java.util.List;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.Testplan;

/**
 *
 * @author jcorbett
 */
public interface TestplanDAO extends DAO<Testplan, ObjectId>
{
	public List<Testplan> getAllTestplansForProject(ObjectId projectid);
	public Query<Testplan> getQueryOfTestplansForProject(ObjectId projectid);

	public List<Testplan> getAllTestplansForUser(ObjectId projectid, String username);
	public Query<Testplan> getQueryOfTestplansForUser(ObjectId projectid, String username);
}
