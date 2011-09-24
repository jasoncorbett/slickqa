package org.tcrun.slickij.webbase;

import org.apache.wicket.Page;
import org.apache.wicket.request.resource.ResourceReference;

/**
 *
 * @author jcorbett
 */
public interface SlickijAction
{
	public ResourceReference getActionIcon();

	public Class<? extends Page> getPageClass();

	public String getActionName();

	public String getRoleRequired();
}
