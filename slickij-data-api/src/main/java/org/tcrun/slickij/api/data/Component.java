package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Property;
import java.io.Serializable;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author jcorbett
 */
public class Component implements Serializable, Copyable<Component>
{
	@Property
	private ObjectId id;

	@Property
	private String name;

	@Property
	private String description;

	@Property
	@Indexed
	private String code;

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
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
			throw new InvalidDataError("Component", "name", "name cannot be null.");
		if(getName().equals(""))
			throw new InvalidDataError("Component", "name", "name cannot be empty.");
		if(getCode() == null || getCode().equals(""))
			setCode(name.toLowerCase().replaceAll(" ", "_"));
	}


    @Override
    public Component createCopy()
    {
        Component copy = new Component();
        copy.setId(getObjectId());
        copy.setDescription(getDescription());
        copy.setName(getName());
        copy.setCode(getCode());
        return copy;
    }
}
