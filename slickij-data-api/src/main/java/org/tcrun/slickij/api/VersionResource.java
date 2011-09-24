package org.tcrun.slickij.api;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.tcrun.slickij.api.data.ProductVersion;

/**
 *
 * @author jcorbett
 */
@Path("/version")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface VersionResource
{
	@GET
	public List<ProductVersion> getProductVersions();

	@GET
	@Path("/{productname}")
	public ProductVersion getVersionOfProductWithName(@PathParam("productname") String productname);
}
