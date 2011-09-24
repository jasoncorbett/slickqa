package org.tcrun.slickij.webgroup.projects;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.OddEvenListItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.tcrun.slickij.api.data.Build;
import org.tcrun.slickij.api.data.Component;
import org.tcrun.slickij.api.data.Project;
import org.tcrun.slickij.api.data.Release;


/**
 *
 * @author slambson
 */
public class ProjectViewPanel extends Panel
{
        List<Component> m_components = new ArrayList<Component>();
        List<String> m_projectTags = new ArrayList<String>();
        Project m_project;
        final SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy h:mm:ss aa Z");
        
	public ProjectViewPanel(String id, Project project)
	{
		super(id);
         
                m_project = project;

                add(new Label("projectname", m_project.getName()) ) ;
                add(new Label("descriptionlabel", "Description:"));
                add(new Label("projectdescription", m_project.getName()) ) ;
                String defaultReleaseName = "not set";
                String defaultBuildName = "not set";
                Release defaultRelease = m_project.findRelease(m_project.getDefaultRelease());
                if (defaultRelease != null)
                {
                    defaultReleaseName = defaultRelease.getName();
                    Build defaultBuild = m_project.findBuild(m_project.getDefaultRelease(), defaultRelease.getDefaultBuild());
                    if (defaultBuild != null)
                        defaultBuildName = defaultBuild.getName();
                }

                add(new Label("defaultrelease", defaultReleaseName) ) ;
                add(new Label("defaultbuild", defaultBuildName));
                add(new Label("lastupdated", dateFormat.format(m_project.getLastUpdated())));

                    // components
                m_components = m_project.getComponents();
                final WebMarkupContainer componentsInfoContainer = new WebMarkupContainer("componentsinfo");
                componentsInfoContainer.setOutputMarkupId(true);
                AjaxLink componentsInfoLink = new AjaxLink("componentsinfolink")
                {
                        boolean toggle = false;
                        @Override
                        public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                            toggle = !toggle;
                            if(toggle)
                             componentsInfoContainer.add(new AttributeModifier("style",true,new Model("display:block")));
                            else
                             componentsInfoContainer.add(new AttributeModifier("style",true,new Model("display:none")));
                             ajaxRequestTarget.addComponent(componentsInfoContainer);
                         }
                };
                componentsInfoLink.add(new Label("componentsinfolinklabel", "Components:"));
                add(componentsInfoLink);

                ListView<Component> componentsList = new ListView<Component>("componentslist", m_components) {

				@Override
				protected ListItem<Component> newItem(int index, IModel<Component> itemModel)
				{
					return new OddEvenListItem<Component>(index, itemModel);
				}

				@Override
				protected void populateItem(ListItem<Component> item)
				{
					final Component component = item.getModelObject();

					item.add(new Label("componentname", component.getName()));
					item.add(new Label("componentdescription", component.getDescription()));
					item.add(new Label("componentcode", component.getCode()));
				}
		};
		componentsInfoContainer.add(componentsList);
                add(componentsInfoContainer);
	}
}

