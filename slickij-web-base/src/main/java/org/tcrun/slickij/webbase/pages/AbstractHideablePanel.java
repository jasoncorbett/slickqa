/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcrun.slickij.webbase.pages;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 * @author jcorbett
 */
public abstract class AbstractHideablePanel extends Panel
{
	public AbstractHideablePanel(String id)
	{
		super(id);
		this.setOutputMarkupId(true);
	}

	public void show(AjaxRequestTarget target)
	{
		target.appendJavaScript("$('#" + this.getMarkupId() + "').show();");
	}

	public void hide(AjaxRequestTarget target)
	{
		target.appendJavaScript("$('#" + this.getMarkupId() + "').hide();");
	}
}
