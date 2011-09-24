/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcrun.slickij.webgroup.reports.resultdetailpanel;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.tcrun.slickij.api.data.Result;
import org.tcrun.slickij.api.data.StoredFile;
import org.tcrun.slickij.webbase.pages.AjaxSwitchingPanel;

/**
 *
 * @author jcorbett
 */
public class FilesPanel extends Panel
{
	private List<HideableLinkablePanel> files;
	protected AjaxSwitchingPanel switchingPanel;

	public FilesPanel(String id, Result result)
	{
		super(id);
		files = new ArrayList<HideableLinkablePanel>();
		files.add(new LogViewPanel(AjaxSwitchingPanel.CHILD_PANEL_ID, result.getLog()));

		for(StoredFile file : result.getFiles())
		{
			if(file.getMimetype().startsWith("image"))
				files.add(new ImageViewPanel(AjaxSwitchingPanel.CHILD_PANEL_ID, file));
			if(file.getMimetype().endsWith("html"))
				files.add(new HTMLSourceViewPanel(AjaxSwitchingPanel.CHILD_PANEL_ID, file));
		}

		switchingPanel = new AjaxSwitchingPanel("file-display-panel", files);

		ListView<HideableLinkablePanel> links = new ListView<HideableLinkablePanel>("file-button-list", files)
		{

			@Override
			protected void populateItem(ListItem<HideableLinkablePanel> item)
			{
				HideableLinkablePanel panel = item.getModelObject();
				final int panelindex = item.getIndex();
				item.add(new Image("file-button-image", panel.getLinkImage()));
				item.add(new Label("file-button-text", panel.getLinkText()));
				item.add(new AjaxEventBehavior("onclick")
				{
					@Override
					protected void onEvent(AjaxRequestTarget target)
					{
						switchingPanel.showPanel(panelindex, target);
					}
				});
			}
		};

		add(links);
		add(switchingPanel);
	}
}
