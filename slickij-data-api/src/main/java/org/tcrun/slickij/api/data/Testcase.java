package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;

import static org.tcrun.slickij.api.data.CopyUtil.copyIfNotNull;

/**
 * Class representing a testcase in the database.
 *
 * @author slambson
 */
@Entity("testcases")
@XmlRootElement
public class Testcase implements Serializable, Copyable<Testcase>
{

    @Id
    @Indexed
    private ObjectId id;

    @Property
    private String name;

    @Property
    private String purpose;

    @Property
    private String requirements;

    @Embedded
    private List<Step> steps;

    @Property
    private String author;

    @Property
    private Map<String, String> attributes;

    @Property
    private Boolean automated;

    @Property
    private int automationPriority;

    @Property
    private String automationTool;

    @Property
    private String automationConfiguration;

    @Property
    private String automationId;

    @Property
    @Indexed
    private String automationKey;

    @Property
    private int stabilityRating;

    @Property
    private List<String> tags;

    @Embedded
    private ProjectReference project;

    @Embedded
    private ComponentReference component;

    @Embedded
    private List<DataDrivenPropertyType> dataDriven;

    @Property
    private boolean deleted;

    @Embedded
    private List<DataExtension<Testcase>> extensions;

    public Map<String, String> getAttributes()
    {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes)
    {
        this.attributes = attributes;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public Boolean isAutomated()
    {
        return automated;
    }

    public void setAutomated(boolean automated)
    {
        this.automated = automated;
    }

    public String getAutomationConfiguration()
    {
        return automationConfiguration;
    }

    public void setAutomationConfiguration(String automationConfiguration)
    {
        this.automationConfiguration = automationConfiguration;
    }

    public String getAutomationId()
    {
        return automationId;
    }

    public void setAutomationId(String automationId)
    {
        this.automationId = automationId;
    }

    public String getAutomationKey()
    {
        return automationKey;
    }

    public void setAutomationKey(String automationKey)
    {
        this.automationKey = automationKey;
    }

    public int getAutomationPriority()
    {
        return automationPriority;
    }

    public void setAutomationPriority(int automationPriority)
    {
        this.automationPriority = automationPriority;
    }

    public String getAutomationTool()
    {
        return automationTool;
    }

    public void setAutomationTool(String automationTool)
    {
        this.automationTool = automationTool;
    }

    public ComponentReference getComponent()
    {
        return component;
    }

    public void setComponent(ComponentReference component)
    {
        this.component = component;
    }

    public boolean isDeleted()
    {
        return deleted;
    }

    public void setDeleted(boolean deleted)
    {
        this.deleted = deleted;
    }

    public List<DataExtension<Testcase>> getExtensions()
    {
        return extensions;
    }

    public void setExtensions(List<DataExtension<Testcase>> extensions)
    {
        this.extensions = extensions;
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

	@JsonIgnore
	public ObjectId getObjectId()
	{
		return id;
	}

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ProjectReference getProject()
    {
        return project;
    }

    public void setProject(ProjectReference project)
    {
        this.project = project;
    }

    public String getPurpose()
    {
        return purpose;
    }

    public void setPurpose(String purpose)
    {
        this.purpose = purpose;
    }

    public String getRequirements()
    {
        return requirements;
    }

    public void setRequirements(String requirements)
    {
        this.requirements = requirements;
    }

    public List<Step> getSteps()
    {
        return steps;
    }

    public void setSteps(List<Step> steps)
    {
        this.steps = steps;
    }

    public List<String> getTags()
    {
        return tags;
    }

    public void setTags(List<String> tags)
    {
        this.tags = tags;
    }

    public int getStabilityRating()
    {
        return stabilityRating;
    }

    public void setStabilityRating(int stabilityRating)
    {
        this.stabilityRating = stabilityRating;
    }

    public List<DataDrivenPropertyType> getDataDriven() {
        return dataDriven;
    }

    public void setDataDriven(List<DataDrivenPropertyType> dataDriven) {
        this.dataDriven = dataDriven;
    }

    public void validate() throws InvalidDataError
    {
        if(getName() == null || getName().equals(""))
	    throw new InvalidDataError("Release", "name", "name cannot be null, or empty.");
        if(getAutomationPriority() == 0)
            // Setting automation priority to 50 as a default
            setAutomationPriority(50);
    }

	public TestcaseReference createReference()
	{
		TestcaseReference ref = new TestcaseReference();
		ref.setActualId(id);
		ref.setName(name);
		ref.setAutomationId(automationId);
		ref.setAutomationKey(automationKey);
		ref.setAutomationTool(automationTool);
		return ref;
	}

    @Override
    public Testcase createCopy()
    {
        Testcase copy = new Testcase();

        copy.setId(id);
        copy.setAuthor(author);
        copy.setAutomated(automated);
        copy.setAutomationConfiguration(automationConfiguration);
        copy.setAutomationId(automationId);
        copy.setAutomationPriority(automationPriority);
        copy.setAutomationKey(automationKey);
        copy.setAutomationTool(automationTool);
        copy.setComponent(copyIfNotNull(component));
        copy.setDeleted(deleted);
        copy.setName(name);
        copy.setProject(copyIfNotNull(project));
        copy.setPurpose(purpose);
        copy.setRequirements(requirements);
        copy.setStabilityRating(stabilityRating);

        List<Step> copyOfSteps = new ArrayList<Step>();
        for(Step orig : steps)
            copyOfSteps.add(copyIfNotNull(orig));
        copy.setSteps(copyOfSteps);

        List<String> copyOfTags = new ArrayList<String>(tags);
        copy.setTags(copyOfTags);

        Map<String, String> copyOfAttributes = new HashMap<String, String>();
        copyOfAttributes.putAll(attributes);
        copy.setAttributes(copyOfAttributes);

        List<DataDrivenPropertyType> copyOfDataDriven = new ArrayList<DataDrivenPropertyType>();
        for(DataDrivenPropertyType orig : dataDriven)
            copyOfDataDriven.add(copyIfNotNull(orig));
        copy.setDataDriven(copyOfDataDriven);

        return copy;
    }
}
