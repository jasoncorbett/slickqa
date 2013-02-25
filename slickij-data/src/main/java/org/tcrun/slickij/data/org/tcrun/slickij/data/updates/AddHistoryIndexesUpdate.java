package org.tcrun.slickij.data.org.tcrun.slickij.data.updates;

import com.google.code.morphia.Datastore;
import com.google.inject.Inject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.tcrun.slickij.api.data.Result;
import org.tcrun.slickij.api.data.dao.UpdateRecordDAO;
import org.tcrun.slickij.data.AbstractApplyOnceSlickUpdate;

/**
 * Adding indexes needed for generating history for results.
 * User: jcorbett
 * Date: 2/1/13
 * Time: 2:52 PM
 */
public class AddHistoryIndexesUpdate extends AbstractApplyOnceSlickUpdate
{
    private Datastore ds;

    @Inject
    public AddHistoryIndexesUpdate(UpdateRecordDAO dao, Datastore ds)
    {
        super(dao);
        this.ds = ds;
    }

    @Override
    public void performUpdate()
    {
        DBCollection collection = ds.getCollection(Result.class);
        DBObject index = new BasicDBObject();

        logDebug("Adding testcase.testcaseId config.configId result.resultId recorded index to Result");
        index = new BasicDBObject();
        index.put("testcase.testcaseId", 1);
        index.put("config.configId", 1);
        index.put("release.releaseId", 1);
        index.put("recorded", -1);
        collection.ensureIndex(index);

        logDebug("Adding testcase.testcaseId recorded index to Result");
        index = new BasicDBObject();
        index.put("testcase.testcaseId", 1);
        index.put("recorded", -1);
        collection.ensureIndex(index);
    }

    @Override
    public String getUpdateId()
    {
        return "f97a93ed-6f1f-40c6-a60f-d644d4ed1403";
    }

    @Override
    public String getDescription()
    {
        return "Update for adding indexes to the results collection for quickly querying indexes";
    }

    @Override
    public String getName()
    {
        return "Add History Indexes";
    }
}
