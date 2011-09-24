package org.tcrun.slickij.webgroup.automation;

import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import com.google.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tcrun.slickij.api.data.HostStatus;
import org.tcrun.slickij.api.data.dao.HostStatusDAO;
import org.tcrun.slickij.api.data.Result;
import org.tcrun.slickij.api.data.TestrunReference;
import org.tcrun.slickij.webgroup.reports.ReportByTestrunPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.HostStatusResource;
import org.tcrun.slickij.api.ResultResource;
import org.tcrun.slickij.api.data.LogEntry;
import org.tcrun.slickij.api.data.ResultStatus;
import org.tcrun.slickij.api.data.RunStatus;
import org.tcrun.slickij.api.data.dao.ResultDAO;

/**
 *
 * @author slambson
 */
class LiveStatusReportPanel extends Panel
{

	@Inject
	private HostStatusDAO hostStatusDao;
	@Inject
	private ResultDAO resultDao;
	@Inject
	private ResultResource resultResource;

	public LiveStatusReportPanel(String id, HostStatusResource hostStatusResource)
	{
		super(id);
		List<HostStatus> hostStatuses = hostStatusResource.getHostStatus(5);
		final SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy h:mm:ss aa Z");
		add(new Label("hostsummary", "Number of Connected Hosts: " + hostStatuses.size()));
		ListView<HostStatus> hoststatuslistview = new ListView<HostStatus>("hoststatusline", hostStatuses)
		{

			@Override
			protected void populateItem(ListItem<HostStatus> item)
			{
				HostStatus hStatus = item.getModelObject();
				item.add(new Label("hostname", hStatus.getHostname()));
				item.add(new Label("lastcheckin", dateFormat.format(hStatus.getLastCheckin())));
				String currentTestcase = "None";
				Result currentWork = hStatus.getCurrentWork();
				BookmarkablePageLink runlink = null;
				if (currentWork != null)
				{
					currentTestcase = currentWork.getTestcase().getName();
					TestrunReference run = currentWork.getTestrun();
					PageParameters params = new PageParameters();
					params.set("testrunid", run.getTestrunId());
					runlink = new BookmarkablePageLink("currentrun", ReportByTestrunPage.class, params);
					runlink.add(new Label("currentrunlinktext", run.getName()));
				} else
				{
					PageParameters params = new PageParameters();
					runlink = new BookmarkablePageLink("currentrun", LiveStatusReportPage.class, params);
					runlink.add(new Label("currentrunlinktext"));
					runlink.setVisible(false);
				}
				item.add(new Label("currenttest", currentTestcase));
				item.add(runlink);
				if(currentWork != null)
					item.add(new Label("runtime", DurationFormatUtils.formatDurationWords((new Date()).getTime() - currentWork.getRecorded().getTime(), true, true)));
				else
					item.add(new Label("runtime", ""));
				Link unassign = new Link<HostStatus>("unassignlink", new Model<HostStatus>(hStatus))
				{

					@Override
					public void onClick()
					{
						HostStatus host = hostStatusDao.get(getModelObject().getHostname());
						if (getModelObject().getCurrentWork() != null
						    && host.getCurrentWork() != null
						    && getModelObject().getCurrentWork().getId().equals(host.getCurrentWork().getId()))
						{
							host.setCurrentWork(null);
							hostStatusDao.save(host);
							Query<Result> updateQuery = resultDao.createQuery();
							updateQuery.criteria("id").equal(new ObjectId(getModelObject().getCurrentWork().getId()));
							updateQuery.criteria("runstatus").equal(RunStatus.RUNNING);
							UpdateOperations<Result> updateOps = resultDao.getDatastore().createUpdateOperations(Result.class);
							updateOps.set("runstatus", RunStatus.TO_BE_RUN);
							updateOps.set("recorded", new Date());
							updateOps.unset("hostname");
							updateOps.unset("files");
							updateOps.unset("log");
							resultDao.update(updateQuery, updateOps);
						}
						setResponsePage(getPage().getClass());
					}
				};
				if(currentWork == null)
					unassign.setVisible(false);
				item.add(unassign);

				Link cancel = new Link<HostStatus>("cancellink", new Model<HostStatus>(hStatus))
				{

					@Override
					public void onClick()
					{
						HostStatus host = hostStatusDao.get(getModelObject().getHostname());
						if (getModelObject().getCurrentWork() != null
						    && host.getCurrentWork() != null
						    && getModelObject().getCurrentWork().getId().equals(host.getCurrentWork().getId()))
						{
							resultResource.cancelResult(host.getCurrentWork().getId(), "User Request on Live Automation Status Page.");
						}
						setResponsePage(getPage().getClass());
					}
				};
				if(currentWork == null)
					cancel.setVisible(false);
				item.add(cancel);

			}
		};
		add(hoststatuslistview);
	}
}
