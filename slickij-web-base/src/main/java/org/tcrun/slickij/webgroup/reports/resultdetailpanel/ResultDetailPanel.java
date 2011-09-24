package org.tcrun.slickij.webgroup.reports.resultdetailpanel;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.tcrun.slickij.api.data.Configuration;
import org.tcrun.slickij.api.data.Result;
import org.tcrun.slickij.api.data.Testcase;
import org.tcrun.slickij.webbase.pages.AbstractDefaultPage;
import org.tcrun.slickij.webgroup.testcases.TestcaseViewPanel;

/**
 *
 * @author jcorbett
 */
public class ResultDetailPanel extends Panel
{
	private static ResourceReference STYLECSS = new PackageResourceReference(AbstractDefaultPage.class, "style.css");
	private static ResourceReference COLORSCSS = new PackageResourceReference(AbstractDefaultPage.class, "colors.css");
	private static ResourceReference RESULTDETAILCSS = new PackageResourceReference(ResultDetailPanel.class, "resultdetail.css");

	public ResultDetailPanel(String id, Result result, Testcase tc, Configuration config)
	{
		super(id);


		final FilesPanel filespanel = new FilesPanel("filespanel", result);
		filespanel.setOutputMarkupId(true);
		add(filespanel);
		final TestcaseViewPanel tcpanel = new TestcaseViewPanel("tcpanel", tc);
		tcpanel.setOutputMarkupId(true);
		tcpanel.add(new AttributeModifier("style", true, new Model<String>("display:none")));
		add(tcpanel);
		final Panel configpanel;
		if(config == null)
			configpanel = new NullConfigurationViewPanel("confpanel");
		else
			configpanel = new ConfigurationViewPanel("confpanel", result, config);
		configpanel.setOutputMarkupId(true);
		configpanel.add(new AttributeModifier("style", true, new Model<String>("display:none")));
		add(configpanel);

		add(new AjaxLink("fileslink") {

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				filespanel.add(new AttributeModifier("style", true, new Model<String>("display:block")));
				tcpanel.add(new AttributeModifier("style", true, new Model<String>("display:none")));
				configpanel.add(new AttributeModifier("style", true, new Model<String>("display:none")));
				target.add(filespanel);
				target.add(tcpanel);
				target.add(configpanel);
			}
		});

		add(new AjaxLink("tclink") {

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				filespanel.add(new AttributeModifier("style", true, new Model<String>("display:none")));
				tcpanel.add(new AttributeModifier("style", true, new Model<String>("display:block")));
				configpanel.add(new AttributeModifier("style", true, new Model<String>("display:none")));
				target.add(filespanel);
				target.add(tcpanel);
				target.add(configpanel);
			}
		});

		add(new AjaxLink("conflink") {

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				filespanel.add(new AttributeModifier("style", true, new Model<String>("display:none")));
				tcpanel.add(new AttributeModifier("style", true, new Model<String>("display:none")));
				configpanel.add(new AttributeModifier("style", true, new Model<String>("display:block")));
				target.add(filespanel);
				target.add(tcpanel);
				target.add(configpanel);
			}
		});
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);
		response.renderCSSReference(STYLECSS);
		response.renderCSSReference(COLORSCSS);
		response.renderCSSReference(RESULTDETAILCSS);
	}
}
