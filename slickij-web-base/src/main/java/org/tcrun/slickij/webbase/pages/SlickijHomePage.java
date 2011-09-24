package org.tcrun.slickij.webbase.pages;

import org.tcrun.slickij.webbase.SimpleUrlRegistration;
import org.tcrun.slickij.webbase.UrlRegistration;

/**
 *
 * @author jcorbett
 */
public class SlickijHomePage extends NotImplementedPage
{
	public SlickijHomePage()
	{
		super();
		setResponsePage(sortedGroupList.get(0).getActions().get(0).getPageClass());
	}
	public static UrlRegistration getRegistration()
	{
		return new SimpleUrlRegistration(SlickijHomePage.class, "/home");
	}
}
