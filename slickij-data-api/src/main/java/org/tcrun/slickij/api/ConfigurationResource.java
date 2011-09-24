package org.tcrun.slickij.api;

import java.util.List;
import java.util.Map;
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
import org.tcrun.slickij.api.data.Configuration;

/**
 *
 * @author jcorbett
 */
@Path("/configurations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ConfigurationResource
{
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public List<Configuration> getMatchingConfigurations(@DefaultValue("") @QueryParam("name") String name,
	                                                     @DefaultValue("") @QueryParam("configurationType") String configurationType,
	                                                     @DefaultValue("") @QueryParam("filename") String filename);

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public Configuration addNewConfiguration(Configuration config);

	@Path("/{configurationid}")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public Configuration getConfiguration(@PathParam("configurationid") String configId);

	@Path("/{configurationid}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@PUT
	public Configuration updateConfiguration(@PathParam("configurationid") String configId, Configuration updatedConfig);

	@Path("/{configurationid}")
	@DELETE
	public void deleteConfiguration(@PathParam("configurationid") String configId);

	@Path("/{configurationid}/configurationData")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public Configuration addNewConfigurationData(@PathParam("configurationid") String configId, Map<String, String> configurationData);

	@Path("/{configurationid}/configurationData/{key}")
	@Produces(MediaType.APPLICATION_JSON)
	@DELETE
	public Configuration deleteConfigurationData(@PathParam("configurationid") String configId, @PathParam("key") String configKey);
}
