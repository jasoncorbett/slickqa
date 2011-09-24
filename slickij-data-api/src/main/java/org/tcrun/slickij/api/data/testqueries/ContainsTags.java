package org.tcrun.slickij.api.data.testqueries;

import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.Query;
import java.util.List;
import org.tcrun.slickij.api.data.Testcase;

/**
 *
 * @author jcorbett
 */
public class ContainsTags implements TestcaseQuery
{
	@Property
	private List<String> tagnames;

	@Override
	public Criteria toMorphiaQuery(Query<Testcase> original)
	{
		return original.criteria("tags").hasAllOf(tagnames);
	}

	@Override
	public String getQueryDescription()
	{
		String retval = "";

		if(tagnames != null && tagnames.size() > 0)
			retval += "has tag with name '" + tagnames.get(0) + "'";
		if(tagnames != null && tagnames.size() > 1)
			for(int i = 1; i < tagnames.size(); i++)
				retval += " and has tag with name '" + tagnames.get(i) + "'";

		return retval;
	}

	public List<String> getTagnames()
	{
		return tagnames;
	}

	public void setTagnames(List<String> tagnames)
	{
		this.tagnames = tagnames;
	}

}
