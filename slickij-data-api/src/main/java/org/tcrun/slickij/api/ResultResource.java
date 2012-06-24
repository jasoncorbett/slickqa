package org.tcrun.slickij.api;

import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.jboss.resteasy.annotations.GZIP;
import org.tcrun.slickij.api.data.LogEntry;
import org.tcrun.slickij.api.data.Result;
import org.tcrun.slickij.api.data.ResultGroupSummary;
import org.tcrun.slickij.api.data.TestRunParameter;
/**
 *
 * @author jcorbett
 */
@Path("/results")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ResultResource
{
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	@GZIP
	public List<Result> getMatchingResults(@Context UriInfo uriInfo);

    @Path("/summary/bybuild/{buildid}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ResultGroupSummary getResultGroupSummaryByBuild(@PathParam("buildid") String buildid);

	@Path("/nextToBeRun")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public Result getNextToBeRun(TestRunParameter parameter);

	@Path("/{resultid}")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public Result getResult(@PathParam("resultid") String resultId);

    @Path("/{resultid}")
    @Produces(MediaType.APPLICATION_JSON)
    @DELETE
    public Result deleteResult(@PathParam("resultid") String resultId);

	@Path("/{resultid}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@PUT
	public Result updateResult(@PathParam("resultid") String resultid, Result update);

	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public Result addResult(Result result);

	@Path("/{resultid}/cancel")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public Result cancelResult(@PathParam("resultid") String resultid, String reason);

	@Path("/{resultid}/reschedule")
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public Result rescheduleResult(@PathParam("resultid") String resultid);

	@Path("/{resultid}/log")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public Integer addToLog(@PathParam("resultid") String resultId, List<LogEntry> logaddon);
}
