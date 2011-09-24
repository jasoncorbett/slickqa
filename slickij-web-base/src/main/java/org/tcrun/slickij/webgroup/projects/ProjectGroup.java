package org.tcrun.slickij.webgroup.projects;

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
public class ProjectGroup implements ActionGroup, Serializable
{
	@Inject
	public Set<ProjectAction> actions;

	public List<ProjectAction> sortedActions;

	@Override
	public String getGroupName()
	{
		return "Projects";
	}

	@Override
	public int getGroupOrder()
	{
		return 30;
	}

	@Override
	public List<? extends SlickijAction> getActions()
	{
		if(sortedActions == null)
		{
			sortedActions = new ArrayList<ProjectAction>(actions);
			Collections.sort(sortedActions, new Comparator<ProjectAction>()
			{

				@Override
				public int compare(ProjectAction o1, ProjectAction o2)
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
