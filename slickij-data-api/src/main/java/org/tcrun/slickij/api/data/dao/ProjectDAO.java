package org.tcrun.slickij.api.data.dao;

import com.google.code.morphia.dao.DAO;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.Project;
import org.tcrun.slickij.api.data.ProjectReference;

/**
 *
 * @author jcorbett
 */
public interface ProjectDAO extends DAO<Project, ObjectId>
{
	//Project findById(String apikey);

	Project findByName(String name);

	Project findByReference(ProjectReference ref);
}
