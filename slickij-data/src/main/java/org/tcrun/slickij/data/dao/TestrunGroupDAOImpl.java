package org.tcrun.slickij.data.dao;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.dao.BasicDAO;
import com.google.code.morphia.query.Query;
import com.google.inject.Inject;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.dao.TestrunGroupDAO;
import org.tcrun.slickij.api.data.TestrunGroup;

import java.util.Date;
import java.util.List;

/**
 * Implementation of the TestrunGroupDAO.
 * User: jcorbett
 * Date: 8/3/12
 * Time: 7:43 PM
 */
public class TestrunGroupDAOImpl extends BasicDAO<TestrunGroup, ObjectId> implements TestrunGroupDAO
{
    @Inject
    public TestrunGroupDAOImpl(Datastore ds)
    {
        super(TestrunGroup.class, ds);
    }


    @Override
    public List<TestrunGroup> getTestrunsCreatedAfter(Date date)
    {
        Query<TestrunGroup> query = createQuery();
        query.criteria("created").greaterThan(date);
        return this.find(query).asList();
    }
}
