package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Property;
import java.io.Serializable;

/**
 *
 * @author jcorbett
 */
public class ConfigurationOverride implements Serializable
{
	@Property
	private String key;

	@Property
	private String value;

	@Property
	private Boolean isRequirement;

	public Boolean getIsRequirement()
	{
		return isRequirement;
	}

	public void setIsRequirement(Boolean isRequirement)
	{
		this.isRequirement = isRequirement;
	}

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
}
