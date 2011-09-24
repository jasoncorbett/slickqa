package org.tcrun.slickij;

import com.mongodb.MongoException;
import java.net.UnknownHostException;
import org.tcrun.slickij.data.ProjectResourceImpl;
import org.tcrun.slickij.api.ProjectResource;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.Datastore;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.mongodb.Mongo;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcrun.slickij.api.data.Project;
import org.tcrun.slickij.core.DataPluginModule;

/**
 *
 * @author jcorbett
 */
public class DefaultModule extends AbstractModule
{
	private static Logger logger = LoggerFactory.getLogger(DefaultModule.class);

	private List<Module> modules;

	public DefaultModule() throws Exception
	{
		modules = new ArrayList<Module>();
		Enumeration<URL> identifier_urls = this.getClass().getClassLoader().getResources("slickij.module");
		while (identifier_urls.hasMoreElements())
		{
			URL identifier_url = identifier_urls.nextElement();
			LineNumberReader identifier_file = new LineNumberReader(new InputStreamReader(identifier_url.openStream()));
			String line = null;
			while ((line = identifier_file.readLine()) != null)
			{
				logger.debug("Found guice module class '{}' on line '{}' of file '{}'.", new Object[] { line, identifier_file.getLineNumber(), identifier_url });
				try
				{
					Class<?> clazz = this.getClass().getClassLoader().loadClass(line);
					if (Module.class.isAssignableFrom(clazz))
					{
						try
						{
							modules.add((Module)clazz.newInstance());
						} catch(IllegalAccessException ex)
						{
							logger.warn("Unable to create instance of guice module '" + line + "' (from line '" + identifier_file.getLineNumber() + "' of '" + identifier_url +"')", ex);
						} catch(InstantiationException ex)
						{
							logger.warn("Unable to create instance of guice module '" + line + "' (from line '" + identifier_file.getLineNumber() + "' of '" + identifier_url +"')", ex);
						}
					} else
					{
						logger.warn("Guice module class '{}' is not really a guice module (from line '{}' of '{}').", new Object[] { line, identifier_file.getLineNumber(), identifier_url });

					}
				} catch (ClassNotFoundException ex)
				{
					logger.warn("Guice module class '{}' not found (from line '{}' of '{}').", new Object[] { line, identifier_file.getLineNumber(), identifier_url });
				}
			}
		}
	}
	//TODO: Test out plugin system / providers and add-ins
	//TODO: Load plugins from filesystem outside of war file
	//TODO: Build layered configuration system (plugins)
	//TODO: Defaults stored in web.xml
	//TODO: Multiple module guice config (perhaps from configuration)
	//TODO: Configuration edit page using wicket

	@Override
	protected void configure()
	{
		Mongo mongodb = null;
		try
		{
			mongodb = new Mongo("127.0.0.1");
		} catch (UnknownHostException ex)
		{
			addError(ex);
		} catch (MongoException ex)
		{
			addError(ex);
		}

		Morphia mapper = new Morphia();
		mapper.map(Project.class);
		bind(Morphia.class).toInstance(mapper);

		for (Module module : modules)
		{
			if(DataPluginModule.class.isAssignableFrom(module.getClass()))
				((DataPluginModule)module).setMorphiaInstance(mapper);
			install(module);
		}

		Datastore ds = mapper.createDatastore(mongodb, "slickij");

		bind(Datastore.class).toInstance(ds);
	}
}
