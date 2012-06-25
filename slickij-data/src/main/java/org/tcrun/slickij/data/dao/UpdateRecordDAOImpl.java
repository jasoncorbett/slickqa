package org.tcrun.slickij.data.dao;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.dao.BasicDAO;
import com.google.inject.Inject;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.UpdateRecord;
import org.tcrun.slickij.api.data.dao.UpdateRecordDAO;

/**
 * Created with IntelliJ IDEA.
 * User: jcorbett
 * Date: 6/24/12
 * Time: 6:40 PM
 */
public class UpdateRecordDAOImpl extends BasicDAO<UpdateRecord, ObjectId> implements UpdateRecordDAO
{
    @Inject
    public UpdateRecordDAOImpl(Datastore ds)
    {
        super(UpdateRecord.class, ds);
    }
}
