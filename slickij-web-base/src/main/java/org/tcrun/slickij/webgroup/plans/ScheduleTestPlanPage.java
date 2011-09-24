package org.tcrun.slickij.webgroup.plans;

import com.google.inject.Inject;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.dao.TestcaseDAO;
import org.tcrun.slickij.api.data.dao.TestplanDAO;
import org.tcrun.slickij.webbase.SimpleUrlRegistration;
import org.tcrun.slickij.webbase.SlickijSession;
import org.tcrun.slickij.webbase.UrlRegistration;
import org.tcrun.slickij.webbase.pages.AbstractDefaultPage;

/**
 *
 * @author lhigginson
 */
public class ScheduleTestPlanPage extends AbstractDefaultPage
{
	@Inject
	private TestcaseDAO tcDAO;

	@Inject
	private TestplanDAO testplanDAO;
	
	public ScheduleTestPlanPage(PageParameters parameters)
	{
		SlickijSession.get().setCurrentGroup("Testplans");
		if(parameters.getNamedKeys().contains("testplanid"))
		{
			add(new ScheduleTestplanPanel("schedulecontainer", new ObjectId(parameters.get("testplanid").toString())));
		} else
		{
			add(new TestplanListPanel("schedulecontainer", this.getClass()));
		}

	}

	@Override
	public String getSubTitle()
	{
		return "Schedule Testplan";
	}

	public static TestplanAction getTestplanAction()
	{
		//return new SimpleTestPlanAction(10, new PackageResourceReference(ScheduleTestPlanPage.class, "images/scheduleplan.png"), ScheduleTestPlanPage.class, "Schedule Test Plan", null);
		return new SimpleTestPlanAction(10, new PackageResourceReference(ScheduleTestPlanPage.class, "ScheduleTestPlanPage.png"), ScheduleTestPlanPage.class, "Schedule Test Plan", null);
	}

	public static UrlRegistration getUrlRegistration()
	{
		return new SimpleUrlRegistration(ScheduleTestPlanPage.class, "/plans/schedule");
	}
}
