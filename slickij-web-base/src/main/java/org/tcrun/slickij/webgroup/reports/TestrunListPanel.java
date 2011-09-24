package org.tcrun.slickij.webgroup.reports;

import com.google.code.morphia.query.Query;
import com.google.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tcrun.slickij.api.data.Testplan;
import org.tcrun.slickij.api.data.Testrun;
import org.tcrun.slickij.api.data.dao.TestplanDAO;
import org.tcrun.slickij.api.data.dao.TestrunDAO;
import org.tcrun.slickij.webbase.SlickijSession;

/**
 *
 * @author jcorbett
 */
class TestrunListPanel extends Panel
{
	@Inject
	TestrunDAO testrunDAO;

	@Inject
	TestplanDAO testplanDAO;

	public TestrunListPanel(String id)
	{
		super(id);
		SlickijSession session = SlickijSession.get();
		final SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy h:mm:ss aa Z");
		LoadableDetachableModel<List<Testrun>> testrunListModel = new LoadableDetachableModel<List<Testrun>>()
		{
			@Override
			protected List<Testrun> load()
			{
				Query<Testrun> query = testrunDAO.createQuery();
				query.or(query.criteria("project.id").equal(SlickijSession.get().getCurrentProject().getObjectId()), query.criteria("project.id").equal(null));
				return query.order("-dateCreated").asList();
			}
		};

		ListView<Testrun> testrunlistview = new ListView<Testrun>("testrunreportline", testrunListModel)
		{
			@Override
			protected void populateItem(ListItem<Testrun> item)
			{
				Testrun run = item.getModelObject();
				PageParameters params = new PageParameters();
				params.set("testrunid", run.getId());
				BookmarkablePageLink link = new BookmarkablePageLink("testrunreportlink", ReportByTestrunPage.class, params);
				link.add(new Label("testrunreportlinktext", run.getName()));
				item.add(link);
				String testplanname = "None Specified";
				if(run.getTestplanObjectId() != null)
				{
					Testplan plan = testplanDAO.get(run.getTestplanObjectId());
					if(plan != null)
					{
						testplanname = plan.getName();
					}
				}
				item.add(new Label("testrunplan", testplanname));
				String testrunconfigname = "None Specified";
				if(run.getConfig() != null)
					testrunconfigname = run.getConfig().getName();
				item.add(new Label("testrunconfig", testrunconfigname));
				String buildname = "None Specified";
				if(run.getRelease() != null)
				{
					buildname = run.getRelease().getName();
					if(run.getBuild() != null)
						buildname += " Build " + run.getBuild().getName();
				}
				item.add(new Label("testrunbuild", buildname));
				String started = " ";
				if(run.getDateCreated() != null)
					started = dateFormat.format(run.getDateCreated());
				item.add(new Label("testrunstarted", started));
			}
		};
		add(testrunlistview);
	}
}
