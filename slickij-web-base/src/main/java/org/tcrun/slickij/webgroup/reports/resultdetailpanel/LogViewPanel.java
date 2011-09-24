package org.tcrun.slickij.webgroup.reports.resultdetailpanel;

import com.google.inject.internal.Collections2;
import java.text.SimpleDateFormat;
import java.util.Collections;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.IResource;
import org.tcrun.slickij.webbase.pages.AbstractHideablePanel;
import java.util.List;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.tcrun.slickij.api.data.LogEntry;

/**
 *
 * @author jcorbett
 */
public class LogViewPanel extends HideableLinkablePanel
{
	public static ResourceReference LOGS_IMAGE = new PackageResourceReference(LogViewPanel.class, "logs.png");
	public static SimpleDateFormat DateFormat = new SimpleDateFormat("d MMM yyyy h:mm:ss aa Z");
	public LogViewPanel(String id, List<LogEntry> logs)
	{
		super(id);
		if(logs != null)
			Collections.reverse(logs);
		ListView<LogEntry> logsview = new ListView<LogEntry>("log-item-list", logs)
		{
			@Override
			protected ListItem<LogEntry> newItem(int index, IModel<LogEntry> itemModel)
			{
				return new ListItem<LogEntry>(index, itemModel)
				{
					@Override
					protected void onComponentTag(ComponentTag tag)
					{
						tag.put("class", "logentry loglevel-" + getModelObject().getLevel().name());
					}
				};
			}

			@Override
			protected void populateItem(ListItem<LogEntry> item)
			{
				LogEntry entry = item.getModelObject();
				item.add(new Label("log-level", entry.getLevel().name()),
				         new Label("log-time", DateFormat.format(entry.getEntryTime())),
						 new Label("log-name", entry.getLoggerName()));
				String logMessage = entry.getMessage();
				if(entry.getExceptionClassName() != null)
				{
					logMessage += "\r\n" + entry.getExceptionClassName();
					logMessage += ": " + entry.getExceptionMessage();
					for(String line : entry.getExceptionStackTrace())
					{
						logMessage += "\r\n" + line;
					}
				}
				item.add(new Label("log-message", logMessage));
			}
		};
		add(logsview);
	}

	@Override
	public IResource getLinkImage()
	{
		return LOGS_IMAGE.getResource();
	}

	@Override
	public String getLinkText()
	{
		return "Logs";
	}
}
