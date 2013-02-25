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
public class TestcaseReference implements Serializable, Copyable<TestcaseReference>
{
	@Property
	private ObjectId testcaseId;

	@Property
	private String name;

	@Property
	private String automationId;

	@Property
	private String automationKey;

	@Property
	@Indexed
	private String automationTool;

	public String getTestcaseId()
	{
		if(testcaseId == null)
			return null;
		else
			return testcaseId.toString();
	}

	public void setTestcaseId(ObjectId id)
	{
		testcaseId = id;
	}

	public String getAutomationId()
	{
		return automationId;
	}

	public void setAutomationId(String automationId)
	{
		this.automationId = automationId;
	}

	public String getAutomationKey()
	{
		return automationKey;
	}

	public void setAutomationKey(String automationKey)
	{
		this.automationKey = automationKey;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@JsonIgnore
	public ObjectId getActualId()
	{
		return testcaseId;
	}

	public void setActualId(ObjectId id)
	{
		this.testcaseId = id;
	}

	public String getAutomationTool()
	{
		return automationTool;
	}

	public void setAutomationTool(String automationTool)
	{
		this.automationTool = automationTool;
	}

    @Override
    public TestcaseReference createCopy()
    {
        TestcaseReference copy = new TestcaseReference();

        copy.setActualId(getActualId());
        copy.setAutomationId(getAutomationId());
        copy.setAutomationKey(getAutomationKey());
        copy.setAutomationTool(getAutomationTool());
        copy.setName(getName());

        return copy;
    }
}
