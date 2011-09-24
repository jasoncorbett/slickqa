/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.PostLoad;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Transient;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.bson.types.ObjectId;

/**
 *
 * @author jcorbett
 */
public class SimpleProjectDataExtension implements DataExtension<Project>, Serializable
{
	@Property
	private String id;

	@Property
	private Map<String, String> simpleData;

	@Transient
	private Project project;

	@Override
	public String getName()
	{
		return "Simple Data Extension";
	}

	@Override
	public String getSummary()
	{
		return "A Simple Data Extension that allows for key/value storage.";
	}

	@Override
	public String getId()
	{
		return id;
	}

	@Override
	public DataExtension update(DataExtension update)
	{
		if(SimpleProjectDataExtension.class.isAssignableFrom(update.getClass()))
		{
			setSimpleData(((SimpleProjectDataExtension)update).getSimpleData());
		}
		return this;
	}

	public Map<String, String> getSimpleData()
	{
		return simpleData;
	}

	public void setSimpleData(Map<String, String> newdata)
	{
		simpleData = newdata;
	}

	@Override
	public void validate() throws InvalidDataError
	{
		if(simpleData == null)
			simpleData = new HashMap<String, String>();
	}

	@PrePersist
	public void prePersist()
	{
		if(id == null)
			id = ObjectId.get().toString();
	}

	@PostLoad
	public void postLoad()
	{
		if(simpleData == null)
			simpleData = new HashMap<String, String>();
	}

	@Override
	public void setParent(Project parent)
	{
		this.project = parent;
	}

	public String getProjectName()
	{
		return project.getName();
	}
}
