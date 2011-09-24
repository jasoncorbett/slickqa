/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcrun.slickij.api.data.testqueries;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Property;

/**
 *
 * @author jcorbett
 */
public class NamedTestcaseQuery
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
}
