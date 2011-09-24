package org.tcrun.slickij.webbase;

import org.apache.wicket.Page;

/**
 *
 * @author jcorbett
 */
public interface UrlRegistration
{
	public Class<? extends Page> getPageClass();
	public String getUrl();
}
