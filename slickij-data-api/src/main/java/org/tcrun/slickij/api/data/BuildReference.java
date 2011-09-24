package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Property;
import java.io.Serializable;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author jcorbett
 */
public class BuildReference implements Serializable
{
	@Property
	private ObjectId buildId;

	@Property
	private String name;

	@JsonIgnore
	public ObjectId getBuildObjectId()
	{
		return buildId;
	}

	public String getBuildId()
	{
		if(buildId == null)
			return null;
		else
			return buildId.toString();
	}

	public void setBuildId(ObjectId buildId)
	{
		this.buildId = buildId;
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
