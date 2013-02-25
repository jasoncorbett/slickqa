package org.tcrun.slickij.api.data.testqueries;

import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.Query;
import org.tcrun.slickij.api.data.Testcase;

/**
 *
 * @author jcorbett
 */
public class IsNotAutomated implements TestcaseQuery
{

	@Override
	public Criteria toMorphiaQuery(Query<Testcase> original)
	{
		return original.criteria("automated").equal(Boolean.FALSE);
	}

	@Override
	public String getQueryDescription()
	{
		return "is not automated";
	}

    @Override
    public void setQueryDescription(String description)
    {
    }

    @Override
    public TestcaseQuery createCopy() {
        return new IsNotAutomated();
    }
}
