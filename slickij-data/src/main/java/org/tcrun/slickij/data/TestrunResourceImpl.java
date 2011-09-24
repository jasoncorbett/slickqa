package org.tcrun.slickij.data;

import com.google.code.morphia.query.Query;
import com.google.inject.Inject;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.TestrunResource;
import org.tcrun.slickij.api.data.Build;
import org.tcrun.slickij.api.data.Configuration;
import org.tcrun.slickij.api.data.DataExtension;
import org.tcrun.slickij.api.data.InvalidDataError;
import org.tcrun.slickij.api.data.Project;
import org.tcrun.slickij.api.data.Release;
import org.tcrun.slickij.api.data.Result;
import org.tcrun.slickij.api.data.Testrun;
import org.tcrun.slickij.api.data.TestRunSummary;
import org.tcrun.slickij.api.data.dao.ConfigurationDAO;
import org.tcrun.slickij.api.data.dao.ProjectDAO;
import org.tcrun.slickij.api.data.dao.ResultDAO;
import org.tcrun.slickij.api.data.dao.TestrunDAO;

/**
 *
 * @author jcorbett
 */
public class TestrunResourceImpl implements TestrunResource
{
	private TestrunDAO m_testrunDAO;
	private ResultDAO m_resultDAO;
	private ConfigurationDAO m_configDAO;
	private ProjectDAO m_projectDAO;

	@Inject
	public TestrunResourceImpl(TestrunDAO p_testrunDAO, ResultDAO p_resultDAO, ConfigurationDAO p_configDAO, ProjectDAO p_projectDAO)
	{
		m_testrunDAO = p_testrunDAO;
		m_resultDAO = p_resultDAO;
		m_configDAO = p_configDAO;
		m_projectDAO = p_projectDAO;
	}

	@Override
	public List<Testrun> getMatchingTestruns(UriInfo uriInfo)
	{
		MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
		if(queryParams == null || queryParams.isEmpty())
			return new ArrayList<Testrun>();

		Query<Testrun> query = m_testrunDAO.createQuery();
		if(queryParams.containsKey("projectid"))
		{
			try
			{
				query.criteria("project.id").equal(new ObjectId(queryParams.getFirst("projectid")));
			} catch(RuntimeException ex)
			{
				throw new WebApplicationException(ex, Status.BAD_REQUEST);
			}
		}
		if(queryParams.containsKey("releaseid"))
		{
			try
			{
				query.criteria("release.releaseId").equal(new ObjectId(queryParams.getFirst("releaseid")));
			} catch(RuntimeException ex)
			{
				throw new WebApplicationException(ex, Status.BAD_REQUEST);
			}
		}
		if(queryParams.containsKey("buildid"))
		{
			try
			{
				query.criteria("build.buildId").equal(new ObjectId(queryParams.getFirst("buildid")));
			} catch(RuntimeException ex)
			{
				throw new WebApplicationException(ex, Status.BAD_REQUEST);
			}
		}
		if(queryParams.containsKey("createdAfter"))
		{
			Date created = null;
			try
			{
				created = DateFormat.getDateInstance().parse(queryParams.getFirst("createdAfter"));
			} catch(ParseException ex)
			{
				throw new WebApplicationException(ex, Status.BAD_REQUEST);
			}
			query.criteria("dateCreated").greaterThanOrEq(created);
		}
		if(queryParams.containsKey("configid"))
		{
			try
			{
				query.criteria("config.configId").equal(new ObjectId(queryParams.getFirst("configid")));
			} catch(RuntimeException ex)
			{
				throw new WebApplicationException(ex, Status.BAD_REQUEST);
			}
		}
		if(queryParams.containsKey("configName"))
			query.criteria("config.name").equal(queryParams.getFirst("configName"));
		if(queryParams.containsKey("projectName"))
			query.criteria("project.name").equal(queryParams.getFirst("projectName"));
		if(queryParams.containsKey("releaseName"))
			query.criteria("release.name").equal(queryParams.getFirst("releaseName"));
		if(queryParams.containsKey("buildName"))
			query.criteria("build.name").equal(queryParams.getFirst("buildName"));
		if(queryParams.containsKey("name"))
			query.criteria("name").equal(queryParams.getFirst("name"));

		return query.asList();
	}

	@Override
	public Testrun createNewTestrun(Testrun testrun)
	{
		try
		{
			testrun.validate();
		} catch(InvalidDataError error)
		{
			throw new WebApplicationException(error, Status.BAD_REQUEST);
		}
		if(testrun.getConfig() != null)
		{
			Configuration config = m_configDAO.findConfigurationByReference(testrun.getConfig());
			if(config == null)
				throw new NotFoundError(Configuration.class);
		}
		if(testrun.getProject() != null)
		{
			Project project = m_projectDAO.findByReference(testrun.getProject());
			if(project == null)
				throw new NotFoundError(Project.class);
			if(testrun.getRelease() != null)
			{
				Release release = project.findReleaseByReference(testrun.getRelease());
				if(release == null)
					throw new NotFoundError(Release.class);
				if(testrun.getBuild() != null)
				{
					Build build = release.findBuildByReference(testrun.getBuild());
					if(build == null)
						throw new NotFoundError(Build.class);
				}
			}
		}
		m_testrunDAO.save(testrun);
		return testrun;
	}

	@Override
	public Testrun getTestrun(String testrunId)
	{
		Testrun retval = null;
		try
		{
			retval = m_testrunDAO.get(new ObjectId(testrunId));
		} catch(RuntimeException ex)
		{
			throw new WebApplicationException(ex, Status.BAD_REQUEST);
		}
		if(retval == null)
			throw new NotFoundError(Testrun.class, testrunId);
		return retval;
	}

	@Override
	public TestRunSummary getTestrunSummary(String testrunId)
	{
		Testrun run = getTestrun(testrunId); // handle the testrun not existing
		return m_testrunDAO.getSummary(run);
	}


	@Override
	public Testrun updateTestrun(String testrunId, Testrun update)
	{
		Testrun real = getTestrun(testrunId);
		if(update.getName() != null)
			real.setName(update.getName());
		if(update.getDateCreated() != null && !update.getDateCreated().equals(new Date(0)))
			real.setDateCreated(update.getDateCreated());
		if(update.getProject() != null)
		{
			Project project = m_projectDAO.findByReference(update.getProject());
			if(project == null)
				throw new NotFoundError(Project.class);
			real.setProject(update.getProject());
		}
		if(update.getRelease() != null && real.getProject() != null)
		{
			Project project = m_projectDAO.findByReference(real.getProject());
			if(project != null)
			{
				Release release = project.findReleaseByReference(update.getRelease());
				if(release == null)
					throw new NotFoundError(Release.class);
				real.setRelease(update.getRelease());
			}
		}
		if(update.getBuild() != null && real.getProject() != null)
		{
			Project project = m_projectDAO.findByReference(real.getProject());
			if(project != null)
			{
				if(real.getRelease() != null)
				{
					Release release = project.findReleaseByReference(real.getRelease());
					if(release != null)
					{
						Build build = release.findBuildByReference(update.getBuild());
						if(build == null)
							throw new NotFoundError(Build.class);
						real.setBuild(update.getBuild());
					}
				} else
				{
					for(Release possible : project.getReleases())
					{
						Build build = possible.findBuildByReference(update.getBuild());
						if(build != null)
						{
							real.setRelease(possible.createReference());
							real.setBuild(update.getBuild());
							break;
						}
					}
				}
			}
		}
		if(update.getConfig() != null)
		{
			Configuration config = m_configDAO.findConfigurationByReference(update.getConfig());
			if(config == null)
				throw new NotFoundError(Configuration.class);
			real.setConfig(update.getConfig());
		}
		m_testrunDAO.save(real);
		return real;
	}

	@Override
	public List<Result> deleteTestrun(String testrunId)
	{
		Testrun testrun = getTestrun(testrunId);
		List<Result> results = m_resultDAO.findResultsByTestrun(testrun);
		for(Result result : results)
		{
			result.setTestrun(null);
			m_resultDAO.save(result);
		}
		m_testrunDAO.delete(testrun);
		return results;
	}

	@Override
	public DataExtension<Testrun> addDataExtension(String testrunId, DataExtension<Testrun> dataExtension)
	{
		Testrun testrun = getTestrun(testrunId);
		try
		{
			dataExtension.validate();
		} catch(InvalidDataError ex)
		{
			throw new WebApplicationException(ex, Status.BAD_REQUEST);
		}
		testrun.getExtensions().add(dataExtension);
		m_testrunDAO.save(testrun);
		return dataExtension;
	}

	protected DataExtension<Testrun> findDataExtension(Testrun testplan, String extensionId)
	{
		DataExtension<Testrun> extension = null;
		for(DataExtension<Testrun> potential : testplan.getExtensions())
		{
			if(extensionId.equals(potential.getId()))
			{
				extension = potential;
				break;
			}
		}

		if(extension == null)
			throw new WebApplicationException(new Exception("Cannot find extension with id " + extensionId), Status.NOT_FOUND);

		return extension;
	}


	@Override
	public DataExtension<Testrun> updateDataExtension(String testrunId, String extensionId, DataExtension<Testrun> dataExtension)
	{
		Testrun testrun = getTestrun(testrunId);
		DataExtension<Testrun> extension = findDataExtension(testrun, extensionId);
		extension.update(dataExtension);
		m_testrunDAO.save(testrun);
		return extension;
	}

	@Override
	public List<DataExtension<Testrun>> deleteDataExtension(String testrunId, String extensionId)
	{
		Testrun testrun = getTestrun(testrunId);
		DataExtension<Testrun> extension = findDataExtension(testrun, extensionId);
		testrun.getExtensions().remove(extension);
		m_testrunDAO.save(testrun);
		return testrun.getExtensions();
		
	}
}
