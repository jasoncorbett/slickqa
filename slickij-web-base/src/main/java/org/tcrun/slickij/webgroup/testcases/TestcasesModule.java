package org.tcrun.slickij.webgroup.testcases;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.tcrun.slickij.webbase.ActionGroup;
import org.tcrun.slickij.webbase.UrlRegistration;

/**
 *
 * @author slambson
 */
public class TestcasesModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		Multibinder<ActionGroup> agbinder = Multibinder.newSetBinder(binder(), ActionGroup.class);
		agbinder.addBinding().to(TestcaseGroup.class);

		Multibinder<TestcaseAction> rabinder = Multibinder.newSetBinder(binder(), TestcaseAction.class);
		rabinder.addBinding().toInstance(TestcaseViewByIDPage.getTestcaseAction());

		Multibinder<UrlRegistration> rbinder = Multibinder.newSetBinder(binder(), UrlRegistration.class);
		rbinder.addBinding().toInstance(TestcaseViewByIDPage.getUrlRegistration());
	}
}
