/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.PostLoad;
import com.google.code.morphia.annotations.Property;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.tcrun.slickij.api.data.testqueries.NamedTestcaseQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author jcorbett
 */
@Entity("testplans")
public class Testplan implements Serializable
{
	@Id
	private ObjectId id;

	@Property
	private String name;

	@Property
	private String createdBy;

	@Embedded
	private ProjectReference project;

	@Property
	private List<String> sharedWith;

	@Property
	private Boolean isprivate;

	@Embedded
	private List<NamedTestcaseQuery> queries;

	@Embedded
	private List<DataExtension<Testplan>> extensions;

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

	public String getCreatedBy()
	{
		return createdBy;
	}

	public void setCreatedBy(String createdBy)
	{
		this.createdBy = createdBy;
	}

	public Boolean getIsprivate()
	{
		return isprivate;
	}

	public void setIsprivate(Boolean isprivate)
	{
		this.isprivate = isprivate;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<NamedTestcaseQuery> getQueries()
	{
		return queries;
	}

	public void setQueries(List<NamedTestcaseQuery> queries)
	{
		this.queries = queries;
	}

	public List<String> getSharedWith()
	{
		return sharedWith;
	}

	public void setSharedWith(List<String> sharedWith)
	{
		this.sharedWith = sharedWith;
	}

	public void validate() throws InvalidDataError
	{
		if(name == null || name.equals(""))
			throw new InvalidDataError("Testplan", "name", "name must not be null or empty");
		if(isprivate == null)
			isprivate = Boolean.FALSE;
		if((createdBy == null || createdBy.equals("")) &&
		   isprivate.equals(Boolean.TRUE))
			throw new InvalidDataError("Testplan", "createdBy", "createdBy cannot be null or empty when isprivate is set to true.");
		if(project == null || (project.getId() == null && project.getName() == null))
			throw new InvalidDataError("Testplan", "project", "project cannot be null, and must have either id or name properties set.");
		if(sharedWith == null)
			sharedWith = new ArrayList<String>();
		if(queries == null)
			queries = new ArrayList<NamedTestcaseQuery>();
	}

	@PostLoad
	public void postLoad()
	{
		if(sharedWith == null)
			sharedWith = new ArrayList<String>();
		if(queries == null)
			queries = new ArrayList<NamedTestcaseQuery>();
		if(extensions == null)
			extensions = new ArrayList<DataExtension<Testplan>>();
		for(DataExtension<Testplan> extension : extensions)
			extension.setParent(this);
	}

	public ProjectReference getProject()
	{
		return project;
	}

	public void setProject(ProjectReference project)
	{
		this.project = project;
	}

	public List<DataExtension<Testplan>> getExtensions()
	{
		return extensions;
	}

	public void setExtensions(List<DataExtension<Testplan>> extensions)
	{
		this.extensions = extensions;
	}
}
