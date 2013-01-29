package org.tcrun.slickij.data;

import com.google.inject.multibindings.Multibinder;
import org.tcrun.slickij.api.*;
import org.tcrun.slickij.api.data.*;
import org.tcrun.slickij.api.data.dao.*;
import org.tcrun.slickij.api.events.EventManager;
import org.tcrun.slickij.data.dao.*;
import com.google.code.morphia.Morphia;
import com.google.inject.AbstractModule;
import java.util.Map;

import org.tcrun.slickij.core.DataPluginModule;
import org.tcrun.slickij.data.org.tcrun.slickij.data.updates.AddIndexesUpdate;
import org.tcrun.slickij.data.org.tcrun.slickij.data.updates.AddSystemConfigurationIndexUpdate;
import org.tcrun.slickij.data.org.tcrun.slickij.data.updates.AddTestrunBuildIndexUpdate;
import org.tcrun.slickij.events.AMQPEventManager;

/**
 *
 * @author jcorbett
 */
public class DataModule extends AbstractModule implements DataPluginModule
{
	Morphia morphia;

	@Override
	protected void configure()
	{
		morphia.map(Project.class);
		morphia.map(Configuration.class);
		morphia.map(Testcase.class);
		morphia.map(Testplan.class);
		morphia.map(Testrun.class);
		morphia.map(Result.class);
		morphia.map(StoredFile.class);
		morphia.map(HostStatus.class);
		morphia.map(UserAccount.class);
		morphia.map(FileChunk.class);
        morphia.map(UpdateRecord.class);
        morphia.map(TestrunGroup.class);
        morphia.map(AMQPSystemConfiguration.class);
		bind(ProjectDAO.class).to(ProjectDAOImpl.class);
		bind(FileChunkDAO.class).to(FileChunkDAOImpl.class);
		bind(ConfigurationDAO.class).to(ConfigurationDAOImpl.class);
		bind(TestplanDAO.class).to(TestplanDAOImpl.class);
		bind(TestcaseDAO.class).to(TestcaseDAOImpl.class);
		bind(TestrunDAO.class).to(TestrunDAOImpl.class);
		bind(ResultDAO.class).to(ResultDAOImpl.class);
		bind(HostStatusDAO.class).to(HostStatusDAOImpl.class);
		bind(UserAccountDAO.class).to(UserAccountDAOImpl.class);
		bind(StoredFileDAO.class).to(StoredFileDAOImpl.class);
        bind(UpdateRecordDAO.class).to(UpdateRecordDAOImpl.class);
        bind(TestrunGroupDAO.class).to(TestrunGroupDAOImpl.class);
        bind(SystemConfigurationDAO.class).to(SystemConfigurationDAOImpl.class);
		bind(ProjectResource.class).to(ProjectResourceImpl.class);
		bind(TestplanResource.class).to(TestplanResourceImpl.class);
		bind(ConfigurationResource.class).to(ConfigurationResourceImpl.class);
		bind(TestcaseResource.class).to(TestcaseResourceImpl.class);
		bind(TestrunResource.class).to(TestrunResourceImpl.class);
		bind(ResultResource.class).to(ResultResourceImpl.class);
		bind(HostStatusResource.class).to(HostStatusResourceImpl.class);
		bind(StoredFileResource.class).to(StoredFileResourceImpl.class);
		bind(ProductVersionMap.class).toProvider(ProductVersionsProvider.class);
		bind(VersionResource.class).to(VersionResourceImpl.class);
        bind(UpdateResource.class).to(UpdateResourceImpl.class);
        bind(TestrunGroupResource.class).to(TestrunGroupResourceImpl.class);
        bind(SystemConfigurationResource.class).to(SystemConfigurationResourceImpl.class);
        bind(ReloadResource.class).to(ReloadResourceImpl.class);
        bind(EventManager.class).to(AMQPEventManager.class).asEagerSingleton();
		bind(SlickApiExceptionMapper.class);
		bind(NotFoundExceptionMapper.class);

        // for updates
        Multibinder<SlickUpdate> slickUpdateMultibinder = Multibinder.newSetBinder(binder(), SlickUpdate.class);
        slickUpdateMultibinder.addBinding().to(AddIndexesUpdate.class);
        slickUpdateMultibinder.addBinding().to(AddTestrunBuildIndexUpdate.class);
        slickUpdateMultibinder.addBinding().to(AddSystemConfigurationIndexUpdate.class);
	}

	@Override
	public void setMorphiaInstance(Morphia morphia)
	{
		this.morphia = morphia;
	}
}
