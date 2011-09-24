package org.tcrun.slickij.webgroup.testcases;

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
public class TestcaseGroup implements ActionGroup, Serializable
{
	@Inject
	public Set<TestcaseAction> actions;

	public List<TestcaseAction> sortedActions;

	@Override
	public String getGroupName()
	{
		return "Testcases";
	}

	@Override
	public int getGroupOrder()
	{
		return 40;
	}

	@Override
	public List<? extends SlickijAction> getActions()
	{
		if(sortedActions == null)
		{
			sortedActions = new ArrayList<TestcaseAction>(actions);
			Collections.sort(sortedActions, new Comparator<TestcaseAction>()
			{

				@Override
				public int compare(TestcaseAction o1, TestcaseAction o2)
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
