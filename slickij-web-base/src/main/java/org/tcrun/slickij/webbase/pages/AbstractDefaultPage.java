package org.tcrun.slickij.webbase.pages;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.tcrun.slickij.webbase.ActionGroup;
import org.tcrun.slickij.webbase.SlickijAction;
import org.tcrun.slickij.webbase.SlickijSession;

/**
 *
 * @author jcorbett
 */
public class AbstractDefaultPage extends AbstractPageWithTitle
{
	@Inject
	private Set<ActionGroup> groups;

	protected List<ActionGroup> sortedGroupList;

	public AbstractDefaultPage()
	{
		sortedGroupList = new ArrayList<ActionGroup>(groups);
		Collections.sort(sortedGroupList, new Comparator<ActionGroup>() {

			@Override
			public int compare(ActionGroup o1, ActionGroup o2)
			{
				Integer priorityOfO1 = new Integer(o1.getGroupOrder());
				Integer priorityOfO2 = new Integer(o2.getGroupOrder());
				return priorityOfO1.compareTo(priorityOfO2);
			}

		});
		SlickijSession session = SlickijSession.get();
		if(session.getCurrentGroup() == null)
			session.setCurrentGroup(sortedGroupList.get(0).getGroupName());
		final int numofgroups = sortedGroupList.size();

		ListView<ActionGroup> groupsView = new ListView<ActionGroup>("mainnavigation", sortedGroupList)
		{
			@Override
			protected void populateItem(final ListItem<ActionGroup> item)
			{
				final ActionGroup group = item.getModelObject();

				Label grouplink = new Label("grouplink", new PropertyModel<ActionGroup>(group, "groupName"))
				{
					@Override
					protected void onComponentTag(final ComponentTag tag)
					{
						super.onComponentTag(tag);

						tag.put("href", "javascript:return false;");
					}
				};
				grouplink.setMarkupId(group.getGroupName().toLowerCase().replace(" ", "") + "link");
				WebMarkupContainer li = new WebMarkupContainer("groupli")
				{
					@Override
					protected void onComponentTag(final ComponentTag tag)
					{
						String css = "";
						if(item.getIndex() == 0)
						{
							css = "toolbarleft ";
						}
						if(item.getIndex() == numofgroups - 1)
						{
							css += "toolbarright ";
						}
						if(item.getIndex() != 0 && item.getIndex() != numofgroups - 1)
						{
							css = "toolbarmiddle ";
						}

						if(SlickijSession.get().getCurrentGroup().equals(group.getGroupName()))
							css += "toolbarselected";

						tag.put("class", css);
					}
				};
				li.add(grouplink);
				item.add(li);
			}
		};
		add(groupsView);

		ListView<ActionGroup> groupPanes = new ListView<ActionGroup>("grouppane", sortedGroupList)
		{

			@Override
			protected void populateItem(ListItem<ActionGroup> item)
			{
				final ActionGroup group = item.getModelObject();
				WebMarkupContainer groupPaneDiv = new WebMarkupContainer("grouppanediv");
				groupPaneDiv.setMarkupId(group.getGroupName().toLowerCase().replace(" ", "") + "linkpane");
				ListView<SlickijAction> actions = new ListView<SlickijAction>("actions", group.getActions())
				{
					@Override
					protected void populateItem(ListItem<SlickijAction> item)
					{
						final SlickijAction action = item.getModelObject();
						Link actionLink = new Link("action")
						{
							@Override
							public void onClick()
							{
								SlickijSession.get().setCurrentGroup(group.getGroupName());
								setResponsePage(action.getPageClass());
							}

						};
						ResourceReference img = action.getActionIcon();
						if(img == null)
							img = new PackageResourceReference(AbstractDefaultPage.class, "images/unknown.png");
						actionLink.add(new Image("actionimg", img));
						actionLink.add(new Label("actionname", action.getActionName()));
						item.add(actionLink);
					}
				};
				groupPaneDiv.add(actions);
				item.add(groupPaneDiv);
			}
		};
		add(groupPanes);
	}
}
