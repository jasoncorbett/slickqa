package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Property;
import java.io.Serializable;
import java.util.Date;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;

import static org.tcrun.slickij.api.data.CopyUtil.copyDateIfNotNull;

/**
 *
 * @author jcorbett
 */
@Entity("fs.files")
public class StoredFile implements Serializable, Copyable<StoredFile>
{
    public static int DEFAULT_CHUNK_SIZE=262144;

	@Id
	private ObjectId id;

	@Property
	private String filename;

	@Property
	private Integer chunkSize;

	@Property
	private Date uploadDate;

	@Property
	private String mimetype;

	@Property
	private String md5;

	@Property
	private Integer length;

	@JsonIgnore
	public ObjectId getObjectId()
	{
		return id;
	}

	public String getId()
	{
		if(id == null)
			return null;
		else
			return id.toString();
	}

	public void setId(ObjectId id)
	{
		this.id = id;
	}

	public String getMimetype()
	{
		return mimetype;
	}

	public void setMimetype(String mimetype)
	{
		this.mimetype = mimetype;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String name)
	{
		this.filename = name;
	}

	public Integer getChunkSize()
	{
		return chunkSize;
	}

	public void setChunkSize(Integer chunkSize)
	{
		this.chunkSize = chunkSize;
	}

	public Integer getLength()
	{
		return length;
	}

	public void setLength(Integer length)
	{
		this.length = length;
	}

	public String getMd5()
	{
		return md5;
	}

	public void setMd5(String md5)
	{
		this.md5 = md5;
	}

	public Date getUploadDate()
	{
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate)
	{
		this.uploadDate = uploadDate;
	}

    public void validate() throws InvalidDataError
    {
        if(getFilename() == null)
            throw new InvalidDataError("StoredFile", "filename", "filename cannot be null");
        if(getMimetype() == null)
            throw new InvalidDataError("StoredFile", "mimetype", "mimetype cannot be null");
        chunkSize = DEFAULT_CHUNK_SIZE;
    }


    @Override
    public StoredFile createCopy()
    {
        StoredFile copy = new StoredFile();
        copy.setId(getObjectId());
        copy.setChunkSize(getChunkSize());
        copy.setFilename(getFilename());
        copy.setLength(getLength());
        copy.setMd5(getMd5());
        copy.setMimetype(getMimetype());
        copy.setUploadDate(copyDateIfNotNull(getUploadDate()));
        return copy;
    }
}
