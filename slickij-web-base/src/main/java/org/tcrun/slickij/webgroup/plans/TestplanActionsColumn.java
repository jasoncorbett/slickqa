package org.tcrun.slickij.webgroup.plans;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.tcrun.slickij.api.data.Testplan;

/**
 *
 * @author jcorbett
 */
public class TestplanActionsColumn implements IColumn<Testplan>
{
	private List<ActionablePage> actions;
	
	public TestplanActionsColumn()
	{
		actions = new ArrayList<ActionablePage>();
		actions.add(new SimpleActionablePage(ViewTestPlansPage.class, "View"));
		actions.add(new SimpleActionablePage(ScheduleTestPlanPage.class, "Schedule"));
	}

	public TestplanActionsColumn(List<ActionablePage> actions)
	{
		this.actions = actions;
	}

	@Override
	public Component getHeader(String componentId)
	{
		return new Label(componentId, "Actions");
	}

	@Override
	public String getSortProperty()
	{
		return null;
	}

	@Override
	public boolean isSortable()
	{
		return false;
	}

	@Override
	public void populateItem(Item<ICellPopulator<Testplan>> cellItem, String componentId, IModel<Testplan> rowModel)
	{
		cellItem.add(new TestplanActionsPanel(componentId, actions, rowModel.getObject().getId()));
	}

	@Override
	public void detach()
	{
		// do nothing
	}
	
}
