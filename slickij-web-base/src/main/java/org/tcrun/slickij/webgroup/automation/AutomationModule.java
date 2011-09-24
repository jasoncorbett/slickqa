package org.tcrun.slickij.webgroup.automation;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.tcrun.slickij.webbase.ActionGroup;
import org.tcrun.slickij.webbase.UrlRegistration;

/**
 *
 * @author slambson
 */
public class AutomationModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		Multibinder<ActionGroup> agbinder = Multibinder.newSetBinder(binder(), ActionGroup.class);
		agbinder.addBinding().to(AutomationGroup.class);

		Multibinder<AutomationAction> rabinder = Multibinder.newSetBinder(binder(), AutomationAction.class);
		rabinder.addBinding().toInstance(LiveStatusReportPage.getAutomationAction());

		Multibinder<UrlRegistration> rbinder = Multibinder.newSetBinder(binder(), UrlRegistration.class);
		rbinder.addBinding().toInstance(LiveStatusReportPage.getUrlRegistration());
	}
}
