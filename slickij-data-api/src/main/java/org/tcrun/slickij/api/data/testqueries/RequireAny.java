package org.tcrun.slickij.api.data.testqueries;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.Query;

import java.util.ArrayList;
import java.util.List;
import org.tcrun.slickij.api.data.Testcase;

/**
 *
 * @author jcorbett
 */
public class RequireAny implements TestcaseQuery
{
	@Embedded
	private List<TestcaseQuery> criteria;

	@Override
	public Criteria toMorphiaQuery(Query<Testcase> original)
	{
		Criteria[] allcriteria = new Criteria[criteria.size()];
		for(int i = 0; i < criteria.size(); i++)
			allcriteria[i] = criteria.get(i).toMorphiaQuery(original);
		return original.or(allcriteria);
	}

	@Override
	public String getQueryDescription()
	{
		String retval = "";
		if(criteria.size() > 0)
		{
			retval = criteria.get(0).getQueryDescription();
		}

		if(criteria.size() > 1)
			for(int i = 1; i < criteria.size(); i++)
				retval = " or " + criteria.get(i).getQueryDescription();

		return retval;
	}

    @Override
    public void setQueryDescription(String description)
    {
    }

    public List<TestcaseQuery> getCriteria()
	{
		return criteria;
	}

	public void setCriteria(List<TestcaseQuery> criteria)
	{
		this.criteria = criteria;
	}


    @Override
    public TestcaseQuery createCopy()
    {
        RequireAny copy = new RequireAny();
        List<TestcaseQuery> copyOfCriteria = new ArrayList<TestcaseQuery>();
        for(TestcaseQuery orig : criteria)
            copyOfCriteria.add(orig.createCopy());
        copy.setCriteria(copyOfCriteria);
        return copy;
    }
}
