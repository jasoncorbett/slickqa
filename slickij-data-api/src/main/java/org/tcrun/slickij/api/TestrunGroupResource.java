package org.tcrun.slickij.api;

import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.TestrunGroup;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * The REST interface for CRUD operations on testrun groups.
 * User: jcorbett
 * Date: 8/3/12
 * Time: 8:08 PM
 */
@Path("/testrungroups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TestrunGroupResource
{
    @GET
    public List<TestrunGroup> getMatchingTestrunGroups(@QueryParam("createdafter") @DefaultValue("0") Long date,
                                                       @QueryParam("name") @DefaultValue("") String name);

    @POST
    public TestrunGroup createTestrunGroup(TestrunGroup trgroup);

    @GET
    @Path("/{trgroupid}")
    public TestrunGroup getTestrunGroup(@PathParam("trgroupid") ObjectId id);

    @DELETE
    @Path("/{trgroupid}")
    public void deleteTestrunGroup(@PathParam("trgroupid") ObjectId id);

    @PUT
    @Path("/{trgroupid}")
    public TestrunGroup updateTestrunGroup(@PathParam("trgroupid") ObjectId id, TestrunGroup update);

    @POST
    @Path("/{trgroupid}/addtestrun/{testrunid}")
    public TestrunGroup addTestrunToTestrunGroup(@PathParam("trgroupid") ObjectId id, @PathParam("testrunid") ObjectId testrunId);

    @DELETE
    @Path("/{trgroupid}/removetestrun/{testrunid}")
    public TestrunGroup removeTestrunFromTestrunGroup(@PathParam("trgroupid") ObjectId id, @PathParam("testrunid") ObjectId testrunId);
}
