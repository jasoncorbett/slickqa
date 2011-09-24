package org.tcrun.slickij.webbase;

import org.apache.wicket.Page;

/**
 *
 * @author jcorbett
 */
public class SimpleUrlRegistration implements UrlRegistration
{
	private Class<? extends Page> pageClass;
	private String url;

	public SimpleUrlRegistration(Class<? extends Page> pageClass, String url)
	{
		this.pageClass = pageClass;
		this.url = url;
	}

	@Override
	public Class<? extends Page> getPageClass()
	{
		return pageClass;
	}

	@Override
	public String getUrl()
	{
		return url;
	}
}
