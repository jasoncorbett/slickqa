package org.tcrun.slickij.api.data.dao;

import com.google.code.morphia.dao.DAO;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.AbstractSystemConfiguration;

/**
 * User: jcorbett
 * Date: 12/3/12
 * Time: 1:12 PM
 */
public interface SystemConfigurationDAO extends DAO<AbstractSystemConfiguration, ObjectId>
{
}
