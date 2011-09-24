package org.tcrun.slickij.webgroup.plans;

import com.google.code.morphia.query.Query;
import com.google.inject.Inject;
import java.util.Iterator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.Testplan;
import org.tcrun.slickij.api.data.dao.TestplanDAO;
import org.tcrun.slickij.webbase.SlickijSession;

/**
 *
 * @author jcorbett
 */
public class TestPlanDataProvider implements ISortableDataProvider<Testplan>, ISortState
{
	@Inject
	private TestplanDAO tpDAO;
	private String sortProperty;
	private SortOrder sortOrder;

	public TestPlanDataProvider(TestplanDAO tpDAO)
	{
		this.tpDAO = tpDAO;
		sortProperty = "name";
		sortOrder = SortOrder.ASCENDING;
	}

	private Query<Testplan> getQuery()
	{
		Query<Testplan> query = null;
		SlickijSession session = SlickijSession.get();

		/*
		if(session.getUserAcct() != null)
		{
			query = tpDAO.getQueryOfTestplansForUser(session.getCurrentProject().getObjectId(), session.getUserAcct().getAccountName());
		} else
		{
		 * 
		 */
			query = tpDAO.getQueryOfTestplansForProject(session.getCurrentProject().getObjectId());
		/*
		}
		 * 
		 */
		if(sortOrder == SortOrder.ASCENDING)
			query.order(sortProperty);
		else
			query.order("-" + sortProperty);
		return query;
	}

	@Override
	public Iterator<? extends Testplan> iterator(int first, int count)
	{
		Query<Testplan> query = getQuery();
		query.offset(first);
		query.limit(count);
		return query.iterator();
	}

	@Override
	public int size()
	{
		return (int) tpDAO.count(getQuery());
	}

	@Override
	public IModel<Testplan> model(final Testplan object)
	{
		return new LoadableDetachableModel<Testplan>(object)
		{
			ObjectId id = null;

			@Override
			protected Testplan load()
			{
				return tpDAO.get(id);
			}

			@Override
			protected void onDetach()
			{
				id = getObject().getObjectId();
				super.onDetach();
			}
		};

	}

	@Override
	public void detach()
	{
		// do nothing
	}

	@Override
	public ISortState getSortState()
	{
		return this;
	}

	@Override
	public void setPropertySortOrder(String property, SortOrder order)
	{
		sortProperty = property;
		sortOrder = order;
	}

	@Override
	public SortOrder getPropertySortOrder(String property)
	{
		if (property == null || !property.equals(sortProperty))
			return SortOrder.NONE;
		return sortOrder;
	}

	
}
