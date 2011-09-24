package org.tcrun.slickij.webgroup.reports.resultdetailpanel;

import org.apache.wicket.request.resource.IResource;

/**
 *
 * @author jcorbett
 */
interface LinkablePanel
{
	public IResource getLinkImage();

	public String getLinkText();
}
