package org.tcrun.slickij.webgroup.reports;

import org.tcrun.slickij.webgroup.reports.resultlistpanel.ResultListPanel;
import com.google.inject.Inject;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.Testplan;
import org.tcrun.slickij.api.data.Testrun;
import org.tcrun.slickij.api.data.dao.ResultDAO;
import org.tcrun.slickij.api.data.dao.TestplanDAO;
import org.tcrun.slickij.api.data.dao.TestrunDAO;
import org.tcrun.slickij.webbase.SimpleUrlRegistration;
import org.tcrun.slickij.webbase.SlickijSession;
import org.tcrun.slickij.webbase.UrlRegistration;
import org.tcrun.slickij.webbase.pages.AbstractDefaultPage;

/**
 *
 * @author jcorbett
 */
public class ReportByTestrunPage extends AbstractDefaultPage
{
	@Inject
	private TestrunDAO testrunDAO;

	@Inject
	private TestplanDAO testplanDAO;

	@Inject
	private ResultDAO resultDAO;

	private Testrun run;

	public ReportByTestrunPage(PageParameters parameters)
	{
		SlickijSession.get().setCurrentGroup("Reports");
		run = null;
		BookmarkablePageLink refresh = new BookmarkablePageLink("refreshlink", ReportByTestrunPage.class, parameters);
		add(refresh);
		PageParameters individualResultsParameters = new PageParameters();
		individualResultsParameters.set("show", "individualresults");
		BookmarkablePageLink individualresults = new BookmarkablePageLink("individualresultslink", ReportByTestrunPage.class, individualResultsParameters);
		add(individualresults);
		PageParameters resultsummaryParameters = new PageParameters();
		BookmarkablePageLink resultsummary = new BookmarkablePageLink("resultsummarylink", ReportByTestrunPage.class, resultsummaryParameters);
		add(resultsummary);

		if(parameters.getNamedKeys().contains("testrunid"))
		{
			ObjectId testrunid = new ObjectId(parameters.get("testrunid").toString());
			run = testrunDAO.get(testrunid);
		}

		if(run != null)
		{
			if(parameters.getNamedKeys().contains("show") && parameters.get("show").toString().equals("individualresults"))
			{
				add(new ResultListPanel("reportpanel", resultDAO.findResultsByTestrun(run)));
				individualresults.setVisible(false);
				resultsummaryParameters.set("testrunid", run.getId());
			} else
			{
				add(new TestrunSummaryPanel("reportpanel", run, testrunDAO));
				resultsummary.setVisible(false);
				individualResultsParameters.set("testrunid", run.getId());
			}
		} else
		{
			add(new TestrunListPanel("reportpanel"));
			individualresults.setVisible(false);
			resultsummary.setVisible(false);
		}

	}

	@Override
	public String getMessagePane1Text()
	{
		if(run != null)
		{
			if(run.getConfig() != null)
			{
				return run.getConfig().getName();
			}
		}
		return super.getMessagePane1Text();
	}

	@Override
	public String getMessagePane2Text()
	{
		if(run != null)
		{
			if(run.getRelease() != null)
			{
				String retval = run.getRelease().getName();
				if(run.getBuild() != null)
				{
					retval += " Build " + run.getBuild().getName();
				}
				return retval;
			}
		}
		return super.getMessagePane2Text();
	}

	@Override
	public String getSubTitle()
	{
		if(run != null)
		{
			if(run.getTestplanId() != null)
			{
				Testplan plan = testplanDAO.get(run.getTestplanObjectId());
				if(plan != null)
					return plan.getName();
			}
			return run.getName();
		}
		return "Choose a Testrun";
	}

	public static ReportAction getReportAction()
	{
		return new SimpleReportAction(10, new PackageResourceReference(ReportByTestrunPage.class, "images/bytestrun.png"), ReportByTestrunPage.class, "Test Run Report", null);
	}

	public static UrlRegistration getUrlRegistration()
	{
		return new SimpleUrlRegistration(ReportByTestrunPage.class, "/reports/bytestrun");
	}
}
