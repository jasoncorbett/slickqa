package org.tcrun.slickij.webgroup.reports;

import java.util.List;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.tcrun.slickij.api.data.TestRunSummary;
import org.tcrun.slickij.api.data.Testrun;
import org.tcrun.slickij.api.data.dao.TestrunDAO;

/**
 *
 * @author jcorbett
 */
public class TestrunSummaryPanel extends Panel
{
	public TestrunSummaryPanel(String id, Testrun run, TestrunDAO dao)
	{
		super(id);

		final TestRunSummary summary = dao.getSummary(run);
		final LoadableDetatchableTestrunSummaryModel model = new LoadableDetatchableTestrunSummaryModel(dao, summary, run.getObjectId());

		ListView<String> summarylist = new ListView<String>("summaryline", new PropertyModel<List<String>>(model, "statusListOrdered"))
		{

			@Override
			protected void populateItem(ListItem<String> item)
			{
				System.out.println("Putting in status for '" + item.getModelObject() + "'");
				item.add(new Label("summarylinetitle", item.getModelObject().replace("_", " ")));
				item.add(new Label("summarylinenumber", model.getObject().getResultsByStatus().get(item.getModelObject()).toString()));
				double percentage = (Double.parseDouble(model.getObject().getResultsByStatus().get(item.getModelObject()).toString()) / Double.parseDouble(String.valueOf(model.getObject().getTotal()))) * 100;
				item.add(new Label("summarylinepercent", String.format("%.2f%%", percentage)));
			}

		};
		add(summarylist);
		add(new Label("totalsummarylinenumber", new PropertyModel<Long>(model, "total")));
		add(new TestrunSummaryPieChartScript("chartscript", "chart", model));
	}
}
