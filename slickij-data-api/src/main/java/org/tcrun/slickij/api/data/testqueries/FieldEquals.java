/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcrun.slickij.api.data.testqueries;

import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.Query;
import org.tcrun.slickij.api.data.Testcase;

/**
 *
 * @author jcorbett
 */
public class FieldEquals implements TestcaseQuery
{
	@Property
	private String fieldName;

	@Property
	private String fieldValue;

	@Override
	public Criteria toMorphiaQuery(Query<Testcase> original)
	{
		return original.criteria(fieldName).equal(fieldValue);
	}

	@Override
	public String getQueryDescription()
	{
		return "field '" + fieldName + "' equals value '" + fieldValue + "'";
	}

	public String getFieldName()
	{
		return fieldName;
	}

	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}

	public String getFieldValue()
	{
		return fieldValue;
	}

	public void setFieldValue(String fieldValue)
	{
		this.fieldValue = fieldValue;
	}
}
