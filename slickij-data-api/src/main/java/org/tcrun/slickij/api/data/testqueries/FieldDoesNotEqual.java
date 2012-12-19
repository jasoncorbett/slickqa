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
public class FieldDoesNotEqual implements TestcaseQuery
{
	@Property
	private String fieldName;

	@Property
	private String fieldValue;

	@Override
	public Criteria toMorphiaQuery(Query<Testcase> original)
	{
		return original.criteria(fieldName).notEqual(fieldValue);
	}

	@Override
	public String getQueryDescription()
	{
		return "field '" + fieldName + "' does not equal value '" + fieldValue + "'";
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

    @Override
    public TestcaseQuery createCopy()
    {
        FieldDoesNotEqual copy = new FieldDoesNotEqual();

        copy.setFieldName(fieldName);
        copy.setFieldValue(fieldValue);

        return copy;
    }
}
