package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Property;
import java.io.Serializable;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Class representing a component reference step in the database.
 *
 * @author slambson
 */
public class ComponentReference implements Serializable
{

    @Property
    private String name;

    @Property
    private ObjectId id;

    @Property
    private String code;

    public void setName(String name)
    {
        this.name = name;
    }

    public String getId() {
		if(id == null)
			return null;
		else
        	return id.toString();
    }

	@JsonIgnore
	public ObjectId getObjectId()
	{
		return id;
	}

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }
}
