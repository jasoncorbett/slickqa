package org.tcrun.slickij.webgroup.plans;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.tcrun.slickij.api.data.Testplan;
import org.tcrun.slickij.api.data.dao.TestcaseDAO;
import org.tcrun.slickij.api.data.testqueries.NamedTestcaseQuery;

/**
 *
 * @author jcorbett
 */
public class NumberOfTestsDataColumn implements IColumn<Testplan>
{
	private TestcaseDAO tcDAO;

	public NumberOfTestsDataColumn(TestcaseDAO tcDAO)
	{
		this.tcDAO = tcDAO;
	}

	@Override
	public Component getHeader(String componentId)
	{
		return new Label(componentId, "Number of Tests");
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
		long total = 0;
		for(NamedTestcaseQuery q : rowModel.getObject().getQueries())
		{
			total += tcDAO.countTestsFromTestcaseQuery(q.getQuery());
		}
		cellItem.add(new Label(componentId, String.valueOf(total)));
	}

	@Override
	public void detach()
	{
		// do nothing
	}
	
}
