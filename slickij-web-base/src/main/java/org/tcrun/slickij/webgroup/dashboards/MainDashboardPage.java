package org.tcrun.slickij.webgroup.dashboards;

import org.apache.wicket.markup.html.basic.Label;
import org.tcrun.slickij.webbase.SimpleUrlRegistration;
import org.tcrun.slickij.webbase.UrlRegistration;
import org.tcrun.slickij.webbase.pages.AbstractDefaultPage;

/**
 *
 * @author jcorbett
 */
public class MainDashboardPage extends AbstractDefaultPage
{

	@Override
	public String getMessagePane1Text()
	{
		return "Main Dashboard";
	}

	public static DashboardAction getDashboardAction()
	{
		return new SimpleDashboardAction(10, null, MainDashboardPage.class, "Main Dashboard", null);
	}

	public static UrlRegistration getUrlRegistration()
	{
		return new SimpleUrlRegistration(MainDashboardPage.class, "/dashboards/main");
	}
}
