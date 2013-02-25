package org.tcrun.slickij.api;

import javax.ws.rs.*;
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

    @POST
    @Path("/{fileid}/addchunk")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public StoredFile addChunk(@PathParam("fileid") ObjectId fileid, byte[] data);

	@GET
	@Path("/{fileid}")
	@Produces(MediaType.APPLICATION_JSON)
	public StoredFile getStoredFile(@PathParam("fileid") ObjectId fileid);

    @PUT
    @Path("/{fileid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public StoredFile updateStoredFile(@PathParam("fileid") ObjectId fileid, StoredFile file);

	@GET
	@Path("/{fileid}/content/{filename}")
	public Response getFileContent(@PathParam("fileid") ObjectId fileid, @PathParam("filename") String filename);



}
