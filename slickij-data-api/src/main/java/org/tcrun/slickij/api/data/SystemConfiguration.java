package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Entity;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * User: jcorbett
 * Date: 12/3/12
 * Time: 10:43 AM
 */
@JsonTypeInfo(use= JsonTypeInfo.Id.CLASS, include= JsonTypeInfo.As.PROPERTY, property="className")
@Entity("system-configurations")
public interface SystemConfiguration
{
    @JsonIgnore
    public ObjectId getObjectId();

    @JsonIgnore
    public void setObjectId(ObjectId id);

    public String getId();
    public void setId(String id);

    public String getConfigurationType();
    public void setConfigurationType(String type);

    public String getName();
    public void setName(String name);

    public void validate() throws InvalidDataError;
    public void update(SystemConfiguration update) throws InvalidDataError;
}
