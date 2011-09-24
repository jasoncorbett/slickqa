package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Property;
import java.io.Serializable;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author jcorbett
 */
public class ReleaseReference implements Serializable
{
	@Property
	private ObjectId releaseId;

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
	public ObjectId getReleaseObjectId()
	{
		return releaseId;
	}

	public String getReleaseId()
	{
		if(releaseId == null)
			return null;
		else
			return releaseId.toString();
	}

	public void setReleaseId(ObjectId releaseId)
	{
		this.releaseId = releaseId;
	}
}
