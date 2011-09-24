package org.tcrun.slickij.webgroup.projects;

import java.io.Serializable;
import org.apache.wicket.Page;
import org.apache.wicket.request.resource.ResourceReference;

/**
 *
 * @author slambson
 */
public class SimpleProjectAction implements ProjectAction, Serializable
{
	private int order;
	private ResourceReference actionIcon;
	private Class<? extends Page> pageClass;
	private String actionName;
	private String roleRequired;

	public SimpleProjectAction(int order, ResourceReference actionIcon, Class<? extends Page> pageClass, String actionName, String roleRequired)
	{
		this.order = order;
		this.actionIcon = actionIcon;
		this.pageClass = pageClass;
		this.actionName = actionName;
		this.roleRequired = roleRequired;
	}

	@Override
	public int getOrder()
	{
		return order;
	}

	@Override
	public ResourceReference getActionIcon()
	{
		return actionIcon;
	}

	@Override
	public Class<? extends Page> getPageClass()
	{
		return pageClass;
	}

	@Override
	public String getActionName()
	{
		return actionName;
	}

	@Override
	public String getRoleRequired()
	{
		return roleRequired;
	}
}
