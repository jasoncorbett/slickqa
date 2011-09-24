package org.tcrun.slickij.webgroup.plans;

import java.io.Serializable;
import org.apache.wicket.Page;

/**
 *
 * @author jcorbett
 */
public interface ActionablePage extends Serializable
{
	public Class<? extends Page> getActionPage();
	public String getActionLinkName();
}
