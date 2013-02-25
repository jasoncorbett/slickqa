package org.tcrun.slickij.api;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.jboss.resteasy.annotations.GZIP;
import org.tcrun.slickij.api.data.DataExtension;
import org.tcrun.slickij.api.data.Testcase;
import org.tcrun.slickij.api.data.Testplan;
import org.tcrun.slickij.api.data.TestplanRunParameters;
import org.tcrun.slickij.api.data.Testrun;

/**
 *
 * @author jcorbett
 */
@Path("/testplans")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TestplanResource
{
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	@GZIP
	public List<Testplan> getTestPlans(@Context UriInfo uriInfo);

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public Testplan addNewTestplan(Testplan testplan);

	@Path("/{testplanid}")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public Testplan getTestPlan(@PathParam("testplanid") String testplanId);

	@Path("/{testplanid}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PUT
	public Testplan updateTestplan(@PathParam("testplanid") String testplanId, Testplan update);

	@Path("/{testplanid}")
	@DELETE
	public void deleteTestplan(@PathParam("testplanid") String testplanId);

	@Path("/{testplanid}/run")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public Testrun runTestPlan(@PathParam("testplanid") String testplanId);

	@Path("/{testplanid}/run")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public Testrun runTestPlan(@PathParam("testplanid") String testplanId, TestplanRunParameters parameters);

	@Path("/{testplanid}/testcases")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	@GZIP
	public List<Testcase> getTestcases(@PathParam("testplanid") String testplanId);

    @Path("/{testplanid}/testcount")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Integer getTestcaseCount(@PathParam("testplanid") String testplanId);

	@Path("/{testplanid}/extensions")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public DataExtension<Testplan> addDataExtension(@PathParam("testplanid") String testplanId, DataExtension<Testplan> dataExtension);

	@Path("/{testplanid}/extensions/{extensionid}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@PUT
	public DataExtension<Testplan> updateDataExtension(@PathParam("testplanid") String testplanId, @PathParam("extensionid") String extensionId, DataExtension<Testplan> dataExtension);

	@Path("/{testplanid}/extensions/{extensionid}")
	@Produces(MediaType.APPLICATION_JSON)
	@DELETE
	public List<DataExtension<Testplan>> deleteDataExtension(@PathParam("testplanid") String testplanId, @PathParam("extensionid") String extensionId);


}
