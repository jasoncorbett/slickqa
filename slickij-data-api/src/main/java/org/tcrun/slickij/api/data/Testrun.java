package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import static org.tcrun.slickij.api.data.CopyUtil.copyDateIfNotNull;
import static org.tcrun.slickij.api.data.CopyUtil.copyIfNotNull;

/**
 *
 * @author jcorbett
 */
@Entity("testruns")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Testrun implements Serializable, Copyable<Testrun>
{
	@Id
	private ObjectId id;

	@Property
	private ObjectId testplanId;

    @NotSaved
    private Testplan testplan;

	@Property
	private String name;

	@Embedded
	private ConfigurationReference config;

	@Embedded
	private ConfigurationReference runtimeOptions;

	
	@Embedded
	private ProjectReference project;

	@Property
	@Indexed
	private Date dateCreated;

	@Embedded
	private ReleaseReference release;

	@Embedded
	private BuildReference build;

	@Embedded
	private List<DataExtension<Testrun>> extensions;

    @Embedded
    private TestRunSummary summary;

    @Property
    private Boolean finished;

    public Testrun()
    {
        finished = false;
    }

	public ConfigurationReference getConfig()
	{
		return config;
	}

	public void setConfig(ConfigurationReference config)
	{
		this.config = config;
	}

	public ConfigurationReference getRuntimeOptions()
	{
		return runtimeOptions;
	}

	public void setRuntimeOptions(ConfigurationReference runtimeOptions)
	{
		this.runtimeOptions = runtimeOptions;
	}

	@JsonIgnore
	public ObjectId getObjectId()
	{
		return id;
	}

	public String getId()
	{
		if(id == null)
			return null;
		else
			return id.toString();
	}

	public void setId(ObjectId id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@JsonIgnore
	public ObjectId getTestplanObjectId()
	{
		return testplanId;
	}

	public String getTestplanId()
	{
		if(testplanId == null)
			return null;
		else
			return testplanId.toString();
	}

	public void setTestplanId(ObjectId testplanId)
	{
		this.testplanId = testplanId;
	}

	public Date getDateCreated()
	{
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated)
	{
		this.dateCreated = dateCreated;
	}

	public ProjectReference getProject()
	{
		return project;
	}

	public void setProject(ProjectReference project)
	{
		this.project = project;
	}

	public BuildReference getBuild()
	{
		return build;
	}

	public void setBuild(BuildReference build)
	{
		this.build = build;
	}

	public ReleaseReference getRelease()
	{
		return release;
	}

	public void setRelease(ReleaseReference release)
	{
		this.release = release;
	}

	public List<DataExtension<Testrun>> getExtensions()
	{
		return extensions;
	}

	public void setExtensions(List<DataExtension<Testrun>> extensions)
	{
		this.extensions = extensions;
	}

	@PostLoad
	public void postLoad()
	{
		if(extensions == null)
			extensions = new ArrayList<DataExtension<Testrun>>();
		for(DataExtension<Testrun> extension : extensions)
			extension.setParent(this);
	}


	public void validate() throws InvalidDataError
	{
		if(dateCreated == null)
			dateCreated = new Date();
		if(name == null)
			name = "Run starting " + dateCreated.toString();
	}


	public TestrunReference createReference()
	{
		TestrunReference ref = new TestrunReference();
		ref.setName(name);
		ref.setTestrunId(id);
		return ref;
	}

    public TestRunSummary getSummary()
    {
        return summary;
    }

    public void setSummary(TestRunSummary summary)
    {
        this.summary = summary;
    }

    public Testplan getTestplan()
    {
        return testplan;
    }

    public void setTestplan(Testplan testplan)
    {
        this.testplan = testplan;
    }

    public Boolean getFinished()
    {
        return finished;
    }

    public void setFinished(Boolean finished)
    {
        this.finished = finished;
    }

    @Override
    public Testrun createCopy()
    {
        Testrun copy = new Testrun();

        copy.setId(id);
        copy.setBuild(copyIfNotNull(build));
        copy.setConfig(copyIfNotNull(config));
        copy.setDateCreated(copyDateIfNotNull(dateCreated));
        copy.setName(name);
        copy.setProject(copyIfNotNull(project));
        copy.setRelease(copyIfNotNull(release));
        copy.setRuntimeOptions(copyIfNotNull(runtimeOptions));
        copy.setSummary(copyIfNotNull(summary));
        copy.setTestplan(copyIfNotNull(testplan));
        copy.setTestplanId(testplanId);
        copy.setFinished(finished);

        return copy;
    }
}
