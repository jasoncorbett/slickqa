package org.tcrun.slickij.webgroup.projects;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.tcrun.slickij.webbase.ActionGroup;
import org.tcrun.slickij.webbase.UrlRegistration;
import org.tcrun.slickij.webgroup.projects.ProjectViewByIDPage;

/**
 *
 * @author slambson
 */
public class ProjectsModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		Multibinder<ActionGroup> agbinder = Multibinder.newSetBinder(binder(), ActionGroup.class);
		agbinder.addBinding().to(ProjectGroup.class);

		Multibinder<ProjectAction> rabinder = Multibinder.newSetBinder(binder(), ProjectAction.class);
		rabinder.addBinding().toInstance(ProjectViewByIDPage.getProjectAction());

		Multibinder<UrlRegistration> rbinder = Multibinder.newSetBinder(binder(), UrlRegistration.class);
		rbinder.addBinding().toInstance(ProjectViewByIDPage.getUrlRegistration());
	}
}
