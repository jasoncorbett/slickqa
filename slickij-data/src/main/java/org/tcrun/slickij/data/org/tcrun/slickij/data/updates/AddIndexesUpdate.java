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
public class AddIndexesUpdate extends AbstractApplyOnceSlickUpdate
{
    private Datastore ds;

    @Inject
    public AddIndexesUpdate(UpdateRecordDAO dao, Datastore ds)
    {
        super(dao);
        this.ds = ds;
    }

    @Override
    public void performUpdate() {
        DBCollection collection = ds.getCollection(Project.class);
        DBObject index = new BasicDBObject();

        logDebug("Adding a Project name index to Project");
        index.put("name", 1);
        collection.ensureIndex(index);

        logDebug("Adding a releases.builds.id index to Project");
        index = new BasicDBObject();
        index.put("releases.builds.id", 1);
        collection.ensureIndex(index);

        collection = ds.getCollection(Configuration.class);

        logDebug("Adding a filename index to Configuration");
        index = new BasicDBObject();
        index.put("filename", 1);
        collection.ensureIndex(index);

        logDebug("Adding a configurationType index to Configuration");
        index = new BasicDBObject();
        index.put("configurationType", 1);
        collection.ensureIndex(index);

        collection = ds.getCollection(Testcase.class);

        logDebug("Adding a automationId index to Testcase");
        index = new BasicDBObject();
        index.put("automationId", 1);
        collection.ensureIndex(index);

        logDebug("Adding a automationKey index to Testcase");
        index = new BasicDBObject();
        index.put("automationKey", 1);
        collection.ensureIndex(index);

        logDebug("Adding a project.id and component.id index to Testcase");
        index = new BasicDBObject();
        index.put("project.id", 1);
        index.put("component.id", 1);
        collection.ensureIndex(index);

        logDebug("Adding a tags index to Testcase");
        index = new BasicDBObject();
        index.put("tags", 1);
        collection.ensureIndex(index);

        collection = ds.getCollection(Result.class);

        logDebug("Adding runstatus hostname index to Result");
        index = new BasicDBObject();
        index.put("runstatus", 1);
        index.put("hostname", 1);
        collection.ensureIndex(index);

        logDebug("Adding runstatus testcase.automationTool recorded index to Result");
        index = new BasicDBObject();
        index.put("runstatus", 1);
        index.put("testcase.automationTool", 1);
        index.put("recorded", -1);
        collection.ensureIndex(index);

        logDebug("Adding status testrun.testrunId recorded index to Result");
        index = new BasicDBObject();
        index.put("testrun.testrunId", 1);
        index.put("status", 1);
        index.put("recorded", 1);
        collection.ensureIndex(index);

        logDebug("Adding status build.buildId index to Result");
        index = new BasicDBObject();
        index.put("build.buildId", 1);
        index.put("status", 1);
        collection.ensureIndex(index);

        collection = ds.getCollection(Testrun.class);

        logDebug("Adding project.id release.releaseId index to Testrun");
        index = new BasicDBObject();
        index.put("project.id", 1);
        index.put("release.releaseId", 1);
        collection.ensureIndex(index);

        logDebug("Adding project.id dateCreated index to Testrun");
        index = new BasicDBObject();
        index.put("project.id", 1);
        index.put("dateCreated", -1);
        collection.ensureIndex(index);

    }

    @Override
    public String getUpdateId() {
        return "9cb06515-7489-4912-a58d-5a1b3f3d2c38";
    }

    @Override
    public String getDescription() {
        return "Adding base indexes to mongodb";
    }

    @Override
    public String getName() {
        return "Add Indexes Update";
    }
}
