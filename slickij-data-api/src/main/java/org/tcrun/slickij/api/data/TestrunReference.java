package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Property;
import java.io.Serializable;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author jcorbett
 */
public class TestrunReference implements Serializable, Copyable<TestrunReference>
{
	@Property
	@Indexed
	private ObjectId testrunId;

	@Property
	private String name;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@JsonIgnore
	public ObjectId getTestrunObjectId()
	{
		return testrunId;
	}

	public String getTestrunId()
	{
		if(testrunId == null)
			return null;
		else
			return testrunId.toString();
	}

	public void setTestrunId(ObjectId testrunId)
	{
		this.testrunId = testrunId;
	}

    @Override
    public TestrunReference createCopy()
    {
        TestrunReference copy = new TestrunReference();
        copy.setTestrunId(getTestrunObjectId());
        copy.setName(getName());
        return copy;
    }
}
