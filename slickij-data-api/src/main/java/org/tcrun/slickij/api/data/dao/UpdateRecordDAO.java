package org.tcrun.slickij.api.data.dao;

import com.google.code.morphia.dao.DAO;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.UpdateRecord;

/**
 * Created with IntelliJ IDEA.
 * User: jcorbett
 * Date: 6/24/12
 * Time: 5:35 PM
 */
public interface UpdateRecordDAO extends DAO<UpdateRecord, ObjectId>
{
}
