package org.tcrun.slickij.data;

import com.google.code.morphia.query.Query;
import com.google.inject.Inject;
import java.util.List;
import java.util.Map;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.ConfigurationResource;
import org.tcrun.slickij.api.data.Configuration;
import org.tcrun.slickij.api.data.InvalidDataError;
import org.tcrun.slickij.api.data.dao.ConfigurationDAO;

/**
 *
 * @author jcorbett
 */
public class ConfigurationResourceImpl implements ConfigurationResource
{
	private ConfigurationDAO m_configDAO;

	@Inject
	public ConfigurationResourceImpl(ConfigurationDAO p_configDAO)
	{
		m_configDAO = p_configDAO;
	}

	@Override
	public List<Configuration> getMatchingConfigurations(String name, String configurationType, String filename)
	{
		Query<Configuration> query = m_configDAO.createQuery();
		if(!name.equals(""))
		{
			query.criteria("name").equal(name);
		}
		if(!configurationType.equals(""))
		{
			query.criteria("configurationType").equal(configurationType);
		}
		if(!filename.equals(""))
		{
			query.criteria("filename").equal(filename);
		}
		return query.asList();
	}

	@Override
	public Configuration addNewConfiguration(Configuration config)
	{
		try
		{
			config.validate();
		} catch(InvalidDataError ex)
		{
			throw new WebApplicationException(ex, Status.BAD_REQUEST);
		}
		List<Configuration> samename = m_configDAO.findConfigurationsByName(config.getName());
		if(samename != null && samename.size() > 0)
			throw new WebApplicationException(new InvalidDataError("Configuration", "name", "A configuration with the name '" + config.getName() + "' already exists."), Status.BAD_REQUEST);
		m_configDAO.save(config);
		return config;
	}

	@Override
	public Configuration getConfiguration(String configId)
	{
		return findConfigurationById(configId);
	}

	@Override
	public Configuration updateConfiguration(String configId, Configuration updatedConfig)
	{
		Configuration config = findConfigurationById(configId);
		if(updatedConfig.getName() != null &&
		   !updatedConfig.getName().equals("") &&
		   !updatedConfig.getName().equals(config.getName()))
		{
			config.setName(updatedConfig.getName());
		}

		if(updatedConfig.getConfigurationType() != null &&
		   !updatedConfig.getConfigurationType().equals("") &&
		   !updatedConfig.getConfigurationType().equals(config.getConfigurationType()))
		{
			config.setConfigurationType(updatedConfig.getConfigurationType());
		}

		if(updatedConfig.getFilename() != null &&
		   !updatedConfig.getFilename().equals("") &&
		   !updatedConfig.getFilename().equals(config.getFilename()))
		{
			config.setFilename(updatedConfig.getFilename());
		}

		if(updatedConfig.getConfigurationData() != null)
		{
			config.setConfigurationData(updatedConfig.getConfigurationData());
		}

		m_configDAO.save(config);
		return config;
	}

	protected Configuration findConfigurationById(String configId)
	{
		Configuration retval = null;
		try
		{
			retval = m_configDAO.get(new ObjectId(configId));
		} catch(RuntimeException ex)
		{
			// in case they provide an invalid objectid
		}
		if(retval == null)
			throw new NotFoundError(Configuration.class, configId);
		return retval;
	}

	@Override
	public void deleteConfiguration(String configId)
	{
		Configuration config = findConfigurationById(configId);
		m_configDAO.delete(config);
	}

	@Override
	public Configuration addNewConfigurationData(String configId, Map<String, String> configurationData)
	{
		Configuration config = findConfigurationById(configId);
		config.getConfigurationData().putAll(configurationData);
		m_configDAO.save(config);
		return config;
	}

	@Override
	public Configuration deleteConfigurationData(String configId, String configKey)
	{
		Configuration config = findConfigurationById(configId);
		config.getConfigurationData().remove(configKey);
		m_configDAO.save(config);
		return config;
	}
}
