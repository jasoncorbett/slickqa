package org.tcrun.slickij.webgroup.reports.resultlistpanel;

import com.google.inject.Inject;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.tcrun.slickij.api.data.Configuration;
import org.tcrun.slickij.api.data.Result;
import org.tcrun.slickij.api.data.Testcase;
import org.tcrun.slickij.api.data.dao.ConfigurationDAO;
import org.tcrun.slickij.api.data.dao.TestcaseDAO;
import org.tcrun.slickij.webgroup.reports.resultdetailpanel.ResultDetailPanel;

/**
 *
 * @author jcorbett
 */
public class ModalResultViewDialog extends Panel
{
	Component dialogView;

	@Inject
	private TestcaseDAO tcdao;

	@Inject
	private ConfigurationDAO configdao;

	public ModalResultViewDialog(String id)
	{
		super(id);
		dialogView = new Label("result-view-dialog-content", "No content yet.");
		this.add(dialogView);
		this.setOutputMarkupId(true);
	}

	public void ShowModalFor(Result result, AjaxRequestTarget target)
	{
		this.remove(dialogView);
		Testcase tc = tcdao.findTestcaseByReference(result.getTestcase());
		Configuration config = configdao.findConfigurationByReference(result.getConfig());
		dialogView = new ResultDetailPanel("result-view-dialog-content", result, tc, config);
		this.add(dialogView);
		target.appendJavaScript("$('#result-view-dialog').dialog({title: 'Result Detail', modal: true, width: Math.round($(window).width() * .90), height: Math.round($(window).height() * .90), show: 'slide'})");
		target.add(this);
	}

	@Override
	protected void onComponentTag(ComponentTag tag)
	{
		super.onComponentTag(tag);
		tag.put("style", "display:none");
	}

	@Override
	public String getMarkupId()
	{
		return "result-view-dialog";
	}

}
