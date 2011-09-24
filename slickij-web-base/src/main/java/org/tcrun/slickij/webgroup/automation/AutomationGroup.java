package org.tcrun.slickij.webgroup.automation;

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
 * @author slambson
 */
public class AutomationGroup implements ActionGroup, Serializable
{
	@Inject
	public Set<AutomationAction> actions;

	public List<AutomationAction> sortedActions;

	@Override
	public String getGroupName()
	{
		return "Automation";
	}

	@Override
	public int getGroupOrder()
	{
		return 60;
	}

	@Override
	public List<? extends SlickijAction> getActions()
	{
		if(sortedActions == null)
		{
			sortedActions = new ArrayList<AutomationAction>(actions);
			Collections.sort(sortedActions, new Comparator<AutomationAction>()
			{

				@Override
				public int compare(AutomationAction o1, AutomationAction o2)
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
		// no role required
		return null;
	}
}
