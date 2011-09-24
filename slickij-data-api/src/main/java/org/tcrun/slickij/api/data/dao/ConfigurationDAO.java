package org.tcrun.slickij.api.data.dao;

import com.google.code.morphia.dao.DAO;
import java.util.List;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.Configuration;
import org.tcrun.slickij.api.data.ConfigurationReference;

/**
 *
 * @author jcorbett
 */
public interface ConfigurationDAO extends DAO<Configuration, ObjectId>
{
	public List<Configuration> findConfigurationsByName(String name);

	public Configuration findConfigurationByReference(ConfigurationReference ref);
}
