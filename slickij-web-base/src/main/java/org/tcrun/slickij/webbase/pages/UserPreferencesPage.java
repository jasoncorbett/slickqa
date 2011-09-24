/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcrun.slickij.webbase.pages;

import org.tcrun.slickij.webbase.SimpleUrlRegistration;
import org.tcrun.slickij.webbase.UrlRegistration;

/**
 *
 * @author jcorbett
 */
public class UserPreferencesPage extends NotImplementedPage
{
	public static UrlRegistration getRegistration()
	{
		return new SimpleUrlRegistration(UserPreferencesPage.class, "/userprefs");
	}
}
