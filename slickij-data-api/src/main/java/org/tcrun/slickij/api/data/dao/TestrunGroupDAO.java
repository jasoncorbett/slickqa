package org.tcrun.slickij.api.data.dao;

import com.google.code.morphia.dao.DAO;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.TestrunGroup;

import java.util.Date;
import java.util.List;

/**
 * DAO for the testrun group data type.
 * User: jcorbett
 * Date: 8/3/12
 * Time: 1:54 PM
 */
public interface TestrunGroupDAO extends DAO<TestrunGroup, ObjectId>
{
    /**
     * Get all the testrun groups with a created date after the one provided.
     * @param date
     * @return
     */
    public List<TestrunGroup> getTestrunsCreatedAfter(Date date);
}
