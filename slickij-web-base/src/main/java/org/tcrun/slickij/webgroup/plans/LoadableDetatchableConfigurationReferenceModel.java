/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcrun.slickij.webgroup.plans;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.model.LoadableDetachableModel;
import org.tcrun.slickij.api.data.Configuration;
import org.tcrun.slickij.api.data.ConfigurationReference;
import org.tcrun.slickij.api.data.dao.ConfigurationDAO;

/**
 *
 * @author jcorbett
 */
public class LoadableDetatchableConfigurationReferenceModel extends LoadableDetachableModel<List<ConfigurationReference>>
{
	private String type;
	private ConfigurationDAO configDAO;

	public LoadableDetatchableConfigurationReferenceModel(ConfigurationDAO dao)
	{
		this.configDAO = dao;
		this.type = "ENVIRONMENT";
		setObject(load());
	}

	public LoadableDetatchableConfigurationReferenceModel(ConfigurationDAO dao, String configType)
	{
		this.configDAO = dao;
		this.type = configType;
		setObject(load());
	}

	@Override
	protected List<ConfigurationReference> load()
	{
		List<Configuration> configurations = configDAO.createQuery().field("configurationType").equal(type).asList();
		List<ConfigurationReference> modelObject = new ArrayList<ConfigurationReference>();
		for(Configuration configuration : configurations)
		{
			modelObject.add(configuration.createReference());
		}
		return modelObject;
	}
	
}
