package org.tcrun.slickij.data.org.tcrun.slickij.data.updates;

import com.google.code.morphia.Datastore;
import com.google.inject.Inject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.tcrun.slickij.api.data.*;
import org.tcrun.slickij.api.data.dao.UpdateRecordDAO;
import org.tcrun.slickij.data.AbstractApplyOnceSlickUpdate;

/**
 * Created with IntelliJ IDEA.
 * User: jcorbett
 * Date: 6/24/12
 * Time: 7:26 PM
 */
public class AddTestrunBuildIndexUpdate extends AbstractApplyOnceSlickUpdate
{
    private Datastore ds;

    @Inject
    public AddTestrunBuildIndexUpdate(UpdateRecordDAO dao, Datastore ds)
    {
        super(dao);
        this.ds = ds;
    }

    @Override
    public void performUpdate() {
        DBCollection collection = ds.getCollection(Testrun.class);
        DBObject index = new BasicDBObject();

        logDebug("Adding project.id release.releaseId index to Testrun");
        index.put("build.buildId", 1);
        collection.ensureIndex(index);

    }

    @Override
    public String getUpdateId() {
        return "36def372-9430-408f-a7ce-3c3e3a0baf14";
    }

    @Override
    public String getDescription() {
        return "Adding a build.buildId index on Testruns to mongodb";
    }

    @Override
    public String getName() {
        return "Add Testrun Build Index Update";
    }
}
