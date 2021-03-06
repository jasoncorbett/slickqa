package org.tcrun.slickij;

import com.mongodb.MongoException;
import java.net.UnknownHostException;
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

import com.mongodb.gridfs.GridFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcrun.slickij.core.DataPluginModule;

/**
 *
 * @author jcorbett
 */
public class DefaultModule extends AbstractModule
{
	private static Logger logger = LoggerFactory.getLogger(DefaultModule.class);
    private static Mongo mongo;

    private static synchronized void initMongodb(String hostname) throws UnknownHostException, MongoException
    {
        if(mongo == null)
            mongo = new Mongo(hostname);
    }

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
	//TODO: Defaults stored in web.xml
    //TODO: Some way to specify a mongodb connection information

	@Override
	protected void configure()
	{
        bind(LogResource.class);
		try
		{
			initMongodb("127.0.0.1");
            Mongo gridfsMongo = new Mongo("127.0.0.1");

            // we are using a separate Mongo in hopes that gridfs won't pollute the existing mongo connection
            // What pollution you ask?  Well if you use gridfs on a connection, the java mongo driver returns gridfs
            // objects from those collections used for gridfs instead of the generic DBObject.  The worst part is that
            // the gridfs object isn't a subclass of DBObject, so you get a class cast exception when using morphia.
            GridFS gridfs = new GridFS(gridfsMongo.getDB("slickij"));
            bind(GridFS.class).toInstance(gridfs);
		} catch (UnknownHostException ex)
		{
			addError(ex);
		} catch (MongoException ex)
		{
			addError(ex);
		}

		Morphia mapper = new Morphia();
		bind(Morphia.class).toInstance(mapper);

		for (Module module : modules)
		{
			if(DataPluginModule.class.isAssignableFrom(module.getClass()))
				((DataPluginModule)module).setMorphiaInstance(mapper);
			install(module);
		}

		Datastore ds = mapper.createDatastore(mongo, "slickij");

		bind(Datastore.class).toInstance(ds);
	}
}
