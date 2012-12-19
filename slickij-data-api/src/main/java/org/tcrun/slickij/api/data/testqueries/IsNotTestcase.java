package org.tcrun.slickij.api.data.testqueries;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.Query;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.Testcase;
import org.tcrun.slickij.api.data.TestcaseReference;

/**
 *
 * @author jcorbett
 */
public class IsNotTestcase implements TestcaseQuery
{
	@Embedded
	private ObjectId testcaseId;

	@Override
	public Criteria toMorphiaQuery(Query<Testcase> original)
	{
		return original.criteria("id").notEqual(testcaseId);
	}

	@Override
	public String getQueryDescription()
	{
		return "Is not testcase with id " + testcaseId.toString();
	}

	public String getTestcaseId()
	{
		if(testcaseId == null)
			return null;
		else
			return testcaseId.toString();
	}

	public void setTestcaseId(ObjectId id)
	{
		testcaseId = id;
	}

    @Override
    public TestcaseQuery createCopy()
    {
        IsNotTestcase copy = new IsNotTestcase();
        copy.setTestcaseId(testcaseId);
        return copy;
    }
}
