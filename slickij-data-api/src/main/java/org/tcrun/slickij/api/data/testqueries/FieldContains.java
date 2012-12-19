package org.tcrun.slickij.api.data.testqueries;

import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.Query;
import org.tcrun.slickij.api.data.Testcase;

/**
 *
 * @author jcorbett
 */
public class FieldContains implements TestcaseQuery
{
	@Property
	private String fieldName;

	@Property
	private String fieldValue;

	@Override
	public Criteria toMorphiaQuery(Query<Testcase> original)
	{
		return original.criteria(fieldName).contains(fieldValue);
	}

	@Override
	public String getQueryDescription()
	{
		throw new UnsupportedOperationException("Not supported yet.");
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
        FieldContains copy = new FieldContains();

        copy.setFieldName(fieldName);
        copy.setFieldValue(fieldValue);

        return copy;
    }
}
