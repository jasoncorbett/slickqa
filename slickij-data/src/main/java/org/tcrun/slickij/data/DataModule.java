package org.tcrun.slickij.data;

import org.tcrun.slickij.data.dao.ConfigurationDAOImpl;
import org.tcrun.slickij.api.data.dao.ConfigurationDAO;
import org.tcrun.slickij.api.data.dao.ProjectDAO;
import org.tcrun.slickij.data.dao.ProjectDAOImpl;
import com.google.code.morphia.Morphia;
import com.google.inject.AbstractModule;
import java.util.Map;
import org.tcrun.slickij.api.ConfigurationResource;
import org.tcrun.slickij.api.HostStatusResource;
import org.tcrun.slickij.api.ProjectResource;
import org.tcrun.slickij.api.ResultResource;
import org.tcrun.slickij.api.StoredFileResource;
import org.tcrun.slickij.api.TestcaseResource;
import org.tcrun.slickij.api.TestplanResource;
import org.tcrun.slickij.api.TestrunResource;
import org.tcrun.slickij.api.VersionResource;
import org.tcrun.slickij.api.data.Configuration;
import org.tcrun.slickij.api.data.FileChunk;
import org.tcrun.slickij.api.data.HostStatus;
import org.tcrun.slickij.api.data.ProductVersion;
import org.tcrun.slickij.api.data.Project;
import org.tcrun.slickij.api.data.Result;
import org.tcrun.slickij.api.data.StoredFile;
import org.tcrun.slickij.api.data.Testcase;
import org.tcrun.slickij.api.data.Testplan;
import org.tcrun.slickij.api.data.Testrun;
import org.tcrun.slickij.api.data.UserAccount;
import org.tcrun.slickij.api.data.dao.FileChunkDAO;
import org.tcrun.slickij.core.DataPluginModule;
import org.tcrun.slickij.api.data.dao.HostStatusDAO;
import org.tcrun.slickij.data.dao.HostStatusDAOImpl;
import org.tcrun.slickij.api.data.dao.ResultDAO;
import org.tcrun.slickij.api.data.dao.StoredFileDAO;
import org.tcrun.slickij.data.dao.ResultDAOImpl;
import org.tcrun.slickij.api.data.dao.TestcaseDAO;
import org.tcrun.slickij.data.dao.TestcaseDAOImpl;
import org.tcrun.slickij.api.data.dao.TestplanDAO;
import org.tcrun.slickij.data.dao.TestplanDAOImpl;
import org.tcrun.slickij.api.data.dao.TestrunDAO;
import org.tcrun.slickij.api.data.dao.UserAccountDAO;
import org.tcrun.slickij.data.dao.FileChunkDAOImpl;
import org.tcrun.slickij.data.dao.StoredFileDAOImpl;
import org.tcrun.slickij.data.dao.TestrunDAOImpl;
import org.tcrun.slickij.data.dao.UserAccountDAOImpl;

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
		bind(SlickApiExceptionMapper.class);
		bind(NotFoundExceptionMapper.class);

	}

	@Override
	public void setMorphiaInstance(Morphia morphia)
	{
		this.morphia = morphia;
	}
}
