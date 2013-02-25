package org.tcrun.slickij.data.dao;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.dao.BasicDAO;
import com.google.inject.Inject;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.dao.SystemConfigurationDAO;
import org.tcrun.slickij.api.data.AbstractSystemConfiguration;

/**
 * User: jcorbett
 * Date: 12/3/12
 * Time: 1:14 PM
 */
public class SystemConfigurationDAOImpl extends BasicDAO<AbstractSystemConfiguration, ObjectId> implements SystemConfigurationDAO
{
    @Inject
    public SystemConfigurationDAOImpl(Datastore ds)
    {
        super(AbstractSystemConfiguration.class, ds);
    }
}
