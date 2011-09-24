package org.tcrun.slickij.webgroup.plans;

import org.apache.wicket.Page;

/**
 *
 * @author jcorbett
 */
public class SimpleActionablePage implements ActionablePage
{
	private Class<? extends Page> page;
	private String actionName;

	public SimpleActionablePage(Class<? extends Page> page, String actionName)
	{
		this.page = page;
		this.actionName = actionName;
	}

	@Override
	public Class<? extends Page> getActionPage()
	{
		return page;
	}

	@Override
	public String getActionLinkName()
	{
		return actionName;
	}
	
}
