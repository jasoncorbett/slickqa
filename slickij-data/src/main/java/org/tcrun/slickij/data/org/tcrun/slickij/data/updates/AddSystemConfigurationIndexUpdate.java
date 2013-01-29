package org.tcrun.slickij.data.org.tcrun.slickij.data.updates;

import com.google.code.morphia.Datastore;
import com.google.inject.Inject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.tcrun.slickij.api.data.AbstractSystemConfiguration;
import org.tcrun.slickij.api.data.dao.UpdateRecordDAO;
import org.tcrun.slickij.data.AbstractApplyOnceSlickUpdate;

/**
 * Add indexes for system configuration properties.  Since configuration type and name are queryable, we need to
 * index them.
 * User: jcorbett
 * Date: 1/24/13
 * Time: 11:54 AM
 */
public class AddSystemConfigurationIndexUpdate extends AbstractApplyOnceSlickUpdate
{
    private Datastore ds;

    @Inject
    public AddSystemConfigurationIndexUpdate(UpdateRecordDAO dao, Datastore ds)
    {
        super(dao);
        this.ds = ds;
    }

    @Override
    public void performUpdate()
    {
        DBCollection collection = ds.getCollection(AbstractSystemConfiguration.class);
        DBObject index = new BasicDBObject();

        logDebug("Adding the configurationType and name index to system-configurations");
        index.put("configurationType", 1);
        index.put("name", 1);
        collection.ensureIndex(index);
    }

    @Override
    public String getUpdateId()
    {
        return "16c17947-2941-4f7d-ad7c-3c87ccc98d7b";
    }

    @Override
    public String getDescription()
    {
        return "Adding indexes for the name and configurationType properties to the system-configurations collection";
    }

    @Override
    public String getName()
    {
        return "Add System Configuration Indexes Update";
    }
}
