package org.tcrun.slickij.webgroup.automation;

import com.google.inject.Inject;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.tcrun.slickij.api.HostStatusResource;
import org.tcrun.slickij.webbase.SimpleUrlRegistration;
import org.tcrun.slickij.webbase.SlickijSession;
import org.tcrun.slickij.webbase.UrlRegistration;
import org.tcrun.slickij.webbase.pages.AbstractDefaultPage;

/**
 *
 * @author slambson
 */
public class LiveStatusReportPage extends AbstractDefaultPage
{
	@Inject
        private HostStatusResource hostStatusResource;

	public LiveStatusReportPage(PageParameters parameters)
	{
		SlickijSession.get().setCurrentGroup("Automation");
		BookmarkablePageLink refresh = new BookmarkablePageLink("refreshlink", LiveStatusReportPage.class, parameters);
		add(refresh);
                add (new LiveStatusReportPanel("livestatusreportpanel", hostStatusResource));
	}

	@Override
	public String getMessagePane1Text()
	{
		return super.getMessagePane1Text();
	}

	@Override
	public String getMessagePane2Text()
	{
		return super.getMessagePane2Text();
	}

	@Override
	public String getSubTitle()
	{
		return "Choose a Testrun";
	}

	public static AutomationAction getAutomationAction()
	{
		return new SimpleAutomationAction(10, new PackageResourceReference(LiveStatusReportPage.class, "LiveStatusReportPage.png"), LiveStatusReportPage.class, "Live Automation Status", null);
	}

	public static UrlRegistration getUrlRegistration()
	{
		return new SimpleUrlRegistration(LiveStatusReportPage.class, "/automation/livestatusreport");
	}
}

