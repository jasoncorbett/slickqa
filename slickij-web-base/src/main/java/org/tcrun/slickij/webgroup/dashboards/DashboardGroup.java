package org.tcrun.slickij.webgroup.dashboards;

import com.google.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.tcrun.slickij.webbase.ActionGroup;
import org.tcrun.slickij.webbase.SlickijAction;

/**
 *
 * @author jcorbett
 */
public class DashboardGroup implements ActionGroup, Serializable
{
	@Inject
	private Set<DashboardAction> actions;
	private List<DashboardAction> sortedActions;

	@Override
	public String getGroupName()
	{
		return "Dashboards";
	}

	@Override
	public int getGroupOrder()
	{
		return 20;
	}

	@Override
	public List<? extends SlickijAction> getActions()
	{
		if(sortedActions == null)
		{
			sortedActions = new ArrayList<DashboardAction>(actions);
			Collections.sort(sortedActions, new Comparator<DashboardAction>() {

				@Override
				public int compare(DashboardAction o1, DashboardAction o2)
				{
					Integer priorityOfO1 = new Integer(o1.getOrder());
					Integer priorityOfO2 = new Integer(o2.getOrder());
					return priorityOfO1.compareTo(priorityOfO2);
				}
			});
		}
		return sortedActions;
	}

	@Override
	public String getRoleRequired()
	{
		return null;
	}
}
