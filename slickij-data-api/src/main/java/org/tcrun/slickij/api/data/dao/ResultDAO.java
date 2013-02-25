package org.tcrun.slickij.api.data.dao;

import com.google.code.morphia.dao.DAO;
import java.util.List;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.*;

/**
 *
 * @author jcorbett
 */
public interface ResultDAO extends DAO<Result, ObjectId>
{
	public List<Result> findResultsByTestrun(Testrun testrun);

	public Result getNextToBeRun(TestRunParameter params);

    public ResultGroupSummary getSummary(ResultQuery query);

    public List<ResultReference> getHistory(Result result);
}
