/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcrun.slickij.data.dao;

import org.tcrun.slickij.api.data.dao.ConfigurationDAO;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.dao.BasicDAO;
import com.google.code.morphia.query.Query;
import com.google.inject.Inject;
import java.util.List;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.Configuration;
import org.tcrun.slickij.api.data.ConfigurationReference;

/**
 *
 * @author jcorbett
 */
public class ConfigurationDAOImpl extends BasicDAO<Configuration, ObjectId> implements ConfigurationDAO
{
	@Inject
	public ConfigurationDAOImpl(Datastore ds)
	{
		super(Configuration.class, ds);
	}

	@Override
	public List<Configuration> findConfigurationsByName(String name)
	{
		return this.createQuery().field("name").equal(name).asList();
	}

	@Override
	public Configuration findConfigurationByReference(ConfigurationReference ref)
	{
		if(ref == null)
			return null;
		Query<Configuration> query = createQuery();

		Configuration result = null;
		if(ref.getConfigObjectId() != null)
			result = query.field("id").equal(ref.getConfigObjectId()).get();
		if(result == null && ref.getFilename() != null)
			result = query.field("filename").equal(ref.getFilename()).get();
		if(result == null && ref.getName() != null)
			result = query.field("name").equal(ref.getName()).get();

		return result;
	}
}
