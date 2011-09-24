package org.tcrun.slickij.webgroup.reports.resultlistpanel;

import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.tcrun.slickij.api.data.Result;

/**
 *
 * @author jcorbett
 */
public class ResultListPanel extends Panel
{
	public ResultListPanel(String id, List<Result> results)
	{
		super(id);
		/*
		final ModalResultViewWindow modalResultView = new ModalResultViewWindow("modal-result-view");
		modalResultView.setMarkupId("modal-result-view");
		add(modalResultView);
		 */
		final ModalResultViewDialog modalResultView = new ModalResultViewDialog("modal-result-view");
		add(modalResultView);
		ListView<Result> resultList = new ListView<Result>("resultitem", results) {

			@Override
			protected void populateItem(ListItem<Result> item)
			{
				final Result result = item.getModelObject();
				WebMarkupContainer resultContainer = new WebMarkupContainer("resultcontainer");
				AjaxLink testNameLink = new AjaxLink("testnamelink")
				{

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						modalResultView.ShowModalFor(result, target);
					}
				};
				testNameLink.add(new Label("testname", result.getTestcase().getName()));
				resultContainer.add(testNameLink);
				resultContainer.add(new Label("resultstatus", result.getStatus().toString().replace("_", " "))
				{
					@Override
					protected void onComponentTag(ComponentTag tag)
					{
						super.onComponentTag(tag);
						tag.put("class", "resultstatusname result-status-" + result.getStatus().toString().replace("_", ""));
					}

				});
				resultContainer.add(new Image("resultstatusimg",new PackageResourceReference(ResultListPanel.class, "images/" + result.getStatus().toString() + ".png")));
				resultContainer.setMarkupId(result.getId());
				item.add(resultContainer);
			}
		};
		add(resultList);
	}
}
