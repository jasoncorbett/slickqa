/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcrun.slickij.api.data.testqueries;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Property;
import org.tcrun.slickij.api.data.Copyable;

/**
 *
 * @author jcorbett
 */
public class NamedTestcaseQuery implements Copyable<NamedTestcaseQuery>
{
	@Property
	private String name;

	@Embedded
	private TestcaseQuery query;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public TestcaseQuery getQuery()
	{
		return query;
	}

	public void setQuery(TestcaseQuery query)
	{
		this.query = query;
	}

    @Override
    public NamedTestcaseQuery createCopy()
    {
        NamedTestcaseQuery copy = new NamedTestcaseQuery();

        copy.setName(name);
        copy.setQuery(query.createCopy());

        return copy;
    }
}
