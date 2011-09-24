package org.tcrun.slickij.webgroup.reports.resultdetailpanel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.OddEvenListItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.tcrun.slickij.api.data.Configuration;
import org.tcrun.slickij.api.data.ConfigurationOverride;
import org.tcrun.slickij.api.data.Result;

/**
 *
 * @author jcorbett
 */
public class ConfigurationViewPanel extends Panel
{
	public ConfigurationViewPanel(String id, Result result, Configuration config)
	{
		super(id);
		add(new Label("config-name", config.getName()));
		add(new Label("config-filename", config.getFilename()));
		add(new Label("config-type", config.getConfigurationType()));

		ListView overrides = new ListView<ConfigurationOverride>("config-override-item-list", result.getConfigurationOverride())
		{
			@Override
			protected ListItem<ConfigurationOverride> newItem(int index, IModel<ConfigurationOverride> itemModel)
			{
				return new OddEvenListItem<ConfigurationOverride>(index, itemModel);
			}

			@Override
			protected void populateItem(ListItem<ConfigurationOverride> item)
			{
				ConfigurationOverride override = item.getModelObject();
				item.add(new Label("config-override-item-name", override.getKey()));
				item.add(new Label("config-override-item-value", override.getValue()));
			}
		};
		if(result.getConfigurationOverride() == null || result.getConfigurationOverride().size() == 0)
		{
			overrides.setVisible(false);
		}
		add(overrides);

		List<Entry<String, String>> configItems = new ArrayList<Entry<String, String>>(config.getConfigurationData().entrySet());
		Collections.sort(configItems, new Comparator<Entry<String, String>>() {

			@Override
			public int compare(Entry<String, String> o1, Entry<String, String> o2)
			{
				return o1.getKey().compareTo(o2.getKey());
			}

		});

		add(new ListView<Entry<String, String>>("config-item-list", configItems)
		{
			@Override
			protected ListItem<Entry<String, String>> newItem(int index, IModel<Entry<String, String>> itemModel)
			{
				return new OddEvenListItem<Entry<String, String>>(index, itemModel);
			}

			@Override
			protected void populateItem(ListItem<Entry<String, String>> item)
			{
				Entry<String, String> configEntry = item.getModelObject();
				item.add(new Label("config-item-name", configEntry.getKey()),
						 new Label("config-item-value", configEntry.getValue()));
			}
		});

	}
}
