package org.tcrun.slickij.api;

import org.tcrun.slickij.api.data.ReloadStatus;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * User: jcorbett
 * Date: 12/4/12
 * Time: 9:46 AM
 */
@Path("/reload")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ReloadResource
{
    @GET
    @Path("/event-system")
    public ReloadStatus reloadEventSubSystem();
}
