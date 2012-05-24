package org.tcrun.slickij.data;

import com.google.code.morphia.query.Query;
import com.google.inject.Inject;
import java.util.Calendar;
import java.util.List;
import org.tcrun.slickij.api.HostStatusResource;
import org.tcrun.slickij.api.data.HostStatus;
import org.tcrun.slickij.api.data.dao.HostStatusDAO;

/**
 *
 * @author jcorbett
 */
public class HostStatusResourceImpl implements HostStatusResource
{
	private HostStatusDAO m_hoststatusDAO;

	@Inject
	public HostStatusResourceImpl(HostStatusDAO p_hoststatusDAO)
	{
		m_hoststatusDAO = p_hoststatusDAO;
	}

	@Override
	public List<HostStatus> getHostStatus(Integer lastReportWithinMinutes)
	{
		Calendar cutoff = Calendar.getInstance();
		cutoff.add(Calendar.MINUTE, (-1 * lastReportWithinMinutes));
		Query<HostStatus> query = m_hoststatusDAO.createQuery();
		query.or(query.criteria("lastCheckin").greaterThanOrEq(cutoff.getTime()),
				 query.criteria("currentWork").exists());
		return query.asList();
	}

	@Override
	public HostStatus getHostStatusForHost(String hostname)
	{
		HostStatus retval = m_hoststatusDAO.get(hostname);
		if(retval == null)
			throw new NotFoundError(HostStatus.class, hostname);
		return retval;
	}
}
