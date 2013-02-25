package org.tcrun.slickij.api.data.testqueries;

import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.Query;
import org.tcrun.slickij.api.data.Testcase;

/**
 *
 * @author jcorbett
 */
public class FieldStartsWith implements TestcaseQuery
{
	private String fieldName;

	private String fieldValue;

	@Override
	public Criteria toMorphiaQuery(Query<Testcase> original)
	{
		return original.criteria(fieldName).startsWith(fieldValue);
	}

	@Override
	public String getQueryDescription()
	{
		return "field '" + fieldName + "' starts with '" + fieldValue + "'";
	}

    @Override
    public void setQueryDescription(String description)
    {
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
        FieldStartsWith copy = new FieldStartsWith();
        copy.setFieldName(fieldName);
        copy.setFieldValue(fieldValue);
        return copy;
    }
}
