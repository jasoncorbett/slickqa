package org.tcrun.slickij.api.data.dao;

import com.google.code.morphia.dao.DAO;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.StoredFile;

/**
 *
 * @author jcorbett
 */
public interface StoredFileDAO extends DAO<StoredFile, ObjectId>
{
	public byte[] getFileContent(ObjectId id);

	public StoredFile saveFileContent(StoredFile file, byte[] data);

    public StoredFile addChunk(StoredFile file, byte[] data);
}
