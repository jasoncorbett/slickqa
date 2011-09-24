package org.tcrun.slickij.webbase.pages;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.tcrun.slickij.webbase.SlickijSession;

/**
 *
 * @author jcorbett
 */
public class UserActionsPanel extends Panel
{
	public UserActionsPanel(String id)
	{
		super(id);
		add(new Link<String>("preferences")
		{
			@Override
			public void onClick()
			{
				setResponsePage(UserPreferencesPage.class);
			}
		});

		add(new Link<String>("logout")
		{
			@Override
			public void onClick()
			{
				SlickijSession.get().invalidate();
				setResponsePage(getApplication().getHomePage());
			}

			@Override
			public boolean isVisible()
			{
				return SlickijSession.get().isAuthenticated();
			}
		});

		add(new Link<String>("login")
		{
			@Override
			public void onClick()
			{
				setResponsePage(SlickijLoginPage.class);
			}

			@Override
			public boolean isVisible()
			{
				return !SlickijSession.get().isAuthenticated();
			}
		});
	}

}
