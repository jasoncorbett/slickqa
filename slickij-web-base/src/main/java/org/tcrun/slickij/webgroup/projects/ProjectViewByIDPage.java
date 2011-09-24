package org.tcrun.slickij.webgroup.projects;

import com.google.inject.Inject;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.tcrun.slickij.api.data.Project;
import org.tcrun.slickij.api.data.ReleaseReference;
import org.tcrun.slickij.api.data.dao.ProjectDAO;
import org.tcrun.slickij.webbase.SimpleUrlRegistration;
import org.tcrun.slickij.webbase.SlickijSession;
import org.tcrun.slickij.webbase.UrlRegistration;
import org.tcrun.slickij.webbase.pages.AbstractDefaultPage;

/**
 *
 * @author slambson
 */
public class ProjectViewByIDPage extends AbstractDefaultPage
{
	@Inject
	private ProjectDAO projectDAO;

	private Project project;

	public ProjectViewByIDPage()
        {
                super();
		SlickijSession.get().setCurrentGroup("Projects");
                //project = SlickijSession.get().getCurrentProject();

                // Pull from DAO to make sure that we have the latest updates to the Project
		//project = projectDAO.find().get();
		project = SlickijSession.get().getCurrentProject();
                System.out.println("Project name: " + project.getName());
                ProjectViewPanel projectViewPanel = new ProjectViewPanel("projectpanel", project);
                add(projectViewPanel);
	}

	@Override
	public String getMessagePane1Text()
	{
		if(project != null)
		{
			if(project.getName() != null)
			{
				return "Project Details";
			}
		}
		return super.getMessagePane1Text();
	}

	@Override
	public String getMessagePane2Text()
	{
		if(project != null)
		{
			if(project.getName() != null)
			{
				String retval = "";
				return retval;
			}
		}
		return super.getMessagePane2Text();
	}

	public static ProjectAction getProjectAction()
	{
		return new SimpleProjectAction(10, new PackageResourceReference(ProjectViewByIDPage.class, "ProjectViewByIDPage.png"), ProjectViewByIDPage.class, "View Project", null);
	}

	public static UrlRegistration getUrlRegistration()
	{
		return new SimpleUrlRegistration(ProjectViewByIDPage.class, "/projects/byid");
	}
}

