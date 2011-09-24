package org.tcrun.slickij.webgroup.plans;

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
 * @author lhigginson
 */
public class TestplanGroup implements ActionGroup, Serializable
{
	@Inject
	public Set<TestplanAction> actions;

	public List<TestplanAction> sortedActions;

	@Override
	public String getGroupName()
	{
		return "Testplans";
	}

	@Override
	public int getGroupOrder()
	{
		return 50;
	}

	@Override
	public List<? extends SlickijAction> getActions()
	{
		if(sortedActions == null)
		{
			sortedActions = new ArrayList<TestplanAction>(actions);
			Collections.sort(sortedActions, new Comparator<TestplanAction>()
			{

				@Override
				public int compare(TestplanAction o1, TestplanAction o2)
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
