package org.tcrun.slickij.api.data.testqueries;

import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.Query;
import org.tcrun.slickij.api.data.Testcase;

/**
 *
 * @author jcorbett
 */
public class HasAttributeWithValue implements TestcaseQuery
{
	@Property
	private String attributeName;

	@Property
	private String attributeValue;

	@Override
	public Criteria toMorphiaQuery(Query<Testcase> original)
	{
		return original.criteria("attributes." + attributeName).equal(attributeValue);
	}

	@Override
	public String getQueryDescription()
	{
		return "Has attribute with name '" + attributeName + "' set to value '" + attributeValue + "'";
	}

	public String getAttributeName()
	{
		return attributeName;
	}

	public void setAttributeName(String attributeName)
	{
		this.attributeName = attributeName;
	}

	public String getAttributeValue()
	{
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue)
	{
		this.attributeValue = attributeValue;
	}


    @Override
    public TestcaseQuery createCopy()
    {
        HasAttributeWithValue copy = new HasAttributeWithValue();
        copy.setAttributeName(attributeName);
        copy.setAttributeValue(attributeValue);
        return copy;
    }
}
