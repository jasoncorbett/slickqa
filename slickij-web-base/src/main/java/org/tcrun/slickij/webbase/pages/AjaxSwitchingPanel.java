package org.tcrun.slickij.webbase.pages;

import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * This panel is a container for other panels, making them hidden or visible as part of an ajax request.
 *
 * @author jcorbett
 */
public class AjaxSwitchingPanel extends Panel
{
	private List<? extends AbstractHideablePanel> panels;

	public static String CHILD_PANEL_ID = "_ajax_switching_panel_";

	public AjaxSwitchingPanel(String id, List<? extends AbstractHideablePanel> panels)
	{
		super(id);
		this.panels = panels;

		ListView panelviews = new ListView<AbstractHideablePanel>("_ajax_switching_panel_list_", panels)
		{
			@Override
			protected void populateItem(ListItem<AbstractHideablePanel> item)
			{
				item.add(item.getModelObject());
			}
		};
		add(panelviews);
	}

	public void showPanel(int panelindex, AjaxRequestTarget target)
	{
		if(panels.isEmpty())
			return;
		if(panelindex < 0)
			panelindex = 0;
		if(panelindex >= panels.size())
			panelindex = panels.size() - 1;
		for(int i = 0; i < panels.size(); i++)
		{
			if(panelindex == i)
				panels.get(i).show(target);
			else
				panels.get(i).hide(target);
		}
	}
}
