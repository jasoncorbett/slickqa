package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Property;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jcorbett
 * Date: 6/24/12
 * Time: 5:37 PM
 */
@Entity("updates")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateRecord
{
    @Id
    private ObjectId id;

    @Property
    private String updateId;

    @Embedded
    private List<LogEntry> logs;

    @JsonIgnore
    public ObjectId getObjectId()
    {
        return id;
    }

    public void setId(ObjectId id)
    {
        this.id = id;
    }

    public String getId()
    {
        if(id == null)
            return null;
        else
            return id.toString();
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public List<LogEntry> getLogs() {
        return logs;
    }

    public void setLogs(List<LogEntry> logs) {
        this.logs = logs;
    }
}
