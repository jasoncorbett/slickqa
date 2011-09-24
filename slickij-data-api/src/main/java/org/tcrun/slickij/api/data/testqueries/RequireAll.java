package org.tcrun.slickij.api.data.testqueries;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.Query;
import java.util.List;
import org.tcrun.slickij.api.data.Testcase;

/**
 *
 * @author jcorbett
 */
public class RequireAll implements TestcaseQuery
{
	@Embedded
	private List<TestcaseQuery> criteria;

	@Override
	public Criteria toMorphiaQuery(Query<Testcase> original)
	{
		Criteria[] allcriteria = new Criteria[criteria.size()];
		for(int i = 0; i < criteria.size(); i++)
			allcriteria[i] = criteria.get(i).toMorphiaQuery(original);
		return original.and(allcriteria);
	}

	@Override
	public String getQueryDescription()
	{
		String retval = "";
		if(criteria.size() > 0)
		{
			retval = criteria.get(0).getQueryDescription();
		}

		for(int i = 1; i < criteria.size(); i++)
			retval = " and " + criteria.get(i).getQueryDescription();

		return retval;
	}

	public List<TestcaseQuery> getCriteria()
	{
		return criteria;
	}

	public void setCriteria(List<TestcaseQuery> criteria)
	{
		this.criteria = criteria;
	}


}
