package org.tcrun.slickij.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.InvalidDataError;
import org.tcrun.slickij.api.data.SystemConfiguration;

/**
 * User: jcorbett
 * Date: 12/3/12
 * Time: 10:38 AM
 */
@Path("/system-configuration")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface SystemConfigurationResource
{

    @GET
    public List<SystemConfiguration> getMatchingConfigurations(@QueryParam("name") @DefaultValue("") String name,
                                                               @QueryParam("config-type") @DefaultValue("") String configType);

    @GET
    @Path("/{id}")
    public SystemConfiguration getConfigurationById(@PathParam("id") ObjectId id);

    @DELETE
    @Path("/{id}")
    public SystemConfiguration deleteConfigurationById(@PathParam("id") ObjectId id);

    @PUT
    @Path("/{id}")
    public SystemConfiguration updateConfiguration(@PathParam("id") ObjectId id, SystemConfiguration configuration) throws InvalidDataError;

    @POST
    public SystemConfiguration createNewConfiguration(SystemConfiguration configuration) throws InvalidDataError;

    /**
     * NOT A REST CALL.  this allows Java classes inside slick to Query for a specific type of system configuration.
     * @param type
     * @param name
     * @param configType
     * @param <T>
     * @return
     */
    public <T extends SystemConfiguration> List<T> getMatchingConfigurationsOfType(Class<T> type, String name, String configType);

}
