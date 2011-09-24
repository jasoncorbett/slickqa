package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Property;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author jcorbett
 */
@Entity("fs.chunks")
public class FileChunk
{
	@Id
	private ObjectId id;

	@Property("files_id")
	private ObjectId filesId;

	@Property("n")
	private Integer chunkNumber;

	@Property("data")
	private byte[] data;

	public Integer getChunkNumber()
	{
		return chunkNumber;
	}

	public void setChunkNumber(Integer chunkNumber)
	{
		this.chunkNumber = chunkNumber;
	}

	public byte[] getData()
	{
		return data;
	}

	public void setData(byte[] data)
	{
		this.data = data;
	}

	public ObjectId getFilesId()
	{
		return filesId;
	}

	public void setFilesId(ObjectId filesId)
	{
		this.filesId = filesId;
	}

	public String getId()
	{
		if(id == null)
			return null;
		else
			return id.toString();
	}

	@JsonIgnore
	public ObjectId getObjectId()
	{
		return id;
	}

	public void setId(ObjectId id)
	{
		this.id = id;
	}


}
