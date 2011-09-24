package org.tcrun.slickij;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import java.util.Set;
import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AnnotationsRoleAuthorizationStrategy;
import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.tcrun.slickij.api.data.dao.UserAccountDAO;
import org.tcrun.slickij.webbase.pages.SlickijHomePage;
import org.tcrun.slickij.webbase.SlickijSession;
import org.tcrun.slickij.webbase.UrlRegistration;
import org.tcrun.slickij.webbase.pages.SlickijLoginPage;

/**
 *
 * @author jcorbett
 */
public class SlickijApplication extends AuthenticatedWebApplication
{
	private Injector injector;

	@Override
	public void init()
	{
		super.init();
		try
		{
			injector = Guice.createInjector(new DefaultModule());
		} catch(Exception e)
		{
			throw new RuntimeException(e);
		}
		getComponentInstantiationListeners().add(new GuiceComponentInjector(this, injector));
		getSecuritySettings().setAuthorizationStrategy(new AnnotationsRoleAuthorizationStrategy(this));
		getMarkupSettings().setStripWicketTags(true);
		Set<UrlRegistration> registrations = injector.getInstance(Key.get(new TypeLiteral<Set<UrlRegistration>>() {}));
		for(UrlRegistration registration : registrations)
		{
			mountPage(registration.getUrl(), registration.getPageClass());
		}
	}

	@Override
	public Class<? extends Page> getHomePage()
	{
		return SlickijHomePage.class;
	}

	@Override
	public Session newSession(Request req, Response resp)
	{
		SlickijSession retval = new SlickijSession(req);
		injector.injectMembers(retval);
		return retval;
	}

	@Override
	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass()
	{
		return SlickijSession.class;
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass()
	{
		return SlickijLoginPage.class;
	}

	@Override
	public RuntimeConfigurationType getConfigurationType()
	{
		return RuntimeConfigurationType.DEPLOYMENT;
	}
}
