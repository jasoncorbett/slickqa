package org.tcrun.slickij.webbase;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.tcrun.slickij.webbase.pages.SlickijLoginPage;
import org.tcrun.slickij.webbase.pages.SlickijHomePage;
import org.tcrun.slickij.webbase.pages.UserPreferencesPage;

/**
 *
 * @author jcorbett
 */
public class WebBaseModule extends AbstractModule
{

	@Override
	protected void configure()
	{
		Multibinder<UrlRegistration> mbinder = Multibinder.newSetBinder(binder(), UrlRegistration.class);
		mbinder.addBinding().toInstance(SlickijLoginPage.getRegistration());
		mbinder.addBinding().toInstance(SlickijHomePage.getRegistration());
		mbinder.addBinding().toInstance(UserPreferencesPage.getRegistration());
	}
}
