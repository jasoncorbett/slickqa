package org.tcrun.slickij.webgroup.testcases;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.OddEvenListItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.tcrun.slickij.api.data.Step;
import org.tcrun.slickij.api.data.Testcase;
import org.apache.wicket.model.Model;


/**
 *
 * @author slambson
 */
public class TestcaseViewPanel extends Panel
{
        List<Step> m_testcaseSteps = new ArrayList<Step>();
        List<String> m_testTags = new ArrayList<String>();
        Testcase m_testcase;
        
	public TestcaseViewPanel(String id, Testcase testcase)
	{
		super(id);
                String testcaseName = "No Test Case selected";
                String isAutomated = "Not Defined";
                String automationTool = "Not Defined";
                String automationId = "Not Defined";
                m_testcase = testcase;

                if (testcase != null)
                {
                    testcaseName = m_testcase.getName();
                    m_testcaseSteps = m_testcase.getSteps();
                    if (m_testcase.isAutomated() == true)
                        isAutomated = "Yes";
                    else
                        isAutomated = "No";
                    if (m_testcase.getAutomationTool() != null)
                        automationTool = testcase.getAutomationTool();
                    if (m_testcase.getAutomationId() != null)
                        automationId = testcase.getAutomationId();
                    m_testTags = m_testcase.getTags();
                }

                add(new Label("testcasename", testcaseName) ) ;
                add(new Label("purposelabel", "Purpose:"));
                add(new Label("testcasepurpose", m_testcase.getPurpose()) {
                        @Override
                        public boolean isVisible()
                        {
                                if (m_testcase.getPurpose() == null)
                                    return false;
                                else
                                    return true;
                        }
                });
                final WebMarkupContainer stepsInfoContainer = new WebMarkupContainer("stepsinfo");
                stepsInfoContainer.setOutputMarkupId(true);
                AjaxLink stepsInfoLink = new AjaxLink("stepsinfolink")
                {
                        boolean toggle = true;
                        @Override
                        public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                            toggle = !toggle;
                            if(toggle)
                             stepsInfoContainer.add(new AttributeModifier("style",true,new Model("display:block")));
                            else
                             stepsInfoContainer.add(new AttributeModifier("style",true,new Model("display:none")));
                             ajaxRequestTarget.addComponent(stepsInfoContainer);
                         }
                };
                stepsInfoLink.add(new Label("stepsinfolinklabel", "Steps:"));
                add(stepsInfoLink);

            ListView<Step> stepList = new ListView<Step>("steplist", m_testcaseSteps) {

				@Override
				protected ListItem<Step> newItem(int index, IModel<Step> itemModel)
				{
					return new OddEvenListItem<Step>(index, itemModel);
				}

				@Override
				protected void populateItem(ListItem<Step> item)
				{
					final Step step = item.getModelObject();
					// Index plus 1 since it is zero based
					int stepNumber = m_testcaseSteps.indexOf(step) + 1;

					item.add(new Label("stepnamelabel", "Step " + stepNumber + ": "));
					item.add(new Label("stepname", step.getName()));
					item.add(new Label("stepresult", step.getExpectedResult()));
				}
		};
		stepsInfoContainer.add(stepList);
                add(stepsInfoContainer);

                final WebMarkupContainer automationInfoContainer = new WebMarkupContainer("automationinfo");
                automationInfoContainer.setOutputMarkupId(true);
                automationInfoContainer.add(new AttributeModifier("style",true,new Model("display:none")));
                AjaxLink automationInfoLink = new AjaxLink("automationinfolink")
                {
                        boolean toggle = false;
                        @Override
                        public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                            toggle = !toggle;
                            if(toggle)
                             automationInfoContainer.add(new AttributeModifier("style",true,new Model("display:block")));
                            else
                             automationInfoContainer.add(new AttributeModifier("style",true,new Model("display:none")));
                             ajaxRequestTarget.addComponent(automationInfoContainer);
                         }
                };
                automationInfoLink.add(new Label("automationinfolinklabel", "Automation Information:"));
                add(automationInfoLink);
                Label isAutomatedLabel = new Label("isautomated", isAutomated);
                automationInfoContainer.add(isAutomatedLabel);
                automationInfoContainer.add(new Label("automationtool", automationTool));
                automationInfoContainer.add(new Label("automationid", automationId));
                add(automationInfoContainer);

                final WebMarkupContainer additionalInfoContainer = new WebMarkupContainer("additionalinfo");
                additionalInfoContainer.setOutputMarkupId(true);
                additionalInfoContainer.add(new AttributeModifier("style",true,new Model("display:none")));
                AjaxLink additionalInfoLink = new AjaxLink("additionalinfolink")
                {
                        boolean toggle = true;
                        @Override
                        public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                            toggle = !toggle;
                            if(toggle)
                             additionalInfoContainer.add(new AttributeModifier("style",true,new Model("display:block")));
                            else
                             additionalInfoContainer.add(new AttributeModifier("style",true,new Model("display:none")));
                             ajaxRequestTarget.addComponent(additionalInfoContainer);
                         }
                };
                additionalInfoLink.add(new Label("additionalinfolinklabel", "Additional Information:"));
                add(additionalInfoLink);
                additionalInfoContainer.add(new Label("stabilityrating", Integer.toString(m_testcase.getStabilityRating())));
                ListView<String> tagsList = new ListView<String>("tagsList", m_testTags) {

			@Override
			protected void populateItem(ListItem<String> item)
			{
				final String tag = item.getModelObject();
				String comma = ",";
				if(m_testTags.indexOf(tag) == (m_testTags.size() - 1))
					comma = "";
                item.add(new Label("testTag", tag + comma));
			}
		};
		additionalInfoContainer.add(tagsList);
                add(additionalInfoContainer);
	}
}

