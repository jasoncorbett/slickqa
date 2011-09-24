package org.tcrun.slickij.api;

import java.util.List;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.tcrun.slickij.api.data.HostStatus;

/**
 *
 * @author jcorbett
 */
@Path("/hoststatus")
@Produces(MediaType.APPLICATION_JSON)
public interface HostStatusResource
{
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public List<HostStatus> getHostStatus(@DefaultValue("5") @QueryParam("checkincutoff") Integer lastReportWithinMinutes);

	@Path("/{hostname}")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public HostStatus getHostStatusForHost(@PathParam("hostname") String hostname);
}
