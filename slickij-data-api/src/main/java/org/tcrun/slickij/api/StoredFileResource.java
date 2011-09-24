package org.tcrun.slickij.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.StoredFile;

/**
 *
 * @author jcorbett
 */
@Path("/files")
public interface StoredFileResource
{
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public StoredFile createStoredFile(StoredFile file);

	@POST
	@Path("/{fileid}/content")
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	@Produces(MediaType.APPLICATION_JSON)
	public StoredFile setFileContent(@PathParam("fileid") ObjectId fileid, byte[] data);

	@GET
	@Path("/{fileid}")
	@Produces(MediaType.APPLICATION_JSON)
	public StoredFile getStoredFile(@PathParam("fileid") ObjectId fileid);

	@GET
	@Path("/{fileid}/content/{filename}")
	public Response getFileContent(@PathParam("fileid") ObjectId fileid, @PathParam("filename") String filename);

}
