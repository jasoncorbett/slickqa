package org.tcrun.slickij.data.dao;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.dao.BasicDAO;
import com.google.inject.Inject;
import java.util.List;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.FileChunk;
import org.tcrun.slickij.api.data.dao.FileChunkDAO;

/**
 *
 * @author jcorbett
 */
public class FileChunkDAOImpl extends BasicDAO<FileChunk, ObjectId> implements FileChunkDAO
{

	@Inject
	public FileChunkDAOImpl(Datastore ds)
	{
		super(FileChunk.class, ds);
	}

	@Override
	public List<FileChunk> getChunksForFile(ObjectId fileId)
	{
		return createQuery().field("filesId").equal(fileId).order("chunkNumber").asList();
	}

	@Override
	public void deleteChunksForFile(ObjectId fileId)
	{
		deleteByQuery(createQuery().field("filesId").equal(fileId));
	}

}
