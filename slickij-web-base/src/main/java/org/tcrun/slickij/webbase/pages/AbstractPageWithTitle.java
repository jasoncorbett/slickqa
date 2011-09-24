package org.tcrun.slickij.webbase.pages;

import java.util.ArrayList;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.tcrun.slickij.api.data.Project;
import org.tcrun.slickij.webbase.SlickijSession;

/**
 *
 * @author jcorbett
 */
public class AbstractPageWithTitle extends WebPage
{
	protected Label subtitle;
	protected Component messagePane1;
	protected Component messagePane2;

	protected Panel useractions;


	public AbstractPageWithTitle()
	{
		SlickijSession session = SlickijSession.get();
		subtitle = new Label("subtitle", new PropertyModel(this, "getSubTitle"));
		add(subtitle);
		messagePane1 = getMessagePane1("messagepane1");
		add(messagePane1);
		messagePane2 = getMessagePane2("messagepane2");
		add(messagePane2);

		useractions = new UserActionsPanel("useractions");
		add(useractions);

		DropDownChoice<Project> projectchoice = new DropDownChoice<Project>("projectchoice", new PropertyModel<Project>(this, "currentProject"), session.getListOfProjects(), new ProjectChoiceRenderer())
		{

			@Override
			protected void onSelectionChanged(Project newSelection)
			{
				super.onSelectionChanged(newSelection);
				setResponsePage(getPage().getClass());
			}

			@Override
			protected boolean wantOnSelectionChangedNotifications()
			{
				return true;
			}

		};

		add(projectchoice);
	}

	public String getSubTitle()
	{
		return " ";
	}

	public Component getMessagePane1(String id)
	{
		String text = getMessagePane1Text();
		if(text != null && !text.equals(""))
			return new Label(id, new PropertyModel(this, "messagePane1Text"));
		else
		{
			Label retval = new Label(id, "");
			//retval.setVisible(false);
			return retval;
		}
	}

	public String getMessagePane1Text()
	{
		return " ";
	}

	public Component getMessagePane2(String id)
	{
		String text = getMessagePane2Text();
		if(text != null && !text.equals(""))
			return new Label(id, new PropertyModel(this, "messagePane2Text"));
		else
		{
			Label retval = new Label(id, "");
			//retval.setVisible(false);
			return retval;
		}
	}

	public String getMessagePane2Text()
	{
		return " ";
	}

	public Project getCurrentProject()
	{
		return SlickijSession.get().getCurrentProject();
	}

	public void setCurrentProject(Project p)
	{
		SlickijSession.get().setCurrentProject(p);
	}
}
