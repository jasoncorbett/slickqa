package org.tcrun.slickij.webgroup.plans;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tcrun.slickij.api.data.Testplan;
import org.tcrun.slickij.webbase.BookmarkablePageLinkWithName;

/**
 *
 * @author jcorbett
 */
public class TestplanLinkDataColumn implements IColumn<Testplan>
{
	Class<? extends Page> target;

	public TestplanLinkDataColumn(Class<? extends Page> pageClass)
	{
		target = pageClass;
	}

	@Override
	public Component getHeader(String componentId)
	{
		return new Label(componentId, "Test Plan Name");
	}

	@Override
	public String getSortProperty()
	{
		return "name";
	}

	@Override
	public boolean isSortable()
	{
		return true;
	}

	@Override
	public void populateItem(Item<ICellPopulator<Testplan>> cellItem, String componentId, IModel<Testplan> rowModel)
	{
		PageParameters params = new PageParameters();
		params.set("testplanid", rowModel.getObject().getObjectId());
		BookmarkablePageLinkWithName<Testplan> link = new BookmarkablePageLinkWithName<Testplan>(componentId, target, params, new PropertyModel<Testplan>(rowModel, "name"));
		cellItem.add(link);
	}

	@Override
	public void detach()
	{
		// do nothing
	}
	
}
