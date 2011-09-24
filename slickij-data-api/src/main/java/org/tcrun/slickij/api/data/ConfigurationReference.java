package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Property;
import java.io.Serializable;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author jcorbett
 */
public class ConfigurationReference implements Serializable
{
	@Property
	private ObjectId configId;

	@Property
	private String name;

	@Property
	private String filename;

	@JsonIgnore
	public ObjectId getConfigObjectId()
	{
		return configId;
	}

	public String getConfigId()
	{
		if(configId == null)
			return null;
		else
			return configId.toString();
	}

	public void setConfigId(ObjectId configId)
	{
		this.configId = configId;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}


}
