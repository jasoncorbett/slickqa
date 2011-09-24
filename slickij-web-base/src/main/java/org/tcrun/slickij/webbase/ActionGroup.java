package org.tcrun.slickij.webbase;

import java.util.List;

/**
 *
 * @author jcorbett
 */
public interface ActionGroup
{
	public String getGroupName();

	public int getGroupOrder();

	public List<? extends SlickijAction> getActions();

	public String getRoleRequired();
}
