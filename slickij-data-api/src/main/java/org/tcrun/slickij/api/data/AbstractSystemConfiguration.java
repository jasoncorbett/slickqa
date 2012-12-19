package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Property;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.SystemConfiguration;

/**
 * User: jcorbett
 * Date: 12/3/12
 * Time: 2:04 PM
 */
@Entity("system-configurations")
public abstract class AbstractSystemConfiguration implements SystemConfiguration
{
    @Id
    protected ObjectId id;

    @Property
    protected String configurationType;

    @Property
    protected String name;

    @Override
    public ObjectId getObjectId()
    {
        return id;
    }

    @Override
    public void setObjectId(ObjectId id)
    {
        this.id = id;
    }

    @Override
    public String getId()
    {
        if (id == null)
            return null;
        else
            return id.toString();
    }

    @Override
    public void setId(String id)
    {
        this.id = new ObjectId(id);
    }

    @Override
    public String getConfigurationType()
    {
        return configurationType;
    }

    @Override
    public void setConfigurationType(String type)
    {
        this.configurationType = type;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void setName(String name)
    {
        this.name = name;
    }
}
