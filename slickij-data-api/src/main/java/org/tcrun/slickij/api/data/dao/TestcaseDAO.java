package org.tcrun.slickij.api.data.dao;

import com.google.code.morphia.dao.DAO;
import java.util.List;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.Testcase;
import org.tcrun.slickij.api.data.TestcaseReference;
import org.tcrun.slickij.api.data.testqueries.TestcaseQuery;

/**
 *
 * @author jcorbett
 */
public interface TestcaseDAO extends DAO<Testcase, ObjectId>
{
	public List<Testcase> findTestsByTestcaseQuery(TestcaseQuery query);
	public long countTestsFromTestcaseQuery(TestcaseQuery query);
	public Testcase findTestcaseByReference(TestcaseReference ref);
}
