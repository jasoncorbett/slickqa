package org.tcrun.slickij.api;

import org.tcrun.slickij.api.data.SlickUpdate;
import org.tcrun.slickij.api.data.UpdateRecord;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * This resource will make available all the updates for Slick.
 * User: jcorbett
 * Date: 6/24/12
 * Time: 5:08 PM
 */
@Path("/updates")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface UpdateResource
{
    @GET
    public List<SlickUpdate> getAvailableUpdates();

    @PUT
    public List<SlickUpdate> performNeededUpdates();

    @Path("/records")
    @GET
    public List<UpdateRecord> getUpdateRecords();
}
