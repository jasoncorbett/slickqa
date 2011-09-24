package org.tcrun.slickij.webgroup.reports;

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
public class ReportGroup implements ActionGroup, Serializable
{
	@Inject
	public Set<ReportAction> actions;

	public List<ReportAction> sortedActions;

	@Override
	public String getGroupName()
	{
		return "Reports";
	}

	@Override
	public int getGroupOrder()
	{
		return 100;
	}

	@Override
	public List<? extends SlickijAction> getActions()
	{
		if(sortedActions == null)
		{
			sortedActions = new ArrayList<ReportAction>(actions);
			Collections.sort(sortedActions, new Comparator<ReportAction>()
			{

				@Override
				public int compare(ReportAction o1, ReportAction o2)
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
