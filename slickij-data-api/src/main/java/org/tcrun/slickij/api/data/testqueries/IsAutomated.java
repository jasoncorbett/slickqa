package org.tcrun.slickij.api.data.testqueries;

import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.Query;
import org.tcrun.slickij.api.data.Testcase;

/**
 *
 * @author jcorbett
 */
public class IsAutomated implements TestcaseQuery
{

	@Override
	public Criteria toMorphiaQuery(Query<Testcase> original)
	{
		return original.criteria("automated").equal(Boolean.TRUE);
	}

	@Override
	public String getQueryDescription()
	{
		return "is automated";
	}

    @Override
    public void setQueryDescription(String description)
    {
    }

    @Override
    public TestcaseQuery createCopy()
    {
        return new IsAutomated();
    }
}
