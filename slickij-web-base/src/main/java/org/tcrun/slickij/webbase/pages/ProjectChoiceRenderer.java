package org.tcrun.slickij.webbase.pages;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.tcrun.slickij.api.data.Project;

/**
 *
 * @author jcorbett
 */
public class ProjectChoiceRenderer implements IChoiceRenderer<Project>
{

	@Override
	public Object getDisplayValue(Project object)
	{
		return object.getName();
	}

	@Override
	public String getIdValue(Project object, int index)
	{
		return object.getId();
	}
}
