package org.tcrun.slickij.webgroup.plans;

import java.util.List;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tcrun.slickij.webbase.BookmarkablePageLinkWithName;

/**
 *
 * @author jcorbett
 */
public class TestplanActionsPanel extends Panel
{
	public TestplanActionsPanel(String id, List<ActionablePage> actions, String testplanId)
	{
		super(id);
		final PageParameters params = new PageParameters();
		params.add("testplanid", testplanId);
		ListView<ActionablePage> testplanactions = new ListView<ActionablePage>("testplanactionscontainer", actions)
		{
			@Override
			protected void populateItem(ListItem<ActionablePage> item)
			{
				item.add(new BookmarkablePageLinkWithName("testplanaction", item.getModelObject().getActionPage(), params, item.getModelObject().getActionLinkName()));
			}
		};
		add(testplanactions);
	}
	
}
