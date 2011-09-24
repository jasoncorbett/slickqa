package org.tcrun.slickij.api.data.dao;

import com.google.code.morphia.dao.DAO;
import java.util.List;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.Result;
import org.tcrun.slickij.api.data.TestRunParameter;
import org.tcrun.slickij.api.data.Testrun;

/**
 *
 * @author jcorbett
 */
public interface ResultDAO extends DAO<Result, ObjectId>
{
	public List<Result> findResultsByTestrun(Testrun testrun);

	public Result getNextToBeRun(TestRunParameter params);
}
