package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Property;
import java.io.Serializable;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Class representing a project reference step in the database.
 *
 * @author slambson
 */
public class ProjectReference implements Serializable
{

	@Property
	private String name;
	@Property
	private ObjectId id;

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

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final ProjectReference other = (ProjectReference) obj;
		if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name))
		{
			return false;
		}
		if (this.id != other.id && (this.id == null || !this.id.equals(other.id)))
		{
			return false;
		}
		return true;
	}

	@Override
	public int hashCode()
	{
		int hash = 3;
		hash = 13 * hash + (this.name != null ? this.name.hashCode() : 0);
		hash = 13 * hash + (this.id != null ? this.id.hashCode() : 0);
		return hash;
	}


}
