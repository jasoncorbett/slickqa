package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.PostLoad;
import com.google.code.morphia.annotations.Property;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jcorbett
 */
public class DataDrivenPropertyType implements Serializable, Copyable<DataDrivenPropertyType>
{

	@Property
	private String name;
	@Property
	private boolean requirement;
	@Property
	public List<String> standardValues;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isRequirement()
	{
		return requirement;
	}

	public void setRequirement(boolean requirement)
	{
		this.requirement = requirement;
	}

	public List<String> getStandardValues()
	{
		return standardValues;
	}

	public void setStandardValues(List<String> standardValues)
	{
		this.standardValues = standardValues;
	}

	@PostLoad
	public void postLoad()
	{
		if(standardValues == null)
			standardValues = new ArrayList<String>();
	}

	public void validate() throws InvalidDataError
	{
		if(getName() == null || getName().equals(""))
			throw new InvalidDataError("DataDrivenPropertyType", "name", "name cannot be null or empty.");
		if(getStandardValues() == null)
			setStandardValues(new ArrayList<String>());
	}

    @Override
    public DataDrivenPropertyType createCopy()
    {
        DataDrivenPropertyType copy = new DataDrivenPropertyType();

        copy.setName(name);
        copy.setRequirement(requirement);

        copy.setStandardValues(new ArrayList<String>(standardValues.size()));
        copy.getStandardValues().addAll(standardValues);

        return copy;
    }
}
