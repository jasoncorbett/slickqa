package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Property;
import java.io.Serializable;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author jcorbett
 */
public class BuildReference implements Serializable, Copyable<BuildReference>
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

    @Override
    public BuildReference createCopy()
    {
        BuildReference copy = new BuildReference();

        copy.setBuildId(buildId);
        copy.setName(name);

        return copy;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BuildReference that = (BuildReference) o;

        if (buildId != null ? !buildId.equals(that.buildId) : that.buildId != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = buildId != null ? buildId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
