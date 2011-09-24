package org.tcrun.slickij.api.data.dao;

import com.google.code.morphia.dao.DAO;
import java.util.List;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.FileChunk;

/**
 *
 * @author jcorbett
 */
public interface FileChunkDAO extends DAO<FileChunk, ObjectId>
{
	public List<FileChunk> getChunksForFile(ObjectId fileId);

	public void deleteChunksForFile(ObjectId fileId);
}
