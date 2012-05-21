package org.tcrun.slickij.data;

import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.ResultResource;
import org.tcrun.slickij.api.data.Build;
import org.tcrun.slickij.api.data.Component;
import org.tcrun.slickij.api.data.Configuration;
import org.tcrun.slickij.api.data.HostStatus;
import org.tcrun.slickij.api.data.InvalidDataError;
import org.tcrun.slickij.api.data.LogEntry;
import org.tcrun.slickij.api.data.LogLevel;
import org.tcrun.slickij.api.data.Project;
import org.tcrun.slickij.api.data.Release;
import org.tcrun.slickij.api.data.Result;
import org.tcrun.slickij.api.data.ResultStatus;
import org.tcrun.slickij.api.data.RunStatus;
import org.tcrun.slickij.api.data.StoredFile;
import org.tcrun.slickij.api.data.TestRunParameter;
import org.tcrun.slickij.api.data.Testcase;
import org.tcrun.slickij.api.data.Testrun;
import org.tcrun.slickij.api.data.dao.ConfigurationDAO;
import org.tcrun.slickij.api.data.dao.HostStatusDAO;
import org.tcrun.slickij.api.data.dao.ProjectDAO;
import org.tcrun.slickij.api.data.dao.ResultDAO;
import org.tcrun.slickij.api.data.dao.TestcaseDAO;
import org.tcrun.slickij.api.data.dao.TestrunDAO;

/**
 *
 * @author jcorbett
 */
public class ResultResourceImpl implements ResultResource
{
	private ResultDAO m_resultDAO;
	private HostStatusDAO m_hoststatusDAO;
	private ProjectDAO m_projectDAO;
	private ConfigurationDAO m_configDAO;
	private TestcaseDAO m_testcaseDAO;
	private TestrunDAO m_testrunDAO;


	@Inject
	public ResultResourceImpl(ResultDAO p_resultDAO, HostStatusDAO p_hoststatusDAO, ProjectDAO p_projectDAO, TestcaseDAO p_testcaseDAO, TestrunDAO p_testrunDAO, ConfigurationDAO p_configDAO)
	{
		m_resultDAO = p_resultDAO;
		m_hoststatusDAO = p_hoststatusDAO;
		m_projectDAO = p_projectDAO;
		m_configDAO = p_configDAO;
		m_testcaseDAO = p_testcaseDAO;
		m_testrunDAO = p_testrunDAO;
	}

	@Override
	public List<Result> getMatchingResults(UriInfo uriInfo)
	{
		Query<Result> query = m_resultDAO.createQuery();
		MultivaluedMap<String, String> params = uriInfo.getQueryParameters();

		if(params.containsKey("testrunid"))
		{
			ObjectId testrunid;
			try
			{
				testrunid = new ObjectId(params.getFirst("testrunid"));
			} catch(RuntimeException ex)
			{
				throw new WebApplicationException(ex, Status.BAD_REQUEST);
			}
			query.criteria("testrun.testrunId").equal(testrunid);
		}
		if(params.containsKey("status"))
		{
			ResultStatus status = ResultStatus.valueOf(params.getFirst("status"));
			query.criteria("status").equal(status);
		} else if(params.containsKey("excludestatus")) // having both status and excludestatus won't work, so favor status above excludestatus
		{
			ResultStatus exclude = ResultStatus.valueOf(params.getFirst("excludestatus"));
			query.criteria("status").notEqual(exclude);
		}
		if(params.containsKey("runstatus"))
		{
			RunStatus status = RunStatus.valueOf(params.getFirst("runstatus"));
			query.criteria("runstatus").equal(status);
		}
		if(!params.containsKey("allfields"))
		{
			query.retrievedFields(false, "log", "build", "release", "project", "testrun", "config");
		}
		//query.order("~recorded");

		return query.asList();
	}

	@Override
	public Result getNextToBeRun(TestRunParameter parameter)
	{
		if(parameter.getHostname() == null)
			throw new WebApplicationException(new Exception("You must at least supply a hostname."), Status.BAD_REQUEST);
		HostStatus host = m_hoststatusDAO.get(parameter.getHostname());
		if(host == null)
		{
			host = new HostStatus();
			host.setHostname(parameter.getHostname());
		}
		host.setLastCheckin(new Date());
		Result work = m_resultDAO.getNextToBeRun(parameter);
		if(work != null)
			host.setCurrentWork(work);
		m_hoststatusDAO.save(host);
		return work;
	}

	@Override
	public Result updateResult(String resultid, Result update)
	{
		Result result = getResult(resultid);
		if(update.getStatus() != null)
		{
			result.setStatus(update.getStatus());
			if(update.getRunstatus() == null && update.getStatus() != ResultStatus.NO_RESULT)
				update.setRunstatus(RunStatus.FINISHED);
			if(update.getRecorded() == null && update.getStatus() != ResultStatus.NO_RESULT)
			{
				update.setRecorded(new Date());
				if(result.getRecorded() != null)
				{
					// set the runlength to the difference between the recorded times / 1000 (to get rid of milliseconds).
					update.setRunlength((int) ((update.getRecorded().getTime() - result.getRecorded().getTime()) / 1000));
				}
			}
		}
		if(update.getRunstatus() != null)
		{
			result.setRunstatus(update.getRunstatus());
			if(update.getRunstatus() == RunStatus.FINISHED)
			{
				if(update.getRunlength() != 0 && result.getRecorded() != null)
					update.setRunlength((int)(result.getRecorded().getTime() - (new Date()).getTime()) / 1000);
				if(result.getHostname() != null)
				{
					HostStatus hoststatus = m_hoststatusDAO.get(result.getHostname());
					if(hoststatus == null)
					{
						// unlikely that the result has a hostname, but there is no host status
						hoststatus = new HostStatus();
						hoststatus.setHostname(result.getHostname());
					}
					hoststatus.setLastCheckin(new Date());
					hoststatus.setCurrentWork(null);
					m_hoststatusDAO.save(hoststatus);
				}
			}
		}
		if(update.getAttributes() != null)
			result.setAttributes(update.getAttributes());
		Project project = null;
		if(update.getProject() != null)
		{
			project = m_projectDAO.findByReference(update.getProject());
			if(project == null)
				throw new NotFoundError(Project.class);
			result.setProject(update.getProject());
		}
		Release release = null;
		if(update.getRelease() != null)
		{
			if(project == null && result.getProject() != null)
				project = m_projectDAO.findByReference(result.getProject());
			if(project != null)
			{
				release = project.findReleaseByReference(update.getRelease());
				if(release != null)
					result.setRelease(update.getRelease());
				else
					throw new NotFoundError(Release.class);
			}
		}
		if(update.getBuild() != null)
		{
			if(project == null && result.getProject() != null)
				project = m_projectDAO.findByReference(result.getProject());
			if(project != null)
			{
				if(release == null && result.getRelease() != null)
					release = project.findReleaseByReference(result.getRelease());
				if(release != null)
				{
					Build build = release.findBuildByReference(update.getBuild());
					if(build == null)
						throw new NotFoundError(Build.class);
					result.setBuild(update.getBuild());
				}
			}
		}
		if(update.getComponent() != null)
		{
			if(project == null && result.getProject() != null)
				project = m_projectDAO.findByReference(result.getProject());
			if(project != null)
			{
				Component comp = project.findComponentByReference(update.getComponent());
				if(comp == null)
					throw new NotFoundError(Component.class);
				result.setComponent(update.getComponent());
			}
		}
		if(update.getConfig() != null)
		{
			Configuration config = m_configDAO.findConfigurationByReference(update.getConfig());
			if(config == null)
				throw new NotFoundError(Configuration.class);
			result.setConfig(update.getConfig());
		}
		if(update.getConfigurationOverride() != null)
			result.setConfigurationOverride(update.getConfigurationOverride());
		if(update.getFiles() != null)
			result.setFiles(update.getFiles());
		if(update.getHostname() != null)
			result.setHostname(update.getHostname());
		if(update.getLog() != null)
			result.setLog(update.getLog());
		if(update.getReason() != null)
			result.setReason(update.getReason());
		if(update.getRecorded() != null)
			result.setRecorded(update.getRecorded());
		if(update.getTestcase() != null)
		{
			Testcase test = m_testcaseDAO.findTestcaseByReference(update.getTestcase());
			if(test == null)
				throw new NotFoundError(Testcase.class);
			result.setTestcase(update.getTestcase());
		}
		if(update.getTestrun() != null)
		{
			Testrun testrun = m_testrunDAO.findByTestrunReference(update.getTestrun());
			if(testrun == null)
				throw new NotFoundError(Testrun.class);
			result.setTestrun(update.getTestrun());
		}

		m_resultDAO.save(result);

		return result;
	}

	@Override
	public Result addResult(Result result)
	{
		try
		{
			result.validate();
		} catch(InvalidDataError error)
		{
			throw new WebApplicationException(error, Status.BAD_REQUEST);
		}
		Testcase test = m_testcaseDAO.findTestcaseByReference(result.getTestcase());
		if(test == null)
			throw new NotFoundError(Testcase.class);
		Project project = null;
		if(result.getProject() != null)
			project = m_projectDAO.findByReference(result.getProject());
		if(project == null)
			project = m_projectDAO.findByReference(test.getProject());
		if(project != null)
		{
			result.setProject(project.createReference());
			Release release = null;
			if(result.getRelease() != null)
				release = project.findReleaseByReference(result.getRelease());
			if(release == null)
				release = project.findRelease(project.getDefaultRelease());
			Build build = null;
			if(release != null)
			{
				result.setRelease(release.createReference());
				if(result.getBuild() != null)
					build = release.findBuildByReference(result.getBuild());
				if(build == null)
					build = release.findBuild(release.getDefaultBuild());
				if(build != null)
					result.setBuild(build.createReference());
				else
					result.setBuild(null);
			} else
			{
				result.setRelease(null);
			}

			if(result.getComponent() != null)
			{
				Component comp = project.findComponentByReference(result.getComponent());
				if(comp == null)
				{
					comp = new Component();
					comp.setCode(result.getComponent().getCode());
					comp.setName(result.getComponent().getName());
					project.getComponents().add(comp);
					m_projectDAO.save(project);
					result.getComponent().setId(comp.getObjectId());
				}
			}
		} else
		{
			result.setProject(null);
			result.setRelease(null);
			result.setBuild(null);
			result.setComponent(null);
		}

		// Configuration
		if(result.getConfig() != null)
		{
			Configuration config = m_configDAO.findConfigurationByReference(result.getConfig());
			if(config == null)
				result.setConfig(null);
		}
		// Testrun
		if(result.getTestrun() != null)
		{
			Testrun testrun = m_testrunDAO.findByTestrunReference(result.getTestrun());
			if(testrun == null)
				result.setTestrun(null);
		}
		if(result.getTestrun() == null)
		{
			Testrun testrun = new Testrun();
			testrun.setProject(result.getProject());
			testrun.setRelease(result.getRelease());
			testrun.setDateCreated(new Date());
			testrun.setBuild(result.getBuild());
			testrun.setConfig(result.getConfig());
			m_testrunDAO.save(testrun);
			result.setTestrun(testrun.createReference());
		}

		// Recorded
		if(result.getRecorded() == null)
			result.setRecorded(new Date());

		// RunStatus
		if(result.getRunstatus() == null)
		{
			if(result.getStatus() == ResultStatus.NO_RESULT)
				result.setRunstatus(RunStatus.RUNNING);
			else
				result.setRunstatus(RunStatus.FINISHED);
		}

		// you have to save the new result before setting up the hoststatus
		m_resultDAO.save(result);

		// Hostname (HostStatus)
		if(result.getHostname() != null)
		{
			HostStatus hoststatus = m_hoststatusDAO.get(result.getHostname());
			if(hoststatus == null)
			{
				hoststatus = new HostStatus();
				hoststatus.setHostname(result.getHostname());
				hoststatus.setLastCheckin(new Date());
			}
			if(result.getRunstatus() == RunStatus.RUNNING)
				hoststatus.setCurrentWork(result);
			else if(result.getRunstatus() == RunStatus.FINISHED)
				hoststatus.setCurrentWork(null);
			m_hoststatusDAO.save(hoststatus);
		}


		return result;
	}

	@Override
	public Result getResult(String resultId)
	{
		ObjectId realResultId = null;
		try
		{
			realResultId = new ObjectId(resultId);
		} catch(RuntimeException ex)
		{
			throw new WebApplicationException(ex, Status.BAD_REQUEST);
		}
		Result retval = m_resultDAO.get(realResultId);
		if(retval == null)
			throw new NotFoundError(Result.class, resultId);

		return retval;
	}

    @Override
    public Result deleteResult(String resultId)
    {
        Result result = getResult(resultId);
        m_resultDAO.delete(result);
        return result;
    }

	@Override
	public Integer addToLog(String resultId, List<LogEntry> logaddon)
	{
		Result result = getResult(resultId);
		if(result.getLog() == null)
			result.setLog(new ArrayList<LogEntry>());
		result.getLog().addAll(logaddon);
		m_resultDAO.save(result);
		return result.getLog().size();
	}

    @Override
    public Result cancelResult(String resultid, String reason)
    {
        Result result = getResult(resultid);
		
		// update the host status
		HostStatus host = m_hoststatusDAO.get(result.getHostname());
		host.setCurrentWork(null);
		m_hoststatusDAO.save(host);
		
		Query<Result> updateQuery = m_resultDAO.createQuery();
		updateQuery.criteria("id").equal(result.getObjectId());
		updateQuery.criteria("runstatus").equal(RunStatus.RUNNING);
		UpdateOperations<Result> updateOps = m_resultDAO.getDatastore().createUpdateOperations(Result.class);

		LogEntry cancelMessage = new LogEntry();
		cancelMessage.setEntryTime(new Date());
		cancelMessage.setLevel(LogLevel.ERROR);
		cancelMessage.setLoggerName("slick");
		cancelMessage.setMessage("Canceled due to: " + reason);

		updateOps.add("log", cancelMessage);
		updateOps.set("runstatus", RunStatus.FINISHED);
		updateOps.set("status", ResultStatus.CANCELLED);
		updateOps.set("recorded", new Date());
		m_resultDAO.update(updateQuery, updateOps);

		return getResult(resultid);
    }

	@Override
	public Result rescheduleResult(String resultid)
	{
		Result res = getResult(resultid);
		if(res.getRunstatus() == RunStatus.FINISHED)
		{
			res.setRunstatus(RunStatus.TO_BE_RUN);
			res.setLog(new ArrayList<LogEntry>());
			res.setFiles(new ArrayList<StoredFile>());
			res.setStatus(ResultStatus.NO_RESULT);
			res.setHostname(null);
			res.setReason(null);
			res.setRecorded(new Date());
			m_resultDAO.save(res);
		}

		return res;
	}
}
