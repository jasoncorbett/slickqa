package org.tcrun.slickij.api;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.jboss.resteasy.annotations.GZIP;
import org.tcrun.slickij.api.data.DataExtension;
import org.tcrun.slickij.api.data.Result;
import org.tcrun.slickij.api.data.Testrun;
import org.tcrun.slickij.api.data.TestRunSummary;

/**
 *
 * @author jcorbett
 */
@Path("/testruns")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TestrunResource
{
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	@GZIP
	public List<Testrun> getMatchingTestruns(@Context UriInfo uriInfo);

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public Testrun createNewTestrun(Testrun testrun);

	@Path("/{testrunid}")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public Testrun getTestrun(@PathParam("testrunid") String testrunId);

	@Path("/{testrunid}/summary")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public TestRunSummary getTestrunSummary(@PathParam("testrunid") String testrunId);

	@Path("/{testrunid}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@PUT
	public Testrun updateTestrun(@PathParam("testrunid") String testrunId, Testrun update);

	@Path("/{testrunid}")
	@Produces(MediaType.APPLICATION_JSON)
	@DELETE
	public List<Result> deleteTestrun(@PathParam("testrunid") String testrunId);

	@Path("/{testrunid}/extensions")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public DataExtension<Testrun> addDataExtension(@PathParam("testrunid") String testrunId, DataExtension<Testrun> dataExtension);

	@Path("/{testrunid}/extensions/{extensionid}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@PUT
	public DataExtension<Testrun> updateDataExtension(@PathParam("testrunid") String testrunId, @PathParam("extensionid") String extensionId, DataExtension<Testrun> dataExtension);

	@Path("/{testrunid}/extensions/{extensionid}")
	@Produces(MediaType.APPLICATION_JSON)
	@DELETE
	public List<DataExtension<Testrun>> deleteDataExtension(@PathParam("testrunid") String testrunId, @PathParam("extensionid") String extensionId);

}
