package org.tcrun.slickij.data;

import com.google.inject.Inject;

import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import com.mongodb.gridfs.GridFS;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.StoredFileResource;
import org.tcrun.slickij.api.data.FileChunk;
import org.tcrun.slickij.api.data.InvalidDataError;
import org.tcrun.slickij.api.data.StoredFile;
import org.tcrun.slickij.api.data.dao.FileChunkDAO;
import org.tcrun.slickij.api.data.dao.StoredFileDAO;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 *
 * @author jcorbett
 */
public class StoredFileResourceImpl implements StoredFileResource
{
	private StoredFileDAO fileDAO;
    private GridFS gridFS;

	@Inject
	public StoredFileResourceImpl(StoredFileDAO fileDAO, GridFS gridFS)
	{
		this.fileDAO = fileDAO;
        this.gridFS = gridFS;
	}

	@Override
	public StoredFile createStoredFile(StoredFile file)
	{
        try
        {
            file.validate();
        } catch (InvalidDataError error)
        {
            throw new WebApplicationException(error);
        }
		fileDAO.save(file);
		return file;
	}

	@Override
	public StoredFile setFileContent(ObjectId fileid, byte[] data)
	{
		StoredFile file = getStoredFile(fileid);
		fileDAO.saveFileContent(file, data);
		return file;
	}

    @Override
    public StoredFile addChunk(@PathParam("fileid") ObjectId fileid, byte[] data) {
        StoredFile file = getStoredFile(fileid);
        if(data.length > file.getChunkSize())
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        fileDAO.addChunk(file, data);
        return file;
    }

    @Override
	public StoredFile getStoredFile(ObjectId fileid)
	{
		StoredFile retval = fileDAO.get(fileid);
		if(retval == null)
			throw new NotFoundError(StoredFile.class, fileid.toString());
		return retval;
	}

    @Override
    public StoredFile updateStoredFile(@PathParam("fileid") ObjectId fileid, StoredFile update)
    {
        StoredFile updated = getStoredFile(fileid);

        if(update.getMimetype() != null)
            updated.setMimetype(update.getMimetype());
        if(update.getMd5() != null)
            updated.setMd5(update.getMd5());
        if(update.getFilename() != null)
            updated.setFilename(update.getFilename());
        fileDAO.save(updated);
        return updated;
    }

    @Override
	public Response getFileContent(ObjectId fileid, String filename)
	{
		final StoredFile file = getStoredFile(fileid);
        return Response.status(Response.Status.OK).header("Content-Type", file.getMimetype()).entity(new StreamingOutput() {
            @Override
            public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                gridFS.find(file.getObjectId()).writeTo(outputStream);
            }
        }).build();
    }
}
