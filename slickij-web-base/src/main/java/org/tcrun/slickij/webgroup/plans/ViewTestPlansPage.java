package org.tcrun.slickij.webgroup.plans;

import com.google.inject.Inject;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.tcrun.slickij.api.data.dao.TestplanDAO;
import org.tcrun.slickij.webbase.SimpleUrlRegistration;
import org.tcrun.slickij.webbase.SlickijSession;
import org.tcrun.slickij.webbase.UrlRegistration;
import org.tcrun.slickij.webbase.pages.AbstractDefaultPage;

/**
 *
 * @author jjones
 */
public class ViewTestPlansPage extends AbstractDefaultPage
{
	@Inject
	private TestplanDAO testplanDAO;

	public ViewTestPlansPage(PageParameters parameters)
	{
		SlickijSession.get().setCurrentGroup("Testplans");
		add(new TestplanListPanel("viewtestplan", this.getClass()));
	}

	@Override
	public String getSubTitle()
	{
		return "View Testplan";
	}

	public static TestplanAction getTestplanAction()
	{
		//return new SimpleTestPlanAction(10, new PackageResourceReference(ScheduleTestPlanPage.class, "images/scheduleplan.png"), ScheduleTestPlanPage.class, "Schedule Test Plan", null);
		return new SimpleTestPlanAction(10, new PackageResourceReference(ViewTestPlansPage.class, "ViewTestPlansPage.png"), ViewTestPlansPage.class, "View Test Plan", null);
	}

	public static UrlRegistration getUrlRegistration()
	{
		return new SimpleUrlRegistration(ViewTestPlansPage.class, "/plans/view");
	}
}
