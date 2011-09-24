package org.tcrun.slickij.api.data;

import java.io.Serializable;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author jcorbett
 */
public class TestplanReference implements Serializable
{
	private ObjectId id;
	private String name;

	public TestplanReference()
	{
	}

	public TestplanReference(Testplan plan)
	{
		this.id = plan.getObjectId();
		this.name = plan.getName();
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getId()
	{
		if(id == null)
			return null;
		else
			return id.toString();
	}

	public void setId(ObjectId id)
	{
		this.id = id;
	}

	@JsonIgnore
	public ObjectId getObjectId()
	{
		return id;
	}

}
