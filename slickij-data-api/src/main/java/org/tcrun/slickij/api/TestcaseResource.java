package org.tcrun.slickij.api;

import java.util.List;
import java.util.Map;
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
import org.tcrun.slickij.api.data.Testcase;
import org.tcrun.slickij.api.data.testqueries.TestcaseQuery;

/**
 *
 * @author slambson
 */
@Path("/testcases")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TestcaseResource
{
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	@GZIP
	public List<Testcase> getMatchingTestcases(@Context UriInfo uriInfo);

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public Testcase addNewTestcase(Testcase testcase);

	@Path("/{testcaseid}")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public Testcase getTestcase(@PathParam("testcaseid") String testcaseId);

	@Path("/query")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	@GZIP
	public List<Testcase> getMatchingTestcases(TestcaseQuery query);

	@Path("/query")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public int countMatchingTestcases(TestcaseQuery query);



	@Path("/{testcaseid}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@PUT
	public Testcase updateTestcase(@PathParam("testcaseid") String testcaseId, Testcase updatedTestcase);

	@Path("/{testcaseid}")
	@Produces(MediaType.APPLICATION_JSON)
	@DELETE
	public Testcase deleteTestcase(@PathParam("testcaseid") String testcaseId);

	@Path("/{testcaseid}/extensions")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public DataExtension<Testcase> addDataExtension(@PathParam("testcaseid") String testcaseId, DataExtension<Testcase> dataExtension);

	@Path("/{testcaseid}/extensions/{extensionid}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@PUT
	public DataExtension<Testcase> updateDataExtension(@PathParam("testcaseid") String testcaseId, @PathParam("extensionid") String extensionId, DataExtension<Testcase> dataExtension);

	@Path("/{testcaseid}/extensions/{extensionid}")
	@Produces(MediaType.APPLICATION_JSON)
	@DELETE
	public List<DataExtension<Testcase>> deleteDataExtension(@PathParam("testcaseid") String testcaseId, @PathParam("extensionid") String extensionId);

	/*@Path("/{testcaseid}/configurationData")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public Configuration addNewConfigurationData(@PathParam("configurationid") String configId, Map<String, String> configurationData);

	@Path("/{configurationid}/configurationData/{key}")
	@Produces(MediaType.APPLICATION_JSON)
	@DELETE
	public Configuration deleteConfigurationData(@PathParam("configurationid") String configId, @PathParam("key") String configKey);*/
}
