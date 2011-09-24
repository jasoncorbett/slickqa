package org.tcrun.slickij.webgroup.plans;

import com.google.code.morphia.query.Query;
import com.google.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.Page;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigationToolbar;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tcrun.slickij.api.data.Testplan;
import org.tcrun.slickij.api.data.Testrun;
import org.tcrun.slickij.api.data.dao.TestcaseDAO;
import org.tcrun.slickij.api.data.dao.TestplanDAO;
import org.tcrun.slickij.api.data.dao.TestrunDAO;

/**
 *
 * @author lhigginson
 */
class TestplanListPanel extends Panel
{
	@Inject
	private TestplanDAO tpDAO;

	@Inject
	private TestcaseDAO tcDAO;

	public TestplanListPanel(String id, Class<? extends Page> linktopage)
	{
		super(id);
		List<IColumn<Testplan>> columns = new ArrayList<IColumn<Testplan>>();
		columns.add(new TestplanLinkDataColumn(linktopage));
		columns.add(new NumberOfTestsDataColumn(tcDAO));
		columns.add(new TestplanActionsColumn());
		TestPlanDataProvider provider = new TestPlanDataProvider(tpDAO);
		DataTable<Testplan> table = new DataTable<Testplan>("testplanlisttable", columns, provider, 15);
		table.addTopToolbar(new HeadersToolbar(table, provider));
		table.addBottomToolbar(new NavigationToolbar(table));
		add(table);
	}
}
