package org.tcrun.slickij.webgroup.plans;

import org.apache.wicket.model.LoadableDetachableModel;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.Testplan;
import org.tcrun.slickij.api.data.dao.TestplanDAO;

/**
 *
 * @author jcorbett
 */
public class LoadableDetatchableTestplanModel extends LoadableDetachableModel<Testplan>
{
	private ObjectId tpId;
	private TestplanDAO tpDao;

	public LoadableDetatchableTestplanModel(TestplanDAO tpDAO, ObjectId tpId)
	{
		this.tpId = tpId;
		this.tpDao = tpDAO;
		setObject(load());
	}

	@Override
	protected Testplan load()
	{
		return tpDao.get(tpId);
	}
	
}
