package org.tcrun.slickij.data;

import com.google.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.StoredFileResource;
import org.tcrun.slickij.api.data.InvalidDataError;
import org.tcrun.slickij.api.data.StoredFile;
import org.tcrun.slickij.api.data.dao.StoredFileDAO;

/**
 *
 * @author jcorbett
 */
public class StoredFileResourceImpl implements StoredFileResource
{
	private StoredFileDAO fileDAO;

	@Inject
	public StoredFileResourceImpl(StoredFileDAO fileDAO)
	{
		this.fileDAO = fileDAO;
	}

	@Override
	public StoredFile createStoredFile(StoredFile file)
	{
		if(file.getFilename() == null)
			throw new WebApplicationException(new InvalidDataError("StoredFile", "filename", "filename cannot be null"));
		if(file.getMimetype() == null)
			throw new WebApplicationException(new InvalidDataError("StoredFile", "mimetype", "mimetype cannot be null"));
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
	public StoredFile getStoredFile(ObjectId fileid)
	{
		StoredFile retval = fileDAO.get(fileid);
		if(retval == null)
			throw new NotFoundError(StoredFile.class, fileid.toString());
		return retval;
	}

	@Override
	public Response getFileContent(ObjectId fileid, String filename)
	{
		StoredFile file = getStoredFile(fileid);
		byte[] data = fileDAO.getFileContent(fileid);
		return Response.status(Response.Status.OK).header("Content-Type", file.getMimetype()).entity(data).build();
	}
}
