package org.tcrun.slickij.webgroup.dashboards;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.tcrun.slickij.webbase.ActionGroup;
import org.tcrun.slickij.webbase.UrlRegistration;

/**
 *
 * @author jcorbett
 */
public class DashboardsModule extends AbstractModule
{

	@Override
	protected void configure()
	{
		Multibinder<DashboardAction> rabinder = Multibinder.newSetBinder(binder(), DashboardAction.class);
		rabinder.addBinding().toInstance(MainDashboardPage.getDashboardAction());
		Multibinder<ActionGroup> agbinder = Multibinder.newSetBinder(binder(), ActionGroup.class);
		agbinder.addBinding().to(DashboardGroup.class);
		Multibinder<UrlRegistration> rbinder = Multibinder.newSetBinder(binder(), UrlRegistration.class);
		rbinder.addBinding().toInstance(MainDashboardPage.getUrlRegistration());
	}
}
