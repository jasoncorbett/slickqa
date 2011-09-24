package org.tcrun.slickij.webgroup.reports;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.tcrun.slickij.webbase.ActionGroup;
import org.tcrun.slickij.webbase.UrlRegistration;

/**
 *
 * @author jcorbett
 */
public class ReportsModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		Multibinder<ActionGroup> agbinder = Multibinder.newSetBinder(binder(), ActionGroup.class);
		agbinder.addBinding().to(ReportGroup.class);

		Multibinder<ReportAction> rabinder = Multibinder.newSetBinder(binder(), ReportAction.class);
		rabinder.addBinding().toInstance(ReportByTestrunPage.getReportAction());

		Multibinder<UrlRegistration> rbinder = Multibinder.newSetBinder(binder(), UrlRegistration.class);
		rbinder.addBinding().toInstance(ReportByTestrunPage.getUrlRegistration());
	}
}
