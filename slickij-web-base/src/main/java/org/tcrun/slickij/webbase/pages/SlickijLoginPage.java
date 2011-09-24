package org.tcrun.slickij.webbase.pages;

import org.apache.wicket.authroles.authentication.panel.SignInPanel;
import org.tcrun.slickij.webbase.SimpleUrlRegistration;
import org.tcrun.slickij.webbase.UrlRegistration;


/**
 *
 * @author jcorbett
 */
public class SlickijLoginPage extends AbstractPageWithTitle
{
	SignInPanel signin = null;

	public SlickijLoginPage()
	{
		super();
		signin = new SignInPanel("loginpanel",false);
		signin.setRememberMe(false);
		add(signin);
	}

	public static UrlRegistration getRegistration()
	{
		return new SimpleUrlRegistration(SlickijLoginPage.class, "/login");
	}
}

