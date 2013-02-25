package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Reference;
import java.io.Serializable;
import java.util.*;

import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;

import static org.tcrun.slickij.api.data.CopyUtil.copyDateIfNotNull;
import static org.tcrun.slickij.api.data.CopyUtil.copyIfNotNull;

/**
 *
 * @author jcorbett
 */
@Entity("results")
public class Result implements Serializable, Copyable<Result>
{
	@Id
	private ObjectId id;

	@Embedded
	private TestrunReference testrun;

	@Embedded
	private ConfigurationReference config;

	@Embedded
	private List<ConfigurationOverride> configurationOverride;

	@Embedded
	private TestcaseReference testcase;

	@Property
	@Indexed
	private Date recorded;

    @Property
    private Date started;

    @Property
    private Date finished;

	@Property
	private ResultStatus status;

	@Property
	@Indexed
	private RunStatus runstatus;

	@Property
	private String reason;

	@Property
	private Map<String, String> attributes;

	@Reference
	private List<StoredFile> files;

	@Embedded
	private List<LogEntry> log;

	@Embedded
	private ProjectReference project;

	@Embedded
	private ComponentReference component;

	@Embedded
	private ReleaseReference release;

	@Embedded
	private BuildReference build;

	@Property
	private int runlength;

	@Embedded
	private List<DataExtension<Result>> extensions;

	@Embedded
	private List<ResultReference> history;

	@Property
	private String hostname;

	public String getId()
	{
		if(id == null)
			return null;
		else
			return id.toString();
	}

	@JsonIgnore
	public ObjectId getObjectId()
	{
		return id;
	}

	public void setId(ObjectId id)
	{
		this.id = id;
	}

	public TestrunReference getTestrun()
	{
		return testrun;
	}

	public void setTestrun(TestrunReference testrun)
	{
		this.testrun = testrun;
	}

	public Map<String, String> getAttributes()
	{
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes)
	{
		this.attributes = attributes;
	}

	public BuildReference getBuild()
	{
		return build;
	}

	public void setBuild(BuildReference build)
	{
		this.build = build;
	}

	public ComponentReference getComponent()
	{
		return component;
	}

	public void setComponent(ComponentReference component)
	{
		this.component = component;
	}

	public ConfigurationReference getConfig()
	{
		return config;
	}

	public void setConfig(ConfigurationReference config)
	{
		this.config = config;
	}

	public List<ConfigurationOverride> getConfigurationOverride()
	{
		return configurationOverride;
	}

	public void setConfigurationOverride(List<ConfigurationOverride> configurationOverride)
	{
		this.configurationOverride = configurationOverride;
	}

	public List<DataExtension<Result>> getExtensions()
	{
		return extensions;
	}

	public void setExtensions(List<DataExtension<Result>> extensions)
	{
		this.extensions = extensions;
	}

	public List<StoredFile> getFiles()
	{
		return files;
	}

	public void setFiles(List<StoredFile> files)
	{
		this.files = files;
	}

	public List<ResultReference> getHistory()
	{
		return history;
	}

	public void setHistory(List<ResultReference> history)
	{
		this.history = history;
	}

	public ProjectReference getProject()
	{
		return project;
	}

	public void setProject(ProjectReference project)
	{
		this.project = project;
	}

	public String getReason()
	{
		return reason;
	}

	public void setReason(String reason)
	{
		this.reason = reason;
	}

	public Date getRecorded()
	{
		return recorded;
	}

	public void setRecorded(Date recorded)
	{
		this.recorded = recorded;
	}

	public ReleaseReference getRelease()
	{
		return release;
	}

	public void setRelease(ReleaseReference release)
	{
		this.release = release;
	}

	public int getRunlength()
	{
		return runlength;
	}

	public void setRunlength(int runlength)
	{
		this.runlength = runlength;
	}

	public RunStatus getRunstatus()
	{
		return runstatus;
	}

	public void setRunstatus(RunStatus runstatus)
	{
		this.runstatus = runstatus;
	}

	public ResultStatus getStatus()
	{
		return status;
	}

	public void setStatus(ResultStatus status)
	{
		this.status = status;
	}

	public TestcaseReference getTestcase()
	{
		return testcase;
	}

	public void setTestcase(TestcaseReference testcase)
	{
		this.testcase = testcase;
	}

	public String getHostname()
	{
		return hostname;
	}

	public void setHostname(String hostname)
	{
		this.hostname = hostname;
	}

	public List<LogEntry> getLog()
	{
		return log;
	}

	public void setLog(List<LogEntry> log)
	{
		this.log = log;
	}

	public ResultReference createReference()
	{
		ResultReference ref = new ResultReference();
		ref.setBuild(build);
		ref.setRecorded(recorded);
		ref.setResultId(id);
		ref.setStatus(status);
		return ref;
	}

	public void validate() throws InvalidDataError
	{
		if(status == null)
			throw new InvalidDataError("Result", "status", "Status cannot be null");
		if(testcase == null)
			throw new InvalidDataError("Result", "testcase", "You must provide some information on the testcase.");
		if(testcase.getName() == null && testcase.getTestcaseId() == null &&
		   testcase.getAutomationId() == null && testcase.getAutomationKey() == null)
			throw new InvalidDataError("Result", "testcase", "Not enough information provided for testcase.");
		if(runstatus == null)
		{
			if(status == ResultStatus.NO_RESULT)
				runstatus = RunStatus.TO_BE_RUN;
			else
				runstatus = RunStatus.FINISHED;
		}
		if(recorded == null)
			recorded = new Date();
	}

	public boolean isSatisfiedBy(TestRunParameter params)
	{
		if(testcase != null && testcase.getAutomationTool() != null)
		{
			if(params.getAutomationTool() == null)
				return false;
			if(!testcase.getAutomationTool().equals(params.getAutomationTool()))
				return false;
		}

		if(configurationOverride == null || configurationOverride.size() == 0)
			return true;

		boolean allRequirementsSatisfied = true;
		for(ConfigurationOverride config : configurationOverride)
		{
			if(!config.getIsRequirement())
				continue;
			if(!params.getFulFilledRequirements().containsKey(config.getKey()))
			{
				allRequirementsSatisfied = false;
				break;
			}
			if(!config.getValue().equals(params.getFulFilledRequirements().get(config.getKey())))
			{
				allRequirementsSatisfied = false;
				break;
			}
		}

		return allRequirementsSatisfied;
	}

    public Date getStarted()
    {
        return started;
    }

    public void setStarted(Date started)
    {
        this.started = started;
    }

    public Date getFinished()
    {
        return finished;
    }

    public void setFinished(Date finished)
    {
        this.finished = finished;
    }

    @Override
    public Result createCopy()
    {
        Result copy = new Result();

        copy.setBuild(copyIfNotNull(build));
        copy.setComponent(copyIfNotNull(component));
        copy.setConfig(copyIfNotNull(config));
        copy.setHostname(hostname);
        copy.setId(id);
        copy.setProject(copyIfNotNull(project));
        copy.setTestrun(copyIfNotNull(testrun));
        copy.setTestcase(copyIfNotNull(testcase));
        copy.setStatus(status);
        copy.setRunstatus(runstatus);
        copy.setRunlength(runlength);
        copy.setRelease(copyIfNotNull(release));
        copy.setRecorded(copyDateIfNotNull(recorded));
        copy.setStarted(copyDateIfNotNull(started));
        copy.setFinished(copyDateIfNotNull(finished));
        copy.setReason(reason);
        copy.setTestrun(copyIfNotNull(testrun));

        copy.setAttributes(new HashMap<String, String>());
        copy.getAttributes().putAll(attributes);

        List<ResultReference> copyOfHistory = new ArrayList<ResultReference>();
        for(ResultReference orig : history)
        {
            copyOfHistory.add(copyIfNotNull(orig));
        }
        copy.setHistory(copyOfHistory);

        List<ConfigurationOverride> copyOfOverrides = new ArrayList<ConfigurationOverride>();
        for(ConfigurationOverride orig : getConfigurationOverride())
        {
            copyOfOverrides.add(copyIfNotNull(orig));
        }
        copy.setConfigurationOverride(copyOfOverrides);

        List<StoredFile> copyOfStoredFiles = new ArrayList<StoredFile>();
        for(StoredFile orig : getFiles())
        {
            copyOfStoredFiles.add(copyIfNotNull(orig));
        }
        copy.setFiles(copyOfStoredFiles);

        List<LogEntry> copyOfLogs = new ArrayList<LogEntry>();
        for(LogEntry orig : log)
        {
            copyOfLogs.add(copyIfNotNull(orig));
        }
        copy.setLog(copyOfLogs);

        return copy;
    }
}
