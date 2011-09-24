package org.tcrun.slickij.data.dao;

import org.tcrun.slickij.api.data.dao.HostStatusDAO;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.dao.BasicDAO;
import com.google.inject.Inject;
import org.tcrun.slickij.api.data.HostStatus;

/**
 *
 * @author jcorbett
 */
public class HostStatusDAOImpl extends BasicDAO<HostStatus, String> implements HostStatusDAO
{
	@Inject
	public HostStatusDAOImpl(Datastore ds)
	{
		super(HostStatus.class, ds);
	}
}
