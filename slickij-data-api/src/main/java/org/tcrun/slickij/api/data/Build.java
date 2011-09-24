package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Property;
import java.io.Serializable;
import java.util.Date;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author jcorbett
 */
public class Build implements Serializable
{
	@Property
	private ObjectId id;

	@Property
	private String name;

	@Property
	private Date built;

	public Date getBuilt()
	{
		return built;
	}

	public void setBuilt(Date built)
	{
		this.built = built;
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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@PrePersist
	public void prePersist()
	{
		if(id == null)
			id = ObjectId.get();
	}

	public void validate() throws InvalidDataError
	{
		if(getName() == null)
			throw new InvalidDataError("Build", "name", "name must not be null.");
		if(getName().equals(""))
			throw new InvalidDataError("Build", "name", "name must not be empty.");
		if(getBuilt() == null || getBuilt().equals(new Date(0)))
			setBuilt(new Date());
	}

	public BuildReference createReference()
	{
		BuildReference retval = new BuildReference();
		retval.setBuildId(id);
		retval.setName(name);
		return retval;
	}
}
