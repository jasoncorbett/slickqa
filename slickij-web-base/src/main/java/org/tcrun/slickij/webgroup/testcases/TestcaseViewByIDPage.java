package org.tcrun.slickij.webgroup.testcases;

import com.google.inject.Inject;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.Testcase;
import org.tcrun.slickij.api.data.dao.TestcaseDAO;
import org.tcrun.slickij.webbase.SimpleUrlRegistration;
import org.tcrun.slickij.webbase.SlickijSession;
import org.tcrun.slickij.webbase.UrlRegistration;
import org.tcrun.slickij.webbase.pages.AbstractDefaultPage;

/**
 *
 * @author slambson
 */
public class TestcaseViewByIDPage extends AbstractDefaultPage
{
	@Inject
	private TestcaseDAO testcaseDAO;

	private Testcase testcase;

	public TestcaseViewByIDPage(PageParameters parameters)
	{
		SlickijSession.get().setCurrentGroup("Testcases");
		testcase = null;
		if(parameters.getNamedKeys().contains("testcaseid"))
		{
			ObjectId testcaseid = new ObjectId(parameters.get("testcaseid").toString());
			testcase = testcaseDAO.get(testcaseid);
		}
                TestcaseViewPanel testcaseViewPanel = new TestcaseViewPanel("testcasepanel", testcase);
                add(testcaseViewPanel);
	}

	@Override
	public String getMessagePane1Text()
	{
		if(testcase != null)
		{
			if(testcase.getName() != null)
			{
				return "Test Case Details";
			}
		}
		return super.getMessagePane1Text();
	}

	@Override
	public String getMessagePane2Text()
	{
		if(testcase != null)
		{
			if(testcase.getName() != null)
			{
				String retval = "";
				return retval;
			}
		}
		return super.getMessagePane2Text();
	}

	public static TestcaseAction getTestcaseAction()
	{
		return new SimpleTestcaseAction(10, new PackageResourceReference(TestcaseViewByIDPage.class, "images/tcview.png"), TestcaseViewByIDPage.class, "Test Case View", null);
	}

	public static UrlRegistration getUrlRegistration()
	{
		return new SimpleUrlRegistration(TestcaseViewByIDPage.class, "/testcases/byid");
	}
}

